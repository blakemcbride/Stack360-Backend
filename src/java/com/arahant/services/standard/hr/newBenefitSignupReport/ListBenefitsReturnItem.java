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
 */
package com.arahant.services.standard.hr.newBenefitSignupReport;

import com.arahant.business.BHRBenefitConfig;

public class ListBenefitsReturnItem {
	private Boolean selected;
	private String configId;
	private String configName;
	private String benefitName;
	private String categoryName;
	
	public ListBenefitsReturnItem() {
	}

	ListBenefitsReturnItem (BHRBenefitConfig bc) {
		selected=true;
		configId=bc.getBenefitConfigId();
		configName=bc.getName();
		benefitName=bc.getBenefitName();
		categoryName=bc.getCategoryName();
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}
}

	
