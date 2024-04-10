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
package com.arahant.services.standard.time.employeeTimesheetChart;
import com.arahant.business.BTimesheet;


/**
 * 
 *
 *
 */
public class GetChartDataReturnItem {
	
	public GetChartDataReturnItem()
	{
		;
	}

	
	private String nameFormatted;
	private double billable;
	private double nonBillable;
	private double timeOff;
	

	public String getNameFormatted()
	{
		return nameFormatted;
	}
	public void setNameFormatted(String nameFormatted)
	{
		this.nameFormatted=nameFormatted;
	}
	public double getBillable()
	{
		return billable;
	}
	public void setBillable(double billable)
	{
		this.billable=billable;
	}
	public double getNonBillable()
	{
		return nonBillable;
	}
	public void setNonBillable(double nonBillable)
	{
		this.nonBillable=nonBillable;
	}
	public double getTimeOff()
	{
		return timeOff;
	}
	public void setTimeOff(double timeOff)
	{
		this.timeOff=timeOff;
	}

}

	
