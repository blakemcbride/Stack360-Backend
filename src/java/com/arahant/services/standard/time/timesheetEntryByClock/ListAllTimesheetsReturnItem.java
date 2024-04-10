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

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;

public class ListAllTimesheetsReturnItem {

	public ListAllTimesheetsReturnItem() {
	}
  
	ListAllTimesheetsReturnItem (BTimesheet bc)
	{
		startDate=bc.getStartDate();
		startTime=bc.getStartTime();
		finalDate=bc.getEndDate();
		finalTime=bc.getEndTime();
		description=bc.getDescription();
		projectName=bc.getProjectName();
        projectDescription=bc.getProjectDescription();
		dayOfWeek=DateUtils.getDayOfWeekAbbreviated(bc.getWorkDate());
	}
	private int startDate;
	private int startTime;
	private int finalDate;
	private int finalTime;
	private String description;
	private String projectName;
    private String projectDescription;
	private String dayOfWeek;

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}

	public int getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(int finalTime) {
		this.finalTime = finalTime;
	}

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }



}

	
