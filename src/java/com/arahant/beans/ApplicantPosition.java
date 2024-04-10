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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "applicant_position")
public class ApplicantPosition extends ArahantBean implements Serializable {

	public static final String ACCEPT_APPLICANT_DATE = "acceptApplicantDate";
	public static final String JOB_START_DATE = "jobStartDate";
	public static final String POSITION_STATUS = "positionStatus";
	public static final String POSITION_TYPE = "position";
	public static final String ORG_GROUP = "orgGroup";
	public static final String ID = "applicantPositionId";
	public static final String JOB_TITLE = "jobTitle";
	public static final String APPLICATIONS = "applications";
	public static final String EXT_REF = "extRef";
	public static final String APPLICANT_POSITION_INFO = "applicantPositionInfo";
	public static final char STATUS_NEW = 'N';
	public static final char STATUS_ACCEPTING = 'A';
	public static final char STATUS_SUSPENDED = 'S';
	public static final char STATUS_FILLED = 'F';
	public static final char STATUS_CANCELLED = 'C';

	private String applicantPositionId;
	private String jobTitle;
	private int acceptApplicantDate;
	private int jobStartDate;
	private OrgGroup orgGroup;
	private char positionStatus;
	private String extRef;
	private HrPosition position;

	private Set<ApplicantApplication> applications = new HashSet<ApplicantApplication>();

	@Column(name = "accept_applicant_date")
	public int getAcceptApplicantDate() {
		return acceptApplicantDate;
	}

	public void setAcceptApplicantDate(int acceptApplicantDate) {
		this.acceptApplicantDate = acceptApplicantDate;
	}

	@Id
	@Column(name = "applicant_position_id")
	public String getApplicantPositionId() {
		return applicantPositionId;
	}

	public void setApplicantPositionId(String applicantPositionId) {
		this.applicantPositionId = applicantPositionId;
	}

	@Column(name = "job_start_date")
	public int getJobStartDate() {
		return jobStartDate;
	}

	public void setJobStartDate(int jobStartDate) {
		this.jobStartDate = jobStartDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id")
	public HrPosition getPosition() {
		return position;
	}

	public void setPosition(HrPosition position) {
		this.position = position;
	}

	@Column(name = "job_title")
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(name = "position_status")
	public char getPositionStatus() {
		return positionStatus;
	}
/*
	@OneToMany(mappedBy = ApplicantApplication.POSITION, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ApplicantApplication> getApplications() {
		return applications;
	}
*/
	public void setApplications(Set<ApplicantApplication> applications) {
		this.applications = applications;
	}

	public void setPositionStatus(char positionStatus) {
		this.positionStatus = positionStatus;
	}

	@Column(name = "ext_ref")
	public String getExtRef() {
		return extRef;
	}

	public void setExtRef(String extRef) {
		this.extRef = extRef;
	}

	@Override
	public String tableName() {
		return "applicant_position";
	}

	@Override
	public String keyColumn() {
		return "applicant_position_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantPositionId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantPosition other = (ApplicantPosition) obj;
		if (this.applicantPositionId != other.getApplicantPositionId() && (this.applicantPositionId == null || !this.applicantPositionId.equals(other.getApplicantPositionId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.applicantPositionId != null ? this.applicantPositionId.hashCode() : 0);
		return hash;
	}
}
