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
package com.arahant.services.standard.security.securityGroup;

import com.arahant.business.BSecurityGroup;
import com.arahant.utils.IDGenerator;

public class ListSecurityGroupsItem {

	private String name, description, groupId, shortGroupId;

	public ListSecurityGroupsItem() {
	}

	/**
	 * @param group
	 */
	ListSecurityGroupsItem(final BSecurityGroup group) {
		name = group.getId();
		description = group.getDescription();
		groupId = group.getSecurityGroupId();
		shortGroupId = IDGenerator.shrinkKey(groupId);
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

	public String getShortGroupId() {
		return shortGroupId;
	}

	public void setShortGroupId(String shortGroupId) {
		this.shortGroupId = shortGroupId;
	}
	
}
