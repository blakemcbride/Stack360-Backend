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
package com.arahant.services.standard.time.timesheetEntry;

import com.arahant.business.BProject;

public class SearchProjectsReturnItem {

	private String projectStatusCode;
	private String projectCategoryCode;
	private String projectTypeCode;
	private String requestingCompanyName;
	private String projectSponsorName;
	private String description;
	private String projectId;
	private String billable;
	private String projectName;

	public SearchProjectsReturnItem() {
	}

	/**
	 * @param project
	 */
	SearchProjectsReturnItem(final BProject p) {
		projectStatusCode = p.getProjectStatusCode();
		projectCategoryCode = p.getProjectCategoryCode();
		projectTypeCode = p.getProjectTypeCode();
		requestingCompanyName = p.getCompanyName();
		projectSponsorName = p.getEmployeeName();
		description = p.getDescription();
		projectId = p.getProjectId();
		billable = p.getBillable() + "";
		projectName = p.getProjectName();
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
	 * @return Returns the projectCategoryCode.
	 */
	public String getProjectCategoryCode() {
		return projectCategoryCode;
	}

	/**
	 * @param projectCategoryCode The projectCategoryCode to set.
	 */
	public void setProjectCategoryCode(final String projectCategoryCode) {
		this.projectCategoryCode = projectCategoryCode;
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

	/**
	 * @return Returns the projectSponsorName.
	 */
	public String getProjectSponsorName() {
		return projectSponsorName;
	}

	/**
	 * @param projectSponsorName The projectSponsorName to set.
	 */
	public void setProjectSponsorName(final String projectSponsorName) {
		this.projectSponsorName = projectSponsorName;
	}

	/**
	 * @return Returns the projectStatusCode.
	 */
	public String getProjectStatusCode() {
		return projectStatusCode;
	}

	/**
	 * @param projectStatusCode The projectStatusCode to set.
	 */
	public void setProjectStatusCode(final String projectStatusCode) {
		this.projectStatusCode = projectStatusCode;
	}

	/**
	 * @return Returns the projectTypeCode.
	 */
	public String getProjectTypeCode() {
		return projectTypeCode;
	}

	/**
	 * @param projectTypeCode The projectTypeCode to set.
	 */
	public void setProjectTypeCode(final String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
	}

	/**
	 * @return Returns the requestingCompanyName.
	 */
	public String getRequestingCompanyName() {
		return requestingCompanyName;
	}

	/**
	 * @param requestingCompanyName The requestingCompanyName to set.
	 */
	public void setRequestingCompanyName(final String requestingCompanyName) {
		this.requestingCompanyName = requestingCompanyName;
	}
}
