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
package com.arahant.services.standard.time.timesheetEntryByClockSimple;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import com.arahant.utils.IntegerDates;
import static com.arahant.utils.IntegerDates.beginningPeriodDate;
import static com.arahant.utils.IntegerDates.endingPeriodDate;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetEntryByClockSimpleOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetEntryByClockSimpleOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimesheetEntryByClockSimpleOps.class);

	public TimesheetEntryByClockSimpleOps() {
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("SetBillable"));
			ret.setChangeDescription(BRight.checkRight(CHANGE_TIMESHEET_DESCRIPTION));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();

		try {
			checkLogin(in);
			boolean includeUser = false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES) == ACCESS_LEVEL_WRITE)
				includeUser = true;

			if (hsu.currentlyArahantUser())
				includeUser = false;

			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(), in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1, includeUser));

			if (!hsu.currentlyArahantUser())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetCurrentStatusReturn getCurrentStatus(/*@WebParam(name = "in")*/final GetCurrentStatusInput in) {
		final GetCurrentStatusReturn ret = new GetCurrentStatusReturn();
		try {
			checkLogin(in);

			Timesheet ts = hsu.createCriteria(Timesheet.class)
					.le(Timesheet.WORKDATE, DateUtils.now())
					.orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME)
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(in.getPersonId()))
					.joinTo(Timesheet.PERSON)
					.eq(Person.PERSONID, in.getPersonId())
					.first();

			if (ts == null) {
				ret.setCurrentStatus("OUT");
				ret.setCurrentStatusSince("Never");
				ret.setCurrentTime(DateUtils.nowTime());
				ret.setServerTimeZone(DateUtils.getTimeZoneOffset());
			} else
				ret.setData(new BTimesheet(ts));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveCurrentStatusReturn saveCurrentStatus(/*@WebParam(name = "in")*/final SaveCurrentStatusInput in) {
		final SaveCurrentStatusReturn ret = new SaveCurrentStatusReturn();
		try {
			checkLogin(in);

			if (in.getSetToIn())
				BTimesheet.clockIn(in.getPersonId(), in.getMachineTimeZone());
			else
				BTimesheet.clockOut(in.getPersonId(), in.getMachineTimeZone());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private Project getDefault(String personId) {
		try {
			Project pr = new BPerson(personId).getDefaultProject();
			if (pr == null)
				throw new ArahantWarning("Please set up default project for user.");
			return pr;
		} catch (Exception e) {
			throw new ArahantWarning("Please set up default project for user.");
		}
	}

	@WebMethod()
	public ListAllTimesheetsReturn listAllTimesheets(/*@WebParam(name = "in")*/final ListAllTimesheetsInput in) {
		final ListAllTimesheetsReturn ret = new ListAllTimesheetsReturn();
		try {
			checkLogin(in);
			
			final BEmployee emp = new BEmployee(in.getEmployeeId());
			IntegerDates.PeriodInfo pi = new IntegerDates.PeriodInfo(emp);
			int today = DateUtils.today();
			ret.setBegDateRange(beginningPeriodDate(pi, today));
			ret.setEndDateRange(endingPeriodDate(pi, today));
			
			int fromDate = in.getFromDate();
			int toDate = in.getToDate();
			if (fromDate == 0  ||  toDate == 0) {
				fromDate = ret.getBegDateRange();
				toDate = ret.getEndDateRange();
			}

			ret.setItem(BTimesheet.search(in.getEmployeeId(), fromDate, toDate, ret.getCap()));
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
