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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 */
@Entity
@Table(name="phone_cr")
public class PhoneChangeRequest extends ChangeRequestBase implements Serializable {

	public static final String TABLE_NAME="phone_cr";
	public static final String PHONE = "realRecord";
	public static final String PHONE_PENDING = "changeRecord";
	
	private Phone realRecord;
	private PhonePending changeRecord;


	@Override
	public String tableName() {
		return TABLE_NAME;
	}


	@Column(name="approval_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getApprovalTime() {
		return approvalTime;
	}

	@ManyToOne
	@JoinColumn(name="approver_id")
	public Person getApprover() {
		return approver;
	}

	@Id
	@Column(name="change_request_id")
	public String getChangeRequestId() {
		return changeRequestId;
	}

	@Column(name="change_status")
	public char getChangeStatus() {
		return changeStatus;
	}

	@ManyToOne
	@JoinColumn(name="project_id")
	public Project getProject() {
		return project;
	}

	@Column(name="request_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getRequestTime() {
		return requestTime;
	}

	@ManyToOne
	@JoinColumn(name="requestor_id")
	public Person getRequestor() {
		return requestor;
	}



	@ManyToOne
	@JoinColumn(name="change_record_id")
	public PhonePending getChangeRecord() {
		return changeRecord;
	}

	public void setChangeRecord(PhonePending changeRecord) {
		this.changeRecord = changeRecord;
	}

	@ManyToOne
	@JoinColumn(name="real_record_id")
	public Phone getRealRecord() {
		return realRecord;
	}

	public void setRealRecord(Phone realRecord) {
		this.realRecord = realRecord;
	}


}
