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
package com.arahant.services.standard.hrConfig.wizardProjectTemplate;
import com.arahant.business.BHREmployeeStatus;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEmployeeStatusesItem {

	private String employeeStatusId;
	private String name;
	
	
	public ListEmployeeStatusesItem()
	{
		
	}

	public ListEmployeeStatusesItem(BHREmployeeStatus e) {
		this.employeeStatusId = e.getEmployeeStatusId();
		this.name = e.getName();
	}

	/**
	 * @return Returns the employeeStatusId.
	 */
	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	/**
	 * @param employeeStatusId The employeeStatusId to set.
	 */
	public void setEmployeeStatusId(final String EmployeeStatusId) {
		this.employeeStatusId = EmployeeStatusId;
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

	
