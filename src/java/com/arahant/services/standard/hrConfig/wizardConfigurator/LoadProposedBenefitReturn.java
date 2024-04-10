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

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;

public class LoadProposedBenefitReturn extends TransmitReturnBase {

	private boolean allowsMultipleBenefits;
	private String avatarPath;
	private String instructions;
	private String screenId;
	private String screenName;
	private String description;

	public boolean isAllowsMultipleBenefits() {
		return allowsMultipleBenefits;
	}

	public void setAllowsMultipleBenefits(boolean allowsMultipleBenefits) {
		this.allowsMultipleBenefits = allowsMultipleBenefits;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	public void setData(final String benefitId) {
		BHRBenefit c = new BHRBenefit(benefitId);
		allowsMultipleBenefits = c.getBenefitCategory().getAllowsMultipleBenefitsBoolean();
		avatarPath = c.getAvatarPath();
		instructions = c.getAdditionalInstructions();
		screenId = c.getOpenEnrollmentScreen() != null ? c.getOpenEnrollmentScreen().getScreenId() : "";
		screenName = c.getOpenEnrollmentScreen() != null ? c.getOpenEnrollmentScreen().getName() : "";
		description = c.getName();
	}
}
