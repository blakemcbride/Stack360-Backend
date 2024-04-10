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
import javax.persistence.*;


@Entity
@Table(name = ApplicantStatus.TABLE_NAME)
public class ApplicantStatus extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "applicant_status";
	public static final String SEQ = "statusOrder";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String NAME = "name";
	public static final String ID = "applicantStatusId";
	private String applicantStatusId;// character(16) NOT NULL,
	private String name;// character varying(40) NOT NULL,
	private short statusOrder;// smallint NOT NULL,
	private int lastActiveDate;// integer NOT NULL DEFAULT 0,
	private char considerForHire;// character(1) NOT NULL,
	private char sendEmail = 'N'; //character(1) DEFAULT 'N' NOT NULL;
	private String emailSource; //character(50);
	private String emailText; // text;
	private String emailSubject;
	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY = "company";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String tableName() {
		return "applicant_status";
	}

	@Override
	public String keyColumn() {
		return "applicant_status_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantStatusId = IDGenerator.generate(this);
	}

	@Id
	@Column(name = "applicant_status_id")
	public String getApplicantStatusId() {
		return applicantStatusId;
	}

	public void setApplicantStatusId(String applicantStatusId) {
		this.applicantStatusId = applicantStatusId;
	}

	@Column(name = "consider_for_hire")
	public char getConsiderForHire() {
		return considerForHire;
	}

	public void setConsiderForHire(char considerForHire) {
		this.considerForHire = considerForHire;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status_order")
	public short getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(short statusOrder) {
		this.statusOrder = statusOrder;
	}

	@Column(name = "email_source")
	public String getEmailSource() {
		return emailSource;
	}

	public void setEmailSource(String emailSource) {
		this.emailSource = emailSource;
	}

	@Column(name = "email_text")
	public String getEmailText() {
		return emailText;
	}

	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}

	@Column(name = "send_email")
	public char getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(char sendEmail) {
		this.sendEmail = sendEmail;
	}

	@Column(name = "email_subject")
	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantStatus other = (ApplicantStatus) obj;
		if (this.applicantStatusId != other.getApplicantStatusId() && (this.applicantStatusId == null || !this.applicantStatusId.equals(other.getApplicantStatusId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + (this.applicantStatusId != null ? this.applicantStatusId.hashCode() : 0);
		return hash;
	}
}
