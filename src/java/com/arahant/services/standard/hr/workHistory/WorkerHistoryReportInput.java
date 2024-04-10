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

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


public class WorkerHistoryReportInput extends TransmitInputBase {

    @Validation(required = true)
    private String employeeId;
    private int firstDate;
    private int lastDate;

    /**
     * @return Returns the employeeId.
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId The employeeId to set.
     */
    public void setEmployeeId(final String employeeId) {
        this.employeeId = employeeId;
    }

    public int getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(int firstDate) {
        this.firstDate = firstDate;
    }

    public int getLastDate() {
        return lastDate;
    }

    public void setLastDate(int lastDate) {
        this.lastDate = lastDate;
    }
}

	
