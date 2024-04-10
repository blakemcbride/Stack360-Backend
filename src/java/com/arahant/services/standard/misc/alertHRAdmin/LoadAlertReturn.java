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
package com.arahant.services.standard.misc.alertHRAdmin;

import com.arahant.beans.Person;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BAlert;
import com.arahant.business.BEmployee;

public class LoadAlertReturn extends TransmitReturnBase {

    void setData(BAlert ba) {

        message = ba.getAlert();
        startDate = ba.getStartDate();
        endDate = ba.getLastDate();
        alertType = ba.getAlertDistribution();

        int i = 0;
        employeeIds = new ListEmployeesForGroupReturnItem[ba.getBean().getPersons().size()];
        for(Person p : ba.getBean().getPersons())
        {
            employeeIds[i]=new ListEmployeesForGroupReturnItem();
            employeeIds[i].setEmployeeId(p.getPersonId());
            employeeIds[i].setEmployeeName(p.getNameLFM());
            i++;

        }

    }

    private String message;
    private int startDate;
    private int endDate;
    private int alertType;
    private ListEmployeesForGroupReturnItem[] employeeIds;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public ListEmployeesForGroupReturnItem[] getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(ListEmployeesForGroupReturnItem[] employeeIds) {
        this.employeeIds = employeeIds;
    }

    void setEmployeeIds(final BEmployee[] e) {
        employeeIds = new ListEmployeesForGroupReturnItem[e.length];
        for (int loop = 0; loop < e.length; loop++) {
            employeeIds[loop] = new ListEmployeesForGroupReturnItem(e[loop]);
        }
    }
}

	
