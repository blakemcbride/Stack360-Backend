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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProject;

/**
 * 
 *
 *
 */
public class LoadProjectDetailsReturn extends TransmitReturnBase {

    void setData(BProject bc) {

        summary = bc.getSummary();
        detail = bc.getDetailDesc();
        dateCreated = bc.getDateReported();
        createdBy = bc.getSponsorNameFormatted();
        totalTimesheetEntries = bc.getTotalTimesheetEntries();
        totalLogEntries = bc.getTotalLogEntries();

    }
    private int dateCreated;
    private String createdBy;
    private int totalTimesheetEntries;
    private int totalLogEntries;
    private String summary;
    private String detail;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(int dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getTotalLogEntries() {
        return totalLogEntries;
    }

    public void setTotalLogEntries(int totalLogEntries) {
        this.totalLogEntries = totalLogEntries;
    }

    public int getTotalTimesheetEntries() {
        return totalTimesheetEntries;
    }

    public void setTotalTimesheetEntries(int totalTimesheetEntries) {
        this.totalTimesheetEntries = totalTimesheetEntries;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

	
