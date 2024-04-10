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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.misc.companyOrgGroup;
import com.arahant.business.BProject;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;

/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class SearchProjectsReturnItem {



	public SearchProjectsReturnItem() {
		super();
	}

	
	/**
	 * @param project
	 */
	SearchProjectsReturnItem(final BProject p) {
		super();
		
		 projectStatusCode=p.getProjectStatusCode();

		 projectCategoryCode=p.getProjectCategoryCode();

		 projectTypeCode=p.getProjectTypeCode();

		 description=p.getDescription();
		 projectId=p.getProjectId();

		 projectName=p.getProjectName();
		 requestingCompanyName=p.getCompanyName();
	}

	private String requestingCompanyName;
	private String description;
	private String projectCategoryCode;
	private String projectId;
	private String projectName;
	private String projectTypeCode;
	private String projectStatusCode;
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}


	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}

	public String getRequestingCompanyName() {
		return requestingCompanyName;
	}

	public void setRequestingCompanyName(String requestingCompanyName) {
		this.requestingCompanyName = requestingCompanyName;
	}




	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}


	
	

	/**
	 * @return Returns the projectCategoryCode.
	 */
	public String getProjectCategoryCode() {
		return projectCategoryCode;
	}


	/**
	 * @param projectCategoryCode The projectCategoryCode to set.
	 */
	public void setProjectCategoryCode(final String projectCategoryCode) {
		this.projectCategoryCode = projectCategoryCode;
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
	public void setProjectId(final String projectId) {
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
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}




	/**
	 * @return Returns the projectStatusCode.
	 */
	public String getProjectStatusCode() {
		return projectStatusCode;
	}


	/**
	 * @param projectStatusCode The projectStatusCode to set.
	 */
	public void setProjectStatusCode(final String projectStatusCode) {
		this.projectStatusCode = projectStatusCode;
	}




	/**
	 * @return Returns the projectTypeCode.
	 */
	public String getProjectTypeCode() {
		return projectTypeCode;
	}


	/**
	 * @param projectTypeCode The projectTypeCode to set.
	 */
	public void setProjectTypeCode(final String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
	}



	
}

	
