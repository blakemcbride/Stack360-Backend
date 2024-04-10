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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;

public class ListOrgGroupsAndEmployeesReturn extends TransmitReturnBase {

    ListOrgGroupsAndEmployeesReturnItem orgGroup[];
    ListEmployeesForGroupReturnItem employees[];
    
    private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);

    public void setCap(int x) {
        cap = x;
    }

    public int getCap() {
        return cap;
    }

    /**
     * @return Returns the item.
     */
    public ListOrgGroupsAndEmployeesReturnItem[] getOrgGroup() {
        if (orgGroup == null) {
            return new ListOrgGroupsAndEmployeesReturnItem[0];
        }
        return orgGroup;
    }

    /**
     * @param item The item to set.
     */
    public void setOrgGroup(final ListOrgGroupsAndEmployeesReturnItem[] orgGroup) {
        this.orgGroup = orgGroup;
    }

    /**
     * @param accounts
     */
    void setOrgGroup(final BOrgGroup[] o) {
        orgGroup = new ListOrgGroupsAndEmployeesReturnItem[o.length];
        for (int loop = 0; loop < o.length; loop++) {
            orgGroup[loop] = new ListOrgGroupsAndEmployeesReturnItem(o[loop]);
        }
    }

    public ListEmployeesForGroupReturnItem[] getEmployees() {
        return employees;
    }

    public void setEmployees(final ListEmployeesForGroupReturnItem[] employees) {
        this.employees = employees;
    }

    void setEmployees(final BEmployee[] e) {
        employees = new ListEmployeesForGroupReturnItem[e.length];
        for (int loop = 0; loop < e.length; loop++) {
            employees[loop] = new ListEmployeesForGroupReturnItem(e[loop]);
        }
    }
}

	
