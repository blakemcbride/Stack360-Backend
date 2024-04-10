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
 * 
 */
package com.arahant.services.standard.hr.projectSummary;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class ListEmployeesForAssignmentReturnItem {
	
	public ListEmployeesForAssignmentReturnItem()
	{
		;
	}

	ListEmployeesForAssignmentReturnItem (final BPerson bc)
	{
		
		displayName=bc.getNameLFM();
		employeeId=bc.getPersonId();

	}
	
	private String displayName;
	private String employeeId;

	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(final String displayName)
	{
		this.displayName=displayName;
	}
	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(final String employeeId)
	{
		this.employeeId=employeeId;
	}

}

	
