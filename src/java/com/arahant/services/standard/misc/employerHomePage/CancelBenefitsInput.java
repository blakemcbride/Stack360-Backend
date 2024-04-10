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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BHRBenefitJoin;

public class CancelBenefitsInput extends TransmitInputBase {
	
	void setData(BHRBenefitJoin hrb, int effectiveDate) {

		if(hrb.isPolicyBenefitJoin())
		{
			for (HrBenefitJoin join : hrb.getDependentBenefitJoins())
			{
				BHRBenefitJoin depJoin = new BHRBenefitJoin(join);
				depJoin.setChangeReason(benefitChangeReasonId);
				depJoin.setCoverageEndDate(effectiveDate);
				depJoin.setPolicyEndDate(effectiveDate);
				depJoin.update();
			}
		}
		hrb.setChangeReason(benefitChangeReasonId);
		hrb.setCoverageEndDate(effectiveDate);
		hrb.setPolicyEndDate(effectiveDate);
		hrb.update();
	}

	@Validation (required=true)
	private String benefitChangeReasonId;
	@Validation (required=true)
	private String[] ids;
	@Validation (required=true)
	private int[] effectiveDates;

	public int[] getEffectiveDates() {
		return effectiveDates;
	}

	public void setEffectiveDates(int[] effectiveDates) {
		this.effectiveDates = effectiveDates;
	}

	public String getBenefitChangeReasonId()
	{
		return benefitChangeReasonId;
	}
	public void setBenefitChangeReasonId(String benefitChangeReasonId)
	{
		this.benefitChangeReasonId=benefitChangeReasonId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}


}

	
