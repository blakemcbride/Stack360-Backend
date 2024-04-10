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
package com.arahant.services.standard.hrConfig.benefitSetup;

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadBenefitForConfigReturn extends TransmitReturnBase {
	private boolean benefitEmployeeSpecificCost;
	private boolean benefitEmployeeChoosesAmount;
	private boolean benefitCoveredUnderCOBRA;
	
	void setData(BHRBenefit bc)
	{	
		benefitEmployeeSpecificCost=bc.getEmployeeIsProvider().equals("Y");
		benefitCoveredUnderCOBRA=bc.getCoveredUnderCOBRA();
		benefitEmployeeChoosesAmount=bc.getEmployeeChoosesAmount();		

	}
	public boolean isBenefitEmployeeChoosesAmount() {
		return benefitEmployeeChoosesAmount;
	}
	public void setBenefitEmployeeChoosesAmount(boolean benefitEmployeeChoosesAmount) {
		this.benefitEmployeeChoosesAmount = benefitEmployeeChoosesAmount;
	}
	public boolean getBenefitEmployeeSpecificCost() {
		return benefitEmployeeSpecificCost;
	}
	public void setBenefitEmployeeSpecificCost(boolean benefitEmployeeSpecificCost) {
		this.benefitEmployeeSpecificCost = benefitEmployeeSpecificCost;
	}
	public boolean getBenefitCoveredUnderCOBRA() {
		return benefitCoveredUnderCOBRA;
	}
	public void setBenefitCoveredUnderCOBRA(boolean benefitCoveredUnderCOBRA) {
		this.benefitCoveredUnderCOBRA = benefitCoveredUnderCOBRA;
	}
}

	
