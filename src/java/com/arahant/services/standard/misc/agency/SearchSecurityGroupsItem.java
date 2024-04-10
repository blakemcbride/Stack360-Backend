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
package com.arahant.services.standard.misc.agency;
import com.arahant.business.BSecurityGroup;


/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchSecurityGroupsItem {


	public SearchSecurityGroupsItem() {

	}
	
	
	/**
	 * @param group
	 */
	public SearchSecurityGroupsItem(final BSecurityGroup group) {
		name=group.getName();
		groupId=group.getSecurityGroupId();
	}


	private String name, groupId;


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
}

	
