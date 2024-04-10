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
import com.arahant.business.BTimesheet;


/**
 * 
 *
 *
 */
public class GetReportDataReturnItem implements Comparable<GetReportDataReturnItem>{
	
	public GetReportDataReturnItem()
	{
		;
	}

	int sortType;
	
	private String lastName;
	private String firstName;
	private double billableHours;
	private double nonBillableHours;
	private double totalHours;
	

	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public double getBillableHours()
	{
		return billableHours;
	}
	public void setBillableHours(double billableHours)
	{
		this.billableHours=billableHours;
	}
	public double getNonBillableHours()
	{
		return nonBillableHours;
	}
	public void setNonBillableHours(double nonBillableHours)
	{
		this.nonBillableHours=nonBillableHours;
	}
	public double getTotalHours()
	{
		return totalHours;
	}
	public void setTotalHours(double totalHours)
	{
		this.totalHours=totalHours;
	}

	public int compareTo(GetReportDataReturnItem o) {
		//sort: 1=lastName, firstName (asc), 2=billable hours (desc), 3=non-billable hours (desc), 4=total hours (desc)
		switch (sortType)
		{
			case 2 : return (int)(o.billableHours-billableHours);
			case 3 : return (int)(o.getNonBillableHours()-nonBillableHours);
			case 4 : return (int)(o.getTotalHours()-getTotalHours());
 
		}
		
		if (lastName.compareTo(o.lastName)==0)
			return firstName.compareTo(o.firstName);
		return lastName.compareTo(o.lastName);
	}

}

	
