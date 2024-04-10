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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.business.BHRBeneficiary;
import com.arahant.services.TransmitInputBase;

/**
 *
 *
 *
 */
public class SaveBeneficiaryInput extends TransmitInputBase {

	@Validation(required = false)
	private String employeeId;
	@Validation(required = false)
	private String address;
	@Validation(required = true)
	private String beneficiary;
	@Validation(type = "date", required = true)
	private int dob;
	@Validation(required = true)
	private int percentage;
	@Validation(required = true)
	private String relationship;
	@Validation(required = false)
	private String ssn;
	@Validation(required = true)
	private String beneficiaryId;
	@Validation(required = true)
	private String type;

	void setData(BHRBeneficiary bc) {
		bc.setAddress(address);
		bc.setBeneficiary(beneficiary);
		bc.setDob(dob);
		bc.setPercentage(percentage);
		bc.setRelationship(relationship);
		bc.setSsn(ssn);
		bc.setBeneficiaryType(type);
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
