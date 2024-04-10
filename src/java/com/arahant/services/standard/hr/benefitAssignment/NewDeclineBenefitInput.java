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

import com.arahant.business.BHRBenefitJoin;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewDeclineBenefitInput extends TransmitInputBase {

	void setData(final BHRBenefitJoin bc)
	{		
		bc.setCoveredPersonId(personId);
		bc.setPayingPersonId(personId);
		bc.setCategoryId(categoryId);
		bc.setBenefitId(benefitId);
		bc.setPolicyStartDate(startDate);
		bc.setPolicyEndDate(endDate);
		bc.setChangeReason(benefitChangeReasonId);

	}
	
	@Validation (required=false)
	private String personId;
	@Validation (required=true)
	private String categoryId;
	@Validation (required=false)
	private String benefitId;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int startDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int endDate;
	@Validation (required=true)
	private String benefitChangeReasonId;

	public String getBenefitChangeReasonId() {
		return benefitChangeReasonId;
	}

	public void setBenefitChangeReasonId(String benefitChangeReasonId) {
		this.benefitChangeReasonId = benefitChangeReasonId;
	}
	
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(final String employeeId)
	{
		this.personId=employeeId;
	}
	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(final String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
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


}

	
