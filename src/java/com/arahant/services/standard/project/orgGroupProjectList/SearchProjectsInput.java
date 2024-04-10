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
import com.arahant.annotation.Validation;

import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectsInput extends TransmitInputBase {
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private String personId;
	@Validation (table="project",column="description",required=false)
	private String projectSummary;
	@Validation (min=2,max=5,required=false)
	private int projectSummarySearchType;
	@Validation (table="project",column="reference",required=false)
	private String extReference;
	@Validation (min=2,max=5,required=false)
	private int	extReferenceSearchType;
	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (type="date",required=false)
	private int toDate;
	@Validation (required=false)
	private String []categoryIds;
	@Validation (required=false)
	private String []typeIds;
	@Validation (required=false)
	private String []statusIds;
	@Validation (required=false)
	private String companyId;

	public boolean getShowAssigned() {
		return showAssigned;
	}

	public void setShowAssigned(boolean showAssigned) {
		this.showAssigned = showAssigned;
	}
	@Validation (required=false)
	private boolean showAssigned;

	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getProjectSummary() {
		return modifyForSearch(projectSummary, projectSummarySearchType);
	}
	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}
	public int getProjectSummarySearchType() {
		return projectSummarySearchType;
	}
	public void setProjectSummarySearchType(final int projectSummarySearchType) {
		this.projectSummarySearchType = projectSummarySearchType;
	}
	
	public int getToDate() {
		return toDate;
	}
	public void setToDate(int toDate) {
		this.toDate = toDate;
	}
	public int getFromDate() {
		return fromDate;
	}
	public void setFromDate(int fromDate) {
		this.fromDate = fromDate;
	}
	public String getExtReference() {
		return modifyForSearch(extReference, extReferenceSearchType);
	}
	public void setExtReference(String extReference) {
		this.extReference = extReference;
	}

	public String[] getCategoryIds() {
		if (categoryIds==null)
			return new String[0];
		return categoryIds;
	}

	public void setCategoryIds(String[] categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String[] getStatusIds() {
		if (statusIds==null)
			return new String[0];
		return statusIds;
	}

	public void setStatusIds(String[] statusIds) {
		this.statusIds = statusIds;
	}

	public String[] getTypeIds() {
		if (typeIds==null)
			return new String[0];
		return typeIds;
	}

	public void setTypeIds(String[] typeIds) {
		this.typeIds = typeIds;
	}
	public int getExtReferenceSearchType() {
		return extReferenceSearchType;
	}
	public void setExtReferenceSearchType(final int extReferenceSearchType) {
		this.extReferenceSearchType = extReferenceSearchType;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}

	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}
	
	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);		
		} else {
			return new BSearchMetaInput(searchMeta,new String[]{"projectName", "summary", "statusCode", "companyPriority",
				"orgGroupPriority", "clientPriority", "employeePriority"});
		}
	}
	
}

	
