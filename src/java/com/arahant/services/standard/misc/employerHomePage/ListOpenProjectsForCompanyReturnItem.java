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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.business.BProject;
import com.arahant.utils.DateUtils;

public class ListOpenProjectsForCompanyReturnItem {
	
	public ListOpenProjectsForCompanyReturnItem()
	{
		
	}

	ListOpenProjectsForCompanyReturnItem (BProject bp)
	{	
		typeCode=bp.getProjectTypeCode();
		summary=bp.getDescription();
		dateTimeRequested=DateUtils.getDateTimeFormatted(bp.getDateReported(), bp.getTimeReported());
		dateRequested=bp.getDateReported();
		timeRequested=bp.getTimeReported();
		assignedTo=bp.getCurrentPersonName();
		projectId=bp.getProjectId();
		statusCode=bp.getProjectStatusCode();
	}
	
	private String typeCode;
	private String summary;
	private String dateTimeRequested;
	private int dateRequested;
	private int timeRequested;
	private String assignedTo;
	private String projectId;
	private String statusCode;

	
	public int getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(int dateRequested) {
		this.dateRequested = dateRequested;
	}

	public int getTimeRequested() {
		return timeRequested;
	}

	public void setTimeRequested(int timeRequested) {
		this.timeRequested = timeRequested;
	}
	public String getTypeCode()
	{
		return typeCode;
	}
	public void setTypeCode(String typeCode)
	{
		this.typeCode=typeCode;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary=summary;
	}
	public String getDateTimeRequested()
	{
		return dateTimeRequested;
	}
	public void setDateTimeRequested(String dateTimeRequested)
	{
		this.dateTimeRequested=dateTimeRequested;
	}
	public String getAssignedTo()
	{
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo)
	{
		this.assignedTo=assignedTo;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}

	
