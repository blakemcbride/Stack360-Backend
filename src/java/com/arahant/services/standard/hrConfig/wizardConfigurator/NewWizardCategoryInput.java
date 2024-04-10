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


package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BScreen;
import com.arahant.business.BWizardConfiguration;
import com.arahant.business.BWizardConfigurationCategory;
import com.arahant.services.TransmitInputBase;

public class NewWizardCategoryInput extends TransmitInputBase {

	@Validation(table = "wizard_config_category", column = "avatar_path", required = false)
	private String avatarPath;
	@Validation(table = "wizard_config_category", column = "instructions", required = false)
	private String instructions;
	@Validation(table = "wizard_config_category", column = "screen_id", required = false)
	private String screenId;
	@Validation(table = "wizard_config_category", column = "description", required = true)
	private String description;
	@Validation(table = "wizard_config_category", column = "wizard_config_cat_id", required = true)
	private String categoryId;
	@Validation(table = "wizard_configuration", column = "wizard_configuration_id", required = true)
	private String wizardConfigurationId;
	@Validation(required = false)
	private boolean generateBenefits;
	@Validation(required = false)
	private boolean generateConfigs;

	public boolean getGenerateBenefits() {
		return generateBenefits;
	}

	public void setGenerateBenefits(boolean generateBenefits) {
		this.generateBenefits = generateBenefits;
	}

	public boolean getGenerateConfigs() {
		return generateConfigs;
	}

	public void setGenerateConfigs(boolean generateConfigs) {
		this.generateConfigs = generateConfigs;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}

	void setData(BWizardConfigurationCategory bc) {
		bc.setAvatarPath(avatarPath);
		bc.setInstructions(instructions);
		bc.setScreen(new BScreen(screenId).getBean());
		bc.setDescription(description);
		bc.setBenefitCategory(new BHRBenefitCategory(categoryId).getBean());

		BWizardConfiguration wizConf = new BWizardConfiguration(wizardConfigurationId);
		bc.setWizardConfiguration(wizConf.getBean());

		bc.setSeqNo(BWizardConfigurationCategory.nextSeq(wizardConfigurationId));
	}
}

	
