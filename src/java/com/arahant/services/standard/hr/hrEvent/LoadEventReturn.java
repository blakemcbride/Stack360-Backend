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
import com.arahant.business.BHREmployeeEvent;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Mar 19, 2007
 *
 */
public class LoadEventReturn extends TransmitReturnBase {

	public LoadEventReturn() {
		super();
	}

	private String supervisorId;
//	private String supervisorFirstName;
//	private String  supervisorLastName;
	private int eventDate;
	private boolean employeeNotified;
	private int employeeNotifiedDate;
	private String summary;
	private String detail;

	
	/**
	 * @param event
	 */
	void setData(final BHREmployeeEvent event) {
		supervisorId=event.getSupervisorId();
		if (supervisorId == null)
			throw new ArahantException("Unexpected null supervisorId");
		//supervisorFirstName=event.getSupervisorFirstName();
		//supervisorLastName=event.getSupervisorLastName();
		eventDate=event.getEventDate();
		employeeNotified=event.getEmployeeNotified()=='Y';
		summary=event.getSummary();
		detail=event.getDetail();
		employeeNotifiedDate=event.getDateNotified();
		
	}


	/**
	 * @return Returns the detail.
	 */
	public String getDetail() {
		return detail;
	}


	/**
	 * @param detail The detail to set.
	 */
	public void setDetail(final String detail) {
		this.detail = detail;
	}


	/**
	 * @return Returns the employeeNotified.
	 */
	public boolean isEmployeeNotified() {
		return employeeNotified;
	}


	/**
	 * @param employeeNotified The employeeNotified to set.
	 */
	public void setEmployeeNotified(final boolean employeeNotified) {
		this.employeeNotified = employeeNotified;
	}


	/**
	 * @return Returns the employeeNotifiedDate.
	 */
	public int getEmployeeNotifiedDate() {
		return employeeNotifiedDate;
	}


	/**
	 * @param employeeNotifiedDate The employeeNotifiedDate to set.
	 */
	public void setEmployeeNotifiedDate(final int employeeNotifiedDate) {
		this.employeeNotifiedDate = employeeNotifiedDate;
	}


	/**
	 * @return Returns the eventDate.
	 */
	public int getEventDate() {
		return eventDate;
	}


	/**
	 * @param eventDate The eventDate to set.
	 */
	public void setEventDate(final int eventDate) {
		this.eventDate = eventDate;
	}


	/**
	 * @return Returns the summary.
	 */
	public String getSummary() {
		return summary;
	}


	/**
	 * @param summary The summary to set.
	 */
	public void setSummary(final String summary) {
		this.summary = summary;
	}


	/**
	 * @return Returns the supervisorFirstName.
	 *
	public String getSupervisorFirstName() {
		return supervisorFirstName;
	}

	public String getSupervisorId() {
		return supervisorId;
	}


	/**
	 * @param supervisorId The supervisorId to set.
	 */
	public void setSupervisorId(final String supervisorId) {
		this.supervisorId = supervisorId;
	}


	/**
	 * @return Returns the supervisorLastName.
	 *
	public String getSupervisorLastName() {
		return supervisorLastName;
	}


	/**
	 * @param supervisorLastName The supervisorLastName to set.
	 *
	public void setSupervisorLastName(final String supervisorLastName) {
		this.supervisorLastName = supervisorLastName;
	}
	 */ 
}

	
