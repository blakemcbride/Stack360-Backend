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
package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class ListAllTimesheetsInput extends TransmitInputBase {

	@Validation (table="employee",column="person_id",required=true)
	private String employeeId;
	@Validation (table="timesheet",column="beginning_date",type="date",required=true)
	private int fromDate;
	@Validation (table="timesheet",column="end_date",type="date",required=true)
	private int toDate;
	

	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(String employeeId)
	{
		this.employeeId=employeeId;
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

	
