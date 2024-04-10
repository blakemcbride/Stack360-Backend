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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = ProjectEmployeeJoin.TABLE_NAME)
public class ProjectEmployeeJoin extends ArahantBean implements Serializable {

	public final static String TABLE_NAME = "project_employee_join";
	public static final String PERSON = "person";
	public static final String EMPLOYEE_PRIORITY = "personPriority";
	public static final String DATE_ASSIGNED = "dateAssigned";
	private static final long serialVersionUID = 1L;
	private String projectEmployeeJoinId;
	private Person person;
	private short personPriority = 10000;
	private int dateAssigned;
	private int timeAssigned;
	private int startDate;
	private Date confirmedDate;
	private String confirmedPersonId;
	private Date verifiedDate;
	private String verifiedPersonId;
	private String manager = "N";
	private String hours = "Y";  // do they report hours
	public static final String REPORTS_HOURS = "hours";
	private String projectShiftId;
	private ProjectShift projectShift;
	public static final String PROJECTSHIFT = "projectShift";
	public static final String PROJECT_EMPLOYEE_JOIN_ID = "projectEmployeeJoinId";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "person_priority")
	public short getPersonPriority() {
		return personPriority;
	}

	public void setPersonPriority(short personPriority) {
		this.personPriority = personPriority;
	}

	@Id
	@Column(name = "project_employee_join_id")
	public String getProjectEmployeeJoinId() {
		return projectEmployeeJoinId;
	}

	public void setProjectEmployeeJoinId(String projectEmployeeJoinId) {
		this.projectEmployeeJoinId = projectEmployeeJoinId;
	}

	@Override
	public String tableName() {
		return ProjectEmployeeJoin.TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "project_employee_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return projectEmployeeJoinId = IDGenerator.generate(this);
	}

	@Column(name = "date_assigned")
	public int getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(int dateAssigned) {
		this.dateAssigned = dateAssigned;
	}

	@Column(name = "time_assigned")
	public int getTimeAssigned() {
		return timeAssigned;
	}

	public void setTimeAssigned(int timeAssigned) {
		this.timeAssigned = timeAssigned;
	}

	@Column(name = "start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@Column(name = "confirmed_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	@Column(name = "confirmed_person_id")
	public String getConfirmedPersonId() {
		return confirmedPersonId;
	}

	public void setConfirmedPersonId(String confirmedPersonId) {
		this.confirmedPersonId = confirmedPersonId;
	}

	@Column(name = "verified_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	@Column(name = "verified_person_id")
	public String getVerifiedPersonId() {
		return verifiedPersonId;
	}

	public void setVerifiedPersonId(String verifiedPersonId) {
		this.verifiedPersonId = verifiedPersonId;
	}

	@Column(name = "manager")
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Column(name = "hours")
	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	@Column(name = "project_shift_id", insertable = false, updatable = false)
	public String getProjectShiftId() {
		return projectShiftId;
	}

	public void setProjectShiftId(String projectShiftId) {
		this.projectShiftId = projectShiftId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_shift_id")
	public ProjectShift getProjectShift() {
		return projectShift;
	}

	public void setProjectShift(ProjectShift projectShift) {
		this.projectShift = projectShift;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProjectEmployeeJoin other = (ProjectEmployeeJoin) obj;
		if (this.projectEmployeeJoinId == null  &&  other.getProjectEmployeeJoinId() == null)
			return true;
		if (this.projectEmployeeJoinId == null  &&  other.getProjectEmployeeJoinId() != null  ||
				this.projectEmployeeJoinId != null  &&  other.getProjectEmployeeJoinId() == null)
			return false;
		return this.projectEmployeeJoinId.equals(other.getProjectEmployeeJoinId());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + (this.projectEmployeeJoinId != null ? this.projectEmployeeJoinId.hashCode() : 0);
		return hash;
	}
}
