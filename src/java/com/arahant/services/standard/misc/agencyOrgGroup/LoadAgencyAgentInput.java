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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agencyOrgGroup;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class LoadAgencyAgentInput extends TransmitInputBase {

    @Validation(required = true)
    private String personId;
    @Validation(required = true)
    private String groupId;
    @Validation(required = false)
    private String companyName;
    @Validation(min = 2, max = 5, required = false)
    private int companyNameSearchType;

    /**
     * @return Returns the groupId.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The groupId to set.
     */
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return Returns the personId.
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId The personId to set.
     */
    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public String getCompanyName() {
            return modifyForSearch(companyName, companyNameSearchType);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyNameSearchType() {
        return companyNameSearchType;
    }

    public void setCompanyNameSearchType(int companyNameSearchType) {
        this.companyNameSearchType = companyNameSearchType;
    }
}

	
