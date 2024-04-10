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


package com.arahant.services.standard.hr.benefitConfigAdvancedCost;

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;

public class LoadBenefitReturn extends TransmitReturnBase {

	private boolean benefitCoveredUnderCOBRA;
	private String benefitAmountModel;
	private String employeeCostModel;
	private String employerCostModel;

	void setData(BHRBenefit bc) {
		benefitCoveredUnderCOBRA = bc.getCoveredUnderCOBRA();
		benefitAmountModel = bc.getBenefitAmountModel() + "";
		employeeCostModel = bc.getEmployeeCostModel() + "";
		employerCostModel = bc.getEmployerCostModel() + "";
	}

	public boolean getBenefitCoveredUnderCOBRA() {
		return benefitCoveredUnderCOBRA;
	}

	public void setBenefitCoveredUnderCOBRA(boolean benefitCoveredUnderCOBRA) {
		this.benefitCoveredUnderCOBRA = benefitCoveredUnderCOBRA;
	}

	public String getBenefitAmountModel() {
		return benefitAmountModel;
	}

	public void setBenefitAmountModel(String benefitAmountModel) {
		this.benefitAmountModel = benefitAmountModel;
	}

	public String getEmployeeCostModel() {
		return employeeCostModel;
	}

	public void setEmployeeCostModel(String employeeCostModel) {
		this.employeeCostModel = employeeCostModel;
	}

	public String getEmployerCostModel() {
		return employerCostModel;
	}

	public void setEmployerCostModel(String employerCostModel) {
		this.employerCostModel = employerCostModel;
	}

}
