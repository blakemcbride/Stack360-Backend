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
 *
 */
package com.arahant.services.standard.crm.prospectAssignment;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class AssignProspectsInput extends TransmitInputBase {

	@Validation (required=true)
	private String [] prospectIds;
	@Validation (required=true)
	private String employeeId;
	@Validation (required=true)
	private int nextContactDate;
	

	public String [] getProspectIds()
	{
		if (prospectIds==null)
			prospectIds= new String [0];
		return prospectIds;
	}
	public void setProspectIds(String [] prospectIds)
	{
		this.prospectIds=prospectIds;
	}
	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(String employeeId)
	{
		this.employeeId=employeeId;
	}

	public int getNextContactDate() {
		return nextContactDate;
	}

	public void setNextContactDate(int nextContactDate) {
		this.nextContactDate = nextContactDate;
	}

}

	
