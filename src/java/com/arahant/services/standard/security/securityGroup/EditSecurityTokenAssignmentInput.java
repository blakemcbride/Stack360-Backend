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
 * Created on Jun 7, 2007
 * 
 */
package com.arahant.services.standard.security.securityGroup;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Jun 7, 2007
 *
 */
public class EditSecurityTokenAssignmentInput extends TransmitInputBase {

	@Validation (table="security_group",column="security_group_id",required=false)
	private String tokenId;
	@Validation (table="security_group",column="security_group_id",required=false)
	private String parentGroupId;
	@Validation (min=0,max=3,required=false)
	private int accessLevel;// (1 = None 2 = Read 3 = Write
	/**
	 * @return Returns the accessLevel.
	 */
	public int getAccessLevel() {
		return accessLevel;
	}
	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final int accessLevel) {
		this.accessLevel = accessLevel;
	}
	/**
	 * @return Returns the parentGroupId.
	 */
	public String getParentGroupId() {
		return parentGroupId;
	}
	/**
	 * @param parentGroupId The parentGroupId to set.
	 */
	public void setParentGroupId(final String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	/**
	 * @return Returns the tokenId.
	 */
	public String getTokenId() {
		return tokenId;
	}
	/**
	 * @param tokenId The tokenId to set.
	 */
	public void setTokenId(final String tokenId) {
		this.tokenId = tokenId;
	}
}

	
