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
package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.beans.Person;
import com.arahant.business.BProject;
import com.arahant.exceptions.ArahantException;

import java.util.List;

public class SearchProjectsReturnItem implements Comparable<SearchProjectsReturnItem> {

	private String projectId;
	private String projectName;
	private String summary;
	private String statusCode;
	private String companyName;
	private int companyPriority;
	private int orgGroupPriority;
	private int clientPriority;
	private int employeePriority;
	private String sortOn;
	private boolean sortAsc;
	private String statusId;
	private String routeStopId;
	private String requestingClient;
	private int promisedDate;
	private double estHours;
	
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

	public int getCompanyPriority() {
		return companyPriority;
	}

	public void setCompanyPriority(int companyPriority) {
		this.companyPriority = companyPriority;
	}

	public int getEmployeePriority() {
		return employeePriority;
	}

	public void setEmployeePriority(int employeePriority) {
		this.employeePriority = employeePriority;
	}

	public int getOrgGroupPriority() {
		return orgGroupPriority;
	}

	public void setOrgGroupPriority(int orgGroupPriority) {
		this.orgGroupPriority = orgGroupPriority;
	}

	public SearchProjectsReturnItem()
	{
	}

	SearchProjectsReturnItem (BProject bp, String personId) {
		projectId = bp.getProjectId();
		projectName = bp.getProjectName();
		summary = bp.getDescription();
		statusCode = bp.getProjectStatusCode();
		companyPriority = bp.getCompanyPriority();
		orgGroupPriority = bp.getOrgGroupPriority();
		clientPriority = bp.getClientPriority();
		companyName = bp.getCompanyName();
		statusId = bp.getProjectStatusId();
		routeStopId = bp.getRouteStopId();

		List<Person> asp = bp.getAssignedPersons2();

		employeePriority = 0;

		if (personId != null && !"".equals(personId)) {
			for (Person bap : asp) {
				if (bap.getPersonId().equals(personId)) {
					if (true) throw new ArahantException("XXYY");
// XXYY					employeePriority = bap.getPriority();
					break;
				}
			}
		}

		promisedDate = bp.getPromisedDate();
		requestingClient = bp.getRequestingCompanyName();
		estHours = bp.getEstimateHours();
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

	public String getRequestingClient() {
		return requestingClient;
	}

	public void setRequestingClient(String requestingClient) {
		this.requestingClient = requestingClient;
	}

	public int getPromisedDate() {
		return promisedDate;
	}

	public void setPromisedDate(int promisedDate) {
		this.promisedDate = promisedDate;
	}

	public double getEstHours() {
		return estHours;
	}

	public void setEstHours(double estHours) {
		this.estHours = estHours;
	}

	@Override
	public int compareTo(SearchProjectsReturnItem o) {

		if (sortAsc)
		{
			if ("projectName".equals(sortOn))
				return projectName.compareTo(o.projectName);
			if ("summary".equals(sortOn))
				return summary.compareTo(o.summary);
			if ("statusCode".equals(sortOn))
				return statusCode.compareTo(o.statusCode);
			if ("companyPriority".equals(sortOn))
				return companyPriority-o.companyPriority;
			if ("orgGroupPriority".equals(sortOn))
				return orgGroupPriority-o.orgGroupPriority;
			if ("clientPriority".equals(sortOn))
				return clientPriority-o.clientPriority;
			if ("employeePriority".equals(sortOn))
				return employeePriority-o.employeePriority;
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
			if ("companyPriority".equals(sortOn))
				return o.companyPriority-companyPriority;
			if ("orgGroupPriority".equals(sortOn))
				return o.orgGroupPriority-orgGroupPriority;
			if ("clientPriority".equals(sortOn))
				return o.clientPriority-clientPriority;
			if ("employeePriority".equals(sortOn))
				return o.employeePriority-employeePriority;
	//		if ("assignPersonFormatted".equals(sortOn))
	//			return o.assignPersonFormatted.compareTo(assignPersonFormatted);
			
		}
		
		return 0;	
	}
}

	
