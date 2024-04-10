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
@Table(name = "time_off_request")
public class TimeOffRequest extends ArahantBean implements Serializable {

	public static final String START_DATE = "startDate";
	public static final String END_DATE = "returnDate";
	public static final String REQUESTING_PERSON = "requestingPerson";
	public static final String REQUEST_ID = "requestId";
	public static final String TO_TIME = "returnTime";
	public static final String FROM_TIME = "startTime";
	public static final String STATUS = "requestStatus";
	public static final String REQUEST_DATE = "requestDate";
	public static final char APPROVED = 'A';
	public static final char ORIGINATED = 'O';
	public static final char REJECTED = 'R';
	public static final char ENTERED = 'E';
	private String requestId;
	private Person requestingPerson;
	private int startDate;
	private int startTime;
	private int returnDate;
	private int returnTime;
	private char requestStatus;
	private Date requestDate;
	private Person approvingPerson;
	private Date approvalDate;
	private String requestingComment;
	private String approvalComment;
	private Project project;

	public TimeOffRequest() {
	}

	@Column(name = "approval_comment")
	public String getApprovalComment() {
		return approvalComment;
	}

	public void setApprovalComment(String approvalComment) {
		this.approvalComment = approvalComment;
	}

	@Column(name = "approval_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approving_person_id")
	public Person getApprovingPerson() {
		return approvingPerson;
	}

	public void setApprovingPerson(Person approvingPerson) {
		this.approvingPerson = approvingPerson;
	}

	@Column(name = "request_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Id
	@Column(name = "request_id")
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Column(name = "request_status")
	public char getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(char requestStatus) {
		this.requestStatus = requestStatus;
	}

	@Column(name = "requesting_comment")
	public String getRequestingComment() {
		return requestingComment;
	}

	public void setRequestingComment(String requestingComment) {
		this.requestingComment = requestingComment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requesting_person_id")
	public Person getRequestingPerson() {
		return requestingPerson;
	}

	public void setRequestingPerson(Person requestingPerson) {
		this.requestingPerson = requestingPerson;
	}

	@Column(name = "return_date")
	public int getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(int returnDate) {
		this.returnDate = returnDate;
	}

	@Column(name = "return_time")
	public int getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(int returnTime) {
		this.returnTime = returnTime;
	}

	@Column(name = "start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@Column(name = "start_time")
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String tableName() {
		return "time_off_request";
	}

	@Override
	public String keyColumn() {
		return "request_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return requestId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeOffRequest other = (TimeOffRequest) obj;
		if (this.requestId != other.requestId && (this.requestId == null || !this.requestId.equals(other.requestId)))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.requestId != null ? this.requestId.hashCode() : 0);
		return hash;
	}
}
