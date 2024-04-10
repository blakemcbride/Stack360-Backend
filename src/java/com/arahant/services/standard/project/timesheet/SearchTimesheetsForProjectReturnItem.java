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
package com.arahant.services.standard.project.timesheet;
import com.arahant.business.BTimesheet;


/**
 * 
 *
 *
 */
public class SearchTimesheetsForProjectReturnItem {
	
	public SearchTimesheetsForProjectReturnItem()
	{
		;
	}

	SearchTimesheetsForProjectReturnItem (BTimesheet bc)
	{
		
		state=bc.getState()+"";
		billable=bc.getBillable()+"";
		timeDescription=bc.getDescription();
		beginTime=bc.getBeginningTime();
		endTime=bc.getEndTime();
		totalHours=bc.getTotalHours();
		workDate=bc.getWorkDate();
		lastName=bc.getEmployeeLastName();
		firstName=bc.getEmployeeFirstName();

	}
	
	private String state;
	private String billable;
	private String timeDescription;
	private int beginTime;
	private int endTime;
	private double totalHours;
	private int workDate;
	private String lastName;
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getWorkDate() {
		return workDate;
	}

	public void setWorkDate(int workDate) {
		this.workDate = workDate;
	}

	
	

	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state=state;
	}
	public String getBillable()
	{
		return billable;
	}
	public void setBillable(String billable)
	{
		this.billable=billable;
	}
	public String getTimeDescription()
	{
		return timeDescription;
	}
	public void setTimeDescription(String timeDescription)
	{
		this.timeDescription=timeDescription;
	}
	public int getBeginTime()
	{
		return beginTime;
	}
	public void setBeginTime(int beginTime)
	{
		this.beginTime=beginTime;
	}
	public int getEndTime()
	{
		return endTime;
	}
	public void setEndTime(int endTime)
	{
		this.endTime=endTime;
	}
	public double getTotalHours()
	{
		return totalHours;
	}
	public void setTotalHours(double totalHours)
	{
		this.totalHours=totalHours;
	}

}

	
