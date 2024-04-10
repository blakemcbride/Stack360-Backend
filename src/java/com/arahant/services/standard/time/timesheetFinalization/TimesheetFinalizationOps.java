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
package com.arahant.services.standard.time.timesheetFinalization;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.BEmployee;
import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeTimesheetFinalizationOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimesheetFinalizationOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(TimesheetFinalizationOps.class);

	/*
	 * input:
 
  finalization cutoff date (0 means no filtering)
  person ids (multiple allowed)
  caller
 
output:
 
  URL to the report PDF (i'll work on the PDF)
	 */

	@WebMethod()
	public GetFinalizeReportReturn getFinalizeReport(/*@WebParam(name = "in")*/final GetFinalizeReportInput in) {
		final GetFinalizeReportReturn ret = new GetFinalizeReportReturn();

		try {
			checkLogin(in);
			ret.setReportUrl(BPerson.getCurrent().getFinalizeReport(in.isIncludeSelf(), in.getCutoffDate()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	/*
	 *  employee person id
  employee login id (need for the message display functionality)
  employee first name
  employee last name
  last entered time on date (formatted for display)
  hours entered on last entered time on date
  finalization date (formattted for display)
  
 
remarks
 
  capped at 50 entries
  subordinate functionality in effect
  no date means show all subordinates
  if date is 12/31, include employees with finalized date of 12/31 or earlier (12/1, 11/1, etC)

	 */

	@WebMethod()
	public ListEmployeesForFinalizedReportReturn listEmployeesForFinalizedReport(/*@WebParam(name = "in")*/final ListEmployeesForFinalizedReportInput in) {
		final ListEmployeesForFinalizedReportReturn ret = new ListEmployeesForFinalizedReportReturn();
		try {
			checkLogin(in);
			final BEmployee[] p = BPerson.getCurrent().listEmployeesForFinalizedReport(in.getCutoffDate(), in.isIncludeSelf());
			ret.setEmployees(p);
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
			final BMessage m = new BMessage();
			m.create();
			m.setFromPerson(BPerson.getCurrent());
			m.setMessage(in.getMessage());
			m.setSubject(in.getSubject());
			m.insert();
			for (int loop = 0; loop < in.getToPersonId().length; loop++)
				BMessage.createToRecord(m.getMessageId(), in.getToPersonId()[loop]);
			ret.setId(m.getMessageId());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}

/*
 * 		EmployeeEditorService.getFinalizeReportObj																																																																																																																																																																																																																																																													
		EmployeeEditorService.listEmployeesForFinalizedReportObj																																																																																																																																																																																																																																																													
		MessageManagementService.createMessageObj																																																																																																																																																																																																																																																													

 */	
