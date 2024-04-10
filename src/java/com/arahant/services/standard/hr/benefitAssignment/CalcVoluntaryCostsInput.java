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



package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.services.TransmitInputBase;

/**
 *
 * @author Blake McBride
 */
public class CalcVoluntaryCostsInput extends TransmitInputBase {
	
	private String benefitConfigId;
	private String employeeId;
	private double benefitAmount;

	public String getBenefitConfigId() {
		return benefitConfigId;
	}

	public void setBenefitConfigId(String benefitConfigId) {
		this.benefitConfigId = benefitConfigId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public double getBenefitAmount() {
		return benefitAmount;
	}

	public void setBenefitAmount(double benefitAmount) {
		this.benefitAmount = benefitAmount;
	}
}
