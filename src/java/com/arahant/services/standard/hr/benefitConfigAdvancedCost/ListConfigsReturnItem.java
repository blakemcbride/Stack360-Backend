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

import com.arahant.business.BHRBenefitConfig;

public class ListConfigsReturnItem {

	private String name;
	private String active;
	private String coverage;
	private String configId;
	private boolean includeInBilling;

	public ListConfigsReturnItem() {
	}

	ListConfigsReturnItem(final BHRBenefitConfig bc) {
		name = bc.getName();
		active = bc.getActive();
		coverage = bc.getCoverage();
		configId = bc.getBenefitConfigId();
		includeInBilling = bc.getIncludeInBilling();
	}

	public boolean getIncludeInBilling() {
		return includeInBilling;
	}

	public void setIncludeInBilling(boolean includeInBilling) {
		this.includeInBilling = includeInBilling;
	}

	/**
	 * @return Returns the configId.
	 */
	public String getConfigId() {
		return configId;
	}

	/**
	 * @param configId The configId to set.
	 */
	public void setConfigId(final String configId) {
		this.configId = configId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getActive() {
		return active;
	}

	public void setActive(final String active) {
		this.active = active;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(final String coverage) {
		this.coverage = coverage;
	}
}
