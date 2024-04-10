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
package com.arahant.services.standard.project.employeeProjectList;
import com.arahant.business.BProject;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 *
 */
public class ListAssignedProjectsReturnItem {
	
	public ListAssignedProjectsReturnItem()
	{
		;
	}

	ListAssignedProjectsReturnItem (final BProject bc)
	{
		
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		projectSummary=bc.getDescription();
		projectStatusCode=bc.getProjectStatusCode();
		dateTimeAssignedFormatted=bc.getDateTimeAssignedFormatted();
		dateTimeReportedFormatted=bc.getDateTimeReportedFormatted();
		employeePriority=bc.getEmployeePriority(ArahantSession.getHSU().getCurrentPerson());

	}
	
	private String projectId;
	private String projectName;
	private String projectSummary;
	private String projectStatusCode;
	private String dateTimeAssignedFormatted;
	private String dateTimeReportedFormatted;
	private int employeePriority;

	public int getEmployeePriority() {
		return employeePriority;
	}

	public void setEmployeePriority(int employeePriority) {
		this.employeePriority = employeePriority;
	}
	
	
	

	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(final String projectId)
	{
		this.projectId=projectId;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(final String projectName)
	{
		this.projectName=projectName;
	}
	public String getProjectSummary()
	{
		return projectSummary;
	}
	public void setProjectSummary(final String projectSummary)
	{
		this.projectSummary=projectSummary;
	}
	public String getProjectStatusCode()
	{
		return projectStatusCode;
	}
	public void setProjectStatusCode(final String projectStatusCode)
	{
		this.projectStatusCode=projectStatusCode;
	}
	public String getDateTimeAssignedFormatted()
	{
		return dateTimeAssignedFormatted;
	}
	public void setDateTimeAssignedFormatted(final String dateTimeAssignedFormatted)
	{
		this.dateTimeAssignedFormatted=dateTimeAssignedFormatted;
	}
	public String getDateTimeReportedFormatted()
	{
		return dateTimeReportedFormatted;
	}
	public void setDateTimeReportedFormatted(final String dateTimeReportedFormatted)
	{
		this.dateTimeReportedFormatted=dateTimeReportedFormatted;
	}
}

	
