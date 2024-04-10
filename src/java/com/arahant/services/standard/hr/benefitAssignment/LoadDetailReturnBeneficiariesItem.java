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
import com.arahant.business.BHRBeneficiary;


/**
 * 
 *
 *
 */
public class LoadDetailReturnBeneficiariesItem {
	
	public LoadDetailReturnBeneficiariesItem()
	{
		;
	}

	LoadDetailReturnBeneficiariesItem (final BHRBeneficiary bc)
	{
		
		beneficiaryId=bc.getBeneficiaryId();
		beneficiary=bc.getBeneficiary();
		relationship=bc.getRelationship();
		percent=bc.getPercentage();
		dob=bc.getDob();
		ssn=bc.getSsn();
		address=bc.getAddress();

	}
	
	private String beneficiaryId;
	private String beneficiary;
	private String relationship;
	private int percent;
	private int dob;
	private String ssn;
	private String address;

	public String getBeneficiaryId()
	{
		return beneficiaryId;
	}
	public void setBeneficiaryId(final String beneficiaryId)
	{
		this.beneficiaryId=beneficiaryId;
	}
	public String getBeneficiary()
	{
		return beneficiary;
	}
	public void setBeneficiary(final String beneficiary)
	{
		this.beneficiary=beneficiary;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(final String relationship)
	{
		this.relationship=relationship;
	}
	public int getPercent()
	{
		return percent;
	}
	public void setPercent(final int percent)
	{
		this.percent=percent;
	}

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return Returns the dob.
	 */
	public int getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(int dob) {
		this.dob = dob;
	}

	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

}

	
