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
package com.arahant.services.standard.hr.benefitHistory;

import com.arahant.business.BHRBeneficiary;
import com.arahant.business.BHREmployeeBeneficiaryH;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadBeneficiaryHistoryReturn extends TransmitReturnBase {

	void setData(BHREmployeeBeneficiaryH bc)
	{
		
		personName=bc.getPersonName();
		relationship=bc.getRelationship();
		percent=bc.getPercent();
		dob=bc.getDob();
		ssn=bc.getSsn();
		address=bc.getAddress();
		beneficiaryType=bc.getBeneficiaryType();
		changeReason=bc.getChangeReason();

	}
	
	private String personName;
	private String relationship;
	private int percent;
	private int dob;
	private String ssn;
	private String address;
	private String beneficiaryType;
	private String changeReason;

	public String getPersonName()
	{
		return personName;
	}
	public void setPersonName(String personName)
	{
		this.personName=personName;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(String relationship)
	{
		this.relationship=relationship;
	}
	public int getPercent()
	{
		return percent;
	}
	public void setPercent(int percent)
	{
		this.percent=percent;
	}
	public int getDob()
	{
		return dob;
	}
	public void setDob(int dob)
	{
		this.dob=dob;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(String ssn)
	{
		this.ssn=ssn;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}
	public String getBeneficiaryType()
	{
		return beneficiaryType;
	}
	public void setBeneficiaryType(String beneficiaryType)
	{
		this.beneficiaryType=beneficiaryType;
	}
	public String getChangeReason()
	{
		return changeReason;
	}
	public void setChangeReason(String changeReason)
	{
		this.changeReason=changeReason;
	}
	/**
	 * @param beneficiary
	 */
	void setData(BHRBeneficiary bc) {
		personName=bc.getPersonName();
		relationship=bc.getRelationship();
		percent=bc.getPercentage();
		dob=bc.getDob();
		ssn=bc.getSsn();
		address=bc.getAddress();
		beneficiaryType=bc.getBeneficiaryType();
		changeReason=bc.getChangeReason();
	}

}

	
