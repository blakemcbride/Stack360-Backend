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
package com.arahant.services.standard.project.projectRoute;

import com.arahant.beans.ProjectStatus;
import com.arahant.beans.Right;
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
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.ProjectRouteReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *         
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectRouteOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectRouteOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectRouteOps.class);

	public ProjectRouteOps() {
		super();
	}

	@WebMethod()
	public ListProjectRoutesReturn listProjectRoutes(/*@WebParam(name = "in")*/final ListProjectRoutesInput in) {
		final ListProjectRoutesReturn ret = new ListProjectRoutesReturn();
		try {
			checkLogin(in);

			ret.setItem(BRoute.list());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProjectRouteReturn newProjectRoute(/*@WebParam(name = "in")*/final NewProjectRouteInput in) {
		final NewProjectRouteReturn ret = new NewProjectRouteReturn();
		try {
			checkLogin(in);

			final BRoute x = new BRoute();
			x.create();
			in.setData(x);
			x.insert();

			if (in.getItem() != null) {
				for (int loop = 0; loop < in.getItem().length; loop++) {
					final BRouteTypeAssoc rta = new BRouteTypeAssoc();
					rta.create();
					rta.setProjectCategoryId(in.getItem()[loop].getProjectCategoryId());
					rta.setProjectTypeId(in.getItem()[loop].getProjectTypeId());
					rta.setRouteId(x.getRouteId());
					rta.insert();
				}
			// only set the route id if we had a success
			}
			ret.setRouteId(x.getRouteId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProjectRouteReturn saveProjectRoute(/*@WebParam(name = "in")*/final SaveProjectRouteInput in) {
		final SaveProjectRouteReturn ret = new SaveProjectRouteReturn();
		try {
			checkLogin(in);

			final BRoute x = new BRoute(in.getRouteId());
			in.setData(x);
			x.update();

			x.deleteAllRouteTypes();

			if (in.getItem() != null) {
				for (int loop = 0; loop < in.getItem().length; loop++) {
					final BRouteTypeAssoc rta = new BRouteTypeAssoc();
					rta.create();
					rta.setProjectCategoryId(in.getItem()[loop].getProjectCategoryId());
					rta.setProjectTypeId(in.getItem()[loop].getProjectTypeId());
					rta.setRouteId(x.getRouteId());
					rta.insert();
				}
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteProjectRouteReturn deleteProjectRoute(/*@WebParam(name = "in")*/final DeleteProjectRouteInput in) {
		final DeleteProjectRouteReturn ret = new DeleteProjectRouteReturn();
		try {
			checkLogin(in);

			new BRoute(in.getId()).delete();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public ListRouteStopsForRouteReturn listRouteStopsForRoute(/*@WebParam(name = "in")*/final ListRouteStopsForRouteInput in) {
		final ListRouteStopsForRouteReturn ret = new ListRouteStopsForRouteReturn();
		try {
			checkLogin(in);

			String key=in.getOrgGroupId();
			if (isEmpty(key))
				key=in.getCompanyId();
			ret.setItem(new BRoute(in.getRouteId()).listRouteStops(key, ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewRouteStopReturn newRouteStop(/*@WebParam(name = "in")*/final NewRouteStopInput in) {
		final NewRouteStopReturn ret = new NewRouteStopReturn();
		try {
			checkLogin(in);

			final BRouteStop x = new BRouteStop();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveRouteStopReturn saveRouteStop(/*@WebParam(name = "in")*/final SaveRouteStopInput in) {
		final SaveRouteStopReturn ret = new SaveRouteStopReturn();
		try {
			checkLogin(in);

			final BRouteStop x = new BRouteStop(in.getRouteStopId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteRouteStopReturn deleteRouteStop(/*@WebParam(name = "in")*/final DeleteRouteStopInput in) {
		final DeleteRouteStopReturn ret = new DeleteRouteStopReturn();
		try {
			checkLogin(in);

			BRouteStop.delete(hsu, in.getId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListRoutePathsReturn listRoutePaths(/*@WebParam(name = "in")*/final ListRoutePathsInput in) {
		final ListRoutePathsReturn ret = new ListRoutePathsReturn();
		try {
			checkLogin(in);

			ret.setItem(new BRouteStop(in.getRouteStopId()).list(ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
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
	public NewRoutePathReturn newRoutePath(/*@WebParam(name = "in")*/final NewRoutePathInput in) {
		final NewRoutePathReturn ret = new NewRoutePathReturn();
		try {
			checkLogin(in);

			final BRoutePath x = new BRoutePath();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveRoutePathReturn saveRoutePath(/*@WebParam(name = "in")*/final SaveRoutePathInput in) {
		final SaveRoutePathReturn ret = new SaveRoutePathReturn();
		try {
			checkLogin(in);

			final BRoutePath x = new BRoutePath(in.getRoutePathId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteRoutePathReturn deleteRoutePath(/*@WebParam(name = "in")*/final DeleteRoutePathInput in) {
		final DeleteRoutePathReturn ret = new DeleteRoutePathReturn();
		try {
			checkLogin(in);

			BRoutePath.delete(hsu, in.getIds());

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

			ret.setItem(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(in.getId())));
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(in.getId())));
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProjectRouteReturn loadProjectRoute(/*@WebParam(name = "in")*/final LoadProjectRouteInput in) {
		final LoadProjectRouteReturn ret = new LoadProjectRouteReturn();
		try {
			checkLogin(in);

			ret.setData(new BRoute(in.getRouteId()));

			finishService(ret);
		} catch (final Throwable e) {
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
			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListCompanyAndOrgGroupsForRouteReturn listCompanyAndOrgGroupsForRoute(/*@WebParam(name = "in")*/final ListCompanyAndOrgGroupsForRouteInput in) {
		final ListCompanyAndOrgGroupsForRouteReturn ret = new ListCompanyAndOrgGroupsForRouteReturn();
		try {
			checkLogin(in);

			ret.setItem(new BRoute(in.getRouteId()).listOrgGroups());

			//do a rollback to be sure I don't create a dummy some how
			hsu.rollbackTransaction();
			hsu.beginTransaction();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListStatusesForRouteStopReturn listStatusesForRouteStop(/*@WebParam(name = "in")*/final ListStatusesForRouteStopInput in) {
		final ListStatusesForRouteStopReturn ret = new ListStatusesForRouteStopReturn();
		try {
			checkLogin(in);

			ret.setItem(new BRouteStop(in.getRouteStopId()).listProjectStatuses(ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchStatusesNotForRouteStopReturn searchStatusesNotForRouteStop(/*@WebParam(name = "in")*/final SearchStatusesNotForRouteStopInput in) {
		final SearchStatusesNotForRouteStopReturn ret = new SearchStatusesNotForRouteStopReturn();
		try {
			checkLogin(in);

			ret.setItem(BProjectStatus.search(in.getCode(), in.getExcludeStatusIds(), ret.getCap()));

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

			BRoute route=null;
			BCompanyBase[] comps =null;
			
			if (isEmpty(in.getRouteId()))
				comps = BCompanyBase.search(in.getName(),false,ret.getHighCap());
			else
			{
				route=new BRoute(in.getRouteId());
				comps=route.searchCompanies(in.getName(), ret.getHighCap());
			}
			//BCompanyBase[] 
			List<BCompanyBase> l = new ArrayList(comps.length + 1);
			BCompanyBase bcb;
			if (in.getIncludeRequesting()&& (route==null || route.hasRequestingCompanies())) {
				//does the route have any nulls in the route stop
					l.add(BCompanyBase.get("ReqCo"));
			}
			Collections.addAll(l, comps);
			ret.setCompanies(l.toArray(new BCompanyBase[l.size()]));
			if (!isEmpty(in.getCompanyId())) {
				bcb = BCompanyBase.get(in.getCompanyId());
				if (bcb != null) {
					ret.setSelectedItem(new SearchCompanyByTypeReturnItem(bcb));
				}
			}

                        if (l.size()==0)
                            throw new ArahantWarning("This route cannot be used until project defaults are set.  " +
                                                     "Prior to setting project defaults, route stops must be created for this route.  " +
                                                     "Once route stops have been added, come back to this screen to set project defaults.");

			hsu.rollbackTransaction();
			hsu.beginTransaction();
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
				if (isEmpty(in.getRouteId()))
					ret.setItem(BCompanyBase.get(in.getCompanyId())
						.searchOrgGroups(in.getName(),ret.getHighCap()));
				else
					ret.setItem(new BRoute(in.getRouteId()).searchOrgGroups(in.getCompanyId(), in.getName(), ret.getHighCap()));
				
				if (!isEmpty(in.getOrgGroupId())) {
					ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(new BOrgGroup(in.getOrgGroupId())));
				}
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
	public SearchRouteStopsReturn searchRouteStops(/*@WebParam(name = "in")*/final SearchRouteStopsInput in) {
		final SearchRouteStopsReturn ret = new SearchRouteStopsReturn();
		try {
			checkLogin(in);

			ret.setItem(new BRoute(in.getRouteId()).search(in.getOrgGroupType(), in.getRouteStopName(), in.getOrgGroupName(),
					in.getCompanyName(), in.getSearchType(), in.getCompanyId(), in.getOrgGroupId(), ret.getHighCap()));


			if (!isEmpty(in.getRouteStopId())) {
				ret.setSelectedItem(new SearchRouteStopsReturnItem(new BRouteStop(in.getRouteStopId())));
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
        @WebMethod()
	public SearchProjectPhasesReturn searchProjectPhases(/*@WebParam(name = "in")*/final SearchProjectPhasesInput in)		
	{
		final SearchProjectPhasesReturn ret=new SearchProjectPhasesReturn();
		try
		{
			checkLogin(in);

			
			ret.setItem(BProjectPhase.search(in.getCode(),in.getDescription(), ret.getHighCap()));
			
			if (!isEmpty(in.getPhaseId()))
				ret.setSelectedItem(new SearchProjectPhasesReturnItem(new BProjectPhase(in.getPhaseId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new ProjectRouteReport().build(in.getRouteId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListCheckListItemsForRouteStopReturn listCheckListItemsForRouteStop(/*@WebParam(name = "in")*/final ListCheckListItemsForRouteStopInput in)		
	{
		final ListCheckListItemsForRouteStopReturn ret=new ListCheckListItemsForRouteStopReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BRouteStopChecklist.list(in.getRouteStopId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
