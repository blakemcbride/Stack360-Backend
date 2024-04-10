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
package com.arahant.services.standard.misc.agencyOrgGroup;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

/**
 * 
 *
 * Created on Oct 13, 2009
 *
 */
public class AddCompanyToAgentInput extends TransmitInputBase {

    @Validation(required = true)
    private String[] companyId;
    @Validation(required = true)
    private String agencyId;
    @Validation(required = true)
    private String agentId;

    public String[] getCompanyId() {
        if (companyId == null) {
            companyId = new String[0];
        }
        return companyId;
    }

    public void setCompanyId(String[] companyId) {
        this.companyId = companyId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}

	
