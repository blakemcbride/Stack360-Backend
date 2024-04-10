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


package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.beans.BenefitCostCalculator;
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;

public class ListBenefitConfigsReturnItem {

	private String configId;
	private String configName;
	private String coverage;
	
	//  from benefit_config_cost
	private double minValue;
	private double maxValue;
	private double stepValue;

	public ListBenefitConfigsReturnItem() {
	}

	ListBenefitConfigsReturnItem(BEmployee be, final BHRBenefitConfig bc) {
		configId = bc.getConfigId();
		configName = bc.getConfigName();
		coverage = bc.getCoverage();
		BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(new BHRBenefitConfig(bc.getBean()), be.getStatusId(), be.getOrgGroups(), 0);
		if (bcc != null) {
			minValue = bcc.getMinValue();
			maxValue = bcc.getMaxValue();
			stepValue = bcc.getStepValue();
		}
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(final String configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(final String configName) {
		this.configName = configName;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(final String coverage) {
		this.coverage = coverage;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getStepValue() {
		return stepValue;
	}

	public void setStepValue(double stepValue) {
		this.stepValue = stepValue;
	}
	
}
