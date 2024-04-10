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
 *
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = PreviousEmployment.TABLE_NAME)
public class PreviousEmployment extends ArahantBean implements Serializable {
  
	public static final String TABLE_NAME = "previous_employment";
	public static final String EMPLOYMENT_ID = "employmentId";
	public static final String PERSON = "person";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";

	private String employmentId;
	private Person person;
	private int startDate;
	private int endDate;
	private String company;
	private String phone;
	private String supervisor;
	private String jobTitle;
	private int startingSalary;
	private int endingSalary;
	private String responsibilities;
	private String reasonForLeaving;
	private char contactSupervisor = 'N';
	private String street;
	private String city;
    private String state;

	@Column(name="company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name="contact_supervisor")
	public char getContactSupervisor() {
		return contactSupervisor;
	}

	public void setContactSupervisor(char contactSupervisor) {
		this.contactSupervisor = contactSupervisor;
	}

	@Id
	@Column(name="employment_id")
	public String getEmploymentId() {
		return employmentId;
	}

	public void setEmploymentId(String employmentId) {
		this.employmentId = employmentId;
	}

	@Column(name="end_date")
	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	@Column(name="job_title")
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="reason_for_leaving")
	public String getReasonForLeaving() {
		return reasonForLeaving;
	}

	public void setReasonForLeaving(String reasonForLeaving) {
		this.reasonForLeaving = reasonForLeaving;
	}

	@Column(name="responsibilities")
	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	@Column(name="start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@Column(name="starting_salary")
	public int getStartingSalary() {
		return startingSalary;
	}

	public void setStartingSalary(int startingSalary) {
		this.startingSalary = startingSalary;
	}

	@Column(name="ending_salary")
	public int getEndingSalary() {
		return endingSalary;
	}

	public void setEndingSalary(int endingSalary) {
		this.endingSalary = endingSalary;
	}

	@Column(name="supervisor")
	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	@Column(name="street")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "employment_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return employmentId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PreviousEmployment other = (PreviousEmployment) obj;
		if ((this.employmentId == null) ? (other.employmentId != null) : !this.employmentId.equals(other.employmentId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.employmentId != null ? this.employmentId.hashCode() : 0);
		return hash;
	}
}
