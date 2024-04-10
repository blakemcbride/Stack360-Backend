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

package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.ProjectGroupReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectOrgGroupProjectListOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OrgGroupProjectListOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(OrgGroupProjectListOps.class);
	
	public OrgGroupProjectListOps() {
	}


	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in)			{
		final SearchProjectsReturn ret=new SearchProjectsReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(searchOrgGroupProjects(in.getSearchMetaInput(),in.getOrgGroupId(), in.getPersonId(), in.getProjectSummary(), 
					in.getExtReference(), in.getToDate(), in.getToDate(), in.getCategoryIds(), 
					in.getTypeIds(), in.getStatusIds(), in.getCompanyId(), in.getShowAssigned(), ret.getCap()),in.getPersonId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	/**
	 * @param searchMeta
	 * @param orgGroupId
	 * @param personId
	 * @param projectSummary
	 * @param extReference
	 * @param fromDate
	 * @param toDate
	 * @param projectCategoryId
	 * @param projectTypeId
	 * @param projectStatusId
	 * @param companyId
	 * @param showAssigned
	 * @param max
	 * @return 
	 *
	 * orgGroupId matches against project's current routeStop orgGroup (if
	 * empty, all subordinate org groups)
	 *
	 * personId matches against project current assigned person .. (note person
	 * can be in more than one org group, maybe not even the requested one,
	 * still return him)
	 *
	 * projectStatusId matches against project's current status
	 *
	 *
	 */
	private static BSearchOutput<BProject> searchOrgGroupProjects(BSearchMetaInput searchMeta, final String orgGroupId, final String personId, final String projectSummary,
			final String extReference, final int fromDate, final int toDate, final String[] projectCategoryId,
			final String[] projectTypeId, final String[] projectStatusId, final String companyId,
			final boolean showAssigned, final int max) {
		HibernateCriteriaUtil<Project> hcu = getSearchOrgProjectsHCU(max, personId, showAssigned, orgGroupId, projectSummary, extReference, fromDate, toDate, projectCategoryId, projectTypeId, projectStatusId, companyId, searchMeta, true);
		BSearchOutput<BProject> ret = new BSearchOutput<BProject>(searchMeta);
		int num = searchMeta.getItemsPerPage();
		searchMeta.setItemsPerPage(num + 6);  // we need to see how many pages there are (count(*) is too slow under postgres)

		HibernateScrollUtil<Project> scr = hcu.getPage(searchMeta);
		// set output
		BProject[] pa = BProject.makeArray(scr, num);
		ret.setItems(pa);

		if (searchMeta.isUsingPaging()) {
			int count = pa.length + searchMeta.getPagingFirstItemIndex();
			while (scr.next()) {
				scr.get();
				count++;
			}
			ret.setTotalItemsPaging(count);
		}
		return ret;
	}

	private static HibernateCriteriaUtil<Project> getSearchOrgProjectsHCU(final int max, final String personId, final boolean showAssigned, final String orgGroupId, final String projectSummary, final String extReference, final int fromDate, final int toDate, final String[] projectCategoryId, final String[] projectTypeId, final String[] projectStatusId, final String companyId, BSearchMetaInput searchMeta, boolean sort) {
		final HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class, "proj").setMaxResults(max);
		HibernateCriteriaUtil empJoinHCU = null;

		if (BusinessLogicBase.isEmpty(personId)) {
			// if person is empty we want to find projects that are not yet assigned to a person
			// but are in the specified org group via the route stop
			if (!showAssigned)
				hcu.sizeEq(Project.PROJECT_EMPLOYEE_JOIN, 0);
			// or the following with the requested company
			BOrgGroup borg = new BOrgGroup(orgGroupId);

			HibernateCriteriaUtil hc2 = hcu.joinTo(Project.CURRENT_ROUTE_STOP).leftJoinTo(RouteStop.ORG_GROUP);
			HibernateCriterionUtil hcri1 = hc2.makeCriteria().eq(OrgGroup.ORGGROUPID, orgGroupId);
			HibernateCriterionUtil hcri2 = hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
			HibernateCriterionUtil hcri3 = hc2.makeCriteria().eq("proj." + Project.REQUESTING_ORG_GROUP, borg.getOrgGroup().getOwningCompany());
			HibernateCriterionUtil hcriand = hc2.makeCriteria().and(hcri2, hcri3);
			HibernateCriterionUtil hcrior = hc2.makeCriteria().or(hcriand, hcri1);
			hcrior.add();
			//	hcu.orderBy(Project.PRIORITY_ORGGROUP).orderBy(Project.PROJECTNAME);
		} else {
			// else if person is present we want to see all projects assigned to person, regardless of org group
			empJoinHCU = hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN);
			//	.orderBy(ProjectEmployeeJoin.EMPLOYEE_PRIORITY);
			empJoinHCU.joinTo(ProjectEmployeeJoin.PERSON).eq(Person.PERSONID, personId);
			//	hcu.orderBy(Project.PROJECTNAME);
		}

		if (!BusinessLogicBase.isEmpty(projectSummary))
			hcu.like(Project.DESCRIPTION, projectSummary);
		if (!BusinessLogicBase.isEmpty(extReference))
			hcu.like(Project.REFERENCE, extReference);
		if (fromDate > 0)
			hcu.ge(Project.DATEREPORTED, fromDate);
		if (toDate > 0)
			hcu.le(Project.DATEREPORTED, toDate);
		if (projectCategoryId.length > 0)
			hcu.joinTo(Project.PROJECTCATEGORY).in(ProjectCategory.PROJECTCATEGORYID, projectCategoryId);
		if (projectTypeId.length > 0)
			hcu.joinTo(Project.PROJECTTYPE).in(ProjectType.PROJECTTYPEID, projectTypeId);
		HibernateCriteriaUtil statusHcu = hcu.joinTo(Project.PROJECTSTATUS);
		if (projectStatusId.length > 0)
			statusHcu.in(ProjectStatus.PROJECTSTATUSID, projectStatusId);
		else
			statusHcu.eq(ProjectStatus.ACTIVE, 'Y');
		if (!BusinessLogicBase.isEmpty(companyId))
			hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.OWNINGCOMPANY, ArahantSession.getHSU().get(CompanyBase.class, companyId));
		if (sort)
			switch (searchMeta.getSortType()) {
				case 1:
					hcu.orderBy(Project.PROJECTNAME, searchMeta.isSortAsc());
					break;
				case 2:
					hcu.orderBy(Project.DESCRIPTION, searchMeta.isSortAsc());
					break;
				case 3:
					statusHcu.orderBy(ProjectStatus.CODE, searchMeta.isSortAsc());
					break;
				case 4:
					hcu.orderBy(Project.PRIORITY_COMPANY, searchMeta.isSortAsc());
					break;
				case 5:
					hcu.orderBy(Project.PRIORITY_ORGGROUP, searchMeta.isSortAsc());
					break;
				case 6:
					hcu.orderBy(Project.PRIORITY_CLIENT, searchMeta.isSortAsc());
					break;
				case 7:
					if (empJoinHCU != null)
						empJoinHCU.orderBy(ProjectEmployeeJoin.EMPLOYEE_PRIORITY, searchMeta.isSortAsc());
					else
						hcu.orderBy(Project.PROJECTNAME, searchMeta.isSortAsc());
					break;
			}
		return hcu;
	}
	
	private static boolean searchEmpty(String val) {
		return val == null || val.trim().length() == 0 || "%".equals(val);
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)			{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();
		try
		{
			checkLogin(in);

			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);
			
			ret.setItem(BProjectCategory.search(all,in.getCode(),in.getDescription(),in.getExcludeIds(),ret.getCap()));

			if (!isEmpty(in.getParentId()))
			{
				BProject p=new BProject(in.getParentId());
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(p.getProjectCategoryId())));
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)			{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();
		try
		{
			checkLogin(in);
			
			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);

			ret.setItem(BProjectType.search(all,in.getCode(),in.getDescription(),in.getExcludeIds(),ret.getCap()));
	
			if (!isEmpty(in.getParentId()))
			{
				BProject p=new BProject(in.getParentId());
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(p.getProjectTypeId())));
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)			{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			 
			ret.setReportUrl(new ProjectGroupReport().build(in.getOrgGroupId(),in.getExtReference(),
					in.getFromDate(),in.getToDate(),in.getPersonId(),in.getStatusIds(),
					in.getCategoryIds(),in.getTypeIds(),in.getProjectSummary(),
					in.getCompanyId(),in.getShowAssigned()));
			
		
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)		
	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			if (BRight.checkRight("SeeAllOrgGroups")==ACCESS_LEVEL_WRITE)
				ret.setItem(BOrgGroup.searchCompanySpecific(in.getName(), false, ret.getHighCap()));
			else
				ret.setItem(BPerson.getCurrent().searchSubordinateGroups(hsu, in.getName(), ret.getHighCap()));
			
			if (!isEmpty(in.getId()))
			{
				if (ret.getItem().length<=ret.getLowCap())
				{
					//if it's in the list, set selected item
					for (SearchOrgGroupsReturnItem ogri : ret.getItem())
						if (in.getId().equals(ogri.getId()))
							ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
				}
				else
				{
					for (BOrgGroup borg : BPerson.getCurrent().searchSubordinateGroups(hsu, in.getName(), 0))
						if (in.getId().equals(borg.getOrgGroupId()))
							ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
				}
			}
			else
			{
				OrgGroup og=hsu.createCriteria(OrgGroup.class)
					.joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
					.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
					.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
					.first();
				
				
				if (og==null)
					og=hsu.createCriteria(OrgGroup.class)
						.joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
						.first();
				
				if (og!=null)
					ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(og)));
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
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
			
			final BProject x=new BProject(in.getId());
			
			boolean changingAssignments=false;
			
			if (in.getPersonIds().length>0)
			{
				List <String> p1=new LinkedList<String>();
				List <String> p2=new LinkedList<String>();
				for (Person p : x.getAssignedPersons2(null))
					p1.add(p.getPersonId());
				
				Collections.addAll(p2, in.getPersonIds());
				
				changingAssignments=!(p1.containsAll(p2)&&p2.containsAll(p1));
			}
			
			
			if (x.getBillable()=='U' && isEmpty(in.getBillable()) && changingAssignments)
			{
				ret.setNeedsBillable(true);
			}
			else
			{
				in.setData(x);
				x.update();
			}
			finishService(ret);
				
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
		
		
	
	@WebMethod()
	public NewProjectReturn newProject(/*@WebParam(name = "in")*/final NewProjectInput in)	{
		final NewProjectReturn ret=new NewProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject bp=new BProject();
			bp.create();
			
			in.makeProject(bp);
			
			if (!isEmpty(in.getParentId()))
			{
				bp.setBillable('U');
				bp.setBillingRate(new BProject(in.getParentId()).getBillingRate());
			}
			
			bp.insert();
			
			ret.setProjectId(bp.getProjectId());
			ret.setProjectName(bp.getProjectName());
			
			if (!isEmpty(in.getParentId()))
				new BProject(in.getParentId()).addChildren(new String[]{bp.getProjectId()});

			
			finishService(ret);

		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;

	}
	
	
	@WebMethod()
	public GetDefaultsForCategoryAndTypeReturn getDefaultsForCategoryAndType(/*@WebParam(name = "in")*/final GetDefaultsForCategoryAndTypeInput in)			{
		final GetDefaultsForCategoryAndTypeReturn ret=new GetDefaultsForCategoryAndTypeReturn();
		try
		{
			checkLogin(in);
			BRouteTypeAssoc rta=new BRouteTypeAssoc(in.getProjectCategoryId(),in.getProjectTypeId());
			
			if (!isEmpty(rta.getRouteId()))
			{
				BRoute rt=new BRoute(rta.getRouteId());
				if (rt.hasInitialRouteStop() && rt.hasInitialStatus())
					ret.setData(rta);
			}
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	
	@WebMethod()
	public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in)			{
		final SearchCompanyByTypeReturn ret=new SearchCompanyByTypeReturn();
		try
		{
			checkLogin(in);
			
			if (hsu.getCurrentPerson().getOrgGroupType()==CLIENT_TYPE)
			{
				BPerson bp=new BPerson(hsu.getCurrentPerson());
				BCompanyBase []ar=new BCompanyBase[1];
				ar[0]=bp.getCompany();
				ret.setCompanies(ar);
			}
			else
				ret.setCompanies(BCompanyBase.searchCompanySpecific(in.getName(), false, ret.getHighCap()));
		
			if (in.getAutoDefault())	
				ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BPerson(hsu.getCurrentPerson()).getCompany()));
			else
				if (!isEmpty(in.getCompanyId()))
					ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getCompanyId())));

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in)		
	{
		final SearchOrgGroupsForCompanyReturn ret=new SearchOrgGroupsForCompanyReturn();
		try
		{
			checkLogin(in);
		
			if ("ReqCo".equals(in.getCompanyId()))
			{
				SearchOrgGroupsForCompanyReturnItem ri=new SearchOrgGroupsForCompanyReturnItem();
				ri.setId("ReqOrg");
				ri.setName("Requesting Organizational Group");
				SearchOrgGroupsForCompanyReturnItem []items=new SearchOrgGroupsForCompanyReturnItem[1];
				items[0]=ri;
				ret.setItem(items);
				ret.setSelectedItem(ri);
			}
			else
			{

				ret.setItem(BCompanyBase.get(in.getCompanyId())
						.searchOrgGroups(in.getName(),ret.getHighCap()));
			}
			if (in.getAutoDefault()&&hsu.getCurrentPerson().getOrgGroupAssociations().size()==1)
				ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(
						new BOrgGroup(hsu.getCurrentPerson().getOrgGroupAssociations()
						.iterator().next().getOrgGroup())));
			
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
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
			BProject proj=null;
			if (!isEmpty(in.getProjectId()))
				proj=new BProject(in.getProjectId());
			//add to query, code and description

			HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription());

			hcu.notIn(ProjectStatus.PROJECTSTATUSID, in.getExcludeIds());

			if (!isEmpty(in.getRouteStopId())) {
				/*the routeStopId is used to filter the statuses down to those statuses that are allowed for that route stop
				- if routeStopId is passed then excludeAlreadyUsed and routePathId should be examined, otherwise ignore them
				- excludeAlreadyUsed and routePathId are really used in tandem as follows:
				- excludeAlreadyUsed = false means you can ignore routePathId and no further filtering is needed
				- excludeAlreadyUsed = true means that i am searching for from statues for a route path:
				- do the routeStopId filtering above for allowed statuses
				- also exclude statuses already used in route paths with 
				the specified from routeStopId, unless the route path is the specified routePathId ***
				
				 *** This is done that way to handle new and existing route paths.  
				For new ones, I won't have a routePathId to send you.  
				For existing ones, I don't want you to exclude the status used as the
				from status of that route path even though it is already used, since it 
				is in fact being used by this one and the user needs to see it.  Therefore,
				I give you the routePathId so you can know to not filter that one out.
				 */
				
				hcu.joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).eq(RouteStop.ROUTE_STOP_ID, in.getRouteStopId());
				
				if (in.getExcludeAlreadyUsed())
				{
					
					List ids=hsu.createCriteria(ProjectStatus.class)
						.selectFields(ProjectStatus.PROJECTSTATUSID)
						.joinTo(ProjectStatus.FROMROUTEPATHS)
						.ne(RoutePath.ROUTE_PATH_ID, in.getRoutePathId())
						.joinTo(RoutePath.FROM_ROUTE_STOP)
						.eq(RouteStop.ROUTE_STOP_ID, in.getRouteStopId())
						.list();
					hcu.notIn(ProjectStatus.PROJECTSTATUSID, ids);
				}
			}
			if (!isEmpty(in.getStatusId())) {
				ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(in.getStatusId()),proj));
			}
			ret.setItem(BProjectStatus.makeArray(hcu.list()),proj);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
				
			ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatus"));
			
			if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
			{
				BProject proj=new BProject(in.getProjectId());
			
				if (isEmpty(proj.getOrgGroupId()))
				{
					BOrgGroup borg=new BOrgGroup(proj.getRequestingOrgGroupId());

					List <String> ogl=borg.getAllOrgGroupsInHierarchy();

					if (hsu.createCriteria(OrgGroupAssociation.class)
						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
						.joinTo(OrgGroupAssociation.ORGGROUP)
						.in(OrgGroup.ORGGROUPID, ogl)
						.exists())
					{

						ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));


						if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
						{
							//see if it's assigned
							if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
								ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));

						}
					}
				}
				if (!isEmpty(proj.getOrgGroupId()))
				{
					BOrgGroup borg=new BOrgGroup(proj.getOrgGroupId());

					List <String> ogl=borg.getAllOrgGroupsInHierarchy();

					if (hsu.createCriteria(OrgGroupAssociation.class)
						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
						.joinTo(OrgGroupAssociation.ORGGROUP)
						.in(OrgGroup.ORGGROUPID, ogl)
						.exists())
					{

						ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));


						if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
						{
							//see if it's assigned
							if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
								ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));

						}
					}
				}
			}

			ret.setClientPriorityAccessLevel(BRight.checkRight("AccessClientPriority"));
			
			int cal=2;
			if (hsu.getCurrentPerson().getOrgGroupType()==CLIENT_TYPE)
				cal=0;
			ret.setCompanyAccessLevel(cal);
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	

        @WebMethod()
	public ListProjectPersonAssignmentsReturn listProjectPersonAssignments(/*@WebParam(name = "in")*/final ListProjectPersonAssignmentsInput in)		
	{
		final ListProjectPersonAssignmentsReturn ret=new ListProjectPersonAssignmentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BProject(in.getProjectId()).getAssignedPersons2());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SearchAssignablePersonsForProjectReturn searchAssignablePersonsForProject(/*@WebParam(name = "in")*/final SearchAssignablePersonsForProjectInput in)		
	{
		final SearchAssignablePersonsForProjectReturn ret=new SearchAssignablePersonsForProjectReturn();
		try
		{
			checkLogin(in);

			if (in.getCompanyId().equals("ReqCo"))
			{
				BProject bp=new BProject(in.getProjectId());
				in.setCompanyId(bp.getCompanyId());
				in.setOrgGroupId(bp.getOrgGroupId());
			}
			
			if (isEmpty(in.getOrgGroupId()))
				in.setOrgGroupId(in.getCompanyId());
			
			ret.setItem(BPerson.searchPersons(in.getOrgGroupId(),in.getLastName(),in.getFirstName(),in.getExcludePersonIds(),ret.getCap()));
			
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)		
	{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.searchPersons(in.getOrgGroupId(), in.getLastName(), in.getFirstName(), ret.getHighCap()));
			
			/*if (!isEmpty(in.getProjectId()))
			{
				BProject bp=new BProject(in.getProjectId());
				if (bp.getCurrentPerson()!=null)
					ret.setSelectedItem(new SearchPersonsReturnItem(bp.getCurrentPerson()));
			}*/
			
			if (!isEmpty(in.getPersonId()))
			{
				if (ret.getItem().length<=ret.getLowCap())
				{
					//if it's in the list, set selected item
					for (SearchPersonsReturnItem ogri : ret.getItem())
						if (in.getPersonId().equals(ogri.getPersonId()))
							ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(in.getPersonId())));
				}
				else
				{
					for (BPerson bp : BPerson.searchPersons(in.getOrgGroupId(), in.getLastName(), in.getFirstName(), 0))
						if (in.getPersonId().equals(bp.getPersonId()))
							ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(in.getPersonId())));

						
				}
		
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
  
        @WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in)		
	{
		final GetProjectDetailReturn ret=new GetProjectDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	

	@WebMethod()
	public ListParentProjectsReturn listParentProjects(/*@WebParam(name = "in")*/final ListParentProjectsInput in)		
	{
		final ListParentProjectsReturn ret=new ListParentProjectsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BProject(in.getId()).listParentProjects(), new BProject(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public LoadBillingReturn loadBilling(/*@WebParam(name = "in")*/final LoadBillingInput in)	{
		final LoadBillingReturn ret=new LoadBillingReturn();
				
		try
		{
			checkLogin(in);
			
			final BProject bp=new BProject(in.getProjectId());
			
			ret.setData(bp);
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	        @WebMethod()
	public CheckRightForBillingReturn checkRightForBilling(/*@WebParam(name = "in")*/final CheckRightForBillingInput in)		
	{
		final CheckRightForBillingReturn ret=new CheckRightForBillingReturn();
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessOtherBillableLevel"));
			ret.setBillableAccessLevel(BRight.checkRight("AccessBillableLevel"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
			
	@WebMethod()
	public SavePrioritiesForProjectsReturn savePrioritiesForProjects(/*@WebParam(name = "in")*/final SavePrioritiesForProjectsInput in)		
	{
		final SavePrioritiesForProjectsReturn ret = new SavePrioritiesForProjectsReturn();
		try {
			checkLogin(in);
			
			for (SavePrioritiesForProjectsInputPriorities priority : in.getPriorities()) {
				BProject project = new BProject(priority.getProjectId());
				
				switch (in.getPriorityType()) {
					case 1:
						project.setCompanyPriority(priority.getPriority());
						break;
					case 2:
						project.setOrgGroupPriority(priority.getPriority());
						break;
					case 3:
						project.setClientPriority(priority.getPriority());
						break;
					case 4:
						project.changePriority(in.getPersonId(), priority.getPriority());
						break;
					default:
						throw new ArahantException("Unepected priority type received.");
				}
				
				project.update();
			}

			finishService(ret);
		} catch (final Throwable e) {
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

}
