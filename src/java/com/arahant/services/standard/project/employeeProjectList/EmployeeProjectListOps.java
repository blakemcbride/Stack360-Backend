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
package com.arahant.services.standard.project.employeeProjectList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.reports.AssignedProjectsReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**  
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectEmployeeProjectListOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeProjectListOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeProjectListOps.class);
	
	public EmployeeProjectListOps() {
		super();
	}
	
	@WebMethod()
	public ListAssignedProjectsReturn listAssignedProjects(/*@WebParam(name = "in")*/final ListAssignedProjectsInput in)			{
		final ListAssignedProjectsReturn ret=new ListAssignedProjectsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BPerson(hsu.getCurrentPerson()).searchProjects(0,0,null,ret.getCap()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
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
			
			ret.setReportUrl(new AssignedProjectsReport().build(hsu.getCurrentPerson(),in.getProjectIds()));
			
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
	
}
