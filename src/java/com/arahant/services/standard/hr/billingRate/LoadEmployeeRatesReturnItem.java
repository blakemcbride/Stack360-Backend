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

package com.arahant.services.standard.hr.billingRate;

import com.arahant.beans.EmployeeRate;

public class LoadEmployeeRatesReturnItem {

    private String employeeRateId;
	private String rateTypeId;
	private String rateTypeName;
	private String rateCode;
	private double rate;

	public LoadEmployeeRatesReturnItem() {
	}

	LoadEmployeeRatesReturnItem(EmployeeRate n) {
        employeeRateId = n.getEmployeeRateId();
        rateTypeId = n.getRateType().getRateTypeId();
        rateTypeName = n.getRateType().getDescription();
        rateCode = n.getRateType().getRateCode();
        rate = n.getRate();
    }

    public String getEmployeeRateId() {
        return employeeRateId;
    }

    public void setEmployeeRateId(String employeeRateId) {
        this.employeeRateId = employeeRateId;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String description) {
        this.rateTypeName = description;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}

	
