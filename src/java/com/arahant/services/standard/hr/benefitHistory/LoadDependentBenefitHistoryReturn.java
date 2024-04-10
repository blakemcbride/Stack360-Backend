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
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadDependentBenefitHistoryReturn extends TransmitReturnBase {


	void setData(BHRBenefitJoinH bc) throws ArahantException
	{
		
		personName=bc.getPersonName();
		relationship=bc.getRelationship();
		amountCovered=bc.getAmountCovered();
		changeReason=bc.getChangeReason();
		usingCOBRA=bc.getUsingCOBRA();

	}
	
	private String personName;
	private String relationship;
	private double amountCovered;
	private String changeReason;
	private boolean usingCOBRA;

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
	 * @throws ArahantException 
	 */
	void setData(BHRBenefitJoin bc) throws ArahantException {
		personName=bc.getPersonName();
		relationship=bc.getRelationshipText();
		amountCovered=bc.getAmountCovered();
		changeReason=bc.getChangeReason();
		usingCOBRA=bc.getUsingCOBRA();
	}

}

	
