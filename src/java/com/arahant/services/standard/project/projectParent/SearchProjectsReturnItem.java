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

package com.arahant.services.standard.project.projectParent;

import com.arahant.business.BProject;
import com.arahant.utils.DateUtils;


/**
 * Created on Feb 12, 2007
 *
 */
public class SearchProjectsReturnItem /*implements Comparable<SearchProjectsReturnItem> */ {

	private int dateReported;
	private String reference;
	private String startDate;
	private String endDate;
	private String requestingNameFormatted;
	private String description;
	private String projectId;
	private String projectName;
	private String status;

	public SearchProjectsReturnItem() {
	}

	SearchProjectsReturnItem(final BProject p) {
		reference = p.getReference();
		startDate = DateUtils.getDateFormatted(p.getEstimatedFirstDate());
		endDate = DateUtils.getDateFormatted(p.getEstimatedLastDate());
		requestingNameFormatted = p.getRequesterNameFormatted();
		description = p.getDescription();
		projectId = p.getProjectId();
		projectName = p.getProjectName();
		dateReported = p.getDateReported();
		status = p.getProjectStatusCode() + " - " + p.getProjectStatusDescription();
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
	 * @return Returns the reference.
	 */
	public String getReference() {
		return reference;
	}


	/**
	 * @param reference The reference to set.
	 */
	public void setReference(final String reference) {
		this.reference = reference;
	}


	/**
	 * @return Returns the requestingCompanyName.
	 */
	public String getRequestingNameFormatted() {
		return requestingNameFormatted;
	}

	public void setRequestingNameFormatted(final String requestingNameFormatted) {
		this.requestingNameFormatted = requestingNameFormatted;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getDateReported() {
		return dateReported;
	}

	public void setDateReported(int dateReported) {
		this.dateReported = dateReported;
	}
}

	
