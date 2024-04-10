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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class AddTimeEntryInput extends TransmitInputBase {

	@Validation(required = true)
	private String projectId;
	@Validation(required = true)
	private String personId;
	@Validation(table = "timesheet", column = "description", required = false)
	private String description;
	@Validation(min = .01, required = true)
	private double totalHours;
	@Validation(type = "date", required = true)
	private int workDate;
	@Validation(type = "date", required = true)
	private int endDate;
	@Validation(min = 0, max = 235959999, type = "time", required = false)
	private int beginningTime;
	@Validation(min = 0, max = 235959999, type = "time", required = false)
	private int endTime;
	@Validation(table = "timesheet", column = "billable", required = true)
	private String billable;

	/**
	 * @return Returns the beginningTime.
	 */
	public int getBeginningTime() {
		return beginningTime;
	}

	/**
	 * @param beginningTime The beginningTime to set.
	 */
	public void setBeginningTime(final int beginningTime) {
		this.beginningTime = beginningTime;
	}

	/**
	 * @return Returns the billable.
	 */
	public String getBillable() {
		return billable;
	}

	/**
	 * @param billable The billable to set.
	 */
	public void setBillable(final String billable) {
		this.billable = billable;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the endTime.
	 */
	public int getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(final int endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return Returns the totalHours.
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/**
	 * @param totalHours The totalHours to set.
	 */
	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * @return Returns the workDate.
	 */
	public int getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate The workDate to set.
	 */
	public void setWorkDate(final int workDate) {
		this.workDate = workDate;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

}
