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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.business.BHREmployeeStatus;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEmployeeStatusesItem {

	private String EmployeeStatusId;
	private String name;
	private String dateType;
	
	
	
	/**
	 * @return Returns the dateType.
	 */
	public String getDateType() {
		return dateType;
	}
	/**
	 * @param dateType The dateType to set.
	 */
	public void setDateType(final String dateType) {
		this.dateType = dateType;
	}
	public ListEmployeeStatusesItem()
	{
		
	}
	/**
	 * @return Returns the EmployeeStatusId.
	 */
	public String getEmployeeStatusId() {
		return EmployeeStatusId;
	}

	/**
	 * @param EmployeeStatusId The EmployeeStatusId to set.
	 */
	public void setEmployeeStatusId(final String EmployeeStatusId) {
		this.EmployeeStatusId = EmployeeStatusId;
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
	 * @param account
	 */
	ListEmployeeStatusesItem(final BHREmployeeStatus account) {
		EmployeeStatusId=account.getEmployeeStatusId();
		name=account.getName();
		dateType=account.getDateType();
	}
}

	
