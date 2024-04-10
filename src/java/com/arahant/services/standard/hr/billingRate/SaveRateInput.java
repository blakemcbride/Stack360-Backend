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

import com.arahant.business.BEmployeeRate;
import com.arahant.business.BRateType;
import com.arahant.services.TransmitInputBase;

public class SaveRateInput extends TransmitInputBase {

    private String employeeRateId;
    private String personId;
	private String rateTypeId;
	private double rate;

    public SaveRateInput() {
    }

    public String getPersonId() {
        return personId;
    }

    public String getEmployeeRateId() {
        return employeeRateId;
    }

    public void setEmployeeRateId(String employeeRateId) {
        this.employeeRateId = employeeRateId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    void setData(final BEmployeeRate n) {
        n.setRate(rate);
        n.setRateType(new BRateType(rateTypeId).getRateType());
	}
}

	
