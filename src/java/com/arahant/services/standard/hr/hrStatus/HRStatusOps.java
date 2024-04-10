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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrStatus;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BRight;
import com.arahant.reports.HREmployeeStatusReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRStatusOps.class);

	public HRStatusOps() {
		super();
	}
	
	
		
	@WebMethod()
		public DeleteEmployeeStatusesReturn deleteEmployeeStatuses(/*@WebParam(name = "in")*/final DeleteEmployeeStatusesInput in)		{
			final DeleteEmployeeStatusesReturn ret=new DeleteEmployeeStatusesReturn();
			try
			{
				checkLogin(in);
				
				BHREmployeeStatus.delete(hsu,in.getIds());
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public GetEmployeeStatusesReportReturn getEmployeeStatusesReport(/*@WebParam(name = "in")*/final GetEmployeeStatusesReportInput in)		{
			final GetEmployeeStatusesReportReturn ret=new GetEmployeeStatusesReportReturn();
			try
			{
				checkLogin(in);
				
				ret.setFileName(new HREmployeeStatusReport().build(in.getActiveType()));
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)		{
			final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
			try
			{
				checkLogin(in);
				
				ret.setItem(BHREmployeeStatus.list(in.getActiveType()));
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public NewEmployeeStatusReturn newEmployeeStatus(/*@WebParam(name = "in")*/final NewEmployeeStatusInput in)				{
			final NewEmployeeStatusReturn ret=new NewEmployeeStatusReturn();
			try
			{
				checkLogin(in);
				
				final BHREmployeeStatus x=new BHREmployeeStatus();
				ret.setId(x.create());
				in.setData(x);
				x.insert();
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public SaveEmployeeStatusReturn saveEmployeeStatus(/*@WebParam(name = "in")*/final SaveEmployeeStatusInput in)		{
			final SaveEmployeeStatusReturn ret=new SaveEmployeeStatusReturn();
			try
			{
				checkLogin(in);
				
				final BHREmployeeStatus x=new BHREmployeeStatus(in.getId());
				in.setData(x);
				x.update();
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
		
	@WebMethod()
		public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		{
			final CheckRightReturn ret=new CheckRightReturn();
		
			try {
				checkLogin(in);
				
				ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

				finishService(ret);
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			
			return ret;
		} 		
}

/*
 * HrStatus		HumanResourcesManagementService.deleteEmployeeStatusesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getEmployeeStatusesReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEmployeeStatusesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.newEmployeeStatusObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveEmployeeStatusObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHRSetups																																																																																																																																																																																																																																																												

 */	
