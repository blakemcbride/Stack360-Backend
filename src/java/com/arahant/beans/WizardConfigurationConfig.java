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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "wizard_config_config")
public class WizardConfigurationConfig extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "wizard_config_config";
	public static final String ID = "wizardConfigurationConfigId";
	public static final String WIZARD_CONFIGURATION_BENEFIT = "wizardConfigurationBenefit";
	public static final String SEQNO = "seqNo";
	public static final String BENEFIT_CONFIG = "benefitConfig";
	public static final String NAME = "name";
	private static final long serialVersionUID = 1L;
	private String wizardConfigurationConfigId;
	private WizardConfigurationBenefit wizardConfigurationBenefit;
	private int seqNo;
	private HrBenefitConfig benefitConfig;
	private String name;

	public WizardConfigurationConfig() {
	}

	@ManyToOne
	@JoinColumn(name = "benefit_config_id")
	public HrBenefitConfig getBenefitConfig() {
		return benefitConfig;
	}

	public void setBenefitConfig(HrBenefitConfig benefitConfig) {
		this.benefitConfig = benefitConfig;
	}

	@Column(name = "config_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "seqno")
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	@Id
	@Column(name = "wizard_config_conf_id")
	public String getWizardConfigurationConfigId() {
		return wizardConfigurationConfigId;
	}

	public void setWizardConfigurationConfigId(String wizardConfigurationConfigId) {
		this.wizardConfigurationConfigId = wizardConfigurationConfigId;
	}

	@ManyToOne
	@JoinColumn(name = "wizard_config_ben_id")
	public WizardConfigurationBenefit getWizardConfigurationBenefit() {
		return wizardConfigurationBenefit;
	}

	public void setWizardConfigurationBenefit(WizardConfigurationBenefit wizardConfigurationBenefit) {
		this.wizardConfigurationBenefit = wizardConfigurationBenefit;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "wizard_config_conf_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setWizardConfigurationConfigId(IDGenerator.generate(this));
		return getWizardConfigurationConfigId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WizardConfigurationConfig other = (WizardConfigurationConfig) obj;
		if ((this.wizardConfigurationConfigId == null) ? (other.wizardConfigurationConfigId != null) : !this.wizardConfigurationConfigId.equals(other.wizardConfigurationConfigId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + (this.wizardConfigurationConfigId != null ? this.wizardConfigurationConfigId.hashCode() : 0);
		return hash;
	}
}
