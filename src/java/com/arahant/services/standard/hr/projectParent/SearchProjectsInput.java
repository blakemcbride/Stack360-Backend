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
package com.arahant.services.standard.hr.projectParent;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectsInput extends TransmitInputBase {

	@Validation (required=false)
	private String personId;
	@Validation (table="project",column="project_status_id",required=false)
	private String statusId;
	@Validation (table="project",column="project_name",required=false)
	private String projectName;
	@Validation (min=2,max=5,required=false)
	private int projectNameSearchType;
	@Validation (required=false)
	private int dateRequested;
	@Validation (required=false)
	private int timeRequested;

	/**
	 * @return Returns the dateRequested.
	 */
	public int getDateRequested() {
		return dateRequested;
	}
	/**
	 * @param dateRequested The dateRequested to set.
	 */
	public void setDateRequested(final int dateRequested) {
		this.dateRequested = dateRequested;
	}
	/**
	 * @return Returns the timeRequested.
	 */
	public int getTimeRequested() {
		return timeRequested;
	}
	/**
	 * @param timeRequested The timeRequested to set.
	 */
	public void setTimeRequested(final int timeRequested) {
		this.timeRequested = timeRequested;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(final String employeeId)
	{
		this.personId=employeeId;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(final String statusId)
	{
		this.statusId=statusId;
	}
	public String getProjectName()
	{
		return modifyForSearch(projectName, projectNameSearchType);
	}
	public void setProjectName(final String projectName)
	{
		this.projectName=projectName;
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
	

}

	
