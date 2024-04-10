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
package com.arahant.services.standard.time.timeOffRequestReview;

import com.arahant.beans.Person;
import com.arahant.beans.TimeOffRequest;
import com.arahant.business.*;
import com.arahant.reports.TimeOffRequestReviewReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimeOffRequestReviewOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimeOffRequestReviewOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimeOffRequestReviewOps.class);

	public TimeOffRequestReviewOps() {
	}

	@WebMethod()
	public SaveTimeOffRequestsReturn saveTimeOffRequests(/*@WebParam(name = "in")*/final SaveTimeOffRequestsInput in) {
		final SaveTimeOffRequestsReturn ret = new SaveTimeOffRequestsReturn();
		try {
			checkLogin(in);

			for (SaveTimeOffRequestsInputItem item : in.getItem()) {
				final BTimeOffRequest x = new BTimeOffRequest(item.getId());
				item.setData(x);
				x.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListTimeOffRequestsReturn listTimeOffRequests(/*@WebParam(name = "in")*/final ListTimeOffRequestsInput in) {
		final ListTimeOffRequestsReturn ret = new ListTimeOffRequestsReturn();
		try {
			checkLogin(in);
			BTimeOffRequest [] r = BTimeOffRequest.listSubordinatesRequests(in.isIncludeApproved());
			ret.setItem(r);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEventsReturn listEvents(/*@WebParam(name = "in")*/final ListEventsInput in) {
		final ListEventsReturn ret = new ListEventsReturn();
		try {
			checkLogin(in);

			BPerson bp = BPerson.getCurrent();

			List<String> subs = BPerson.getSubordinateIds(false, true);

			HashSet<Person> peopleSet = new HashSet<Person>();
			peopleSet.addAll(hsu.createCriteria(Person.class)
					.in(Person.PERSONID, subs)
					.joinTo(Person.TIME_OFF_REQUESTS)
					.ne(TimeOffRequest.STATUS, 'R')
					.dateInside(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, in.getDate())
					.list());

			/*	peopleSet.addAll(hsu.createCriteria(Person.class)
			 .in(Person.PERSONID, subs)
			 .joinTo(Person.APPOINTMENT_PERSON_JOIN)
			 .joinTo(AppointmentPersonJoin.APPOINTMENT)
			 .eq(Appointment.DATE, in.getDate())
			 .list());
			 */
			ArrayList<Person> people = new ArrayList<Person>(peopleSet.size());
			people.addAll(peopleSet);

			ListEventsReturnItem[] item = new ListEventsReturnItem[people.size()];
			for (int loop = 0; loop < item.length; loop++)
				//create a return item per person
				item[loop] = new ListEventsReturnItem(people.get(loop), in.getDate());

			ret.setItem(item);

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

			ret.setReportUrl(new TimeOffRequestReviewReport().build());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAccruedTimeOffReturn listAccruedTimeOff(/*@WebParam(name = "in")*/final ListAccruedTimeOffInput in) {
		final ListAccruedTimeOffReturn ret = new ListAccruedTimeOffReturn();
		try {
			checkLogin(in);

			BEmployee bemp = new BEmployee(in.getPersonId());

			//get all benefit id's that this guy has access to
			BProject[] projs = BHRAccruedTimeOff.listAccrualProjects(in.getPersonId());

			ListAccruedTimeOffReturnItem[] i = new ListAccruedTimeOffReturnItem[projs.length];

			for (int loop = 0; loop < i.length; loop++) {
				i[loop] = new ListAccruedTimeOffReturnItem();
				i[loop].setName(projs[loop].getBenefitName());
				i[loop].setHours(projs[loop].getBenefitAccrual(bemp));
			}

			ret.setItem(i);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
