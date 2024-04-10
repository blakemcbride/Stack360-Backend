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


package com.arahant.beans;

import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name=HrEmployeeEvent.TABLE_NAME)
public class HrEmployeeEvent extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "hr_employee_event";

	// Fields    

	private String eventId;

	public static final String EVENTID = "eventId";

	private Person employeeId;

	public static final String EMPLOYEEID = "employeeId";

	private Person supervisorId;

	public static final String SUPERVISORID = "supervisorId";

	private int eventDate;

	public static final String EVENTDATE = "eventDate";

	private char employeeNotified;

	public static final String EMPLOYEENOTIFIED = "employeeNotified";

	private String summary;

	public static final String SUMMARY = "summary";

	private String detail;

	public static final String DETAIL = "detail";

	private int dateNotified;

	public static final String DATENOTIFIED = "dateNotified";

	private String eventType = "N";

	public static final String EVENT_TYPE = "eventType";

	// Constructors

	/** default constructor */
	public HrEmployeeEvent() {
	}

	/** minimal constructor */
	public HrEmployeeEvent(final String eventId, final Person employeeId,
						   final Employee supervisorId, final int eventDate,
						   final char employeeNotified, final String summary, final int dateNotified) {
		this.eventId = eventId;
		this.employeeId = employeeId;
		this.supervisorId = supervisorId;
		this.eventDate = eventDate;
		this.employeeNotified = employeeNotified;
		this.summary = summary;
		this.dateNotified = dateNotified;
	}

	/** full constructor */
	public HrEmployeeEvent(final String eventId, final Person employeeId,
						   final Employee supervisorId, final int eventDate,
						   final char employeeNotified, final String summary, final String detail,
						   final int dateNotified) {
		this.eventId = eventId;
		this.employeeId = employeeId;
		this.supervisorId = supervisorId;
		this.eventDate = eventDate;
		this.employeeNotified = employeeNotified;
		this.summary = summary;
		this.detail = detail;
		this.dateNotified = dateNotified;
	}

	// Property accessors
	@Id
	@Column (name="event_id")
	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(final String eventId) {
		this.eventId = eventId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="employee_id")
	public Person getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(final Person employeeId) {
		this.employeeId = employeeId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="supervisor_id")
	public Person getSupervisorId() {
		return this.supervisorId;
	}

	public void setSupervisorId(final Person supervisorId) {
		this.supervisorId = supervisorId;
	}

	@Column (name="event_date")
	public int getEventDate() {
		return this.eventDate;
	}

	public void setEventDate(final int eventDate) {
		this.eventDate = eventDate;
	}

	@Column (name="employee_notified")
	public char getEmployeeNotified() {
		return this.employeeNotified;
	}

	public void setEmployeeNotified(final char employeeNotified) {
		this.employeeNotified = employeeNotified;
	}

	@Column (name="summary")
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	@Column (name="detail")
	public String getDetail() {
		return this.detail;
	}

	public void setDetail(final String detail) {
		this.detail = detail;
	}

	@Column (name="date_notified")
	public int getDateNotified() {
		return this.dateNotified;
	}

	public void setDateNotified(final int dateNotified) {
		this.dateNotified = dateNotified;
	}

	@Column (name="event_type")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String keyColumn() {
		return "event_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	public String tableName() {
		
		return TABLE_NAME;
	}
	
	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	public String generateId() throws ArahantException {
		setEventId(IDGenerator.generate(this));
		return eventId;
	}
	

	public boolean equals(Object o)
	{
		if (eventId==null && o==null)
			return true;
		if (eventId!=null && o instanceof HrEmployeeEvent)
			return eventId.equals(((HrEmployeeEvent)o).getEventId());
		
		return false;
	}
	
	public int hashCode()
	{
		if (eventId==null)
			return 0;
		return eventId.hashCode();
	}
	
}
