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
package com.arahant.services.standard.hr.hrEEORace;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHREEORace;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEEORaceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREEORaceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HREEORaceOps.class);

	public HREEORaceOps() {
		super();
	}
	
	
		
	@WebMethod()
		public DeleteEEORacesReturn deleteEEORaces(/*@WebParam(name = "in")*/final DeleteEEORacesInput in)		{
			final DeleteEEORacesReturn ret=new DeleteEEORacesReturn();
			try
			{
				checkLogin(in);
				
				BHREEORace.delete(hsu,in.getIds());
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public GetEEORacesReportReturn getEEORacesReport(/*@WebParam(name = "in")*/final GetEEORacesReportInput in)		{
			final GetEEORacesReportReturn ret=new GetEEORacesReportReturn();
			try
			{
				checkLogin(in);
				
				ret.setFileName(BHREEORace.getReport(hsu));
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public ListEEORacesReturn listEEORaces(/*@WebParam(name = "in")*/final ListEEORacesInput in)		{
			final ListEEORacesReturn ret=new ListEEORacesReturn();
			try
			{
				checkLogin(in);
				
				ret.setItem(BHREEORace.list(hsu));
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
	@WebMethod()
		public NewEEORaceReturn newEEORace(/*@WebParam(name = "in")*/final NewEEORaceInput in)				{
			final NewEEORaceReturn ret=new NewEEORaceReturn();
			try
			{
				checkLogin(in);
				
				final BHREEORace x=new BHREEORace();
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
		public SaveEEORaceReturn saveEEORace(/*@WebParam(name = "in")*/final SaveEEORaceInput in)		{
			final SaveEEORaceReturn ret=new SaveEEORaceReturn();
			try
			{
				checkLogin(in);
				
				final BHREEORace x=new BHREEORace(in.getId());
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
 * HrEEORace		HumanResourcesManagementService.deleteEEORacesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getEEORacesReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEEORacesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.newEEORaceObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveEEORaceObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHRSetups																																																																																																																																																																																																																																																												

 */	
