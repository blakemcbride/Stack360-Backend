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

import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.calculations.FidelityLifeCostCalculator;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.TransmitReturnBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetFidelityCostRangeReturn extends TransmitReturnBase {
	
	private double minPPP = 6.0;
	private double minMonthly = 13.0;
	private double minAnnually = 156.0;

	public void setData(GetFidelityCostRangeInput in) {
		List<String> bjl = new ArrayList<String>();
		if (in.getBenefitJoinIds() != null)
			bjl.addAll(Arrays.asList(in.getBenefitJoinIds()));
		BHRBenefitJoin bbj = new BHRBenefitJoin(in.getBenefitJoinId());
		double ppy = BEmployee.getPPY(bbj.getPayingPersonId());
		boolean bubbleError = false;
		try {
			double LTRCost = 0;
			List<String> ltrIds = new ArrayList<String>();
			for (String s : bjl) {
				BHRBenefitJoin ltrBJ = new BHRBenefitJoin(s);
				if (new BHRBenefit(ltrBJ.getBenefitId()).getInternalId().contains("LTR")) {
					ltrIds.add(ltrBJ.getBenefitJoinId());
					for (HrBenefitJoin ltrbj : ltrBJ.getDependentBenefitJoins())
						ltrIds.add(ltrbj.getBenefitJoinId());
				}
			}
			if (ltrIds.size() > 0)
				LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.ANNUAL, true);
			minAnnually += FidelityLifeCostCalculator.roundDecimal(LTRCost, 2);
			if (ltrIds.size() > 0)
				LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.MONTHLY, true) / 12.0;
			minMonthly += FidelityLifeCostCalculator.roundDecimal(LTRCost, 2);
			if (ltrIds.size() > 0)
				LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.WEEKLY, true) / ppy;
			minPPP += FidelityLifeCostCalculator.roundDecimal(LTRCost, 2);


		} catch (Exception ex) {
			if (bubbleError)
				throw new ArahantWarning(ex.getMessage());
			else
				ex.printStackTrace();
		}
	}

	public double getMinAnnually() {
		return minAnnually;
	}

	public void setMinAnnually(double minAnnually) {
		this.minAnnually = minAnnually;
	}

	public double getMinMonthly() {
		return minMonthly;
	}

	public void setMinMonthly(double minMonthly) {
		this.minMonthly = minMonthly;
	}

	public double getMinPPP() {
		return minPPP;
	}

	public void setMinPPP(double minPPP) {
		this.minPPP = minPPP;
	}
}
