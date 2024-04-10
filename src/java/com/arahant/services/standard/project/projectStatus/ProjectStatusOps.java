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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.project.projectStatus;

import com.arahant.beans.Right;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


import com.arahant.business.BProjectStatus;
import com.arahant.business.BRight;
import com.arahant.reports.ProjectStatusReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectStatusOps extends ServiceBase {
	
	private static final ArahantLogger logger = new ArahantLogger(ProjectStatusOps.class);

	@WebMethod()
	public DeleteProjectStatusReturn deleteProjectStatus(/*@WebParam(name = "in")*/final DeleteProjectStatusInput in)	{
		final DeleteProjectStatusReturn ret=new DeleteProjectStatusReturn();

		try {
			checkLogin(in);
			
			BProjectStatus.delete(hsu,in.getProjectStatusIds());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListProjectStatusesReturn listProjectStatuses(/*@WebParam(name = "in")*/final ListProjectStatusesInput in)	{
		final ListProjectStatusesReturn ret=new ListProjectStatusesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectStatuses(BProjectStatus.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}

	@WebMethod()
	public NewProjectStatusReturn newProjectStatus(/*@WebParam(name = "in")*/final NewProjectStatusInput in)	{

		final NewProjectStatusReturn ret=new NewProjectStatusReturn();
		try {
			checkLogin(in);
			
			final BProjectStatus bp=new BProjectStatus();
			ret.setProjectStatusId(bp.create());
			in.setData(bp);
			bp.insert();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}
	
	@WebMethod()
	public SaveProjectStatusReturn saveProjectStatus(/*@WebParam(name = "in")*/final SaveProjectStatusInput in)	{
		final SaveProjectStatusReturn ret=new SaveProjectStatusReturn();
		
		try {
			checkLogin(in);
			
			final BProjectStatus bp=new BProjectStatus(in.getProjectStatusId());
			in.setData(bp);
			bp.update();
			
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
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public GetProjectStatusReportReturn getProjectStatusReport(/*@WebParam(name = "in")*/final GetProjectStatusReportInput in)	{
		final GetProjectStatusReportReturn ret=new GetProjectStatusReportReturn();

		try {			
			checkLogin(in);
			
			ret.setReportUrl(new ProjectStatusReport().build());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
 
}

	/*		
ProjectManagementService.deleteProjectStatusObj																																																																																																																																																																																																																																																													
ProjectManagementService.listProjectStatusesObj																																																																																																																																																																																																																																																													
ProjectManagementService.newProjectStatusObj																																																																																																																																																																																																																																																													
ProjectManagementService.saveProjectStatusObj																																																																																																																																																																																																																																																													
SecurityManagementService.checkRights																																																																																																																																																																																																																																																													

*/
