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
public class RemoveGroupFromGroupInput extends TransmitInputBase {

	@Validation (required=true)
	private  String []childGroupIDs;
	@Validation (required=true)
	private String parentGroupID;

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
	/**
	 * @return Returns the childGroupIDs.
	 */
	public String[] getChildGroupIDs() {
            if (childGroupIDs==null)
                return new String[0];
		return childGroupIDs;
	}
	/**
	 * @param childGroupIDs The childGroupIDs to set.
	 */
	public void setChildGroupIDs(final String[] childGroupIDs) {
		this.childGroupIDs = childGroupIDs;
	}
}

	
