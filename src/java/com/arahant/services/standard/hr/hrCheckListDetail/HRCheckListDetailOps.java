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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrCheckListDetail;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;



/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrCheckListDetailOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRCheckListDetailOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRCheckListDetailOps.class);

	public HRCheckListDetailOps() {
		super();
	}

	
	@WebMethod()
	public DeleteChecklistDetailsReturn deleteChecklistDetails(/*@WebParam(name = "in")*/final DeleteChecklistDetailsInput in)	{
		final DeleteChecklistDetailsReturn ret=new DeleteChecklistDetailsReturn();
		
		try
		{
			checkLogin(in);
			
			BHRCheckListDetail.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetChecklistDetailsReportReturn getChecklistDetailsReport(/*@WebParam(name = "in")*/final GetChecklistDetailsReportInput in)	{
		final GetChecklistDetailsReportReturn ret=new GetChecklistDetailsReportReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setFileName(BHRCheckListDetail.getReport(hsu,in.getEmployeeId(),in.getEmployeeStatusId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public ListChecklistDetailsReturn listChecklistDetails(/*@WebParam(name = "in")*/final ListChecklistDetailsInput in)	{
		final ListChecklistDetailsReturn ret=new ListChecklistDetailsReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setDetail(BHRCheckListDetail.list(in.getEmployeeId(),in.getEmployeeStatusId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)	{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);
			
			ret.setDetail(BHREmployeeStatus.list(hsu));
			
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
			
			ret.setAccessLevel(BRight.checkRight("CheckListDetail"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	@WebMethod()
	public NewChecklistDetailReturn newChecklistDetail(/*@WebParam(name = "in")*/final NewChecklistDetailInput in)	{
		final NewChecklistDetailReturn ret=new NewChecklistDetailReturn();
		
		try
		{
			checkLogin(in);
			for (int loop=0;loop<in.getChecklistItemId().length;loop++)
			{
			final BHRCheckListDetail cli=new BHRCheckListDetail();
				ret.setId(cli.create());
				cli.setChecklistItemId(in.getChecklistItemId()[loop]);
				in.setData(cli);
				cli.insert();
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveChecklistDetailReturn saveChecklistDetail(/*@WebParam(name = "in")*/final SaveChecklistDetailInput in)	{
		final SaveChecklistDetailReturn ret=new SaveChecklistDetailReturn();
		
		try
		{
			checkLogin(in);
			final BHRCheckListDetail cli=new BHRCheckListDetail(in.getChecklistDetailId());
			in.setData(cli);
			cli.update();
			
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
				
		try
		{
			checkLogin(in);
			
			ret.setEmployees(BEmployee.searchEmployees(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(),null));
					
			if (!isEmpty(in.getPersonId()))
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getPersonId())));
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}

/*
 * HrCheckListDetail		EmployeeEditorService.searchEmployeesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.deleteChecklistDetailsObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getChecklistDetailsReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEmployeeStatusesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listChecklistDetailsObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.newChecklistDetailObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveChecklistDetailObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHR																																																																																																																																																																																																																																																												

 */	
