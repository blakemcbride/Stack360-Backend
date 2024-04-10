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
 *
 */
package com.arahant.services.standard.misc.developerHomepage;
import com.arahant.business.BProject;

public class ListAssignedProjectsReturnItem {
	
	public ListAssignedProjectsReturnItem()
	{

	}

	ListAssignedProjectsReturnItem (BProject bc)
	{
		projectId=bc.getProjectId();
		externalId=bc.getProjectName();
		title=bc.getDescription();
		description=bc.getDetailDesc();
		status=bc.getProjectStatusCode();
		company=bc.getCompanyName();
		estimatedHours=bc.getEstimateHours();
		loggedHours=bc.getTotalLoggedTime();
		createdDate=bc.getDateReported();
	}
	
	private String projectId;
	private String externalId;
	private String title;
	private String description;
	private String status;
	private String company;
	private double estimatedHours;
	private double loggedHours;
	private int createdDate;
	

	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId=externalId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getCompany()
	{
		return company;
	}
	public void setCompany(String company)
	{
		this.company=company;
	}
	public double getEstimatedHours()
	{
		return estimatedHours;
	}
	public void setEstimatedHours(double estimatedHours)
	{
		this.estimatedHours=estimatedHours;
	}
	public double getLoggedHours()
	{
		return loggedHours;
	}
	public void setLoggedHours(double loggedHours)
	{
		this.loggedHours=loggedHours;
	}
	public int getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(int createdDate)
	{
		this.createdDate=createdDate;
	}

}

	
