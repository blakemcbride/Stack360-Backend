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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Arahant
 */

@Entity
@Table(name=OnboardingConfig.TABLE_NAME)
public class OnboardingConfig extends ArahantBean implements Serializable{


	public static final String TABLE_NAME = "onboarding_config";
	public static final String ONBOARDING_CONFIG_ID = "onboardingConfigId";
	public static final String COMPANY = "company";
	public static final String CONFIG_NAME = "configName";

	private String onboardingConfigId;
	private String configName;
	private CompanyDetail company;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name="config_name")
	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Id
	@Column(name="onboarding_config_id")
	public String getOnboardingConfigId() {
		return onboardingConfigId;
	}

	public void setOnboardingConfigId(String onboardingConfigId) {
		this.onboardingConfigId = onboardingConfigId;
	}


	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "onboarding_config_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return onboardingConfigId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OnboardingConfig other = (OnboardingConfig) obj;
		if ((this.onboardingConfigId == null) ? (other.onboardingConfigId != null) : !this.onboardingConfigId.equals(other.onboardingConfigId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.onboardingConfigId != null ? this.onboardingConfigId.hashCode() : 0);
		return hash;
	}



}
