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
import com.arahant.business.BScreen;
import com.arahant.business.BWizardConfigurationBenefit;
import com.arahant.services.TransmitInputBase;

public class SaveWizardBenefitInput extends TransmitInputBase {

	@Validation(table = "wizard_config_benefit", column = "wizard_config_ben_id", required = true)
	private String benefitWizardId;
	@Validation(table = "wizard_config_benefit", column = "avatar_path", required = false)
	private String avatarPath;
	@Validation(table = "wizard_config_benefit", column = "instructions", required = false)
	private String instructions;
	@Validation(table = "wizard_config_benefit", column = "screen_id", required = false)
	private String screenId;
	@Validation(table = "wizard_config_benefit", column = "benefit_name", required = true)
	private String name;
	@Validation(table = "wizard_config_benefit", column = "decline_message", required = false)
	private String declineMessage;

	public String getBenefitWizardId() {
		return benefitWizardId;
	}

	public void setBenefitWizardId(String benefitWizardId) {
		this.benefitWizardId = benefitWizardId;
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

	public String getDeclineMessage() {
		return declineMessage;
	}

	public void setDeclineMessage(String declineMessage) {
		this.declineMessage = declineMessage;
	}

	void setData(BWizardConfigurationBenefit bc) {
		bc.setAvatarPath(avatarPath);
		bc.setInstructions(instructions);
		if (!isEmpty(screenId))
			bc.setScreen(new BScreen(screenId).getBean());
		else
			bc.setScreen(null);
		bc.setName(name);
		bc.setDeclineMessage(declineMessage);
	}
}
