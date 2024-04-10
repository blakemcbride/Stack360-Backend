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
package com.arahant.services.standard.hrConfig.timeRelatedBenefit;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class ListEmployeesForOrgGroupInput extends TransmitInputBase {

    @Validation(required = true)
    private String groupId;
    @Validation(table = "person", column = "lname", required = false)
    private String lastName;
    @Validation(min = 2, max = 5, required = false)
    private int lastNameSearchType;
    @Validation(required = true)
    private boolean supervisor;
	@Validation(required = false)
	private String[] excludeIds;

    /**
     * @return Returns the supervisor.
     */
    public boolean getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor The supervisor to set.
     */
    public void setSupervisor(final boolean supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return Returns the lastNameStartsWith.
     */
    public String getLastName() {
        return modifyForSearch(lastName, lastNameSearchType);
    }

    /**
     * @param lastNameStartsWith The lastNameStartsWith to set.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
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

    public int getLastNameSearchType() {
        return lastNameSearchType;
    }

    public void setLastNameSearchType(int lastNameSearchType) {
        this.lastNameSearchType = lastNameSearchType;
    }

	public String[] getExcludeIds() {
		return excludeIds;
	}

	public void setExcludeIds(String[] excludeIds) {
		this.excludeIds = excludeIds;
	}

	
}

	
