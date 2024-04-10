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
 * Created on Oct 8, 2007
 * 
 */
package com.arahant.services.standard.hr.benefitAssignment;


/**
 * 
 *
 * Created on Oct 8, 2007
 *
 */
public class NewAssignedBenefitConfigInputBeneficiary {

	private String beneficiary, relationship;
	private int percentage;
	private int dob;
	private String ssn;
	private String address;
	private String beneficiaryType;
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBeneficiaryType() {
		return beneficiaryType;
	}
	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}
	public int getDob() {
		return dob;
	}
	public void setDob(int dob) {
		this.dob = dob;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	/** 
	 * @return Returns the beneficiary.
	 */
	public String getBeneficiary() {
		return beneficiary;
	}
	/**
	 * @param beneficiary The beneficiary to set.
	 */
	public void setBeneficiary(final String beneficiary) {
		this.beneficiary = beneficiary;
	}
	
	/**
	 * @return Returns the percentage.
	 */
	public int getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage The percentage to set.
	 */
	public void setPercentage(final int percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return Returns the relationship.
	 */
	public String getRelationship() {
		return relationship;
	}
	/**
	 * @param relationship The relationship to set.
	 */
	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

}

	
