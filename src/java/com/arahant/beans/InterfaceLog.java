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
@Table(name=InterfaceLog.TABLE_NAME)
public class InterfaceLog extends ArahantBean implements Serializable {

	public static final String TABLE_NAME="interface_log";

	public static final String INTERFACE="interfaceCode";
	public static final String STATUS="statusCode";
	public static final String LAST_RUN="lastRun";

	public static final short INTERFACE_COBRA_GUARD=0;
	public static final short STATUS_OK=0;
	public static final short STATUS_ERROR=1;
	public static final String COMPANY_ID = "companyId";
	public static final String COMPANY = "company";
	

	private String interfaceLogId;
	private short interfaceCode;
	private Date lastRun;
	private String statusMessage;
	private short statusCode;
	private String companyId;
	private CompanyDetail company;


	@Column(name="interface_code")
	public short getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(short interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	@Id
	@Column(name="interface_log_id")
	public String getInterfaceLogId() {
		return interfaceLogId;
	}

	public void setInterfaceLogId(String interfaceLogId) {
		this.interfaceLogId = interfaceLogId;
	}

	@Column(name="last_run")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getLastRun() {
		return lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	@Column(name="status_code")
	public short getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(short statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name="status_message")
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Column(name="company_id", updatable=false, insertable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ManyToOne
	@JoinColumn(name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "interface_log_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return interfaceLogId=IDGenerator.generate(this);
	}


}
