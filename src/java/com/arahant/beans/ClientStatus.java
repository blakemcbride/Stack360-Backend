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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=ClientStatus.TABLE_NAME)
public class ClientStatus extends ArahantBean implements Serializable {
	public static final String TABLE_NAME="client_status";

	public static final String SEQ="seqNo";
	public static final String CODE="code";
	public static final String DESCRIPTION="description";
	public static final String LAST_ACTIVE_DATE="lastActiveDate";
	public static final String ACTIVE="active";

	private String clientStatusId;
	private String code;
	private String description;
	private short seqNo;
	private int lastActiveDate;
	private char active = 'Y';

	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY="company";

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column (name="company_id", insertable=false, updatable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Id
	@Column(name="client_status_id")
	public String getClientStatusId() {
		return clientStatusId;
	}

	public void setClientStatusId(String clientStatusId) {
		this.clientStatusId = clientStatusId;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name="seqno")
	public short getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(short seqNo) {
		this.seqNo = seqNo;
	}

	@Column(name="active")
	public char getActive() {
		return active;
	}

	public void setActive(char active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ClientStatus other = (ClientStatus) obj;
		if ((this.clientStatusId == null) ? (other.clientStatusId != null) : !this.clientStatusId.equals(other.clientStatusId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + (this.clientStatusId != null ? this.clientStatusId.hashCode() : 0);
		return hash;
	}




	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "client_status_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return clientStatusId=IDGenerator.generate(this);
	}


}
