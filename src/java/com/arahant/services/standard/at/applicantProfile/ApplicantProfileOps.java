/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
 */

package com.arahant.services.standard.at.applicantProfile;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.ExternalFile;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.KissConnection;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateTime;
import org.kissweb.DateUtils;
import org.kissweb.FileUtils;
import org.kissweb.builder.BuildUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardAtApplicantProfileOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ApplicantProfileOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(ApplicantProfileOps.class);

    public ApplicantProfileOps() {
        super();
    }

    @WebMethod()
    public ListCompaniesReturn listCompanies(/*@WebParam(name = "in")*/final ListCompaniesInput in) {
        final ListCompaniesReturn ret = new ListCompaniesReturn();
        try {
            checkLogin(in);

            ret.setItem(BCompany.listCompanies(hsu));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchApplicantSourcesReturn searchApplicantSources(/*@WebParam(name = "in")*/final SearchApplicantSourcesInput in) {
        final SearchApplicantSourcesReturn ret = new SearchApplicantSourcesReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantSource.search(in.getDescription(), ret.getHighCap()));

            if (!isEmpty(in.getId()))
                ret.setSelectedItem(new SearchApplicantSourcesReturnItem(new BApplicantSource(in.getId())));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchApplicantStatusesReturn searchApplicantStatuses(/*@WebParam(name = "in")*/final SearchApplicantStatusesInput in) {
        final SearchApplicantStatusesReturn ret = new SearchApplicantStatusesReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantStatus.search(in.getName(), ret.getHighCap()));

            if (!isEmpty(in.getId()))
                ret.setSelectedItem(new SearchApplicantStatusesReturnItem(new BApplicantStatus(in.getId())));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListEEORacesReturn listEEORaces(/*@WebParam(name = "in")*/final ListEEORacesInput in) {
        final ListEEORacesReturn ret = new ListEEORacesReturn();
        try {
            checkLogin(in);

            ret.setItem(BHREEORace.list(hsu));

            ret.setSelectedItem(new ListEEORacesReturnItem());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public LoadApplicantReturn loadApplicant(/*@WebParam(name = "in")*/final LoadApplicantInput in) {
        final LoadApplicantReturn ret = new LoadApplicantReturn();
        try {
            checkLogin(in);

            ret.setData(new BApplicant(in.getId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SaveApplicantReturn saveApplicant(/*@WebParam(name = "in")*/final SaveApplicantInput in) {
        final SaveApplicantReturn ret = new SaveApplicantReturn();
        try {
            checkLogin(in);

            final BApplicant x = new BApplicant(in.getId());
            in.setData(x);
            x.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public LoadQuestionsReturn loadQuestions(/*@WebParam(name = "in")*/final LoadQuestionsInput in) {
        final LoadQuestionsReturn ret = new LoadQuestionsReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantQuestion.listExternal(in.getPositionId(), null));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public GetFormReturn getForm(/*@WebParam(name = "in")*/final GetFormInput in) {
        final GetFormReturn ret = new GetFormReturn();
        try {
            checkLogin(in);

            String personFormId = in.getId();
            String httpUrl = new BPersonForm(personFormId).getReport();
            String localUrl = FileSystemUtils.httpPathToAbsolutePath(httpUrl);
            String content = FileUtils.readFile(localUrl);

            if (content.contains("SIGNATURE")) {
                (new File(localUrl)).delete();
                httpUrl = fixForm(personFormId);
            }

            ret.setReportUrl(httpUrl);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    /**
     * There was a bug in the system that allowed users to create form templates without the $SIGNATURE and
     * $DATE variables.  This method fixes that problem by fixing the form they signed and re-generating the
     * form.
     *
     * @param personFormId
     * @return
     */
    private String fixForm(String personFormId) throws Exception {
        final Connection db = KissConnection.get();
        Record rec = db.fetchOne("select person_id from person_form where person_form_id = ?", personFormId);
        final String personId = rec.getString("person_id");
        rec = db.fetchOne("select applicant_application_id " +
                "from applicant_application " +
                "where person_id = ? " +
                "      and offer_elec_signed_date is not null " +
                "order by offer_elec_signed_date desc", personId);
        final String applicantApplicationId = rec.getString("applicant_application_id");
        final String zippedGpgFileName = ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicantApplicationId, ".zip.gpg");
        final File zippedGpgFile = new File(zippedGpgFileName);
        final String zippedGpgBaseName = zippedGpgFile.getName();
        final String basename = zippedGpgBaseName.substring(0, zippedGpgBaseName.lastIndexOf(".zip.gpg"));
        final File tempZipGpgFile = FileSystemUtils.createTempFile(basename, ".zip.gpg");
        final String tempBasename = tempZipGpgFile.getName().replace(".zip.gpg", "");
        final String tempDir = tempZipGpgFile.getParent();
        final File zipFile = new File(tempDir + "/" + tempBasename + ".zip");
        FileUtils.copy(zippedGpgFile.getAbsolutePath(), tempZipGpgFile.getAbsolutePath());
        BuildUtils.run(false, true, false, false, tempDir, "gpg -q " + tempZipGpgFile.getName());
        tempZipGpgFile.delete();
        BuildUtils.run(false, true, false, false, tempDir, "unzip " + zipFile.getName());
        zipFile.delete();
        // At this point we have the sig and html files extracted from the gpg file to the tempDir
        final String htmlFileName = tempDir + "/" + basename + ".html";
        final String sigFileName = tempDir + "/" + basename + ".sig";
        final String sigContents = FileUtils.readFile(sigFileName);
        final String date = extractLineContent(sigContents, "Date:  ");
        final String signature = extractLineContent(sigContents, "Signature:  ");
        final String ip = extractLineContent(sigContents, "IP Address:  ");
        String html = FileUtils.readFile(htmlFileName);
        html = html.replace("SIGNATURE", "<i><b>" + signature + "</b></i> (electronically signed from " + ip + ")");
        html = html.replaceFirst("DATE", date);
        ExternalFile.saveData(ExternalFile.PERSON_FORM, personFormId, "html", html);
        (new File(htmlFileName)).delete();
        (new File(sigFileName)).delete();
        final File of = FileSystemUtils.createReportFile("SignedOffer", "html");
        FileUtils.write(of.getAbsolutePath(), html);
        return FileSystemUtils.getHTTPPath(of);
    }

    private static String extractLineContent(String content, String prefix) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        return null; // Or appropriate error handling
    }

    @WebMethod()
    public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in) {
        final ListFormTypesReturn ret = new ListFormTypesReturn();
        try {
            checkLogin(in);

            ret.setItem(BFormType.list());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListAnswersForQuestionReturn listAnswersForQuestion(/*@WebParam(name = "in")*/final ListAnswersForQuestionInput in) {
        final ListAnswersForQuestionReturn ret = new ListAnswersForQuestionReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantQuestionChoice.list(in.getId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchJobsReturn searchJobs(/*@WebParam(name = "in")*/final SearchJobsInput in) {
        final SearchJobsReturn ret = new SearchJobsReturn();
        try {
            checkLogin(in);

            int today = DateUtils.today();
            Connection db = hsu.getKissConnection();
            List<Record> recs = db.fetchAll("select j.*, og.group_name  " +
                    "from applicant_position j " +
                    "join org_group og " +
                    "  on j.org_group_id = og.org_group_id " +
                    "order by j.job_title");
            SearchJobsReturnItem[] items = new SearchJobsReturnItem[recs.size()];
            for (int i=0 ; i < recs.size() ; i++) {
                Record rec = recs.get(i);
                items[i] = new SearchJobsReturnItem(rec.getString("applicant_position_id"), rec.getString("job_title"), rec.getString("group_name"),
                        rec.getString("position_id"));
            }
            ret.setItem(items);

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in) {
        final SearchOrgGroupsReturn ret = new SearchOrgGroupsReturn();
        try {
            checkLogin(in);

            int type = COMPANY_TYPE;

            if (BProperty.getBoolean("ShowClientsInApplicantGroups"))
                type |= CLIENT_TYPE;

            ret.setItem(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), 0, type, ret.getHighCap()));
            if (!isEmpty(in.getId())) {
                if (ret.getItem().length <= ret.getLowCap()) {
                    //if it's in the search, set selected item
                    for (SearchOrgGroupsReturnItem ogri : ret.getItem())
                        if (in.getId().equals(ogri.getId()))
                            ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
                } else {
                    for (BOrgGroup borg : BPerson.getCurrent().searchSubordinateGroups(hsu, in.getName(), 0))
                        if (in.getId().equals(borg.getOrgGroupId()))
                            ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));

                }
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchApplicationStatusesReturn searchApplicationStatuses(/*@WebParam(name = "in")*/final SearchApplicationStatusesInput in) {
        final SearchApplicationStatusesReturn ret = new SearchApplicationStatusesReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicationStatus.search(in.getName(), ret.getHighCap()));

            if (!isEmpty(in.getId()))
                ret.setSelectedItem(new SearchApplicationStatusesReturnItem(new BApplicationStatus(in.getId())));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }
}
