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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agencyOrgGroup;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class NewGroupInput extends TransmitInputBase {

	@Validation (required=false)
	private String parentGroupID;
	@Validation (table="org_group",column="name",required=true)
	private String name;
	@Validation (table="org_group",column="external_id",required=false)
	private String externalId;

	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}
	/**
	 * @return Returns the parentGroupID.
	 */
	public String getParentGroupID() {
		return parentGroupID;
	}
	/**
	 * @param parentGroupID The parentGroupID to set.
	 */
	public void setParentGroupID(final String parentGroupID) {
		this.parentGroupID = parentGroupID;
	}
}

	
