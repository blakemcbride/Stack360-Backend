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

import com.arahant.beans.Appointment;
import com.arahant.beans.AppointmentPersonJoin;
import com.arahant.beans.Person;
import com.arahant.beans.TimeOffRequest;
import com.arahant.business.BAppointment;
import com.arahant.business.BEvent;
import com.arahant.business.BEvent.Hours;
import com.arahant.business.BTimeOffRequest;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;

public class ListEventsReturnItem {

	private String personName;
	private ListEventsReturnItemHour[] hour;

	public ListEventsReturnItem() {
	}

	ListEventsReturnItem(Person p, int date) {
		personName = p.getNameFML();

		hour = new ListEventsReturnItemHour[24];
		//make the hours
		for (int loop = 0; loop < 24; loop++)
			hour[loop] = new ListEventsReturnItemHour();

		//get all the events for this person
		List<TimeOffRequest> reqs = ArahantSession.getHSU().createCriteria(TimeOffRequest.class)
				.ne(TimeOffRequest.STATUS, 'R')
				.dateInside(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, date)
				.eq(TimeOffRequest.REQUESTING_PERSON, p)
				.list();

		//get all the events for this person
		List<Appointment> appointments = ArahantSession.getHSU().createCriteria(Appointment.class)
				.eq(Appointment.DATE, date)
				.joinTo(Appointment.PERSON_JOINS)
				.eq(AppointmentPersonJoin.PERSON, p)
				.list();

		List<BEvent> requests = new ArrayList<BEvent>(reqs.size() + appointments.size());
		for (TimeOffRequest tor : reqs)
			requests.add(new BTimeOffRequest(tor));
		for (Appointment a : appointments)
			requests.add(new BAppointment(a));

		for (int reqLoop = 0; reqLoop < requests.size(); reqLoop++) {
			BEvent bto = requests.get(reqLoop);
			Hours[] h = bto.getHours(date);

			for (int loop = 0; loop < h.length; loop++)
				if (h[loop].getStartMinute() != -1)
					hour[loop].eventDetails.add(new ListEventsReturnItemHourEventDetail(bto, h[loop], reqLoop, requests.size()));
		}


		for (int loop = 0; loop < hour.length; loop++)
			hour[loop].construct();
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public ListEventsReturnItemHour[] getHour() {
		if (hour == null)
			return new ListEventsReturnItemHour[0];
		return hour;
	}

	public void setHour(ListEventsReturnItemHour[] hour) {
		this.hour = hour;
	}
}
