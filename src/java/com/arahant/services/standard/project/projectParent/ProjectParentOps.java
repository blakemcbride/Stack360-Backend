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



package com.arahant.services.standard.project.projectParent;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.ProjectReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DelimitedFileWriter;
import org.kissweb.database.ArrayListString;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectParentOps extends ServiceBase {

    static ArahantLogger logger = new ArahantLogger(ProjectParentOps.class);

    @WebMethod()
    public DeleteProjectReturn deleteProject(/*@WebParam(name = "in")*/final DeleteProjectInput in) {
        final DeleteProjectReturn ret = new DeleteProjectReturn();
        try {
            checkLogin(in);

            BProject.delete(hsu, in.getProjectIds());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public NewProjectReturn newProject(/*@WebParam(name = "in")*/final NewProjectInput in) {
        final NewProjectReturn ret = new NewProjectReturn();
        try {
            checkLogin(in);

			if(!isEmpty(in.getProjectName()) && ArahantSession.getHSU().createCriteria(Project.class).eq(Project.PROJECTNAME, in.getProjectName()).exists())
				throw new ArahantWarning("Project ID not unique.");

            final BProject bp = new BProject();
            bp.create();

            in.makeProject(bp);

            String company_id = bp.getRequestingOrgGroupId();
            if (company_id != null  &&  !company_id.isEmpty()) {
                ClientCompany cc = hsu.get(ClientCompany.class, bp.getRequestingOrgGroupId());
                if (cc != null)
                    bp.setProjectCode(cc.getDefaultProjectCode());
            }

            if (!isEmpty(in.getParentId())) {
                bp.setBillable('U');
                bp.setBillingRate(new BProject(in.getParentId()).getBillingRate());
            }

            bp.insert();

            ret.setProjectId(bp.getProjectId());
            ret.setProjectName(bp.getProjectName());

            if (!isEmpty(in.getParentId())) {
                new BProject(in.getParentId()).addChildren(new String[]{bp.getProjectId()});
            }

			if(!isEmpty(in.getProjectName()) && ArahantSession.getHSU().createCriteria(Project.class).ne(Project.PROJECTID, ret.getProjectId()).eq(Project.PROJECTNAME, in.getProjectName()).exists())
			{
				throw new ArahantWarning("Project ID not unique.");
			}

            final String shift = in.getShift();
            if (shift != null  &&  !shift.isEmpty()) {
                BProjectShift bs = new BProjectShift();
                bs.create();
                bs.setProject(bp);
                bs.setShiftStart(in.getShift());
                bs.setDescription(in.getShift());
                bs.insert();
            }

            finishService(ret);

        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;

    }

    /*
     * 1. Return projects based on employee, project status, and the join table
    2. Return projects accessible to all persons regardless of status
    3. Return projects most recently used by some made up criteria

    Trickiness - on item 3, suppose you are taking 5 projects, that would be 5 projects that don't necessarily appear in 1 or 2 (so always 5 more if there are 5 more)?
     */
    @WebMethod()
    public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
        final SearchProjectsReturn ret = new SearchProjectsReturn();

        try {
            checkLogin(in);

            ret.setProjects(BProject.search(in.getSearchMetaInput(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
                    in.getFromDate(), in.getToDate(), in.getUser(), in.getExtReference(), in.getProjectSummary(), in.getStatusType(),in.getExcludeId(),
                    ret.getCap()+1), in.getSubType());

            finishService(ret);

        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
        final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();
        try {
            checkLogin(in);

            boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

            ret.setProjectCategories(BProjectCategory.search(all, in.getCode(), in.getDescription(), ret.getHighCap()));
            if (!isEmpty(in.getCategoryId())) {
                ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(in.getCategoryId())));
            }
            else if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProject(in.getProjectId()).getProjectCategory()));
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
        final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();
        try {
            checkLogin(in);

            boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

            ret.setProjectTypes(BProjectType.search(all, in.getCode(), in.getDescription(), ret.getHighCap()));

            if (!isEmpty(in.getTypeId())) {
                ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(in.getTypeId())));
            }
            else  if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProject(in.getProjectId()).getProjectType()));
            }

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchProjectSubTypesReturn searchProjectSubTypes(/*@WebParam(name = "in")*/final SearchProjectSubTypesInput in) {
        final SearchProjectSubTypesReturn ret = new SearchProjectSubTypesReturn();
        try {
            checkLogin(in);

            ret.setProjectSubTypes();

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

            ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			ret.setProjectIdManualEntry(BProperty.getBoolean("ProjectIDManualEntry"));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public GetDefaultsForCategoryAndTypeReturn getDefaultsForCategoryAndType(/*@WebParam(name = "in")*/final GetDefaultsForCategoryAndTypeInput in) {
        final GetDefaultsForCategoryAndTypeReturn ret = new GetDefaultsForCategoryAndTypeReturn();
        try {
            checkLogin(in);
            BRouteTypeAssoc rta = new BRouteTypeAssoc(in.getProjectCategoryId(), in.getProjectTypeId());

            if (!isEmpty(rta.getRouteId())) {
                BRoute rt = new BRoute(rta.getRouteId());
                if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
                    ret.setData(rta);
                }
            }
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
        final GetReportReturn ret = new GetReportReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(new ProjectReport().build(in.getProjectName(), in.getProjectSummary(),
                    in.getExtReference(), in.getCompanyId(), in.getCategory(),
                    in.getType(), in.getStatus(), in.getFromDate(), in.getToDate(),
                    false, false, in.getStatusType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ExportListReturn exportList(/*@WebParam(name = "in")*/final ExportListInput in) {
        final ExportListReturn ret = new ExportListReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(exportList(in.getProjectName(), in.getProjectSummary(),
                    in.getExtReference(), in.getCompanyId(), in.getCategory(),
                    in.getType(), in.getStatus(), in.getFromDate(), in.getToDate(),
                    false, false, in.getStatusType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    private String exportList(String projectName, String summary, String extRef, String requestEntityId, String categoryId,
                              String typeId, String statusId, int startDate, int endDate,
                              boolean showStatus, boolean showCategory, int statusType) throws Exception {

        File csvFile = FileSystemUtils.createTempFile("ProjectListExport-", ".csv");
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath());
        dfw.setDateFormat("MM/dd/yyyy");

        writeColumnHeader(dfw);

        HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class);

        hcu.orderBy(Project.PROJECTNAME);

        hcu.like(Project.REFERENCE, extRef);
        hcu.like(Project.DESCRIPTION, summary);

        if (!isEmpty(categoryId))
            hcu.joinTo(Project.PROJECTCATEGORY).eq(ProjectCategory.PROJECTCATEGORYID, categoryId);

        switch (statusType) {
            case 3: hcu.eq(Project.PROJECTSTATUS,hsu.get(ProjectStatus.class,statusId));
                break;
            case 1: hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
                break;
            case 2: hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'N');
                break;
        }

        if (!isEmpty(typeId))
            hcu.joinTo(Project.PROJECTTYPE).eq(ProjectType.PROJECTTYPEID, typeId);

        if (!isEmpty(requestEntityId))
            hcu.joinTo(Project.REQUESTING_ORG_GROUP)
                    .in(OrgGroup.ORGGROUPID, new BOrgGroup(requestEntityId).getAllGroupsForCompany());

        if (!isEmpty(projectName))
            hcu.like(Project.PROJECTNAME,projectName);

        if (startDate>0)
            hcu.ge(Project.DATEREPORTED,startDate);

        if (endDate>0)
            hcu.le(Project.DATEREPORTED,endDate);

        HibernateScrollUtil<Project> scr = hcu.scroll();
        while (scr.next()) {
            Project p = scr.get();
            BProject bp = new BProject(p);
            dfw.writeField(p.getProjectName());
            dfw.writeField(p.getDescription());
            dfw.writeField(p.getProjectStatus().getDescription());
            dfw.writeDate(p.getEstimatedFirstDate());
            dfw.writeDate(p.getEstimatedLastDate());
            dfw.writeField(p.getReference());
            dfw.writeField(bp.getRequesterNameFormatted());
            dfw.writeField(bp.getSponsorNameFormatted());
            dfw.writeField(bp.getDateTimeReportedFormatted());
            dfw.endRecord();
        }
        scr.close();
        dfw.close();
        return FileSystemUtils.getHTTPPath(csvFile);
    }

    private void writeColumnHeader(DelimitedFileWriter dfw) throws Exception {
        dfw.writeField("ID");
        dfw.writeField("Summary");
        dfw.writeField("Status");
        dfw.writeField("Start Date");
        dfw.writeField("End Date");
        dfw.writeField("External Reference");
        dfw.writeField("Client");
        dfw.writeField("Who Added");
        dfw.writeField("When Added");
        dfw.endRecord();
    }

    @WebMethod()
    public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in) {
        final SearchPersonsReturn ret = new SearchPersonsReturn();
        try {
            checkLogin(in);

            ret.setItem(BPerson.searchPersons(in.getOrgGroupId(), in.getLastName(), in.getFirstName(), ret.getHighCap()));

            if (!isEmpty(in.getProjectId())) {
                BProject bp = new BProject(in.getProjectId());
                if (bp.getCurrentPerson() != null) {
                    ret.setSelectedItem(new SearchPersonsReturnItem(bp.getCurrentPerson()));
                }
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in) {
        final SearchCompanyByTypeReturn ret = new SearchCompanyByTypeReturn();
        try {
            checkLogin(in);

            if (hsu.getCurrentPerson().getOrgGroupType() == CLIENT_TYPE) {
                final Connection db = KissConnection.get();
                final Person person = hsu.getCurrentPerson();
                int today = org.kissweb.DateUtils.today();
                List<Record> recs = db.fetchAll("select org_group_id " +
                                               "from org_group_association " +
                                               "where person_id = ? " +
                                               "      and (start_date = 0 or start_date >= ?)" +
                                               "      and (final_date = 0 or final_date <= ?)", person.getPersonId(), today, today);
                BCompanyBase[] ar = new BCompanyBase[recs.size()];
                int i = 0;
                for (Record rec : recs)
                    ar[i++] = BCompanyBase.get(rec.getString("org_group_id"));
                ret.setCompanies(ar);
            } else {
                ret.setCompanies(BCompanyBase.searchCompanySpecific(in.getName(), false, ret.getHighCap()+1));
            }


            if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(new BProject(in.getProjectId()).getRequestingCompanyId())));
            } else if (in.getAutoDefault()) {
//                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BPerson(hsu.getCurrentPerson()).getCompany()));
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BCompany(hsu.getCurrentCompany())));
            } else if (!isEmpty(in.getId())) {
                if (ret.getCompanies().length <= ret.getLowCap()) {
                    //if it's in the list, set selected item
                    for (SearchCompanyByTypeReturnItem ogri : ret.getCompanies()) {
                        if (in.getId().equals(ogri.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }
                } else {
                    for (BCompanyBase bp : BCompanyBase.search(in.getName(), false, 0)) {
                        if (in.getId().equals(bp.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }


                }
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in) {
        final SearchOrgGroupsForCompanyReturn ret = new SearchOrgGroupsForCompanyReturn();
        try {
            checkLogin(in);

            if ("ReqCo".equals(in.getCompanyId())) {
                SearchOrgGroupsForCompanyReturnItem ri = new SearchOrgGroupsForCompanyReturnItem();
                ri.setId("ReqOrg");
                ri.setName("Requesting Organizational Group");
                SearchOrgGroupsForCompanyReturnItem[] items = new SearchOrgGroupsForCompanyReturnItem[1];
                items[0] = ri;
                ret.setItem(items);
                ret.setSelectedItem(ri);
            } else {
                if (isEmpty(in.getCompanyId()))
                {
                    in.setCompanyId(new BProject(in.getProjectId()).getRequestingCompanyId());
                }
                ret.setItem(BCompanyBase.get(in.getCompanyId()).searchOrgGroups(in.getName(), ret.getHighCap()));
            }

            if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(new BOrgGroup(new BProject(in.getProjectId()).getRequestingGroupId())));
            } else if (in.getAutoDefault() && hsu.getCurrentPerson().getOrgGroupAssociations().size() == 1) {
                ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(
                        new BOrgGroup(hsu.getCurrentPerson().getOrgGroupAssociations().iterator().next().getOrgGroup())));
            }


            hsu.rollbackTransaction();
            hsu.beginTransaction();
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
        final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();
        try {
            checkLogin(in);

            //	ret.setItem(BProjectStatus.search(hsu, in.getCode(), in.getDescription(),in.getFromRouteStopId(), in.getStatusType(),ret.getHighCap()));


            //TODO: refactor into biz object
            BProject proj = null;
            if (!isEmpty(in.getProjectId())) {
                proj = new BProject(in.getProjectId());
            }
            //add to query, code and description

            HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).geOrEq(ProjectStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription());

            if (!isEmpty(in.getStatusId())) {
                ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(in.getStatusId()), proj));
            }
            ret.setItem(BProjectStatus.makeArray(hcu.list()), proj);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    //empty or one of projectName, description, dateReportedFormatted, reference, requestingNameFormatted
    private class ProjectSearchComparator implements Comparator<SearchProjectsReturnItem> {

        private String field;
        private boolean asc;

        @Override
        public int compare(SearchProjectsReturnItem o1, SearchProjectsReturnItem o2) {

            if (!asc) {
                SearchProjectsReturnItem t = o2;
                o2 = o1;
                o1 = t;
            }

            if ("projectName".equals(field)) {
                return o1.getProjectName().compareTo(o2.getProjectName());
            }

            if ("description".equals(field)) {
                return o1.getDescription().compareTo(o2.getDescription());
            }

            if ("reference".equals(field)) {
                return o1.getReference().compareTo(o2.getReference());
            }

            if ("requestingNameFormatted".equals(field)) {
                return o1.getRequestingNameFormatted().compareTo(o2.getRequestingNameFormatted());
            }

            //must be the date
            return o1.getDateReported() - o2.getDateReported();
        }
    }

    @WebMethod()
    public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in) {
        final GetProjectDetailReturn ret = new GetProjectDetailReturn();
        try {
            checkLogin(in);

            ret.setData(new BProject(in.getProjectId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public LoadProjectDetailsReturn loadProjectDetails(/*@WebParam(name = "in")*/final LoadProjectDetailsInput in) {
        final LoadProjectDetailsReturn ret = new LoadProjectDetailsReturn();
        try {
            checkLogin(in);

            ret.setData(new BProject(in.getId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }
    
    @WebMethod()
	public SaveProjectReturn saveProject(/*@WebParam(name = "in")*/final SaveProjectInput in)		
	{
		final SaveProjectReturn ret=new SaveProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject x=new BProject(in.getProjectId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
    public LoadBillingRateTypesReturn loadBillingRateTypes(/*@WebParam(name = "in")*/final LoadBillingRateTypesInput in) {
        final LoadBillingRateTypesReturn ret = new LoadBillingRateTypesReturn();

        try {
            checkLogin(in);

            ret.setItem(BRateType.list());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    /*
    public static void main (String args[])
    {
        try
        {
            ArahantSession.getHSU().beginTransaction();
            ArahantSession.getHSU().setCurrentPersonToArahant();
            ProjectParentOps op=new ProjectParentOps();
            SaveProjectInput in=new SaveProjectInput();
            in.setUser("arahant");
            in.setPassword("supervisor");
            in.setContextCompanyId(ArahantSession.getHSU().getFirst(CompanyDetail.class).getOrgGroupId());
            in.setCategoryId("1");
            in.setProjectId("1");
            in.setSummary("test");
            in.setOrgGroupId("3");
            in.setTypeId("4");
            in.setDeleteId("5");
            
            op.saveProject(in);

            ArahantSession.getHSU().commitTransaction();
        }
        catch (Exception e)
        {
            ArahantSession.getHSU().rollbackTransaction();
        }
    }
    */
}
