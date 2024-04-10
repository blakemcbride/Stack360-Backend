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
package com.arahant.services.standard.hr.hrOrgGroup;

import com.arahant.beans.Employee;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroupAssociation;
import com.arahant.business.BRight;
import com.arahant.reports.EmployeeAssociatedOrgGroups;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrOrgGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrOrgGroupOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrOrgGroupOps.class);
	
	public HrOrgGroupOps() {
		super();
	}
	
	@WebMethod()
	public ListOrgGroupsReturn listOrgGroups(/*@WebParam(name = "in")*/final ListOrgGroupsInput in)			{
		final ListOrgGroupsReturn ret=new ListOrgGroupsReturn();
		try
		{
			checkLogin(in);

            BOrgGroupAssociation.deleteExpiredAssocations();
			
			final BEmployee emp=new BEmployee(in.getEmployeeId());

			ret.setItem(BOrgGroupAssociation.listOrgGroupAssociations(emp));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	 
	

	@WebMethod()
	public DissassociateFromOrgGroupsReturn dissassociateFromOrgGroups(/*@WebParam(name = "in")*/final DissassociateFromOrgGroupsInput in)			{
		final DissassociateFromOrgGroupsReturn ret=new DissassociateFromOrgGroupsReturn();
		try
		{
			checkLogin(in);
			
			new BEmployee(in.getEmployeeId()).unassignToOrgGroup(in.getOrgGroupIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public AssociateToOrgGroupsReturn associateToOrgGroups(/*@WebParam(name = "in")*/final AssociateToOrgGroupsInput in)			{
		final AssociateToOrgGroupsReturn ret=new AssociateToOrgGroupsReturn();
		try
		{
			checkLogin(in);
			
			new BEmployee(in.getEmployeeId()).assignToOrgGroup(in.getOrgGroupIds(), in.getSupervisor(), in.getStartDate(), in.getFinalDate());
			
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

			ret.setReportUrl(new EmployeeAssociatedOrgGroups().getReport(hsu.get(Employee.class,in.getEmployeeId())));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("HRNotes"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

        @WebMethod()
	public SaveOrgGroupAssociationReturn saveOrgGroupAssociation(/*@WebParam(name = "in")*/final SaveOrgGroupAssociationInput in)
	{
		final SaveOrgGroupAssociationReturn ret=new SaveOrgGroupAssociationReturn();
		try
		{
			checkLogin(in);
			
			BEmployee bemp=new BEmployee(in.getPersonId());
			bemp.assignToOrgGroup(in.getOrgGroupId(), in.getSupervisor(), in.getStartDate(), in.getFinalDate());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*@WebParam(name = "in")*/final ListOrgGroupsForGroupInput in)		
	{
		final ListOrgGroupsForGroupReturn ret=new ListOrgGroupsForGroupReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).listNotInOrgGroups(in.getOrgGroupId(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
