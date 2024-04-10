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
 * Created on May 15, 2007
 * 
 */
package com.arahant.services.main;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on May 15, 2007
 *
 */
public class ListScreensAndGroupsInput extends TransmitInputBase {

	@Validation (required=false)
	private String groupId;

	@Validation (required=false)
	private String currentScreenId;

	@Validation (required=false)
	private int depth;

	@Validation (required=false)
	private String employeeId;

	@Validation (required=false)
	private int asOfDate;

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

	public String getCurrentScreenId() {
		return currentScreenId;
	}

	public void setCurrentScreenId(String currentScreenId) {
		this.currentScreenId = currentScreenId;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(int asOfDate) {
		this.asOfDate = asOfDate;
	}
}

	
