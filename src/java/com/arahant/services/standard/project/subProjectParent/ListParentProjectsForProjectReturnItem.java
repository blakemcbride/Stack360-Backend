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
public class ListParentProjectsForProjectReturnItem {
	
	public ListParentProjectsForProjectReturnItem()
	{
		;
	}

	ListParentProjectsForProjectReturnItem (BProject bc)
	{
		
		description=bc.getDescription();
		dateReportedFormatted=bc.getDateReportedFormatted();
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		reference=bc.getReference();
		requestingNameFormatted=bc.getRequestingCompanyName();

	}
	
	private String description;
private String dateReportedFormatted;
private String projectId;
private String projectName;
private String reference;
private String requestingNameFormatted;
;

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getDateReportedFormatted()
	{
		return dateReportedFormatted;
	}
	public void setDateReportedFormatted(String dateReportedFormatted)
	{
		this.dateReportedFormatted=dateReportedFormatted;
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
	public String getReference()
	{
		return reference;
	}
	public void setReference(String reference)
	{
		this.reference=reference;
	}
	public String getRequestingNameFormatted()
	{
		return requestingNameFormatted;
	}
	public void setRequestingNameFormatted(String requestingNameFormatted)
	{
		this.requestingNameFormatted=requestingNameFormatted;
	}

}

	
