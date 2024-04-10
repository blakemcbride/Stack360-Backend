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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class ListOrgGroupsAndEmployeesInput extends TransmitInputBase {

    @Validation(required = false)
    private String orgGroupId;
    @Validation(required = false)
    private String[] employeeIds;
    @Validation(required = false)
    private String orgGroupNameStartsWith;
    @Validation(required = false)
    private String employeeLastNameStartsWith;
    @Validation(required=false)
    private String upOneLevel;

    public String getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(String orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public String[] getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String[] employeeIds) {
        this.employeeIds = employeeIds;
    }

    public String getEmployeeLastNameStartsWith() {
        return employeeLastNameStartsWith;
    }

    public void setEmployeeLastNameStartsWith(String employeeLastNameStartsWith) {
        this.employeeLastNameStartsWith = employeeLastNameStartsWith;
    }

    public String getOrgGroupNameStartsWith() {
        return orgGroupNameStartsWith;
    }

    public void setOrgGroupNameStartsWith(String orgGroupNameStartsWith) {
        this.orgGroupNameStartsWith = orgGroupNameStartsWith;
    }

    public String getUpOneLevel() {
        return upOneLevel;
    }

    public void setUpOneLevel(String upOneLevel) {
        this.upOneLevel = upOneLevel;
    }
}

	
