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
 * Created on Apr 5, 2007
 * 
 */
package com.arahant.services.standard.time.unpaidTime;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BTimesheet;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Apr 5, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeUnpaidTimeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class UnpaidTimeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			UnpaidTimeOps.class);


	public UnpaidTimeOps() {
		super();
	}
	@WebMethod()
	public ListUnpaidTimeReturn listUnpaidTime(/*@WebParam(name = "in")*/final ListUnpaidTimeInput in)	{
		final ListUnpaidTimeReturn ret=new ListUnpaidTimeReturn();
		
		try
		{
			checkLogin(in);

			ret.setData(BTimesheet.listUnpaidTime(hsu));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public GetUnpaidTimeReportReturn getUnpaidTimeReport(/*@WebParam(name = "in")*/final GetUnpaidTimeReportInput in)	{
		final GetUnpaidTimeReportReturn ret=new GetUnpaidTimeReportReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setFileName(BTimesheet.getUnpaidReport(hsu));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public CompleteUnpaidTimeReturn completeUnpaidTime(/*@WebParam(name = "in")*/final CompleteUnpaidTimeInput in)	{
		final CompleteUnpaidTimeReturn ret=new CompleteUnpaidTimeReturn();
		
		try
		{
			checkLogin(in);
			
			BTimesheet.markUnpaidHandled(hsu,in.getUnpaidTimesheetIds());

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
}

/*

UnpaidTime
 
1. listUnpaidTime
 
A. input: none
B. output: unpaidTimeId (whatever it is) employee f name, employee l name, date, hours, project name, project description
C. remarks: lists all approved time that is logged against a project that is associated to a benefit with unpaid = true that has not yet been completed for the unpaid process
 
 
2. completeUnpaidTime
 
A. input: one or more unpaidTimeIds
B. remarks: marks the unpaidTime as completed, causing it to be removed from view of listUnpaidTime
 
 
3. getUnpaidTimeReport
 
A. input: none
B. remarks: lists all approved time that is logged against a project that is associated to a benefit with unpaid = true that has not yet been completed for the unpaid process ... include employee f name, employee l name, date, hours, project name, project description
 */
