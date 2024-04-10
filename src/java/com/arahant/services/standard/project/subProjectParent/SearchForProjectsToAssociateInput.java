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
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchForProjectsToAssociateInput extends TransmitInputBase {

	@Validation (required=true)
	private String parentId;
	@Validation (table="project",column="project_name",required=false)
	private String 	projectName;
	@Validation (required=false)
	private String 	companyId;

	@Validation (required=false)
	private String	category;
	@Validation (required=false)
	private String	type; 
	@Validation (required=false)
	private String	status;
	@Validation (table="project",column="description",required=false)
	private String  summary;
	
	@Validation (min=2,max=5,required=false)
	private int projectNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int summarySearchType;


	public String getParentId()
	{
		return parentId;
	}
	public void setParentId(String parentId)
	{
		this.parentId=parentId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProjectName() {
		return modifyForSearch(projectName,projectNameSearchType);
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectNameSearchType() {
		return projectNameSearchType;
	}

	public void setProjectNameSearchType(int projectNameSearchType) {
		this.projectNameSearchType = projectNameSearchType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSummary() {
		return modifyForSearch(summary,summarySearchType);
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getSummarySearchType() {
		return summarySearchType;
	}

	public void setSummarySearchType(int summarySearchType) {
		this.summarySearchType = summarySearchType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}

	
