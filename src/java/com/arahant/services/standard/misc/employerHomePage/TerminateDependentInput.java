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
package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.business.BHREmplDependent;
import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefitJoin;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class TerminateDependentInput extends TransmitInputBase {

	void setData(BHREmplDependent bhr) {

		bhr.setDateInactive(termDate);
		for (BHRBenefitJoin bj : bhr.getBenefitJoins())
		{
			bj.setCoverageEndDate(termDate);
			bj.setChangeReason(benefitChangeReasonId);
			bj.update();
		}
	}

	@Validation (required=true)
	private String dependentId;
	@Validation (type="date",required=true)
	private int termDate;
	@Validation (required=true)
	private String benefitChangeReasonId;
	

	public String getDependentId()
	{
		return dependentId;
	}
	public void setDependentId(String dependentId)
	{
		this.dependentId=dependentId;
	}
	public int getTermDate()
	{
		return termDate;
	}
	public void setTermDate(int termDate)
	{
		this.termDate=termDate;
	}
	public String getBenefitChangeReasonId()
	{
		return benefitChangeReasonId;
	}
	public void setBenefitChangeReasonId(String benefitChangeReasonId)
	{
		this.benefitChangeReasonId=benefitChangeReasonId;
	}




}

	
