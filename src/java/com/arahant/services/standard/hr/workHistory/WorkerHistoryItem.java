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


package com.arahant.services.standard.hr.workHistory;


public class WorkerHistoryItem {

    private String projectId;
    private String extRef;
    private String summary;
    private float billableHours;
    private float nonbillableHours;
    private int dateLastWorked;

    public WorkerHistoryItem() {
    }

    public WorkerHistoryItem(String projectId, String extRef, String summary, float billableHours, float nonBillableHours, int dateLastWorked) {
        this.projectId = projectId;
        this.extRef = extRef;
        this.summary = summary;
        this.billableHours = billableHours;
        this.nonbillableHours = nonBillableHours;
        this.dateLastWorked = dateLastWorked;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getExtRef() {
        return extRef;
    }

    public void setExtRef(String extRef) {
        this.extRef = extRef;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getBillableHours() {
        return billableHours;
    }

    public void setBillableHours(float billableHours) {
        this.billableHours = billableHours;
    }

    public float getNonbillableHours() {
        return nonbillableHours;
    }

    public void setNonbillableHours(float nonbillableHours) {
        this.nonbillableHours = nonbillableHours;
    }

    public int getDateLastWorked() {
        return dateLastWorked;
    }

    public void setDateLastWorked(int dateLastWorked) {
        this.dateLastWorked = dateLastWorked;
    }
}

	
