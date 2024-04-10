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
package com.arahant.services.standard.hr.hrCallLogParent;
import com.arahant.beans.AIProperty;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrCallLogParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrCallLogParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrCallLogParentOps.class);
	
	public HrCallLogParentOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)			{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HR));
			ret.setAddAccessLevel(BRight.checkRight("canCreateProjects"));
			
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProjectsForPersonReturn listProjectsForPerson(/*@WebParam(name = "in")*/final ListProjectsForPersonInput in)			{
		final ListProjectsForPersonReturn ret=new ListProjectsForPersonReturn();
		try
		{
			checkLogin(in);

			final BPerson emp=new BPerson(in.getPersonId());
			ret.setItem(emp.getProjectsWhereSubjectPerson());
			ret.setSsn(emp.getSsn());
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
			
			BPerson []emps=BEmployee.listEmployeesInPersonsGroup(hsu.getCurrentPerson(),ret.getCap());
			
			BPerson []peeps;
			
			if (emps.length==0)
			{
				//add current user
				peeps=new BPerson[emps.length+1];
				for (int loop=0;loop<emps.length;loop++)
					peeps[loop]=emps[loop];

				peeps[peeps.length-1]=new BPerson(ArahantSession.getCurrentPerson()); //add current
			}
			else
				peeps=emps;

			ret.setItem(peeps);
			
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
	public NewProjectReturn newProject(/*@WebParam(name = "in")*/final NewProjectInput in)			{
		final NewProjectReturn ret=new NewProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject x=new BProject();
			ret.setProjectId(x.create());
			in.setData(x);
			x.setProjectTypeId(in.getProjectTypeId());
			x.setRequestingOrgGroupId(hsu.getCurrentCompany().getOrgGroupId());
			x.calculateRouteAndStatus(); //will also calc the category
			
			x.insert();
			
			ret.setProjectName(x.getProjectName());
			
			finishService(ret);
		}
		catch (final Throwable e) {
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
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in)		
	{
		final ListStatusesReturn ret=new ListStatusesReturn();
		try
		{
			checkLogin(in);

			AIProperty prop=new AIProperty("CalculateDefaultCategory", in.getProjectTypeId(), in.getPersonId());

			BRouteTypeAssoc rta=new BRouteTypeAssoc(prop.getValue(),in.getProjectTypeId());
			
			if (!isEmpty(rta.getRouteId()))
			{
				BRoute br=new BRoute(rta.getRouteId());
				ret.setItem(br.getInitialRouteStopStatuses());
			}	
			else	
				ret.setItem(new ListStatusesReturnItem[0]);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
