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
@Table(name = "quickbooks_sync")
public class QuickbooksSync extends ArahantBean implements Serializable {

	public static final String DIRECTION = "direction";
	public static final String FIELD_CODE = "interfaceCode";
	public static final char TYPE_DOWN = 'T';
	public static final char TYPE_UP = 'F';
	private static final long serialVersionUID = 1L;
	private String syncId;
	private CompanyDetail company;
	private Date interfaceTime;
	private short interfaceCode;
	private char direction = 'F';

	public QuickbooksSync() {}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "direction")
	public char getDirection() {
		return direction;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}

	@Column(name = "interface_code")
	public short getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(short interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	@Column(name = "interface_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInterfaceTime() {
		return interfaceTime;
	}

	public void setInterfaceTime(Date interfaceTime) {
		this.interfaceTime = interfaceTime;
	}

	@Id
	@Column(name = "sync_id")
	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	@Override
	public String tableName() {
		return "quickbooks_sync";
	}

	@Override
	public String keyColumn() {
		return "sync_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return syncId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuickbooksSync other = (QuickbooksSync) obj;
		if ((this.syncId == null) ? (other.syncId != null) : !this.syncId.equals(other.syncId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + (this.syncId != null ? this.syncId.hashCode() : 0);
		return hash;
	}
}
