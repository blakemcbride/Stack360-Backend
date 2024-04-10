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
package com.arahant.services.standard.time.timesheetClientReview;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class SearchProjectsInput extends TransmitInputBase {

	@Validation(table = "project", column = "project_name", required = false)
	private String projectName;
	@Validation(required = false)
	private String companyId;
	@Validation(required = false)
	private String category;
	@Validation(required = false)
	private String type;
	@Validation(required = false)
	private String status;
	@Validation(table = "project", column = "description", required = false)
	private String summary;
	@Validation(required = false)
	private boolean quickList;
	@Validation(required = false)
	private String personId;
	@Validation(min = 2, max = 5, required = false)
	private int projectNameSearchType;
	@Validation(min = 2, max = 5, required = false)
	private int summarySearchType;

	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getCategory()
	 */
	public String getCategory() {
		return category;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setCategory(java.lang.String)
	 */

	public void setCategory(final String category) {
		this.category = category;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getCompanyId()
	 */

	public String getCompanyId() {
		return companyId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setCompanyId(java.lang.String)
	 */

	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getPersonId()
	 */
	public String getPersonId() {
		return personId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setPerson(java.lang.String)
	 */

	public void setPersonId(final String personId) {
		this.personId = personId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getProjectName()
	 */

	public String getProjectName() {
		return modifyForSearch(projectName, projectNameSearchType);
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setProjectName(java.lang.String)
	 */

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#isQuickList()
	 */

	public boolean isQuickList() {
		return quickList;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setQuickList(boolean)
	 */

	public void setQuickList(final boolean quickList) {
		this.quickList = quickList;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getStatus()
	 */

	public String getStatus() {
		return status;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setStatus(java.lang.String)
	 */

	public void setStatus(final String status) {
		this.status = status;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getSummary()
	 */

	public String getSummary() {
		return modifyForSearch(summary, summarySearchType);
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setSummary(java.lang.String)
	 */

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#getType()
	 */
	public String getType() {
		return type;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.project.ISearchProjects#setType(java.lang.String)
	 */

	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return Returns the projectNameSearchType.
	 */
	public int getProjectNameSearchType() {
		return projectNameSearchType;
	}

	/**
	 * @param projectNameSearchType The projectNameSearchType to set.
	 */
	public void setProjectNameSearchType(final int projectNameSearchType) {
		this.projectNameSearchType = projectNameSearchType;
	}

	/**
	 * @return Returns the summarySearchType.
	 */
	public int getSummarySearchType() {
		return summarySearchType;
	}

	/**
	 * @param summarySearchType The summarySearchType to set.
	 */
	public void setSummarySearchType(final int summarySearchType) {
		this.summarySearchType = summarySearchType;
	}
}
