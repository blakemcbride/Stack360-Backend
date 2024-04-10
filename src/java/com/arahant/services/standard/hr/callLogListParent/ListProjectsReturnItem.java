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
package com.arahant.services.standard.hr.callLogListParent;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class ListProjectsReturnItem {
	
	public ListProjectsReturnItem()
	{
		;
	}

	ListProjectsReturnItem (final BProject bc) throws ArahantException
	{
		
		dateTimeRequestedFormatted=DateUtils.getDateTimeFormatted(bc.getDateReported(), bc.getTimeReported());
		personFirstName=bc.getDoneForPersonFirstName();
		personLastName=bc.getDoneForPersonLastName();
		statusCode=bc.getProjectStatusCode();
		summary=bc.getDescription();
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		typeCode=bc.getProjectTypeCode();
		personStatus=bc.getDoneForPersonStatusName();
		
		personId=bc.getWorkDoneForEmployeeId();
		if (personId.equals(""))
			personType="";
		else
			if (new BPerson(personId).isEmployee())
			{
				personType="Emp";
			}
			else
			{
				personType="Dep";
			}

	}
	
	private String dateTimeRequestedFormatted;
	private String personFirstName;
	private String personLastName;
	private String statusCode;
	private String summary;
	private String projectId;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}
	private String projectName;
	private String typeCode;
	private String personStatus;
	private String personId;
	private String personType;
	
	
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
	 * @return Returns the assignedEmployeeFirstName.
	 */
	public String getPersonFirstName() {
		return personFirstName;
	}

	/**
	 * @param assignedEmployeeFirstName The assignedEmployeeFirstName to set.
	 */
	public void setPersonFirstName(final String assignedEmployeeFirstName) {
		this.personFirstName = assignedEmployeeFirstName;
	}

	/**
	 * @return Returns the assignedEmployeeLastName.
	 */
	public String getPersonLastName() {
		return personLastName;
	}

	/**
	 * @param assignedEmployeeLastName The assignedEmployeeLastName to set.
	 */
	public void setPersonLastName(final String assignedEmployeeLastName) {
		this.personLastName = assignedEmployeeLastName;
	}

	/**
	 * @return Returns the statusCode.
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode The statusCode to set.
	 */
	public void setStatusCode(final String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return Returns the summary.
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary The summary to set.
	 */
	public void setSummary(final String summary) {
		this.summary = summary;
	}

	/**
	 * @return Returns the dateTimeRequestedFormatted.
	 */
	public String getDateTimeRequestedFormatted() {
		return dateTimeRequestedFormatted;
	}

	/**
	 * @param dateTimeRequestedFormatted The dateTimeRequestedFormatted to set.
	 */
	public void setDateTimeRequestedFormatted(final String dateTimeRequestedFormatted) {
		this.dateTimeRequestedFormatted = dateTimeRequestedFormatted;
	}

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
	

}

	
