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
package com.arahant.services.standard.hr.projectSummary;

import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import java.util.List;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrProjectSummaryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectSummaryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectSummaryOps.class);
	
	public ProjectSummaryOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			//if this is a vendor, return 1, otherwise return 2
			if (hsu.getCurrentPerson().getOrgGroupType()==VENDOR_TYPE)
				ret.setTypeAccessLevel(1);
			else
				ret.setTypeAccessLevel(2);
			
			ret.setDetailAccessLevel(1); // WMCO
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public GetAssignedEmployeeReturn getAssignedEmployee(/*@WebParam(name = "in")*/final GetAssignedEmployeeInput in)		
	{
		final GetAssignedEmployeeReturn ret=new GetAssignedEmployeeReturn();
		try
		{
			checkLogin(in);
			
			ret.setEmployeeId(new BProjectType(in.getProjectTypeId()).getRoutedEmployee(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeesForAssignmentReturn listEmployeesForAssignment(/*@WebParam(name = "in")*/final ListEmployeesForAssignmentInput in)			{
		final ListEmployeesForAssignmentReturn ret=new ListEmployeesForAssignmentReturn();
		try
		{
			checkLogin(in);

			BPerson[] emps = BEmployee.listEmployeesInPersonsGroup(hsu.getCurrentPerson(), ret.getCap());

			if (emps.length == 0) {
				if (!isEmpty(in.getProjectId())) {
					BProject proj = new BProject(in.getProjectId());

					List<Person> bap = proj.getAssignedPersons2(null);
					final int sz = bap.size();
					emps = new BPerson[sz];

					for (int loop = 0; loop < sz; loop++)
						emps[loop] = new BPerson(bap.get(loop).getPersonId());
				}
			}
		
			ret.setItem(emps);

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*@WebParam(name = "in")*/final ListProjectTypesInput in)	{
		final ListProjectTypesReturn ret=new ListProjectTypesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectTypes(BProjectType.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in)			{
		final ListStatusesReturn ret=new ListStatusesReturn();
		try
		{
			checkLogin(in);

			BProject proj=new BProject(in.getProjectId()); //WMCO WORKFLOW
			
			if (!isEmpty(proj.getRouteStopId()))
			{
				BRouteStop brs=new BRouteStop(proj.getRouteStopId());
				ret.setItem(brs.listProjectStatuses(ret.getCap()));
			}
			else
				ret.setItem(BProjectStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSummaryReturn loadSummary(/*@WebParam(name = "in")*/final LoadSummaryInput in)			{
		final LoadSummaryReturn ret=new LoadSummaryReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProjectReturn saveProject(/*@WebParam(name = "in")*/final SaveProjectInput in)			{
		final SaveProjectReturn ret=new SaveProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject x=new BProject(in.getProjectId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
