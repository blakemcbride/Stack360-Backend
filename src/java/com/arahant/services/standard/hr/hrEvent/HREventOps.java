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
 * Created on Mar 19, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvent;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.beans.HrEmployeeEvent;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.utils.Collections;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeEvent;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import org.kissweb.DateTime;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.*;


/**
 * 
 *
 * Created on Mar 19, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEventOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREventOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HREventOps.class);
	
	public HREventOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("HREvents"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	private static class SortEntries implements Comparator<BHREmployeeEvent> {

		@Override
		public int compare(BHREmployeeEvent e1, BHREmployeeEvent e2) {
			int d1 = e1.getEventDate();
			int d2 = e2.getEventDate();
			if (d1 != d2)
				return d2 - d1;
			return e2.getEventTime() - e1.getEventTime();
		}
	}
	
	@WebMethod()
	public ListEventsReturn listEvents (/*@WebParam(name = "in")*/final ListEventsInput in)	{
		final ListEventsReturn ret=new ListEventsReturn();
		final int searchMax = BProperty.getInt(StandardProperty.SEARCH_MAX);
		final String personId = in.getPersonId();

		try {
			checkLogin(in);
			final Connection db = hsu.getKissConnection();
			int eventId = 1;

			BHREmployeeEvent[] lst = BHREmployeeEvent.list(hsu, personId, searchMax);
			if (in.getIncludeAssignments()) {
				final ArrayList<BHREmployeeEvent> alst = new ArrayList<>();
				Collections.addAll(alst, lst);
				final List<Record> recs = db.fetchAll(
						"select peh.change_date, peh.change_time, peh.change_type, " +
								"       proj.description, " +
								"       peh.change_person_id " +
								"from project_employee_history peh " +
								"join project_shift ps " +
								"  on peh.project_shift_id = ps.project_shift_id " +
								"join project proj " +
								"  on ps.project_id = proj.project_id " +
								"where peh.person_id = ?", personId);
				for (Record rec : recs) {
					BHREmployeeEvent e = new BHREmployeeEvent(new HrEmployeeEvent());
					e.setEventTime(rec.getInt("change_time"));
					e.setEventDate(rec.getInt("change_date"));
					if (rec.getString("change_type").equals("A"))
						e.setSummary("Assigned " + rec.getString("description"));
					else
						e.setSummary("De-assigned " + rec.getString("description"));
					e.setSupervisorId(rec.getString("change_person_id"));
					e.setEventId(String.valueOf(eventId++));
					alst.add(e);
				}
				lst = alst.toArray(new BHREmployeeEvent[0]);
			}
			if (in.getIncludeStatusChanges()) {
				final ArrayList<BHREmployeeEvent> alst = new ArrayList<>();
				Collections.addAll(alst, lst);
				final List<Record> recs = db.fetchAll(
						"select esh.effective_date, es.name " +
								"from hr_empl_status_history esh " +
								"join hr_employee_status es " +
								"  on esh.status_id = es.status_id " +
								"where esh.employee_id = ?", personId);
				for (Record rec : recs) {
					BHREmployeeEvent e = new BHREmployeeEvent(new HrEmployeeEvent());
					e.setEventDate(rec.getInt("effective_date"));
					e.setSummary("Status change - " + rec.getString("name"));
					e.setEventId(String.valueOf(eventId++));
					alst.add(e);
				}
				lst = alst.toArray(new BHREmployeeEvent[0]);
			}
			if (in.getIncludeCheckins()) {
				final ArrayList<BHREmployeeEvent> alst = new ArrayList<>();
				Collections.addAll(alst, lst);
				final List<Record> recs = db.fetchAll(
								"select c.*, p.lname, p.mname, p.fname " +
								"from worker_confirmation c " +
                                "left join person p " +
                                "  on c.who_added = p.person_id " +
								"where c.person_id = ? " +
                                "      and c.confirmation_time > ? " +
								"order by c.confirmation_time desc", personId,
						DateUtils.toDate(DateUtils.addDays(DateUtils.today(), -90)));
				for (Record rec : recs) {
					BHREmployeeEvent e = new BHREmployeeEvent(new HrEmployeeEvent());
					Date dt = rec.getDateTime("confirmation_time");
					int day = DateUtils.toInt(dt);
					e.setEventDate(day);
					e.setSummary("Worker check in");

					String detail;
					String whoAdded = rec.getString("who_added");
					if (whoAdded != null && !whoAdded.isEmpty() && !personId.equals(whoAdded)) {
						detail = "Reported by " + rec.getString("lname") + ", " + rec.getString("fname") + "\n";
						e.setSupervisorId(whoAdded);
					} else {
						detail = "";
						e.setSupervisorId(personId);
					}
					detail += "Reported on " + (new DateTime(dt).format()) + "\n";
					String notes = rec.getString("notes");
					if (notes != null  &&  !notes.isEmpty())
						detail += rec.getString("notes");
					e.setDetail(detail);
					e.setEventId(String.valueOf(eventId++));
					alst.add(e);
				}
				lst = alst.toArray(new BHREmployeeEvent[0]);
			}
			if (in.getIncludeAssignments() || in.getIncludeStatusChanges() || in.getIncludeCheckins()) {
				Arrays.sort(lst, new SortEntries());
				hsu.rollbackTransaction();  //  make sure records not saved
			}
			ret.setData(lst, searchMax);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public LoadEventReturn loadEvent (/*@WebParam(name = "in")*/final LoadEventInput in)	{
		final LoadEventReturn ret=new LoadEventReturn();
		
		try {
			checkLogin(in);
			
			ret.setData(new BHREmployeeEvent(in.getId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewEventReturn newEvent(/*@WebParam(name = "in")*/final NewEventInput in)	{
		final NewEventReturn ret=new NewEventReturn();
		
		try {
			checkLogin(in);

			final BHREmployeeEvent e=new BHREmployeeEvent();
			ret.setId(e.create());
			in.setData(e);
			e.insert();

			hsu.commitTransaction();
/*
			try {
				e.sendEmailNotification('W');
			} catch (Exception ex) {

			}
*/
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveEventReturn saveEvent(/*@WebParam(name = "in")*/final SaveEventInput in)	{
		final SaveEventReturn ret=new SaveEventReturn();
		
		try {
			checkLogin(in);
			
			final BHREmployeeEvent e=new BHREmployeeEvent(in.getId());
			in.setData(e);
			e.update();

			hsu.commitTransaction();
/*
			try {
				e.sendEmailNotification('W');
			} catch (Exception ex) {

			}
*/
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public DeleteEventReturn deleteEvent(/*@WebParam(name = "in")*/final DeleteEventInput in)	{
		final DeleteEventReturn ret=new DeleteEventReturn();
		
		try {
			checkLogin(in);
			
			BHREmployeeEvent.delete(hsu,in.getIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)	{
		final GetReportReturn ret=new GetReportReturn();
		final int searchMax = BProperty.getInt(StandardProperty.SEARCH_MAX);
		
		try {
			checkLogin(in);
			ret.setFileName(BHREmployeeEvent.getReport(hsu, in.getPersonId(), in.getStartDate(),in.getEndDate(),in.isAsc(), searchMax));
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
			{
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getPersonId())));
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}


/*
 * 1. check rights
 
input - standard
output - based on hr access (not setup)
remarks - ordered by most recent event date
 
 
 
2. list events for employee
 
input - employeeid
output - id, supervisor first name, supervisor last name, formatted event date, employee notified Y/N, formatted employee notified date, summary
 
 
 
3. load event
 
input - id
output - supervisor id, supervisor first name, supervisor last name, non-formatted event date, boolean employee notified, non-formatted employee notified date, summary, detail
 
 
 
4. new event
 
input - employee id, supervisor id, event date, employee notified boolean, employee notified date, summary, detail
output - event id
 
 
 
5. save event
 
input - id, supervisor id, event date, employee notified boolean, employee notified date, summary, detail
output 
 
 
 
6. delete event
 
input - one or more event ids
output
 
 
 
7. report
 
input - one or more event ids

8. Need an employee search exactly like in HrEvaluation
 */
	
