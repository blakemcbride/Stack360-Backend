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

import com.arahant.business.BCompany;

/**
 * 
 *
 *
 */
public class SearchAssCompaniesReturnItem {

    public SearchAssCompaniesReturnItem() {
    }

    SearchAssCompaniesReturnItem(BCompany bc) {

        companyName = bc.getName();
        companyId = bc.getCompanyId();
    //    companyPhone = bc.getMainPhoneNumber();
        companyExternalId = bc.getIdentifier();

    }
    private String companyName;
    private String companyId;
    //private String companyPhone;
    private String companyExternalId;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	/*
    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }
 */
    public String getCompanyExternalId() {
        return companyExternalId;
    }

    public void setCompanyExternalId(String companyExternalId) {
        this.companyExternalId = companyExternalId;
    }
}

	
