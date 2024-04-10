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
@Table(name = "quickbooks_change")
@DiscriminatorColumn(name = "arahant_table_name", discriminatorType = DiscriminatorType.STRING)
public class QuickbooksChange extends ArahantBean implements Serializable {

	public final static String QB_ID = "qbRecordId";
	public final static String RECORD_CHANGED = "recordChanged";
	public final static String ARAHANT_ID = "arahantRecordId";
	public final static String TABLE = "arahantTableName";
	private static final long serialVersionUID = 1L;
	private String recordId;
	private String arahantTableName;
	private String arahantRecordId;
	private String qbRecordId;
	private int qbRecordRevision;
	private char recordChanged = 'Y';

	public QuickbooksChange() {}

	@Column(name = "arahant_record_id", insertable = false, updatable = false)
	public String getArahantRecordId() {
		return arahantRecordId;
	}

	public void setArahantRecordId(String arahantRecordId) {
		this.arahantRecordId = arahantRecordId;
	}

	@Column(name = "arahant_table_name", insertable = false, updatable = false)
	public String getArahantTableName() {
		return arahantTableName;
	}

	public void setArahantTableName(String arahantTableName) {
		this.arahantTableName = arahantTableName;
	}

	@Column(name = "qb_record_id")
	public String getQbRecordId() {
		return qbRecordId;
	}

	public void setQbRecordId(String qbRecordId) {
		this.qbRecordId = qbRecordId;
	}

	@Column(name = "qb_record_revision")
	public int getQbRecordRevision() {
		return qbRecordRevision;
	}

	public void setQbRecordRevision(int qbRecordRevision) {
		this.qbRecordRevision = qbRecordRevision;
	}

	@Column(name = "record_changed")
	public char getRecordChanged() {
		return recordChanged;
	}

	public void setRecordChanged(char recordChanged) {
		this.recordChanged = recordChanged;
	}

	@Id
	@Column(name = "record_id")
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Override
	public String tableName() {
		return "quickbooks_change";
	}

	@Override
	public String keyColumn() {
		return "record_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return recordId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuickbooksChange other = (QuickbooksChange) obj;
		if ((this.recordId == null) ? (other.recordId != null) : !this.recordId.equals(other.recordId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + (this.recordId != null ? this.recordId.hashCode() : 0);
		return hash;
	}
}
