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
package com.arahant.services.standard.misc.employeeHomePage;
import com.arahant.business.BProject;

public class ListProjectsForEmployeeReturnItem {
	
	public ListProjectsForEmployeeReturnItem()
	{
		
	}

	ListProjectsForEmployeeReturnItem (BProject bc)
	{
		
		projectName=bc.getProjectName();
		projectId=bc.getProjectId();
		projectStatusName=bc.getProjectStatusCode();
		projectSummary=bc.getSummary();

	}
	
	private String projectName;
	private String projectId;
	private String projectStatusName;
	private String projectSummary;
	

	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
		this.projectName=projectName;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public String getProjectStatusName()
	{
		return projectStatusName;
	}
	public void setProjectStatusName(String projectStatusName)
	{
		this.projectStatusName=projectStatusName;
	}
	public String getProjectSummary()
	{
		return projectSummary;
	}
	public void setProjectSummary(String projectSummary)
	{
		this.projectSummary=projectSummary;
	}

}

	
