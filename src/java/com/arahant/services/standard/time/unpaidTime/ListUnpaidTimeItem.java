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
 * Created on Apr 5, 2007
 * 
 */
package com.arahant.services.standard.time.unpaidTime;

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;



/**
 * 
 *
 * Created on Apr 5, 2007
 *
 */
public class ListUnpaidTimeItem {

//unpaidTimeId (whatever it is) employee f name, employee l name, date, hours, project name, project description
	
	private String unpaidTimesheetId;
	private String employeeFName;
	private String employeeLName;
	private String date;
	private double hours;
	private String projectName;
	private String projectDescriptiion;

	

	/**
	 * @return Returns the date.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * @return Returns the employeeFName.
	 */
	public String getEmployeeFName() {
		return employeeFName;
	}

	/**
	 * @param employeeFName The employeeFName to set.
	 */
	public void setEmployeeFName(final String employeeFName) {
		this.employeeFName = employeeFName;
	}

	/**
	 * @return Returns the employeeLName.
	 */
	public String getEmployeeLName() {
		return employeeLName;
	}

	/**
	 * @param employeeLName The employeeLName to set.
	 */
	public void setEmployeeLName(final String employeeLName) {
		this.employeeLName = employeeLName;
	}

	/**
	 * @return Returns the hours.
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @param hours The hours to set.
	 */
	public void setHours(final double hours) {
		this.hours = hours;
	}

	/**
	 * @return Returns the projectDescriptiion.
	 */
	public String getProjectDescriptiion() {
		return projectDescriptiion;
	}

	/**
	 * @param projectDescriptiion The projectDescriptiion to set.
	 */
	public void setProjectDescriptiion(final String projectDescriptiion) {
		this.projectDescriptiion = projectDescriptiion;
	}

	/**
	 * @return Returns the projectName.
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName The projectName to set.
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return Returns the unpaidTimesheetId.
	 */
	public String getUnpaidTimesheetId() {
		return unpaidTimesheetId;
	}

	/**
	 * @param unpaidTimesheetId The unpaidTimesheetId to set.
	 */
	public void setUnpaidTimesheetId(final String unpaidTimesheetId) {
		this.unpaidTimesheetId = unpaidTimesheetId;
	}

	public ListUnpaidTimeItem() {
		super();
	}

	/**
	 * @param timesheet
	 */
	ListUnpaidTimeItem(final BTimesheet t) {
		unpaidTimesheetId=t.getTimesheetId();
		employeeFName=t.getEmployeeFirstName();
		employeeLName=t.getEmployeeLastName();
		date=DateUtils.getDateFormatted(t.getWorkDate());
		hours=t.getTotalHours();
		projectName=t.getProjectName();
		projectDescriptiion=t.getProjectDescription();
	}
}


	
