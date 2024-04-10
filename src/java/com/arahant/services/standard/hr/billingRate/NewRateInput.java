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

import com.arahant.annotation.Validation;
import com.arahant.business.BEmployeeRate;
import com.arahant.business.BRateType;
import com.arahant.services.TransmitInputBase;


public class NewRateInput extends TransmitInputBase {

	private String personId;
	private String rateTypeId;
	private double rate;

    public NewRateInput() {
    }

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String employeeId) {
		this.personId = employeeId;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(final double rate) {
		this.rate = rate;
	}

	public String getRateTypeId() {
		return rateTypeId;
	}

	public void setRateTypeId(final String rateTypeId) {
		this.rateTypeId = rateTypeId;
	}

	void setData(final BEmployeeRate n) {
	    n.setPersonId(personId);
	    n.setRateType((new BRateType(rateTypeId)).getRateType());
	    n.setRate(rate);
	}
}

	
