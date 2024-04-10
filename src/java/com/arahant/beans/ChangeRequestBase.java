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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.Date;

/**
 *
 */
public abstract class ChangeRequestBase extends ArahantBean {

	public static final String REQUESTOR="requestor";

	public static final char STATUS_PENDING='P';
	public static final char STATUS_APPROVED='A';
	public static final char STATUS_REJECTED='R';

	public static final String CHANGE_STATUS="changeStatus";
	public static final String PROJECT="project";

	protected String changeRequestId;
	protected char changeStatus;
	protected Date requestTime;
	protected Person requestor;
	protected Person approver;
	protected Date approvalTime;
	protected Project project;

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	public void setApprover(Person approver) {
		this.approver = approver;
	}

	public void setChangeRequestId(String changeRequestId) {
		this.changeRequestId = changeRequestId;
	}

	public void setChangeStatus(char changeStatus) {
		this.changeStatus = changeStatus;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public void setRequestor(Person requestor) {
		this.requestor = requestor;
	}


	@Override
	public String keyColumn() {
		return "change_request_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return changeRequestId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ChangeRequestBase other = (ChangeRequestBase) obj;
		if ((this.changeRequestId == null) ? (other.changeRequestId != null) : !this.changeRequestId.equals(other.changeRequestId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + (this.changeRequestId != null ? this.changeRequestId.hashCode() : 0);
		return hash;
	}




}
