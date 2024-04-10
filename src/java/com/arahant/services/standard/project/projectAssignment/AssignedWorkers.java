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

package com.arahant.services.standard.project.projectAssignment;

/**
 * Author: Blake McBride
 * Date: 2/12/21
 */
public class AssignedWorkers {

    private String personId;
    private int startDate;
    private boolean startDateChanged;
    private boolean manager;
    private boolean reportsHours;
    private String projectEmployeeJoinId;
    private String shiftId;
    private String originalShiftId;

    public AssignedWorkers() {}

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public boolean isStartDateChanged() {
        return startDateChanged;
    }

    public void setStartDateChanged(boolean startDateChanged) {
        this.startDateChanged = startDateChanged;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public boolean isReportsHours() {
        return reportsHours;
    }

    public void setReportsHours(boolean reportsHours) {
        this.reportsHours = reportsHours;
    }

    public String getProjectEmployeeJoinId() {
        return projectEmployeeJoinId;
    }

    public void setProjectEmployeeJoinId(String projectEmployeeJoinId) {
        this.projectEmployeeJoinId = projectEmployeeJoinId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getOriginalShiftId() {
        return originalShiftId;
    }

    public void setOriginalShiftId(String originalShiftId) {
        this.originalShiftId = originalShiftId;
    }
}
