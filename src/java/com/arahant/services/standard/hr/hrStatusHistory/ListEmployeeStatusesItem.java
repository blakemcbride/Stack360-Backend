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
 *
 * Created on Feb 22, 2007
*/

package com.arahant.services.standard.hr.hrStatusHistory;

import com.arahant.business.BHREmployeeStatus;

public class ListEmployeeStatusesItem {

	private String EmployeeStatusId;
	private String name;
	private String dateType;
	private String active;

	public ListEmployeeStatusesItem()
	{
	}

	public String getDateType() {
		return dateType;
	}
	/**
	 * @param dateType The dateType to set.
	 */
	public void setDateType(final String dateType) {
		this.dateType = dateType;
	}

	public String getEmployeeStatusId() {
		return EmployeeStatusId;
	}

	public void setEmployeeStatusId(final String EmployeeStatusId) {
		this.EmployeeStatusId = EmployeeStatusId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	ListEmployeeStatusesItem(final BHREmployeeStatus account) {
		EmployeeStatusId = account.getEmployeeStatusId();
		name = account.getName();
		dateType = account.getDateType();
		active = account.getActiveFlag() + "";
	}
}

	
