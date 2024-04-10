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

package com.arahant.services.standard.time.timesheetAccountingReview;

import com.arahant.business.BTimesheet;


public class SearchTimesheetsReturnItem {

	private String billable;
	private String firstName;
	private String lastName;
	private String projectName;
	private String timeDescription;
	private String timesheetId;
	private String timesheetState;
	private double billableHours;
	private double nonbillableHours;
	private int workDate;
	private String invoiceId;

	public SearchTimesheetsReturnItem()
	{
	}

	SearchTimesheetsReturnItem (BTimesheet bc) {
		billable = bc.getBillable() + "";
		invoiceId = bc.getInvoiceIdentifier();
		firstName = bc.getFirstName();
		lastName = bc.getLastName();
		projectName = bc.getProjectName();
		timeDescription = bc.getDescription();
		timesheetId = bc.getTimesheetId();
		timesheetState = bc.getState() + "";
		if (bc.getBillable() == 'Y')
			billableHours = bc.getTotalHours();
		else
			nonbillableHours = bc.getTotalHours();
		workDate = bc.getWorkDate();
	}

	public String getBillable()
	{
		return billable;
	}
	public void setBillable(String billable)
	{
		this.billable=billable;
	}

	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
		this.projectName=projectName;
	}
	public String getTimeDescription()
	{
		return timeDescription;
	}
	public void setTimeDescription(String timeDescription)
	{
		this.timeDescription=timeDescription;
	}
	public String getTimesheetId()
	{
		return timesheetId;
	}
	public void setTimesheetId(String timesheetId)
	{
		this.timesheetId=timesheetId;
	}
	public String getTimesheetState()
	{
		return timesheetState;
	}
	public void setTimesheetState(String timesheetState)
	{
		this.timesheetState=timesheetState;
	}
	public int getWorkDate()
	{
		return workDate;
	}
	public void setWorkDate(int workDate)
	{
		this.workDate=workDate;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

    public double getBillableHours() {
        return billableHours;
    }

    public void setBillableHours(double billableHours) {
        this.billableHours = billableHours;
    }

    public double getNonbillableHours() {
        return nonbillableHours;
    }

    public void setNonbillableHours(double nonbillableHours) {
        this.nonbillableHours = nonbillableHours;
    }
}

	
