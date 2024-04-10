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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.projectParent;

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

    @Validation(table = "project", column = "project_name", required = false)
    private String projectName;
    @Validation(required = false)
    private String companyId;
    @Validation(type = "date", required = false)
    private int fromDate;
    @Validation(type = "date", required = false)
    private int toDate;
    @Validation(required = false)
    private String category;
    @Validation(required = false)
    private String type;
    @Validation(required = false)
    private String status;
    @Validation(table = "project", column = "description", required = false)
    private String projectSummary;
    @Validation(min = 2, max = 5, required = false)
    private int projectSummarySearchType;
    @Validation(table = "project", column = "reference", required = false)
    private String extReference;
    @Validation(min = 2, max = 5, required = false)
    private int extReferenceSearchType;
    @Validation(min = 0, max = 3, required = false)
    private int statusType;
    @Validation(min = 2, max = 5, required = false)
    private int projectNameSearchType;
    @Validation(required = false)
    private SearchMetaInput searchMeta;
    @Validation(required = false)
    private String excludeId;
    private String subType;

    public String getExcludeId() {
        return excludeId;
    }

    public void setExcludeId(String excludeId) {
        this.excludeId = excludeId;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    /**
     * @return Returns the projectNameSearchType.
     */
    public int getProjectNameSearchType() {
        return projectNameSearchType;
    }

    /**
     * @param projectNameSearchType The projectNameSearchType to set.
     */
    public void setProjectNameSearchType(final int projectNameSearchType) {
        this.projectNameSearchType = projectNameSearchType;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getCategory()
     */

    public String getCategory() {
        return category;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setCategory(java.lang.String)
     */

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getFromDate()
     */
    public int getFromDate() {
        return fromDate;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setFromDate(int)
     */

    public void setFromDate(final int fromDate) {
        this.fromDate = fromDate;
    }

    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getProjectName()
     */
    public String getProjectName() {
        return modifyForSearch(projectName, projectNameSearchType);
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setProjectName(java.lang.String)
     */

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getStatus()
     */
    public String getStatus() {
        return status;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setStatus(java.lang.String)
     */

    public void setStatus(final String status) {
        this.status = status;
    }

    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getToDate()
     */
    public int getToDate() {
        return toDate;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setToDate(int)
     */

    public void setToDate(final int toDate) {
        this.toDate = toDate;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#getType()
     */

    public String getType() {
        return type;
    }
    /* (non-Javadoc)
     * @see com.arahant.services.project.ISearchProjects#setType(java.lang.String)
     */

    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return Returns the extReference.
     */
    public String getExtReference() {
        return modifyForSearch(extReference, extReferenceSearchType);
    }

    /**
     * @param extReference The extReference to set.
     */
    public void setExtReference(final String extReference) {
        this.extReference = extReference;
    }

    /**
     * @return Returns the extReferenceSearchType.
     */
    public int getExtReferenceSearchType() {
        return extReferenceSearchType;
    }

    /**
     * @param extReferenceSearchType The extReferenceSearchType to set.
     */
    public void setExtReferenceSearchType(final int extReferenceSearchType) {
        this.extReferenceSearchType = extReferenceSearchType;
    }

    /**
     * @return Returns the projectSummary.
     */
    public String getProjectSummary() {
        return modifyForSearch(projectSummary, projectSummarySearchType);
    }

    /**
     * @param projectSummary The projectSummary to set.
     */
    public void setProjectSummary(final String projectSummary) {
        this.projectSummary = projectSummary;
    }

    /**
     * @return Returns the projectSummarySearchType.
     */
    public int getProjectSummarySearchType() {
        return projectSummarySearchType;
    }

    /**
     * @param projectSummarySearchType The projectSummarySearchType to set.
     */
    public void setProjectSummarySearchType(final int projectSummarySearchType) {
        this.projectSummarySearchType = projectSummarySearchType;
    }

    public SearchMetaInput getSearchMeta() {
        return searchMeta;
    }

    public void setSearchMeta(SearchMetaInput searchMeta) {
        this.searchMeta = searchMeta;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    BSearchMetaInput getSearchMetaInput() {
        if (searchMeta == null) {
            return new BSearchMetaInput(0, true, false, 0);
        } else {
            //projectName, description, dateReportedFormatted, reference, requestingNameFormatted
            return new BSearchMetaInput(searchMeta, new String[]{"projectName", "description", "dateReportedFormatted", "reference",
                        "requestingNameFormatted", "status"});
        }
    }
}

	
