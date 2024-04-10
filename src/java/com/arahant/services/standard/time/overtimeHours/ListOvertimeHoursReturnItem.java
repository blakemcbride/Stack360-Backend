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
package com.arahant.services.standard.time.overtimeHours;
import com.arahant.business.BOvertimeApproval;


/**
 * 
 *
 *
 */
public class ListOvertimeHoursReturnItem {
	
	public ListOvertimeHoursReturnItem()
	{
	
	}

	ListOvertimeHoursReturnItem (BOvertimeApproval bc)
	{
		
		date=bc.getDateFormatted();
		approvedHours=bc.getApprovedHours();
		hoursTaken=bc.getHoursTaken();
		id=bc.getId();
		intDate=bc.getDate();
	}
	
	private String date;
	private double approvedHours;
	private double hoursTaken;
	private String id;
	private int intDate;

	public int getIntDate() {
		return intDate;
	}

	public void setIntDate(int intDate) {
		this.intDate = intDate;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	

	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date=date;
	}
	public double getApprovedHours()
	{
		return approvedHours;
	}
	public void setApprovedHours(double approvedHours)
	{
		this.approvedHours=approvedHours;
	}
	public double getHoursTaken()
	{
		return hoursTaken;
	}
	public void setHoursTaken(double hoursTaken)
	{
		this.hoursTaken=hoursTaken;
	}

}

	
