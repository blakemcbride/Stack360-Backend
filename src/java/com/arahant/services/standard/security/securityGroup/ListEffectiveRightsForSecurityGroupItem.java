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
 * Created on Jun 8, 2007
 * 
 */
package com.arahant.services.standard.security.securityGroup;
import com.arahant.business.BRight;


/**
 * 
 *
 * Created on Jun 8, 2007
 *
 */
public class ListEffectiveRightsForSecurityGroupItem {

	private String name, description, accessLevel;// (Read, Write)

	public ListEffectiveRightsForSecurityGroupItem()
	{
		;
	}
	/**
	 * @param right
	 */
	ListEffectiveRightsForSecurityGroupItem(final BRight right) {
		name=right.getName();
		description=right.getDescription();
		switch (right.getAccessLevel())
		{
			case 1: accessLevel = "Read";
					break;
			case 2: accessLevel = "Write";
					break;
			default: accessLevel = "None";
		}
	}
	/**
	 * @return Returns the accessLevel.
	 */
	public String getAccessLevel() {
		return accessLevel;
	}

	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final String accessLevel) {
		this.accessLevel = accessLevel;
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
}

	
