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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BHRPhysician;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefitJoin;


public class NewPhysicianInput extends TransmitInputBase {

	@Validation (required = true, table="physician", column="physician_name")
	private String physicianName;
	@Validation (required = true, table="physician", column="physician_code")
	private String physicianCode;
	@Validation (required = true, table="physician", column="address")
	private String address;
	@Validation (required = false, table="physician", column="change_reason")
	private String changeDescription;
	@Validation (type = "date",required = false)
	private int changeDate;
	@Validation (required = true, table="physician", column="benefit_join_id")
	private String benefitJoinId;
	@Validation (required = false, table="physician", column="annual_visit")
	private boolean annualVisit;
	

	public String getPhysicianName()
	{
		return physicianName;
	}
	public void setPhysicianName(String physicianName)
	{
		this.physicianName = physicianName;
	}
	public String getPhysicianCode()
	{
		return physicianCode;
	}
	public void setPhysicianCode(String physicianCode)
	{
		this.physicianCode = physicianCode;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getChangeDescription()
	{
		return changeDescription;
	}
	public void setChangeDescription(String changeDescription)
	{
		this.changeDescription = changeDescription;
	}
	public int getChangeDate()
	{
		return changeDate;
	}
	public void setChangeDate(int changeDate)
	{
		this.changeDate = changeDate;
	}
	public String getBenefitJoinId()
	{
		return benefitJoinId;
	}
	public void setBenefitJoinId(String benefitJoinId)
	{
		this.benefitJoinId = benefitJoinId;
	}
	public boolean getAnnualVisit()
	{
		return annualVisit;
	}
	public void setAnnualVisit(boolean annualVisit)
	{
		this.annualVisit = annualVisit;
	}


	void setData(BHRPhysician bc) {
		
		bc.setPhysicianName(physicianName);
		bc.setPhysicianCode(physicianCode);
		bc.setAddress(address);
		bc.setChangeReason(changeDescription);
		bc.setChangeDate(changeDate);
		bc.setBenefitJoin(new BHRBenefitJoin(benefitJoinId).getBean());
		bc.setAnnualVisit(annualVisit);

	}
	
}

	
