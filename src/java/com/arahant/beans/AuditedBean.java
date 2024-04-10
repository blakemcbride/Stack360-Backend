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

import java.util.Date;

public abstract class AuditedBean extends ArahantBean implements IAuditedBean {

	@Override
	public abstract ArahantHistoryBean historyObject();  //  This is the critical method that links a table to its history table
	protected String recordPersonId;
	protected char recordChangeType = 'N';
	protected Date recordChangeDate = new java.util.Date();

	/**
	 * @return Returns the recordChangeDate.
	 */
	@Override
	public abstract Date getRecordChangeDate();

	/**
	 * @param recordChangeDate The recordChangeDate to set.
	 */
	@Override
	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	/**
	 * @return Returns the recordChangePerson.
	 */
	@Override
	public abstract String getRecordPersonId();

	/**
	 * @param recordChangePerson The recordChangePerson to set.
	 */
	@Override
	public void setRecordPersonId(String recordChangePerson) {
		this.recordPersonId = recordChangePerson;
	}

	/**
	 * @return Returns the recordChangeType.
	 */
	@Override
	public abstract char getRecordChangeType();

	/**
	 * @param recordChangeType The recordChangeType to set.
	 */
	@Override
	public void setRecordChangeType(char recordChangeType) {
		this.recordChangeType = recordChangeType;
	}

	@Override
	public String getChangeTypeFormatted() {
		char changeType = getRecordChangeType();
		String changeTypeFormatted = "";

		if (changeType == 'N')
			changeTypeFormatted = "New";
		else if (changeType == 'M')
			changeTypeFormatted = "Modify";
		else if (changeType == 'D')
			changeTypeFormatted = "Delete";

		return changeTypeFormatted;
	}
}
