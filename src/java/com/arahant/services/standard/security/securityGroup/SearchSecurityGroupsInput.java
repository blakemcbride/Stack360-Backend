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
 *parentGroupId (required), name (required),
 * nameSearchType (standard), typeIndicator (0 = Both, 1 = Token, 2 = Group)
 */
public class SearchSecurityGroupsInput extends TransmitInputBase {

	@Validation (table="security_group",column="security_group_id",required=true)
	private String parentGroupId;
	@Validation (table="security_group",column="id",required=false)
	private String name;
	@Validation (min=0,max=10,required=false)
	private int nameSearchType;
	@Validation (min=0,max=2,required=false)
	private int typeIndicator;// (0 = Both  1 = Token  2 = Group)
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * @return Returns the nameSearchType.
	 */
	public int getNameSearchType() {
		return nameSearchType;
	}
	/**
	 * @param nameSearchType The nameSearchType to set.
	 */
	public void setNameSearchType(final int nameSearchType) {
		this.nameSearchType = nameSearchType;
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
	 * @return Returns the typeIndicator.
	 */
	public int getTypeIndicator() {
		return typeIndicator;
	}
	/**
	 * @param typeIndicator The typeIndicator to set.
	 */
	public void setTypeIndicator(final int typeIndicator) {
		this.typeIndicator = typeIndicator;
	}
	
}

	
