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


package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 *
 */
public class SearchOrgGroupsInput extends TransmitInputBase {

    @Validation(required = false)
    private String reportId;
    @Validation(required = false)
    private String name;
    @Validation(min = 2, max = 5, required = false)
    private int nameSearchType;

    /**
     * @return Returns the group_id.
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * @param group_id The group_id to set.
     */
    public void setReportId(final String reportId) {
        this.reportId = reportId;
    }

    public String getName() {
        return modifyForSearch(name, nameSearchType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameSearchType() {
        return nameSearchType;
    }

    public void setNameSearchType(int nameSearchType) {
        this.nameSearchType = nameSearchType;
    }
}

	
