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
 * Created on Mar 19, 2007
 */

package com.arahant.services.standard.hr.hrEvent;

import com.arahant.business.BHREmployeeEvent;
import com.arahant.utils.DateUtils;
import org.kissweb.DateTime;

import java.util.Date;

public class ListEventsItem {

	private String id;
	private String supervisorId;
	private String supervisorName;
	private String eventDateFormatted;
	private String employeeNotified;// Yes/No, 
	private String employeeNotifiedDateFormatted;
	private String summary;
	private String detail;
	private long eventDate;

	ListEventsItem(final BHREmployeeEvent event) {
		id = event.getEventId();
		if (event.getSupervisorId() != null) {
			supervisorId = event.getSupervisorId();
			supervisorName = event.getSupervisorLastName() + ", " + event.getSupervisorFirstName();
		}
		eventDateFormatted = DateUtils.getDateFormatted(event.getEventDate());
		switch (event.getEmployeeNotified()) {
			case 'Y':
				employeeNotified = "Yes";
				break;
			case 'N':
				employeeNotified = "No";
				break;
			default:
				employeeNotified = "";
				break;
		}
		employeeNotifiedDateFormatted = DateUtils.getDateFormatted(event.getDateNotified());
		summary = event.getSummary();
		if (summary == null || summary.equals("OTHER")) {
			String detail = event.getDetail();
			if (detail != null && !detail.isEmpty())
				summary = detail;
		}
		detail = event.getDetail();
		eventDate = new DateTime(event.getEventDate(), event.getEventTime()).getMilliseconds();
	}

	public ListEventsItem() {
	}

	public String getEmployeeNotified() {
		return employeeNotified;
	}

	public void setEmployeeNotified(final String employeeNotified) {
		this.employeeNotified = employeeNotified;
	}

	public String getEmployeeNotifiedDateFormatted() {
		return employeeNotifiedDateFormatted;
	}

	public void setEmployeeNotifiedDateFormatted(
			final String employeeNotifiedDateFormatted) {
		this.employeeNotifiedDateFormatted = employeeNotifiedDateFormatted;
	}

	public String getEventDateFormatted() {
		return eventDateFormatted;
	}

	public void setEventDateFormatted(final String eventDateFormatted) {
		this.eventDateFormatted = eventDateFormatted;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getEventDate() {
		return eventDate;
	}

	public void setEventDate(long eventDate) {
		this.eventDate = eventDate;
	}
}

	
