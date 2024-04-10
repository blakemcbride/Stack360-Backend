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

/**
 * 
 *
 * Created on Oct 7, 2007
 *
 */
public class NewAssignedBenefitConfigInputItem {

	private String personId;
	private double amountCovered;
	/**
	 * Coverage start date
	 */
	private int startDate;
	/**
	 * overage end date
	 */
	private int endDate;    //  coverage end date
	private int originalCoverageDate;
	private int dob;
	private String ssn;
	private String address;
	private String beneficiaryType;
	private String comments;
	private String otherInsurance;
	private boolean otherInsurancePrimary;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
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
	 * @return Returns the amountCovered.
	 */
	public double getAmountCovered() {
		return amountCovered;
	}
	/**
	 * @param amountCovered The amountCovered to set.
	 */
	public void setAmountCovered(final double amountCovered) {
		this.amountCovered = amountCovered;
	}
	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}

	public int getOriginalCoverageDate() {
		return originalCoverageDate;
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		this.originalCoverageDate = originalCoverageDate;
	}

	/**
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}

	public String getOtherInsurance() {
		return otherInsurance;
	}

	public void setOtherInsurance(String otherInsurance) {
		this.otherInsurance = otherInsurance;
	}

	public boolean getOtherInsurancePrimary() {
		return otherInsurancePrimary;
	}

	public void setOtherInsurancePrimary(boolean otherInsurancePrimary) {
		this.otherInsurancePrimary = otherInsurancePrimary;
	}
	
	
}

	
