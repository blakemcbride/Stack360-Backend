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
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = ApplicantSource.TABLE_NAME)
public class ApplicantSource extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "applicant_source";
	private List<Applicant> applicants;
	public static final String DESCRIPTION = "description";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String ID = "applicantSourceId";
	private String applicantSourceId; // character(16) NOT NULL,
	private String description;// character varying(40) NOT NULL,
	private int lastActiveDate;// integer NOT NULL DEFAULT 0,
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
		return "applicant_source";
	}

	@Override
	public String keyColumn() {
		return "applicant_source_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantSourceId = IDGenerator.generate(this);
	}

	@Id
	@Column(name = "applicant_source_id")
	public String getApplicantSourceId() {
		return applicantSourceId;
	}

	public void setApplicantSourceId(String applicantSourceId) {
		this.applicantSourceId = applicantSourceId;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantSource other = (ApplicantSource) obj;
		if (this.applicantSourceId != other.getApplicantSourceId() && (this.applicantSourceId == null || !this.applicantSourceId.equals(other.getApplicantSourceId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + (this.applicantSourceId != null ? this.applicantSourceId.hashCode() : 0);
		return hash;
	}

	@OneToMany(mappedBy = Applicant.APPLICANT_SOURCE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<Applicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<Applicant> applicants) {
		this.applicants = applicants;
	}
}
