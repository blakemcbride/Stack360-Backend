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

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class GetFidelityCostsInput extends TransmitInputBase {

	@Validation(required = true)
	private String benefitJoinId;
	private String[] benefitJoinIds;
	private double requestedCostPPP;
	private double requestedCostMonthly;
	private double requestedCostAnnually;
	private double annualCoverage;
	private boolean doReverseCosts;

	public boolean getDoReverseCosts() {
		return doReverseCosts;
	}

	public void setDoReverseCosts(boolean doReverseCosts) {
		this.doReverseCosts = doReverseCosts;
	}

	public double getAnnualCoverage() {
		return annualCoverage;
	}

	public void setAnnualCoverage(double annualCoverage) {
		this.annualCoverage = annualCoverage;
	}

	public double getRequestedCostAnnually() {
		return requestedCostAnnually;
	}

	public void setRequestedCostAnnually(double requestedCostAnnually) {
		this.requestedCostAnnually = requestedCostAnnually;
	}

	public double getRequestedCostMonthly() {
		return requestedCostMonthly;
	}

	public void setRequestedCostMonthly(double requestedCostMonthly) {
		this.requestedCostMonthly = requestedCostMonthly;
	}

	public double getRequestedCostPPP() {
		return requestedCostPPP;
	}

	public void setRequestedCostPPP(double requestedCostPPP) {
		this.requestedCostPPP = requestedCostPPP;
	}

	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	public String[] getBenefitJoinIds() {
		if (benefitJoinIds == null)
			benefitJoinIds = new String[0];
		return benefitJoinIds;
	}

	public void setBenefitJoinIds(String[] benefitJoinIds) {
		this.benefitJoinIds = benefitJoinIds;
	}
}
