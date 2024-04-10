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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrCheckListDetail;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListChecklistDetailsInput extends TransmitInputBase {


	public ListChecklistDetailsInput() {
		super();
	}
	
	@Validation (required=false)
	private String employeeStatusId;
	@Validation (required=false)
	private String employeeId;

	/**
	 * @return Returns the employeeId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
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
	public void setEmployeeStatusId(final String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}
}

	
