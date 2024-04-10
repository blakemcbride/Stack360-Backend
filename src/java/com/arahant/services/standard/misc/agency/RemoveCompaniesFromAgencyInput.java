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
package com.arahant.services.standard.misc.agency;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAgency;
import com.arahant.annotation.Validation;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class RemoveCompaniesFromAgencyInput extends TransmitInputBase {

    @Validation(min = 0, max = 5000)
    private String ids[];
    private String agencyId;

    public void setIds(String x[]) {
        ids = x;
    }

    public String[] getIds() {
        return ids;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }
}

	