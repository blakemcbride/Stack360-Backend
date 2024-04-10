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
import com.arahant.annotation.Validation;

/**
 * 
 *
 * Created on Oct 13, 2009
 *
 */
public class SearchCompaniesInput extends TransmitInputBase {

    @Validation(required = false)
    private int associatedIndicator;
    @Validation(required = false)
    private String name;
    @Validation(min = 2, max = 5, required = false)
    private int nameSearchType;
    @Validation(required = true)
    private String agencyId;

    public int getAssociatedIndicator() {
        return associatedIndicator;
    }

    public void setAssociatedIndicator(int associatedIndicator) {
        this.associatedIndicator = associatedIndicator;
    }

    public String getName() {
        return modifyForSearch(name, nameSearchType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameSearchType() {
        return nameSearchType;
    }

    public void setNameSearchType(int nameSearchType) {
        this.nameSearchType = nameSearchType;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

}

	
