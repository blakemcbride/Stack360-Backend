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
package com.arahant.services.standard.time.timesheetEntrySemiMonthly;

import com.arahant.business.BProject;
import com.arahant.business.BProjectShift;

public class GetQuickListItem {

	private String companyName;
	private String companyAndDescription;
	private String description;
	private String projectId;
	private String billable;
	private String projectName;
	private String shiftId;
	private String shiftStart;

	public GetQuickListItem() {
	}

	GetQuickListItem(final BProjectShift projectShift) {
		BProject project = projectShift.getProject();
		companyName = project.getCompanyName();
		description = project.getDescription();
		projectId = project.getProjectId();
		billable = project.getBillable() + "";
		projectName = project.getProjectName();
		companyAndDescription = description + " (" + companyName + ")";
		shiftId = projectShift.getProjectShiftId();
		shiftStart = projectShift.getShiftStart();
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
	 * @return Returns the companyAndDescription.
	 */
	public String getCompanyAndDescription() {
		return companyAndDescription;
	}

	/**
	 * @param companyAndDescription The companyAndDescription to set.
	 */
	public void setCompanyAndDescription(final String companyAndDescription) {
		this.companyAndDescription = companyAndDescription;
	}

	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName The companyName to set.
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
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

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public String getShiftStart() {
		return shiftStart;
	}

	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}
}
