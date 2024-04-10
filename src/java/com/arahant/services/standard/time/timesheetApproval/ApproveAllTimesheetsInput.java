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
package com.arahant.services.standard.time.timesheetApproval;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class ApproveAllTimesheetsInput extends TransmitInputBase {

	@Validation (required=true)
	private String [] employeeIds;
	@Validation (type="date",required=true)
	private int fromDate;
	@Validation (type="date",required=true)
	private int toDate;
	

	public String [] getEmployeeIds()
	{
		if (employeeIds==null)
			employeeIds= new String [0];
		return employeeIds;
	}
	public void setEmployeeIds(String [] employeeIds)
	{
		this.employeeIds=employeeIds;
	}
	public int getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(int fromDate)
	{
		this.fromDate=fromDate;
	}
	public int getToDate()
	{
		return toDate;
	}
	public void setToDate(int toDate)
	{
		this.toDate=toDate;
	}


}

	
