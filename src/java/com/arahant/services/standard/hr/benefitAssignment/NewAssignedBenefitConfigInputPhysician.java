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
package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.annotation.Validation;



/**
 * 
 *
 *
 */
public class NewAssignedBenefitConfigInputPhysician {


	@Validation (table = "hr_physician",column = "physician_name",required = true)
	private String name;
	@Validation (table = "hr_physician",column = "physician_code",required = true)
	private String code;
	@Validation (table = "hr_physician",column = "address",required = false)
	private String address;
	@Validation (table = "hr_physician",column = "change_reason",required = false)
	private String changeReason;
	@Validation (table = "hr_physician",column = "change_date",required = false)
	private int changeDate;
	@Validation (table = "hr_physician",column = "annual_visit",required = false)
	private boolean annualVisit;
	@Validation (table = "person",column = "person_id",required = true)
	private String personId;

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean getAnnualVisit() {
		return annualVisit;
	}

	public void setAnnualVisit(boolean annualVisit) {
		this.annualVisit = annualVisit;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}
}

	
