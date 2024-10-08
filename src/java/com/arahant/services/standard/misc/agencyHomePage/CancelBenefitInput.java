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
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefitJoin;

public class CancelBenefitInput extends TransmitInputBase {
	
	void setData(BHRBenefitJoin hrb) {
				
		hrb.setChangeReason(benefitChangeReasonId);
		hrb.setCoverageEndDate(effectiveDate);

	}

	@Validation (required=true)
	private String benefitChangeReasonId;
	@Validation (type="date", required=true)
	private int effectiveDate;
	@Validation (required=true)
	private String benefitId;

	public String getBenefitChangeReasonId()
	{
		return benefitChangeReasonId;
	}
	public void setBenefitChangeReasonId(String benefitChangeReasonId)
	{
		this.benefitChangeReasonId=benefitChangeReasonId;
	}
	public int getEffectiveDate()
	{
		return effectiveDate;
	}
	public void setEffectiveDate(int effectiveDate)
	{
		this.effectiveDate=effectiveDate;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

}

	
