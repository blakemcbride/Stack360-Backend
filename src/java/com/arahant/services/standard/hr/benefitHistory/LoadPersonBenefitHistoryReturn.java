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

import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRBenefitJoinH;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadPersonBenefitHistoryReturn extends TransmitReturnBase {

	void setData(BHRBenefitJoinH bc)
	{
		
		personName=bc.getHistoryChangePerson().getNameLFM();
		insuranceId=bc.getInsuranceId();
		amountPaid=bc.getAmountPaid();
		amountCovered=bc.getAmountCovered();
		changeReason=bc.getChangeReason();
		benefitApproved=bc.getBenefitApproved();
		usingCOBRA=bc.getUsingCOBRA();

	}
	
	private String personName;
	private String insuranceId;
	private double amountPaid;
	private double amountCovered;
	private String changeReason;
	private boolean benefitApproved;
	private boolean usingCOBRA;

	public String getPersonName()
	{
		return personName;
	}
	public void setPersonName(String personName)
	{
		this.personName=personName;
	}
	public String getInsuranceId()
	{
		return insuranceId;
	}
	public void setInsuranceId(String insuranceId)
	{
		this.insuranceId=insuranceId;
	}
	public double getAmountPaid()
	{
		return amountPaid;
	}
	public void setAmountPaid(double amountPaid)
	{
		this.amountPaid=amountPaid;
	}
	public double getAmountCovered()
	{
		return amountCovered;
	}
	public void setAmountCovered(double amountCovered)
	{
		this.amountCovered=amountCovered;
	}
	public String getChangeReason()
	{
		return changeReason;
	}
	public void setChangeReason(String changeReason)
	{
		this.changeReason=changeReason;
	}
	public boolean getBenefitApproved()
	{
		return benefitApproved;
	}
	public void setBenefitApproved(boolean benefitApproved)
	{
		this.benefitApproved=benefitApproved;
	}
	public boolean getUsingCOBRA()
	{
		return usingCOBRA;
	}
	public void setUsingCOBRA(boolean usingCOBRA)
	{
		this.usingCOBRA=usingCOBRA;
	}
	/**
	 * @param join
	 */
	void setData(BHRBenefitJoin bc) {
		personName=bc.getHistoryChangePerson().getNameLFM();
		insuranceId=bc.getInsuranceId();
		amountPaid=bc.getAmountPaidAnnual();
		amountCovered=bc.getAmountCovered();
		changeReason=bc.getChangeReason();
		benefitApproved=bc.getBenefitApproved();
		usingCOBRA=bc.getUsingCOBRA();
	}

}

	
