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


/**
 * 
 */
package com.arahant.services.standard.project.orgGroupProjectListClient;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.RoutePath;
import com.arahant.beans.RouteStop;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.reports.ProjectGroupReport;
import com.arahant.reports.ProjectGroupReportClient;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

  
/**
 *          
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectOrgGroupProjectListClientOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OrgGroupProjectListClientOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			OrgGroupProjectListClientOps.class);
	
	public OrgGroupProjectListClientOps() {
		super();
	}


	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in)			{
		final SearchProjectsReturn ret=new SearchProjectsReturn();
		try
		{
			checkLogin(in);
			ret.sortAsc=in.getSortAsc();
			ret.sortOn=in.getSortOn();
			
			ret.setItem(BProject.searchOrgGroupProjectsClient(in.getOrgGroupId(), in.getProjectSummary(), 
					in.getExtReference(), in.getToDate(), in.getToDate(), in.getCategoryIds(), 
					in.getTypeIds(), in.getStatusIds(), 
					in.getShowAssigned(), in.getShowUnassigned(), ret.getCap()),in.getOrgGroupId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
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
			 
			ret.setReportUrl(new ProjectGroupReportClient().build(in.getOrgGroupId(),in.getExtReference(),
					in.getFromDate(),in.getToDate(),in.getStatusIds(),
					in.getCategoryIds(),in.getTypeIds(),in.getProjectSummary(),
					in.getShowAssigned(), in.getShowUnassigned()));
			
		
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
				ret.setCompanies(BCompanyBase.search(in.getName(),false,ret.getHighCap()));
		
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
		try {
			checkLogin(in);

			ret.setItem(new BProject(in.getProjectId()).getAssignedPersons2());
			
			finishService(ret);
		} catch (final Exception e) {
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
	public SavePrioritiesForProjectsReturn savePrioritiesForProjects(/*@WebParam(name = "in")*/final SavePrioritiesForProjectsInput in)		
	{
		final SavePrioritiesForProjectsReturn ret = new SavePrioritiesForProjectsReturn();
		try {
			checkLogin(in);
			
			for (SavePrioritiesForProjectsInputPriorities priority : in.getPriorities()) {
				BProject project = new BProject(priority.getProjectId());
				
				project.setClientPriority(priority.getPriority());
				
				project.update();
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
