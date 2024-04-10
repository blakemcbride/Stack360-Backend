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

import com.arahant.utils.ArahantSession;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = PersonCR.TABLE_NAME)
public class PersonCR extends ChangeRequestBase implements Serializable {

	public static final String TABLE_NAME = "person_cr";
	public static final String PERSON = "realRecord";
	public static final String PERSON_ID = "realRecordId";
	public static final String PERSON_PENDING = "changeRecord";
	public static final String PERSON_PENDING_ID = "changeRecordId";
	private static final long serialVersionUID = 1L;
	private Person realRecord;
	private Person changeRecord;
	private String realRecordId;
	private String changeRecordId;

	public PersonCR() {
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Column(name = "approval_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getApprovalTime() {
		return approvalTime;
	}

	@ManyToOne
	@JoinColumn(name = "approver_id")
	public Person getApprover() {
		return approver;
	}

	@Id
	@Column(name = "change_request_id")
	public String getChangeRequestId() {
		return changeRequestId;
	}

	@Column(name = "change_status")
	public char getChangeStatus() {
		return changeStatus;
	}

	@ManyToOne
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	@Column(name = "request_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getRequestTime() {
		return requestTime;
	}

	@ManyToOne
	@JoinColumn(name = "requestor_id")
	public Person getRequestor() {
		return requestor;
	}

	@ManyToOne
	@JoinColumn(name = "change_record_id")
	public Person getChangeRecord() {
		return changeRecord;
	}

	public void setChangeRecord(Person changeRecord) {
		if (changeRecord.getRecordType() == 'R') {
			//I was going to check here to make sure we're working with a change record
			//but I believe PersonCR records, after approved become linked to the real record that was approved
			//System.out.println("REAL RECORD " + changeRecord.getPersonId() + " USED TO SET CHANGE RECORD. (" + this.changeRequestId + ")");
			//throw new ArahantException("Cannot set Change Record with Real Record - " + changeRecord.getPersonId());
		}
		this.changeRecord = changeRecord;
	}

	@Column(name = "change_record_id", insertable = false, updatable = false)
	public String getChangeRecordId() {
		return changeRecordId;
	}

	public void setChangeRecordId(String changeRecordId) {
		this.changeRecordId = changeRecordId;
	}

	@ManyToOne
	@JoinColumn(name = "real_record_id")
	public Person getRealRecord() {
		return realRecord;
	}

	public void setRealRecord(Person realRecord) {
		this.realRecord = realRecord;
	}

	@Column(name = "real_record_id", insertable = false, updatable = false)
	public String getRealRecordId() {
		return realRecordId;
	}

	public void setRealRecordId(String realRecordId) {
		this.realRecordId = realRecordId;
	}

	public static void main(String args[]) {
		for (Person c : ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').joinTo(Person.CHANGE_REQS).list())
			System.out.println(c.getNameLFM());
	}
}
