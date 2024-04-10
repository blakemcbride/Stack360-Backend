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
package com.arahant.services.standard.misc.alertAgent;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAlert;
import com.arahant.annotation.Validation;

public class NewAlertInput extends TransmitInputBase {

    void setData(BAlert ba) {

    }

    @Validation(required = true)
    private String message;
    @Validation(min=19000101, max=30000101, required = true)
    private int startDate;
    @Validation(min=19000101, max=30000101, required = true)
    private int endDate;
    @Validation(required = true)
    private short alertType;
    @Validation(required = false)
    private String companyId;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public short getAlertType() {
        return alertType;
    }

    public void setAlertType(short alertType) {
        this.alertType = alertType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}

	
