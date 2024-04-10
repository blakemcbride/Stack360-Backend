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
package com.arahant.services.standard.project.projectCurrentStatus;
import com.arahant.beans.Person;
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class ListProjectsForPersonReturnItem {
	
	public ListProjectsForPersonReturnItem()
	{
		;
	}

	ListProjectsForPersonReturnItem (BProject bc, Person p)
	{
		
		clientPriority=bc.getClientPriority();
		companyPriority=bc.getCompanyPriority();
		employeePriority=bc.getEmployeePriority(p);
		orgGroupPriority=bc.getOrgGroupPriority();
		projectId=bc.getProjectId();
		projectName=bc.getProjectName();
		statusCode=bc.getProjectStatusCode();
		summary=bc.getSummary();

	}
	
	private int clientPriority;
private int companyPriority;
private int employeePriority;
private int orgGroupPriority;
private String projectId;
private String projectName;
private String statusCode;
private String summary;
;

	public int getClientPriority()
	{
		return clientPriority;
	}
	public void setClientPriority(int clientPriority)
	{
		this.clientPriority=clientPriority;
	}
	public int getCompanyPriority()
	{
		return companyPriority;
	}
	public void setCompanyPriority(int companyPriority)
	{
		this.companyPriority=companyPriority;
	}
	public int getEmployeePriority()
	{
		return employeePriority;
	}
	public void setEmployeePriority(int employeePriority)
	{
		this.employeePriority=employeePriority;
	}
	public int getOrgGroupPriority()
	{
		return orgGroupPriority;
	}
	public void setOrgGroupPriority(int orgGroupPriority)
	{
		this.orgGroupPriority=orgGroupPriority;
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
	public String getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(String statusCode)
	{
		this.statusCode=statusCode;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary=summary;
	}

}

	
