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
package com.arahant.services.standard.time.timesheetByPayPeriod;

import com.arahant.business.BEmployee;
import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;


public class SearchTimesheetsReturnItem {
	SearchTimesheetsReturnItem() {

	}

	SearchTimesheetsReturnItem(BTimesheet bc1) {
		employeeId = bc1.getPersonId();
		employeeName = bc1.getEmployeeNameLFM();
		timesheetId = bc1.getTimesheetId();
		timesheetDate = bc1.getWorkDate();
		startTime = bc1.getStartTime();
		endTime = bc1.getEndTime();
		elapsedTime = bc1.getTotalHours();
		description = bc1.getDescription();
		projectDescription = bc1.getProjectDescription();
		projectName = bc1.getProjectName();
		projectId = bc1.getProjectID();
		colorSet = false;
		rowType = 1;
		dayOfWeek = DateUtils.getDayOfWeekAbbreviated(timesheetDate);
	}

	SearchTimesheetsReturnItem(BEmployee be, int date, double totalDailyHours) {
		employeeNameTotal = be.getNameLFM();
		elapsedTime = totalDailyHours;
		description = "Daily Total for: " + DateUtils.getDateFormatted(date);
		colorSet = false;
		rowType = 2;
	}

	SearchTimesheetsReturnItem(BEmployee be, double totalHours) {
		employeeNameTotal = be.getNameLFM();
		elapsedTime = totalHours;
		description = "Period Total";
		colorSet = false;
		rowType = 3;
	}

	private String employeeId;
	private String employeeName;
	private String employeeNameTotal;
	private String timesheetId;
	private int timesheetDate;
	private int startTime;
	private int endTime;
	private double elapsedTime;
	private String description;
	private String projectDescription;
	private String projectName;
	private String projectId;
	private boolean colorSet;
	private int rowType;
	private String dayOfWeek;

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeNameTotal() {
		return employeeNameTotal;
	}

	public void setEmployeeNameTotal(String employeeNameTotal) {
		this.employeeNameTotal = employeeNameTotal;
	}
	
	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getRowType() {
		return rowType;
	}

	public void setRowType(int rowType) {
		this.rowType = rowType;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getTimesheetDate() {
		return timesheetDate;
	}

	public void setTimesheetDate(int timesheetDate) {
		this.timesheetDate = timesheetDate;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
	}

	public boolean getColorSet() {
		return colorSet;
	}

	public void setColorSet(boolean colorSet) {
		this.colorSet = colorSet;
	}
	
}

	
