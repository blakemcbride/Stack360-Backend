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
 *
 * Created on Feb 4, 2007
 */
package com.arahant.services.standard.time.timesheetEntryExceptions;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeTimesheetEntryExceptionsOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimesheetEntryExceptionsOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(TimesheetEntryExceptionsOps.class);
	

	/*
	 * getTimesheetExceptionReport
 
Input:
 
  caller
  fromDate
  toDate
 
Output:
 
  reportURL
	 */

	@WebMethod()
	public GetTimesheetExceptionReportReturn getTimesheetExceptionReport(/*@WebParam(name = "in")*/final GetTimesheetExceptionReportInput in) {
		final GetTimesheetExceptionReportReturn ret = new GetTimesheetExceptionReportReturn();

		try {
			checkLogin(in);
			ret.setReportUrl(BPerson.getCurrent().getTimesheetExceptionReport(in.getFromDate(), in.getToDate(), in.isIncludeSelf()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	/*
	 * listEmployeesForTimesheetExceptionReport
	 
	Input:
	 
	  caller
	  from date
	  to date
	 
	 
	Output:
	 
	  employee person id (need for the message display functionality)
	  employee login id (need for the message display functionality)
	  employee first name
	  employee last name
	  date during the range that time was missed
	 
	remarks
	 
	 capped at 50 entries
	 subordinate functionality in effect
	 */

	@WebMethod()
	public ListEmployeesForTimesheetExceptionReportReturn listEmployeesForTimesheetExceptionReport(/*@WebParam(name = "in")*/final ListEmployeesForTimesheetExceptionReportInput in) {
		final ListEmployeesForTimesheetExceptionReportReturn ret = new ListEmployeesForTimesheetExceptionReportReturn();

		try {
			checkLogin(in);

			final BPerson[] p = BPerson.getCurrent().listEmployeesForTimesheetExceptionReport(in.isIncludeSelf(), in.getFromDate(), in.getToDate());

			ret.setMissingTimes(p);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CreateMessageReturn createMessage(/*@WebParam(name = "in")*/final CreateMessageInput in) {
		final CreateMessageReturn ret = new CreateMessageReturn();

		try {
			checkLogin(in);

			for (int loop = 0; loop < in.getToPersonId().length; loop++) {
				final BMessage m = new BMessage();
				m.create();
				m.setFromPerson(BPerson.getCurrent());
				m.setMessage(in.getMessage());
				m.setSubject(in.getSubject());
				m.insert();
				BMessage.createToRecord(m.getMessageId(), in.getToPersonId()[loop]);
				ret.setId(m.getMessageId());
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}

/*
EmployeeEditorService.getTimesheetExceptionReportObj																																																																																																																																																																																																																																																													
EmployeeEditorService.listEmployeesForTimesheetExceptionReportObj																																																																																																																																																																																																																																																													
MessageManagementService.createMessageObj																																																																																																																																																																																																																																																													
*/
