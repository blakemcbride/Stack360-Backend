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
import com.arahant.business.BProject;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class SearchProjectsReturnItem {
	
	public SearchProjectsReturnItem()
	{
		;
	}

	SearchProjectsReturnItem (final BProject bc) throws ArahantException
	{
		
		dateTimeRequestedFormatted=DateUtils.getDateTimeFormatted(bc.getDateReported(),bc.getTimeReported());
		personFirstName=bc.getDoneForPersonFirstName();
		personLastName=bc.getDoneForPersonLastName();
		statusCode=bc.getProjectStatusCode();
		summary=bc.getDescription();
		projectName=bc.getProjectName();
		projectId=bc.getProjectId();
		typeCode=bc.getProjectTypeCode();
		personStatus=bc.getDoneForPersonStatusName();
		
	}
	
	private String dateTimeRequestedFormatted;
	private String personFirstName;
	private String personLastName;
	private String statusCode;
	private String summary;
	private String projectName;
	private String projectId;
	private String typeCode;
	private String personStatus;
	
	/**
	 * @return Returns the employeeStatus.
	 */
	public String getPersonStatus() {
		return personStatus;
	}

	/**
	 * @param employeeStatus The employeeStatus to set.
	 */
	public void setPersonStatus(final String employeeStatus) {
		this.personStatus = employeeStatus;
	}

	/**
	 * @return Returns the typeCode.
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @param typeCode The typeCode to set.
	 */
	public void setTypeCode(final String typeCode) {
		this.typeCode = typeCode;
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

	public String getDateTimeRequestedFormatted()
	{
		return dateTimeRequestedFormatted;
	}
	public void setDateTimeRequestedFormatted(final String dateTimeRequestedFormatted)
	{
		this.dateTimeRequestedFormatted=dateTimeRequestedFormatted;
	}
	public String getPersonFirstName()
	{
		return personFirstName;
	}
	public void setPersonFirstName(final String employeeFirstName)
	{
		this.personFirstName=employeeFirstName;
	}
	public String getPersonLastName()
	{
		return personLastName;
	}
	public void setPersonLastName(final String employeeLastName)
	{
		this.personLastName=employeeLastName;
	}
	public String getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(final String statusCode)
	{
		this.statusCode=statusCode;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(final String summary)
	{
		this.summary=summary;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(final String projectName)
	{
		this.projectName=projectName;
	}

}

	
