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

package com.arahant.services.standard.at.applicant;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ApplicantReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.HibernateSessionUtil;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.kissweb.database.Record;

import java.util.ArrayList;
import java.util.List;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardAtApplicantOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ApplicantOps extends ServiceBase {

    private static final ArahantLogger logger = new ArahantLogger(ApplicantOps.class);

    public ApplicantOps() {
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
    public SearchPositionsReturn searchPositions(/*@WebParam(name = "in")*/final SearchPositionsInput in) {
        final SearchPositionsReturn ret = new SearchPositionsReturn();
        try {
            checkLogin(in);

            int today = DateUtils.today();
            Connection db = hsu.getKissConnection();
            List<Record> recs = db.fetchAll("select p.position_id, p.position_name, og.group_name " +
                    "from hr_position p " +
                    "join org_group og " +
                    "  on p.org_group_id = og.org_group_id " +
                    "where (p.first_active_date = 0 or p.first_active_date <= ?) " +
                    "      and (p.last_active_date = 0 or p.last_active_date >= ?) " +
                    "order by p.seqno", today, today);
            SearchPositionReturnItem [] items = new SearchPositionReturnItem[recs.size()];
            for (int i=0 ; i < recs.size() ; i++) {
                Record rec = recs.get(i);
                items[i] = new SearchPositionReturnItem(rec.getString("position_id"), rec.getString("position_name"), rec.getString("group_name"));
            }
            ret.setItem(items);

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

    private static BSearchOutput<BApplicant> search(HibernateSessionUtil hsu, final SearchApplicantsInput in, int cap, final ArrayList<String> empStat) throws Exception {
        BSearchMetaInput metaInput = in.getSearchMetaInput();
        String applicantSource = in.getApplicantSource();
        String applicantStatus = in.getApplicantStatus();
        String applicationSource = in.getApplicationSource();
        String applicationStatus = in.getApplicationStatus();
        String firstName = in.getFirstName();
        String lastName = in.getLastName();
        String positionId = in.getPositionId();
        boolean includeEmployees = in.getIncludeEmployeesWithApps();
        boolean includedOutdated = in.getIncludeOutdatedApplicants();
        boolean includeInactive = in.getIncludeInactive();
        boolean sortLastEventFirst = in.getSortLastEventFirst();
        boolean sortLastEventLast = in.getSortLastEventLast();
        int page = in.getPage();  // desired page number (starting at 1)
        String state = in.getState();
        boolean countGood = true;

        if (applicationStatus != null && !applicationStatus.isEmpty())
            includeInactive = true;

        final Connection db = hsu.getKissConnection();
        final Command cmd = db.newCommand();

        // int itemsPerPage = metaInput.getItemsPerPage();  // ignored.  cap used instead.

        final ArrayList<Object> args = new ArrayList<>();
        String sql =
                "with latest_changes as (" +
                        "SELECT cl.* " +
                        "FROM change_log cl " +
                        "INNER JOIN ( " +
                        "    SELECT person_id, MAX(change_when) AS max_change_when " +
                        "    FROM change_log " +
                        "    GROUP BY person_id " +
                        ") AS lc ON cl.person_id = lc.person_id  " +
                        "                     AND cl.change_when = lc.max_change_when) " +

                        "select a.person_id, es.name,  " +


                        "case " +
                        "    when lc.change_when is not null then " +
                        "         (cast(date_part('year', lc.change_when) as int) * 10000 " +
                        "          + cast(date_part('month', lc.change_when) as int) * 100 " +
                        "          + cast(date_part('day', lc.change_when) as int)) " +
                        "    when aa.application_date is not null then aa.application_date " +
                        "    else a.first_aware_date " +
                        "end as change_when2 " +


                        "from applicant a " +
                        "left join latest_changes lc " +
                        "  on a.person_id = lc.person_id " +
                        "left join applicant_application aa " +
                        "  on a.person_id = aa.person_id " +
                        "left join applicant_app_status aas " +
                        "  on aa.applicant_app_status_id = aas.applicant_app_status_id " +
                        "left join applicant_position ap " +
                        "  on aa.applicant_position_id = ap.applicant_position_id " +
                        "join person p " +
                        "  on a.person_id = p.person_id " +
                        "left join applicant_source asrc " +
                        "  on a.applicant_source_id = asrc.applicant_source_id " +
                        "left join applicant_status astat " +
                        "  on a.applicant_status_id = astat.applicant_status_id " +
                        "left join employee e " +
                        "  on a.person_id = e.person_id " +
                        "left join address ad " +
                        "  on p.person_id = ad.person_join " +
                        "left join current_employee_status esh " +
                        "  on a.person_id = esh.employee_id " +
                        "left join hr_employee_status es " +
                        "  on esh.status_id = es.status_id " +
                        "where 1=1 ";
        if (applicantSource != null && !applicantSource.isEmpty()) {
            sql += "and a.applicant_source_id = ? ";
            args.add(applicantSource);
        }
        if (applicantStatus != null && !applicantStatus.isEmpty()) {
            sql += "and a.applicant_status_id = ? ";
            args.add(applicantStatus);
        }
        if (applicationSource != null && !applicationSource.isEmpty()) {
            sql += "and a.applicant_status_id = ? ";
            args.add(applicationSource);
        }
        if (positionId != null && !positionId.isEmpty()) {
            sql += "and aa.position_id = ? ";
            args.add(positionId);
        }
        if (applicationStatus != null && !applicationStatus.isEmpty()) {
            sql += "and aa.applicant_app_status_id = ? ";
            args.add(applicationStatus);
        }
        if (firstName != null && !firstName.isEmpty() && !firstName.equals("%")) {
            sql += "and lower(p.fname) like ? ";
            args.add(firstName.toLowerCase() + "%");
        }
        if (lastName != null && !lastName.isEmpty() && !lastName.equals("%")) {
            sql += "and lower(p.lname) like ? ";
            args.add(lastName.toLowerCase() + "%");
        }
        if (state != null && !state.isEmpty()) {
            sql += "and ad.state = ? ";
            args.add(state);
        }
        if (!includeEmployees)
            sql += "and (es.active <> 'Y' or es.active is null) ";
        if (!includedOutdated) {
            // they have been an employee after their application
            sql += "and not (esh.effective_date is not null and aa.application_date is not null and aa.application_date < esh.effective_date) ";
        }
        if (!includeInactive)
            sql += "and (aas.is_active = 'Y' or aas.is_active is null) ";

        sql += "order by ";
        if (sortLastEventFirst)
            sql += "change_when2, ";
        else if (sortLastEventLast)
            sql += "change_when2 desc, ";
        switch (metaInput.getSortType()){
            case 1 :
            default:
                sql += "lower(p.lname), lower(p.fname), lower(p.mname) ";
                break;
            case 2 :
                sql += "lower(p.fname) ";
                break;
            case 3 :
                sql += "lower(p.mname) ";
                break;
            case 4 :
                sql += "asrc.description, lower(p.lname), lower(p.fname), lower(p.mname) ";
                break;
            case 5 :
                sql += "astat.status_order, lower(p.lname), lower(p.fname), lower(p.mname) ";
                break;
            case 6 :
                sql += "a.first_aware_date desc, lower(p.lname), lower(p.fname), lower(p.mname) ";
                break;
            case 7 :
                sql += "(e.person_id is null), lower(p.lname), lower(p.fname), lower(p.mname) ";
                break;
        }

        //List<Record> recs = db.fetchAll(page, cap, sql, args);
        int nPossibleRecords = 0;
        Cursor c = cmd.query(sql, args);
        ArrayList<BApplicant> items = new ArrayList<>();
        String prevPersonId = "";
        lbl1:
        while (c.isNext() && items.size() < cap) {
            Record rec = c.getRecord();
            String personId = rec.getString("person_id");
            if (personId.equals(prevPersonId))
                continue;
            prevPersonId = personId;

            SearchApplicantsInputAnswer[] ans = in.getAnswers();
            if (ans != null && ans.length > 0) {
                for (SearchApplicantsInputAnswer a : ans) {
                    Record arec = db.fetchOne("select aa.*, aq.data_type " +
                            "from applicant_answer aa " +
                            "join applicant_question aq " +
                            "  on aa.applicant_question_id = aq.applicant_question_id " +
                            "where aa.person_id = ? and aa.applicant_question_id = ?", personId, a.getId());

                    if (a.getComparator() == 9) {
                        if (arec == null) {
                            countGood = false;
                            continue lbl1;  // failed test; skip this person
                        }
                    } else if (a.getComparator() == 10) {
                        if (arec != null) {
                            countGood = false;
                            continue lbl1;  // failed test; skip this person
                        }
                    } else {
                        String astr;
                        if (arec == null) {
                            countGood = false;
                            continue lbl1;  // no answer for this question; skip this person
                        }
                        switch (arec.getChar("data_type")) {
                            case 'D': // date
                                switch (a.getComparator()) {
                                    case 6:  // before
                                        if (arec.getInt("date_answer") > a.getDateAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 7: // after
                                        if (arec.getInt("date_answer") < a.getDateAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 8:  //  on
                                        if (arec.getInt("date_answer") != a.getDateAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                }
                                break;
                            case 'S':  // string
                                astr = arec.getString("string_answer");
                                astr = astr == null ? "" : astr;
                                switch (a.getComparator()) {
                                    case 2: // starts with
                                        if (!astr.startsWith(a.getTextAnswer())) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 3: // ends with
                                        if (!astr.endsWith(a.getTextAnswer())) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 4: // contains
                                        if (!astr.contains(a.getTextAnswer())) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 5: // equals
                                        if (!astr.equals(a.getTextAnswer()))
                                            continue lbl1;  // failed test; skip this person
                                        break;
                                }
                                break;
                            case 'N': // number
                                switch (a.getComparator()) {
                                    case 11: // greater than
                                        if (arec.getDouble("numeric_answer") < a.getNumericAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 12: // less than
                                        double m = arec.getDouble("numeric_answer");
                                        double n = a.getNumericAnswer();
                                        if (arec.getDouble("numeric_answer") > a.getNumericAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 13: // equals
                                        if (arec.getDouble("numeric_answer") != a.getNumericAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                    case 14: // not equals
                                        if (arec.getDouble("numeric_answer") == a.getNumericAnswer()) {
                                            countGood = false;
                                            continue lbl1;  // failed test; skip this person
                                        }
                                        break;
                                }
                                break;
                            case 'Y': // yes/no
                                astr = arec.getString("string_answer");
                                astr = astr == null ? "" : astr;
                                if (!astr.equals(a.getYnAnswer())) {
                                    countGood = false;
                                    continue lbl1;  // failed test; skip this person
                                }
                            case 'L': // list
                                astr = arec.getString("applicant_question_choice_id");
                                astr = astr == null ? "" : astr;
                                if (!astr.equals(a.getListAnswerId())) {
                                    countGood = false;
                                    continue lbl1;  // failed test; skip this person
                                }
                                break;
                        }
                    }
                }
            }
            if (++nPossibleRecords > cap*(page-1)) {
                items.add(new BApplicant(personId));
                empStat.add(rec.getString("name"));
            }
        }
        final BSearchOutput<BApplicant> out = new BSearchOutput<>(metaInput);
        out.setItems(items.toArray(new BApplicant[0]));

        if (countGood) {
            sql = sql.replaceAll("select a.person_id, es.name, .+? change_when2 ", "select count(*) ")
                    .replaceAll(" order by .+", "");
           // sql = sql.replaceAll("left join empl_status_now .+on esh.status_id = es.status_id", "");
            //args.remove(0);
            Record rec = db.fetchOne(sql, args);
            Long count = rec.getLong("count");
            out.setTotalItemsPaging(count.intValue());
        } else
            out.setTotalItemsPaging(-1);
        return out;
    }

    @WebMethod()
    public SearchApplicantsReturn searchApplicants(/*@WebParam(name = "in")*/final SearchApplicantsInput in) {
        final SearchApplicantsReturn ret = new SearchApplicantsReturn();
        try {
            checkLogin(in);

            final ArrayList<String> empStat = new ArrayList<>();
            final BSearchOutput<BApplicant> items = search(hsu, in, ret.getCap(), empStat);
            ret.setItem(items, empStat, in.getSortLastEventFirst(), in.getSortLastEventLast());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
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
    public NewApplicantReturn newApplicant(/*@WebParam(name = "in")*/final NewApplicantInput in) {
        final NewApplicantReturn ret = new NewApplicantReturn();
        try {
            checkLogin(in);

            final BApplicant x = new BApplicant();
            ret.setId(x.create());
            in.setData(x);
            x.insert();
            BChangeLog.applicantStatusChange(x.getPersonId(), null, null, "New applicant added");

            finishService(ret);
        } catch (final Exception e) {
            ret.setId("");
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
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

        try {
            checkLogin(in);

            ret.setAccess();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public DeleteApplicantsReturn deleteApplicants(/*@WebParam(name = "in")*/final DeleteApplicantsInput in) {
        final DeleteApplicantsReturn ret = new DeleteApplicantsReturn();
        try {
            checkLogin(in);

            BApplicant.delete(in.getIds());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
        final GetReportReturn ret = new GetReportReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(new ApplicantReport().build(in.getSearchType(),
                    in.getApplicationSource(), in.getApplicationStatus(), in.getApplicantSource(),
                    in.getApplicantStatus(), in.getFirstName(), in.getLastName(), in.getJobTypeId(),
                    in.getPositionId(), false));

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

            ret.setItem(BApplicantQuestion.listExternal(in.getJobTypeId(), null));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchQuestionsForJobTypeReturn searchQuestionsForJobType(/*@WebParam(name = "in")*/final SearchQuestionsForJobTypeInput in) {
        final SearchQuestionsForJobTypeReturn ret = new SearchQuestionsForJobTypeReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantQuestion.searchExternal(in.getJobTypeId(), 1, in.getQuestion(), ret.getHighCap()));

            if (!isEmpty(in.getId()))
                ret.setSelectedItem(new SearchQuestionsForJobTypeReturnItem(new BApplicantQuestion(in.getId())));

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

            ret.setReportUrl(new BPersonForm(in.getId()).getReport());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
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

}
