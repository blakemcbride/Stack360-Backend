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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = ApplicantContact.TABLE_NAME)
public class ApplicantContact extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "applicant_contact";
	public static final String DATE = "contactDate";
	public static final String TIME = "contactTime";
	public static final String APPLICATION = "application";
	public static final String APPLICANT = "applicant";
	public static final String ID = "applicantContactId";
	private String applicantContactId;// character(16) NOT NULL,
	private Applicant applicant;//person_id character(16) NOT NULL,
	private ApplicantApplication application;//applicant_application_id character(16),
	private int contactDate;// integer NOT NULL DEFAULT 0,
	private int contactTime = -1; //integer NOT NULL DEFAULT 0,
	private char contactMode; //character(1) NOT NULL,
	private char contactStatus;// character(1) NOT NULL,
	private String description;// character varying(80) NOT NULL,
	private String whoAdded = ArahantSession.getHSU().getCurrentPerson().getPersonId();

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "applicant_contact_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantContactId = IDGenerator.generate(this);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	@Id
	@Column(name = "applicant_contact_id")
	public String getApplicantContactId() {
		return applicantContactId;
	}

	public void setApplicantContactId(String applicantContactId) {
		this.applicantContactId = applicantContactId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_application_id")
	public ApplicantApplication getApplication() {
		return application;
	}

	public void setApplication(ApplicantApplication application) {
		this.application = application;
	}

	@Column(name = "contact_date")
	public int getContactDate() {
		return contactDate;
	}

	public void setContactDate(int contactDate) {
		this.contactDate = contactDate;
	}

	@Column(name = "contact_mode")
	public char getContactMode() {
		return contactMode;
	}

	public void setContactMode(char contactMode) {
		this.contactMode = contactMode;
	}

	@Column(name = "contact_status")
	public char getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(char contactStatus) {
		this.contactStatus = contactStatus;
	}

	@Column(name = "contact_time")
	public int getContactTime() {
		return contactTime;
	}

	public void setContactTime(int contactTime) {
		this.contactTime = contactTime;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "who_added")
	public String getWhoAdded() {
		return whoAdded;
	}

	public void setWhoAdded(String whoAdded) {
		this.whoAdded = whoAdded;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantContact other = (ApplicantContact) obj;
		if (this.applicantContactId != other.getApplicantContactId() && (this.applicantContactId == null || !this.applicantContactId.equals(other.getApplicantContactId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.applicantContactId != null ? this.applicantContactId.hashCode() : 0);
		return hash;
	}
}
