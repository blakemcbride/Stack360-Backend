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

package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.TimesheetEntryByClock1Report;
import com.arahant.reports.TimesheetEntryByClock2Report;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetEntryByClockOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetEntryByClockOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			TimesheetEntryByClockOps.class);

	public TimesheetEntryByClockOps() {
		super();
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
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(in.getPersonId()))
					.orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME)
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

	/*public static void main (String args[])
	 {
	 SaveCurrentStatusInput si = new SaveCurrentStatusInput();
	 si.setPerson("00000-0000000004");
	 si.setSetToIn(false);
	 TimesheetEntryByClockOps te = new TimesheetEntryByClockOps();
	 te.saveCurrentStatus(si);
	 }*/
	@WebMethod()
	public SearchTimesheetsReturn searchTimesheets(/*@WebParam(name = "in")*/final SearchTimesheetsInput in) {
		final SearchTimesheetsReturn ret = new SearchTimesheetsReturn();
		try {
			checkLogin(in);

			BTimesheet tsar[] = BTimesheet.searchClockEntries(in.getPersonId(), in.getFromDate(), in.getToDate(), ret.getCap());
			ret.setItem(tsar);

			//calculate timesheet totals by person by day with rounding rules

			HashMap<String, HashMap<Integer, Integer>> totals = new HashMap<String, HashMap<Integer, Integer>>();

			for (BTimesheet t : tsar) {
				if (!totals.containsKey(t.getPersonId()))
					totals.put(t.getPersonId(), new HashMap<Integer, Integer>());

				HashMap<Integer, Integer> dayTotals = totals.get(t.getPersonId());

				if (!dayTotals.containsKey(t.getWorkDate()))
					dayTotals.put(t.getWorkDate(), 0);

				dayTotals.put(t.getWorkDate(), dayTotals.get(t.getWorkDate()) + t.getElapsedTime());
			}

			int masterTotal = 0;

			for (String p : totals.keySet())
				for (int date : totals.get(p).keySet())
					masterTotal += BTimesheet.round(totals.get(p).get(date));

			ret.setTotalFormatted(BTimesheet.getSpan(masterTotal));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchTimesheetsRolledUpReturn searchTimesheetsRolledUp(/*@WebParam(name = "in")*/final SearchTimesheetsRolledUpInput in) {
		final SearchTimesheetsRolledUpReturn ret = new SearchTimesheetsRolledUpReturn();
		try {
			checkLogin(in);

			BTimesheet[] tsa = BTimesheet.searchClockEntries(in.getPersonId(), in.getFromDate(), in.getToDate(), 0);

			HashMap<Integer, SearchTimesheetsRolledUpReturnItem> map = new HashMap<Integer, SearchTimesheetsRolledUpReturnItem>();

			for (BTimesheet t : tsa) {
				if (t.getEndTime() == -1)
					continue;
				//for every day in timesheet
				for (int loop = t.getStartDate(); loop <= t.getEndDate(); loop = DateUtils.add(loop, 1))
					if (map.containsKey(loop)) {
						SearchTimesheetsRolledUpReturnItem ri = map.get(loop);
						ri.setElapsedTime(ri.getElapsedTime() + t.getElapsedTime(loop));
						ri.setElapsedTimeFormatted(BTimesheet.getSpan(ri.getElapsedTime()));
					} else
						map.put(loop, new SearchTimesheetsRolledUpReturnItem(t, loop));
			}

			ArrayList<SearchTimesheetsRolledUpReturnItem> al = new ArrayList<SearchTimesheetsRolledUpReturnItem>(map.values().size());

			//cap it at 50
			if (map.values().size() < ret.getCap())
				al.addAll(map.values());
			else {
				int count = 0;
				for (SearchTimesheetsRolledUpReturnItem si : map.values()) {
					if (++count > ret.getCap())
						break;
					al.add(si);
				}
			}
			Collections.sort(al);
			ret.setItem(al.toArray(new SearchTimesheetsRolledUpReturnItem[al.size()]));

			int masterTotal = 0;
			for (SearchTimesheetsRolledUpReturnItem si : al)
				masterTotal += BTimesheet.round(si.getElapsedTime());

			ret.setTotalFormatted(BTimesheet.getSpan(masterTotal));

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
	public NewTimesheetEntryReturn newTimesheetEntry(/*@WebParam(name = "in")*/final NewTimesheetEntryInput in) {
		final NewTimesheetEntryReturn ret = new NewTimesheetEntryReturn();
		try {
			checkLogin(in);

			//throw exception if this will be after an open timesheet
			Timesheet ts = hsu.createCriteria(Timesheet.class)
					.le(Timesheet.WORKDATE, DateUtils.now())
					.orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME)
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(in.getPersonId()))
					.joinTo(Timesheet.PERSON)
					.eq(Person.PERSONID, in.getPersonId())
					.first();

			if (ts != null && ts.getEndTime() == -1)
				if ((in.getStartDate() > ts.getWorkDate()
						|| (in.getStartDate() == ts.getWorkDate() && in.getStartTime() >= ts.getBeginningTime())))
					throw new ArahantWarning("Can not create timesheet when person is currently clocked in.");
			if (ts != null && in.getFinalTime() == -1
					&& (in.getStartDate() < ts.getWorkDate() || in.getStartDate() == ts.getWorkDate()
					&& in.getStartTime() <= ts.getBeginningTime()))
				throw new ArahantWarning("Can not create open timesheet prior to closed timesheets.");



			if (hsu.createCriteria(Timesheet.class)
					.le(Timesheet.WORKDATE, DateUtils.now())
					.dateTimeSpanCompare(Timesheet.WORKDATE, Timesheet.BEGINNINGTIME, Timesheet.ENDDATE, Timesheet.ENDTIME, in.getStartDate(),
					in.getStartTime(), in.getFinalDate(), in.getFinalTime())
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(in.getPersonId()))
					.joinTo(Timesheet.PERSON)
					.eq(Person.PERSONID, in.getPersonId())
					.exists())
				throw new ArahantWarning("Times overlap with another timesheet");

			final BTimesheet x = new BTimesheet();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveTimesheetEntryReturn saveTimesheetEntry(/*@WebParam(name = "in")*/final SaveTimesheetEntryInput in) {
		final SaveTimesheetEntryReturn ret = new SaveTimesheetEntryReturn();
		try {
			checkLogin(in);

			final BTimesheet x = new BTimesheet(in.getId());

			//throw exception if this will be after an open timesheet
			Timesheet ts = hsu.createCriteria(Timesheet.class)
					.le(Timesheet.WORKDATE, DateUtils.now())
					.ne(Timesheet.TIMESHEETID, in.getId())
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(x.getPersonId()))
					.orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME)
					.joinTo(Timesheet.PERSON)
					.eq(Person.PERSONID, x.getPersonId())
					.first();

			if (ts != null && ts.getEndTime() == -1)
				if ((in.getStartDate() > ts.getWorkDate()
						|| (in.getStartDate() == ts.getWorkDate() && in.getStartTime() >= ts.getBeginningTime())))
					throw new ArahantWarning("Can not create timesheet when person is currently clocked in.");
			if (ts != null && in.getFinalTime() == -1
					&& (in.getStartDate() < ts.getWorkDate() || in.getStartDate() == ts.getWorkDate()
					&& in.getStartTime() <= ts.getBeginningTime()))
				throw new ArahantWarning("Can not create open timesheet prior to closed timesheets.");

			if (hsu.createCriteria(Timesheet.class)
					.ne(Timesheet.TIMESHEETID, in.getId())
					.dateTimeSpanCompare(Timesheet.WORKDATE, Timesheet.BEGINNINGTIME, Timesheet.ENDDATE, Timesheet.ENDTIME, in.getStartDate(),
							in.getStartTime(), in.getFinalDate(), in.getFinalTime())
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, getDefault(x.getPersonId()))
					.joinTo(Timesheet.PERSON)
					.eq(Person.PERSONID, x.getPersonId())
					.exists())
				throw new ArahantWarning("Times overlap with another timesheet");

			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteTimesheetEntriesReturn deleteTimesheetEntries(/*@WebParam(name = "in")*/final DeleteTimesheetEntriesInput in) {
		final DeleteTimesheetEntriesReturn ret = new DeleteTimesheetEntriesReturn();
		try {
			checkLogin(in);

			BTimesheet.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			if (!in.getRolledUp())
				ret.setReportUrl(new TimesheetEntryByClock1Report().build(in.getPersonId(), in.getFromDate(), in.getToDate()));
			else
				ret.setReportUrl(new TimesheetEntryByClock2Report().build(in.getPersonId(), in.getFromDate(), in.getToDate()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAllTimesheetsReturn listAllTimesheets(/*@WebParam(name = "in")*/final ListAllTimesheetsInput in) {
		final ListAllTimesheetsReturn ret = new ListAllTimesheetsReturn();
		try {
			checkLogin(in);

			ret.setItem(BTimesheet.search(in.getEmployeeId(), in.getFromDate(), in.getToDate(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetDateMetaReturn getDateMeta(/*@WebParam(name = "in")*/final GetDateMetaInput in) {
		final GetDateMetaReturn ret = new GetDateMetaReturn();
		try {
			checkLogin(in);

			ret.setData();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
