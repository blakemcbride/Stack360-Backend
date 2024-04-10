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


/**
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.crm.clientOrgGroup;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class ListClientContactsInput extends TransmitInputBase{
	@Validation (table="org_group",column="org_group_id",required=true)
	private String groupId;
	@Validation (table="person",column="lname",required=false)
	private String lastNameStartsWith;
	@Validation (required=true)
	private boolean primary;
	/**
	 * @return Returns the groupId.
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId The groupId to set.
	 */
	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * @return Returns the lastNameStartsWith.
	 */
	public String getLastNameStartsWith() {
		return lastNameStartsWith+"%";
	}

	/**
	 * @param lastNameStartsWith The lastNameStartsWith to set.
	 */
	public void setLastNameStartsWith(final String lastNameStartsWith) {
		this.lastNameStartsWith = lastNameStartsWith;
	}

	/**
	 * @return Returns the primary.
	 */
	public boolean getPrimary() {
		return primary;
	}

	/**
	 * @param primary The primary to set.
	 */
	public void setPrimary(final boolean primary) {
		this.primary = primary;
	}
}

	
