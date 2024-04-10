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

import com.arahant.annotation.Validation;
import com.arahant.business.BHREmployeeEvent;
import com.arahant.services.TransmitInputBase;

public class SaveEventInput extends TransmitInputBase {

	@Validation (required=true)
	private String id;
	@Validation (required=true)
	private String supervisorId;
	@Validation (type="date",required=true)
	private int eventDate;
	@Validation (required=false)
	private boolean employeeNotified;
	@Validation (type="date",required=false)
	private int employeeNotifiedDate;
	@Validation (table="hr_employee_event",column="summary",required=true)
	private String summary;
	@Validation (min=1,max=4000,table="hr_employee_event",column="detail",required=false)
	private String detail;

	void setData(final BHREmployeeEvent e) {
		e.setSupervisorId(supervisorId);
		e.setEventDate(eventDate);
		e.setEmployeeNotified(employeeNotified?'Y':'N');
		e.setDateNotified(employeeNotifiedDate);
		e.setSummary(summary);
		e.setDetail(detail);
		e.setEventType('N');
	}
	
	public SaveEventInput() {
		super();
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(final String detail) {
		this.detail = detail;
	}

	public boolean isEmployeeNotified() {
		return employeeNotified;
	}

	public void setEmployeeNotified(final boolean employeeNotified) {
		this.employeeNotified = employeeNotified;
	}

	public int getEmployeeNotifiedDate() {
		return employeeNotifiedDate;
	}

	public void setEmployeeNotifiedDate(final int employeeNotifiedDate) {
		this.employeeNotifiedDate = employeeNotifiedDate;
	}

	public int getEventDate() {
		return eventDate;
	}

	public void setEventDate(final int eventDate) {
		this.eventDate = eventDate;
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

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(final String supervisorId) {
		this.supervisorId = supervisorId;
	}
}

	
