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
package com.arahant.services.standard.hr.hrCallLogParent;

import com.arahant.business.BProject;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class ListProjectsForEmployeeReturnItem {
	
	public ListProjectsForEmployeeReturnItem()
	{
		;
	}

	ListProjectsForEmployeeReturnItem (final BProject bc)
	{
		
		
		dateTimeRequested=DateUtils.getDateTimeFormatted(bc.getDateReported(), bc.getTimeReported());
		dateTimeCompleted=DateUtils.getDateTimeFormatted(bc.getDateCompleted(), bc.getTimeCompleted());
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		statusCode=bc.getProjectStatusCode();
		summary=bc.getDescription();
		typeCode=bc.getProjectTypeCode();
		assignedTo=bc.getCurrentPersonName();

	}
	
	private String dateTimeRequested;
	private String dateTimeCompleted;
	private String projectId;
	private String projectName;
	private String statusCode;
	private String summary;
	private String typeCode;
	private String assignedTo;

	public String getDateTimeRequested()
	{
		return dateTimeRequested;
	}
	public void setDateTimeRequested(final String dateTimeRequested)
	{
		this.dateTimeRequested=dateTimeRequested;
	}
	public String getDateTimeCompleted()
	{
		return dateTimeCompleted;
	}
	public void setDateTimeCompleted(final String dateTimeCompleted)
	{
		this.dateTimeCompleted=dateTimeCompleted;
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
	public String getTypeCode()
	{
		return typeCode;
	}
	public void setTypeCode(final String typeCode)
	{
		this.typeCode=typeCode;
	}
	public String getAssignedTo()
	{
		return assignedTo;
	}
	public void setAssignedTo(final String assignedTo)
	{
		this.assignedTo=assignedTo;
	}

}

	
