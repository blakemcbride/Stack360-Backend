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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BWizardConfigurationConfig;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BWizardConfigurationBenefit;


public class NewWizardConfigInput extends TransmitInputBase {

	@Validation (required = true)
	private String configId;
	@Validation (table = "wizard_config_benefit",column = "wizard_config_ben_id",required = true)
	private String wizardBenefitId;
	@Validation (table = "wizard_config_config",column = "config_name",required = true)
	private String name;
	

	public String getConfigId()
	{
		return configId;
	}
	public void setConfigId(String configId)
	{
		this.configId = configId;
	}
	public String getWizardBenefitId()
	{
		return wizardBenefitId;
	}
	public void setWizardBenefitId(String wizardBenefitId)
	{
		this.wizardBenefitId = wizardBenefitId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}


	void setData(BWizardConfigurationConfig bc) {
		
		bc.setBenefitConfig(new BHRBenefitConfig(configId).getBean());
		BWizardConfigurationBenefit wizBen = new BWizardConfigurationBenefit(wizardBenefitId);
		bc.setWizardConfigurationBenefit(wizBen.getBean());
		bc.setName(name);
		bc.setSeqNo(wizBen.getWizardConfigs().size());

	}
	
}

	
