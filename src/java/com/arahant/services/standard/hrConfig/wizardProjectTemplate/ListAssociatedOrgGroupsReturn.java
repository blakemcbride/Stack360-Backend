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
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.hrConfig.wizardProjectTemplate;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;
import com.arahant.services.standard.misc.companyOrgGroup.AssociatedGroups;

/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class ListAssociatedOrgGroupsReturn extends TransmitReturnBase {

    private AssociatedGroups[] orgGroups;
    private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private String[] excludeIds;

    /**
     * @param string
     */
    public ListAssociatedOrgGroupsReturn(final String msg) {
        super(msg);
    }

    /**
     *
     */
    public ListAssociatedOrgGroupsReturn() {
        super();
    }

    /**
     * @return Returns the orgGroups.
     */
    public AssociatedGroups[] getOrgGroups() {
        return orgGroups;
    }

    /**
     * @param orgGroups The orgGroups to set.
     */
    public void setOrgGroups(final AssociatedGroups[] orgGroups) {
        this.orgGroups = orgGroups;
    }

    /**
     * @param groups
     */
    void setOrgGroups(final BOrgGroup[] groups) {
        orgGroups = new AssociatedGroups[groups.length];
        for (int loop = 0; loop < orgGroups.length; loop++) {
            orgGroups[loop] = new AssociatedGroups(groups[loop]);
        }
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

	public String[] getExcludeIds() {
		return excludeIds;
	}

	public void setExcludeIds(String[] excludeIds) {
		this.excludeIds = excludeIds;
	}
}

	
