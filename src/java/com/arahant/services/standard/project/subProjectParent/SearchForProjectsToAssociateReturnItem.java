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
package com.arahant.services.standard.project.subProjectParent;
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class SearchForProjectsToAssociateReturnItem {
	
	public SearchForProjectsToAssociateReturnItem()
	{
		;
	}

	SearchForProjectsToAssociateReturnItem (BProject bc)
	{
		
		requestingCompanyName=bc.getRequestingCompanyName();
		description=bc.getDescription();
		projectCategoryCode=bc.getProjectCategoryCode();
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		projectTypeCode=bc.getProjectTypeCode();
		projectStatusCode=bc.getProjectStatusCode();

	}
	
	private String requestingCompanyName;
private String description;
private String projectCategoryCode;
private String projectId;
private String projectName;
private String projectTypeCode;
private String projectStatusCode;
;

	public String getRequestingCompanyName()
	{
		return requestingCompanyName;
	}
	public void setRequestingCompanyName(String requestingCompanyName)
	{
		this.requestingCompanyName=requestingCompanyName;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getProjectCategoryCode()
	{
		return projectCategoryCode;
	}
	public void setProjectCategoryCode(String projectCategoryCode)
	{
		this.projectCategoryCode=projectCategoryCode;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
		this.projectName=projectName;
	}
	public String getProjectTypeCode()
	{
		return projectTypeCode;
	}
	public void setProjectTypeCode(String projectTypeCode)
	{
		this.projectTypeCode=projectTypeCode;
	}
	public String getProjectStatusCode()
	{
		return projectStatusCode;
	}
	public void setProjectStatusCode(String projectStatusCode)
	{
		this.projectStatusCode=projectStatusCode;
	}

}

	
