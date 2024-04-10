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

package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.annotation.Validation;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;
import com.arahant.services.TransmitInputBase;


/**
 *         Created on Feb 8, 2007
 */
public class SearchBillableTimesheetsInput extends TransmitInputBase {

    @Validation(table = "org_group", column = "org_group_id", required = true)
    private String companyId;
    @Validation(required = false)
    private String[] excludedTimesheetIds;
    @Validation(type = "date", required = false)
    private int timesheetStartDate;
    @Validation(type = "date", required = false)
    private int timesheetEndDate;
    @Validation(table = "project", column = "project_id", required = false)
    private String projectId;
    @Validation(table = "person", column = "person_id", required = false)
    private String personId;
    @Validation(min = 0, max = 2, required = false)
    private int billableItemsIndicator;
    @Validation(required = false)
    private SearchMetaInput searchMeta;
    @Validation(required = true)
    private Boolean getAll;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String[] getExcludedTimesheetIds() {
        if (excludedTimesheetIds == null)
            return new String[0];

        return excludedTimesheetIds;
    }

    public void setExcludedTimesheetIds(String[] excludedTimesheetIds) {
        this.excludedTimesheetIds = excludedTimesheetIds;
    }

    public int getTimesheetStartDate() {
        return timesheetStartDate;
    }

    public void setTimesheetStartDate(int timesheetStartDate) {
        this.timesheetStartDate = timesheetStartDate;
    }

    public int getTimesheetEndDate() {
        return timesheetEndDate;
    }

    public void setTimesheetEndDate(int timesheetEndDate) {
        this.timesheetEndDate = timesheetEndDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getBillableItemsIndicator() {
        return billableItemsIndicator;
    }

    public void setBillableItemsIndicator(int billableItemsIndicator) {
        this.billableItemsIndicator = billableItemsIndicator;
    }

    public Boolean getGetAll() {
        return getAll;
    }

    public void setGetAll(Boolean getAll) {
        this.getAll = getAll;
    }

    public SearchMetaInput getSearchMeta() {
        return searchMeta;
    }

    public void setSearchMeta(SearchMetaInput searchMeta) {
        this.searchMeta = searchMeta;
    }

    BSearchMetaInput getSearchMetaInput() {
        if (getAll)
            searchMeta.setUsingPaging(false);
        if (searchMeta == null) {
            return new BSearchMetaInput(0, true, false, 0);
        } else {
            return new BSearchMetaInput(searchMeta, new String[]{"projectName", "externalReference", "billable", "billingRate",
                    "workDate", "totalHours", "timeDescription", "lastName", "firstName"});

            //externalReference, projectName, billable, billingRate, workDate, totalHours, timeDescription, lastName, firstName
        }
    }


}

	
