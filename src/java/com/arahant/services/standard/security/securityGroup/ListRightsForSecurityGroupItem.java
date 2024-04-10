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
import com.arahant.business.interfaces.BRightOrSecurityGroup;


/**
 * 
 *
 * Created on Jun 7, 2007
 *
 */
public class ListRightsForSecurityGroupItem {

	private String rightId, name, description, type, tokenAccessLevel;
	//type (Group, Token), tokenAccessLevel (None, Read, Write)

	/**
	 * @param right
	 */
	ListRightsForSecurityGroupItem(final BRightOrSecurityGroup right) {
		rightId=right.getDbId();
		name=right.getName();
		description=right.getDescription();
		tokenAccessLevel=right.getTokenAccessLevel();
		type=right.getType();
		
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

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

	/**
	 * @return Returns the rightId.
	 */
	public String getRightId() {
		return rightId;
	}

	/**
	 * @param rightId The rightId to set.
	 */
	public void setRightId(final String rightId) {
		this.rightId = rightId;
	}

	/**
	 * @return Returns the tokenAccessLevel.
	 */
	public String getTokenAccessLevel() {
		return tokenAccessLevel;
	}

	/**
	 * @param tokenAccessLevel The tokenAccessLevel to set.
	 */
	public void setTokenAccessLevel(final String tokenAccessLevel) {
		this.tokenAccessLevel = tokenAccessLevel;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public ListRightsForSecurityGroupItem() {

	}

	
}

	
