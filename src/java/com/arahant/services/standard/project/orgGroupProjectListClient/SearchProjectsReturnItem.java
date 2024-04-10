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
package com.arahant.services.standard.project.orgGroupProjectListClient;
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class SearchProjectsReturnItem implements Comparable<SearchProjectsReturnItem> {

	private String projectId;
	private String projectName;
	private String summary;
	private String statusCode;
	private String companyName;
	private int clientPriority;
	String sortOn;
	boolean sortAsc;
	private String statusId;
	private String routeStopId;
	private String assigned;

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	
	
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	

	public int getClientPriority() {
		return clientPriority;
	}

	public void setClientPriority(int clientPriority) {
		this.clientPriority = clientPriority;
	}


	public SearchProjectsReturnItem()
	{
		
	}

	SearchProjectsReturnItem (BProject bp, String orgGroupId)
	{
		projectId = bp.getProjectId();
		projectName = bp.getProjectName();
		summary = bp.getDescription();
		statusCode = bp.getProjectStatusCode();
		clientPriority=bp.getClientPriority();
		companyName=bp.getCompanyName();
		statusId=bp.getProjectStatusId();
		routeStopId=bp.getRouteStopId();
		assigned=bp.getAssignedToOrgGroup(orgGroupId);
	}

	public String getRouteStopId() {
		return routeStopId;
	}

	public void setRouteStopId(String routeStopId) {
		this.routeStopId = routeStopId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
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
	public void setProjectId(String projectId) {
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
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public void setStatusCode(String statusCode) {
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
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int compareTo(SearchProjectsReturnItem o) {

		if (sortAsc)
		{
			if ("projectName".equals(sortOn))
				return projectName.compareTo(o.projectName);
			if ("summary".equals(sortOn))
				return summary.compareTo(o.summary);
			if ("statusCode".equals(sortOn))
				return statusCode.compareTo(o.statusCode);
			if ("clientPriority".equals(sortOn))
				return clientPriority-o.clientPriority;
		
	//		if ("assignPersonFormatted".equals(sortOn))
	//			return assignPersonFormatted.compareTo(o.assignPersonFormatted);
				
		}
		else
		{
			if ("projectName".equals(sortOn))
				return o.projectName.compareTo(projectName);
			if ("summary".equals(sortOn))
				return o.summary.compareTo(summary);
			if ("statusCode".equals(sortOn))
				return o.statusCode.compareTo(statusCode);
			if ("clientPriority".equals(sortOn))
				return o.clientPriority-clientPriority;

	//		if ("assignPersonFormatted".equals(sortOn))
	//			return o.assignPersonFormatted.compareTo(assignPersonFormatted);
			
		}
		
		return 0;	
	}
}

	
