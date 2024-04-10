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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class SearchProjectsInput extends TransmitInputBase {

    @Validation(table = "project", column = "project_name", required = false)
    private String projectName;
    @Validation(required = false)
    private String companyId;
    @Validation(required = false)
    private String category;
    @Validation(required = false)
    private String type;
    @Validation(required = false)
    private String status;
    @Validation(table = "project", column = "description", required = false)
    private String summary;
    @Validation(required = false)
    private boolean quickList;
    @Validation(required = false)
    private String personId;
    @Validation(min = 2, max = 5, required = false)
    private int projectNameSearchType;
    @Validation(min = 2, max = 5, required = false)
    private int summarySearchType;


    public int getProjectNameSearchType() {
        return projectNameSearchType;
    }

    public void setProjectNameSearchType(final int projectNameSearchType) {
        this.projectNameSearchType = projectNameSearchType;
    }

    public int getSummarySearchType() {
        return summarySearchType;
    }

    public void setSummarySearchType(final int summarySearchType) {
        this.summarySearchType = summarySearchType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public String getProjectName() {
        return modifyForSearch(projectName, projectNameSearchType);
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public boolean isQuickList() {
        return quickList;
    }

    public void setQuickList(final boolean quickList) {
        this.quickList = quickList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getSummary() {
        return modifyForSearch(summary, summarySearchType);
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

}
