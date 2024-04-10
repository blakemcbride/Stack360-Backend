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
package com.arahant.services.standard.misc.companySearch;

import com.arahant.business.BCompanyBase;

/**
 * 
 *
 *
 */
public class SearchCompanyReturnItem {

    public SearchCompanyReturnItem() {
        ;
    }

    SearchCompanyReturnItem(BCompanyBase bc) {

        address = bc.getStreet();
        companyIdentifier = bc.getIdentifier();
        companyId = bc.getCompanyId();
        companyName = bc.getName();
        fax = bc.getMainContactWorkFax();
        companyPhone = bc.getMainPhoneNumber();
        companyContactLastName = bc.getMainContactLname();
        companyContactFirstName = bc.getMainContactFname();
        companyContactPhone = bc.getMainContactWorkPhone();
        
    }

    private String address;
    private String companyIdentifier;
    private String companyId;
    private String companyName;
    private String fax;
    private String companyPhone;
    private String companyContactLastName;
    private String companyContactFirstName;
    private String companyContactPhone;

    public String getCompanyContactFirstName() {
        return companyContactFirstName;
    }

    public void setCompanyContactFirstName(String companyContactFirstName) {
        this.companyContactFirstName = companyContactFirstName;
    }

    public String getCompanyContactLastName() {
        return companyContactLastName;
    }

    public void setCompanyContactLastName(String companyContactLastName) {
        this.companyContactLastName = companyContactLastName;
    }

    public String getCompanyContactPhone() {
        return companyContactPhone;
    }

    public void setCompanyContactPhone(String companyContactPhone) {
        this.companyContactPhone = companyContactPhone;
    }

    public String getCompanyIdentifier() {
        return companyIdentifier;
    }

    public void setCompanyIdentifier(String companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return companyPhone;
    }

    public void setPhone(String phone) {
        this.companyPhone = phone;
    }
}

	
