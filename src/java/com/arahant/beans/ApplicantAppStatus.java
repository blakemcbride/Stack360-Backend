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
@Table(name = "applicant_app_status")
public class ApplicantAppStatus extends ArahantBean implements Serializable {

	public static final String SEQ = "statusOrder";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String NAME = "statusName";
	public static final String ID = "applicantAppStatusId";
	private String applicantAppStatusId;// character(16) NOT NULL,
	private String statusName;// character varying(40) NOT NULL,
	public static final String STATUS_ORDER = "statusOrder";
	private short statusOrder;// smallint NOT NULL,
	private int lastActiveDate;// integer NOT NULL DEFAULT 0,
	private char isActive;// character(1) DEFAULT 'Y'::bpchar,
	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY = "company";
	private short phase = 0;

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
		return "applicant_app_status";
	}

	@Override
	public String keyColumn() {
		return "applicant_app_status_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantAppStatusId = IDGenerator.generate(this);
	}

	@Id
	@Column(name = "applicant_app_status_id")
	public String getApplicantAppStatusId() {
		return applicantAppStatusId;
	}

	public void setApplicantAppStatusId(String applicantAppStatusId) {
		this.applicantAppStatusId = applicantAppStatusId;
	}

	@Column(name = "is_active")
	public char getIsActive() {
		return isActive;
	}

	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "status_name")
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Column(name = "status_order")
	public short getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(short statusOrder) {
		this.statusOrder = statusOrder;
	}

	@Column(name = "phase")
	public short getPhase() {
		return phase;
	}

	public void setPhase(short phase) {
		this.phase = phase;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantAppStatus other = (ApplicantAppStatus) obj;
		if (this.applicantAppStatusId != other.getApplicantAppStatusId() && (this.applicantAppStatusId == null || !this.applicantAppStatusId.equals(other.getApplicantAppStatusId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.applicantAppStatusId != null ? this.applicantAppStatusId.hashCode() : 0);
		return hash;
	}
}
