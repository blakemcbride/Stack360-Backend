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
package com.arahant.services.standard.time.employeeTimesheetReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (min=1,max=4,required=false)
	private int sort;
	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (type="date",required=false)
	private int toDate;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (min=0,max=2,required=false)
	private int type;
	

	public int getSort()
	{
		return sort;
	}
	public void setSort(int sort)
	{
		this.sort=sort;
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
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}


}

	
