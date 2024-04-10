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
import com.arahant.business.BHRBenefit;
import com.arahant.business.BScreen;
import com.arahant.business.BWizardConfigurationBenefit;
import com.arahant.business.BWizardConfigurationCategory;
import com.arahant.services.TransmitInputBase;

public class NewWizardBenefitInput extends TransmitInputBase {

	@Validation(table = "wizard_config_benefit", column = "avatar_path", required = false)
	private String avatarPath;
	@Validation(table = "wizard_config_benefit", column = "instructions", required = false)
	private String instructions;
	@Validation(table = "wizard_config_benefit", column = "screen_id", required = false)
	private String screenId;
	@Validation(table = "wizard_config_benefit", column = "benefit_name", required = true)
	private String name;
	@Validation(table = "hr_benefit", column = "benefit_id", required = true)
	private String benefitId;
	@Validation(table = "wizard_config_category", column = "wizard_config_cat_id", required = true)
	private String wizardCategoryId;
	@Validation(required = false)
	private boolean generateConfigs;
	@Validation(table = "wizard_config_benefit", column = "decline_message", required = false)
	private String declineMessage;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getWizardCategoryId() {
		return wizardCategoryId;
	}

	public void setWizardCategoryId(String wizardCategoryId) {
		this.wizardCategoryId = wizardCategoryId;
	}

	public String getDeclineMessage() {
		return declineMessage;
	}

	public void setDeclineMessage(String declineMessage) {
		this.declineMessage = declineMessage;
	}

	void setData(BWizardConfigurationBenefit bc) {
		bc.setAvatarPath(avatarPath);
		bc.setInstructions(instructions);
		bc.setScreen(new BScreen(screenId).getBean());
		bc.setName(name);
		bc.setBenefit(new BHRBenefit(benefitId).getBean());

		BWizardConfigurationCategory wizCat = new BWizardConfigurationCategory(wizardCategoryId);
		bc.setWizardConfigurationCategory(wizCat.getBean());

		bc.setSeqNo(wizCat.getWizardBenefits().size());
		bc.setDeclineMessage(declineMessage);
	}
}
