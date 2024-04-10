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

import java.util.Date;

/**
 *
 */
public interface IAuditedBean {
	public static final String HISTORY_DATE = "recordChangeDate";
	
	public abstract ArahantHistoryBean historyObject();
	/**
	 * @return Returns the recordChangeDate.
	 */
	public abstract Date getRecordChangeDate();

	/**
	 * @param recordChangeDate The recordChangeDate to set.
	 */
	public void setRecordChangeDate(Date recordChangeDate);

	/**
	 * @return Returns the recordChangePerson.
	 */
	public abstract String getRecordPersonId();

	/**
	 * @param recordChangePerson The recordChangePerson to set.
	 */
	public void setRecordPersonId(String recordChangePerson);

	/**
	 * @return Returns the recordChangeType.
	 */
	public abstract char getRecordChangeType();

	/**
	 * @param recordChangeType The recordChangeType to set.
	 */
	public void setRecordChangeType(char recordChangeType);

	public String getChangeTypeFormatted();

	public Object keyValue();
}
