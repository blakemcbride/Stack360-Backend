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
package com.arahant.services.standard.project.routeStopCheckListDetail;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProjectCheckListReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectRouteStopCheckListDetailOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class RouteStopCheckListDetailOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			RouteStopCheckListDetailOps.class);
	
	public RouteStopCheckListDetailOps() {
		super();
	}
	
        @WebMethod()
	public ListCheckListDetailsForRouteStopReturn listCheckListDetailsForRouteStop(/*@WebParam(name = "in")*/final ListCheckListDetailsForRouteStopInput in)		
	{
		final ListCheckListDetailsForRouteStopReturn ret=new ListCheckListDetailsForRouteStopReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProjectChecklistDetail.list(in.getProjectId(),in.getRouteStopId()));
			
			//eliminate dummy records
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
	public ListCompanyAndOrgGroupsForProjectReturn listCompanyAndOrgGroupsForProject(/*@WebParam(name = "in")*/final ListCompanyAndOrgGroupsForProjectInput in) {
		final ListCompanyAndOrgGroupsForProjectReturn ret = new ListCompanyAndOrgGroupsForProjectReturn();
		try {
			checkLogin(in);

			BProject proj=new BProject(in.getProjectId());
			
			ret.setItem(new BRoute(proj.getRouteId()).listOrgGroups());
			
			BRouteStop rs=new BRouteStop(proj.getRouteStopId());
			
			for (int loop=0; loop<ret.getItem().length;loop++)
			{
				ListCompanyAndOrgGroupsForProjectReturnItem item=ret.getItem()[loop];

				if (rs.getOrgGroupId().equals(item.getOrgGroupId()))
				{
					ret.setCurrentIndex(loop);
					break;
				}
			}

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
	public ListRouteStopsForProjectReturn listRouteStopsForProject(/*@WebParam(name = "in")*/final ListRouteStopsForProjectInput in) {
		final ListRouteStopsForProjectReturn ret = new ListRouteStopsForProjectReturn();
		try {
			checkLogin(in);

			String key=in.getOrgGroupId();
			if (isEmpty(key))
				key=in.getCompanyId();
			
			BProject proj=new BProject(in.getProjectId());
			
			ret.setCurrentRouteStopId(proj.getRouteStopId());
			
			ret.setItem(new BRoute(proj.getRouteId()).listRouteStops(key, ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public LoadCheckListDetailReturn loadCheckListDetail(/*@WebParam(name = "in")*/final LoadCheckListDetailInput in)		
	{
		final LoadCheckListDetailReturn ret=new LoadCheckListDetailReturn();
		try
		{
			checkLogin(in);
			
			if (!isEmpty(in.getCheckListDetailId()))
				ret.setData(new BProjectChecklistDetail(in.getCheckListDetailId()));
			else
				ret.setData(new BRouteStopChecklist(in.getCheckListId()));
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewCheckListDetailReturn newCheckListDetail(/*@WebParam(name = "in")*/final NewCheckListDetailInput in)		
	{
		final NewCheckListDetailReturn ret=new NewCheckListDetailReturn();
		try
		{
			checkLogin(in);
			
			final BProjectChecklistDetail x=new BProjectChecklistDetail();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SaveCheckListDetailReturn saveCheckListDetail(/*@WebParam(name = "in")*/final SaveCheckListDetailInput in)		
	{
		final SaveCheckListDetailReturn ret=new SaveCheckListDetailReturn();
		try
		{
			checkLogin(in);
			
			final BProjectChecklistDetail x=new BProjectChecklistDetail(in.getCheckListDetailId());
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
        public DeleteCheckListDetailReturn deleteCheckListDetail(/*@WebParam(name = "in")*/final DeleteCheckListDetailInput in)		
	{
		final DeleteCheckListDetailReturn ret=new DeleteCheckListDetailReturn();
		try
		{
			checkLogin(in);
			
			BProjectChecklistDetail.delete(in.getCheckListDetailIds());
			
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
		
			ret.setReportUrl(new ProjectCheckListReport().build(in.getProjectId(),in.getRouteStopId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}
