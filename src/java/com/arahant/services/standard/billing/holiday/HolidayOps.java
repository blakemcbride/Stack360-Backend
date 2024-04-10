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
package com.arahant.services.standard.billing.holiday;

import com.arahant.beans.Right;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.BHoliday;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;


        
/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingHolidayOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HolidayOps extends ServiceBase {
	
	static ArahantLogger logger = new ArahantLogger(HolidayOps.class);
   
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();


		try {
			checkLogin(in);

                        if ((BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==BRight.ACCESS_LEVEL_WRITE) && ArahantSession.multipleCompanySupport)
                        {
                            ret.setAllCompanyAccess(true);
                        }
                        else
                            ret.setAllCompanyAccess(false);
			
			ret.setAccessLevel(BRight.checkRight("AccessHolidays"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
		

	@WebMethod()
	public ListHolidaysReturn listHolidays(/*@WebParam(name = "in")*/final ListHolidaysInput in)	{
		final ListHolidaysReturn ret=new ListHolidaysReturn();


		try {
			checkLogin(in);
			
			ret.setHolidays(BHoliday.list(hsu)); 
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	
	@WebMethod()
	public AddHolidayReturn addHoliday(/*@WebParam(name = "in")*/final AddHolidayInput in)	{
		final AddHolidayReturn ret=new AddHolidayReturn();
		try {
			checkLogin(in);
			
			final BHoliday bh=new BHoliday();
			final String id=bh.create();
                        if (in.isApplyToAll())
                                bh.setAllCompanies(true);
			in.makeHoliday(bh);
			bh.insert();

			ret.setId(id);
			
			finishService(ret);
		
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
		
	}

	@WebMethod()
	public DeleteHolidayReturn deleteHoliday(/*@WebParam(name = "in")*/final DeleteHolidayInput in)	{
		final DeleteHolidayReturn ret=new DeleteHolidayReturn();
		
		try {
			checkLogin(in);
			
			BHoliday.deleteHolidays(hsu,in.getHolidayId());
				
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveHolidayReturn saveHoliday(/*@WebParam(name = "in")*/final SaveHolidayInput in)	{	
		final SaveHolidayReturn ret=new SaveHolidayReturn();
		try {
			checkLogin(in);
			
			final BHoliday bh=new BHoliday(in.getId());
                        if(in.isApplyToAll())
                            bh.setAllCompanies(true);
			in.makeHoliday(bh);
			bh.update();
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}    
	   
	@WebMethod()
	public ResetHolidaysReturn resetHolidays(/*@WebParam(name = "in")*/final ResetHolidaysInput in)	{
		final ResetHolidaysReturn ret=new ResetHolidaysReturn();
		
		try {
			checkLogin(in);
			
			BHoliday.resetHolidays(hsu);
		
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	/*
	  	SecurityManagementService.checkRights																																																																																																																																																																																																																																																													
		TimesheetManagementService.addHolidayObj																																																																																																																																																																																																																																																													
		TimesheetManagementService.deleteHolidayObj																																																																																																																																																																																																																																																													
		TimesheetManagementService.listHolidaysObj																																																																																																																																																																																																																																																													
		TimesheetManagementService.resetHolidaysObj																																																																																																																																																																																																																																																													
		TimesheetManagementService.saveHolidayObj																																																																																																																																																																																																																																																													

	 */
}

	
