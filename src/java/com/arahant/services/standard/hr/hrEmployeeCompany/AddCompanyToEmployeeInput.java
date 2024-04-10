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
package com.arahant.services.standard.hr.hrEmployeeCompany;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import java.util.Date;

/**
 * 
 *
 * Created on Oct 16, 2009
 *
 */
public class AddCompanyToEmployeeInput extends TransmitInputBase {

    @Validation(required = true)
    private String companyId;
    @Validation(required = true)
    private String agencyId;
    @Validation(required = true)
    private String employeeId;
    @Validation(required = true)
    private String companyStatus;
    @Validation(required = true)
    private int companyStatusDate;
    @Validation(required = false)
    private String companyStatusNote;


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public int getCompanyStatusDate() {
        return companyStatusDate;
    }

    public void setCompanyStatusDate(int companyStatusDate) {
        this.companyStatusDate = companyStatusDate;
    }

    public String getCompanyStatusNote() {
        return companyStatusNote;
    }

    public void setCompanyStatusNote(String companyStatusNote) {
        this.companyStatusNote = companyStatusNote;
    }

}

	
