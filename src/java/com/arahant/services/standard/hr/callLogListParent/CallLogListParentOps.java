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
package com.arahant.services.standard.hr.callLogListParent;
import com.arahant.beans.AIProperty;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrCallLogListParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class CallLogListParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			CallLogListParentOps.class);
	
	public CallLogListParentOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			ret.setAddAccessLevel(BRight.checkRight("canCreateProjects"));

			finishService(ret);
		} catch (final Throwable e) {
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

			BPerson [] emps= BPerson.getCurrent().getBEmployee().listSubordinates(true); //listEmployeesInPersonsGroup(hsu.getCurrentPerson(),ret.getCap());

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
	public ListProjectsReturn listProjects(/*@WebParam(name = "in")*/final ListProjectsInput in)			{
		final ListProjectsReturn ret=new ListProjectsReturn();
		try
		{
	 		checkLogin(in);

			if (!isEmpty(in.getEmployeeId()))
				ret.setItem(new BPerson(in.getEmployeeId()).listProjects(ret.getCap(),in.getStart(), in.getProjectTypeId(), in.getLocationId()));
			else
				ret.setItem(new BPerson(hsu.getCurrentPerson().getPersonId()).listProjectsForGroup(ret.getCap(),in.getStart(), in.getProjectTypeId(), in.getLocationId()));
				
			finishService(ret);
		}
		catch (final Throwable e) {
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
		//	x.setProjectCategoryId(hsu.getFirst(ProjectCategory.class).getProjectCategoryId());
			x.setProjectTypeId(in.getProjectTypeId());
			x.setRequestingOrgGroupId(hsu.getCurrentCompany().getOrgGroupId());
			x.calculateRouteAndStatus();
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
	public ListProjectTypesReturn listProjectTypes(/*@WebParam(name = "in")*/final ListProjectTypesInput in)	{
		final ListProjectTypesReturn ret=new ListProjectTypesReturn();

		try {
			checkLogin(in);
			
 			ret.setProjectTypes(BProjectType.list(hsu), (isEmpty(in.getEmployeeId()))?null:new BPerson(in.getEmployeeId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)			{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.search2(in.getFirstName(),in.getLastName(),in.getSsn(),in.getSearchType(), ret.getCap()));
			
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

                        String defaultCat=prop.getValue();
                        if(isEmpty(defaultCat))
                            defaultCat=BProperty.get("DefaultProjectCategoryId");
                        
                        if (isEmpty(defaultCat))
                            throw new ArahantWarning("Please set up system property DefaultProjectCategoryId");

			BRouteTypeAssoc rta=new BRouteTypeAssoc(defaultCat,in.getProjectTypeId());
			
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

	@WebMethod()
	public ListLocationsReturn listLocations(/*@WebParam(name = "in")*/final ListLocationsInput in)		
	{
		final ListLocationsReturn ret=new ListLocationsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOrgGroup.getLocations());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
