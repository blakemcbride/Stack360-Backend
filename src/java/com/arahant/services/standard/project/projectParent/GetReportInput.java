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
package com.arahant.services.standard.project.projectParent;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private String projectName;
	@Validation (required=false)
	private String companyId;
	@Validation (required=false)
	private String category;
	@Validation (required=false)
	private String type;
	@Validation (required=false)
	private String status;
	@Validation (required=false)
	private String projectSummary;
	@Validation (required=false)
	private String extReference;
	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (type="date",required=false)
	private int toDate;
	@Validation (min=2,max=5,required=false)
	private int projectSummarySearchType;
	@Validation (min=2,max=5,required=false)
	private int extReferenceSearchType;
	@Validation (min=0,max=3,required=false)
	private int statusType;
	@Validation (min=2,max=5,required=false)
	private int projectNameSearchType;

	public int getProjectNameSearchType() {
		return projectNameSearchType;
	}

	public void setProjectNameSearchType(int projectNameSearchType) {
		this.projectNameSearchType = projectNameSearchType;
	}

	public int getStatusType() {
		return statusType;
	}

	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}
	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectName() {
		return modifyForSearch(projectName,projectNameSearchType);
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	
	public String getProjectSummary() {
		return modifyForSearch(projectSummary, projectSummarySearchType);
	}

	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	public String getExtReference() {
		return modifyForSearch(extReference, extReferenceSearchType);
	}

	public void setExtReference(String extReference) {
		this.extReference = extReference;
	}

	public int getFromDate() {
		return fromDate;
	}

	public void setFromDate(int fromDate) {
		this.fromDate = fromDate;
	}

	public int getToDate() {
		return toDate;
	}

	public void setToDate(int toDate) {
		this.toDate = toDate;
	}

	public int getProjectSummarySearchType() {
		return projectSummarySearchType;
	}

	public void setProjectSummarySearchType(int projectSummarySearchType) {
		this.projectSummarySearchType = projectSummarySearchType;
	}

	public int getExtReferenceSearchType() {
		return extReferenceSearchType;
	}

	public void setExtReferenceSearchType(int extReferenceSearchType) {
		this.extReferenceSearchType = extReferenceSearchType;
	}
}

	
