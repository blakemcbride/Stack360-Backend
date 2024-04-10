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
package com.arahant.business;

import com.arahant.beans.BenefitRider;
import com.arahant.beans.WizardConfigurationBenefit;
import java.util.ArrayList;
import java.util.List;


public final class BWizardScreen {

	private BScreenOrGroup screen;
	private String name;
	private String contextId;
	private boolean isDefault = false;
	private String[] benefitIds;

	BWizardScreen(BScreenOrGroup screen, String contextId, String name, BWizardConfigurationBenefit bwizBen) {
		setScreen(screen);
		setContextId(contextId);
		setName(name);

		List<String> benIds = new ArrayList<String>();

		benIds.add(bwizBen.getBenefit().getBenefitId());
		BHRBenefit bb = new BHRBenefit(bwizBen.getBenefit());
		for (BenefitRider br : bb.getBenefitRiders())
			benIds.add(br.getRiderBenefitId());

		benefitIds = new String[benIds.size()];
		int count = 0;
		for (String s : benIds)
			benefitIds[count++] = s;
	}

	BWizardScreen(BScreenOrGroup screen, String contextId, String name, BHRBenefit bBen) {
		setScreen(screen);
		setContextId(contextId);
		setName(name);

		List<String> benIds = new ArrayList<String>();

		benIds.add(bBen.getBenefitId());
		for (BenefitRider br : bBen.getBenefitRiders())
			benIds.add(br.getRiderBenefitId());

		benefitIds = new String[benIds.size()];
		int count = 0;
		for (String s : benIds)
			benefitIds[count++] = s;
	}

	BWizardScreen(BScreenOrGroup screen) {
		setScreen(screen);
		setContextId("");
		setName(screen.getName());
	}
	
	BWizardScreen(BScreenOrGroup screen, String name) {
		setScreen(screen);
		setContextId("");
		setName(name);
	}

	BWizardScreen(BScreenOrGroup wizardScreen, String benefitCatId, String name, BWizardConfigurationCategory bwizCat) {
		setScreen(wizardScreen);
		setContextId(benefitCatId);
		setName(name);

		List<String> benIds = new ArrayList<String>();

		for (WizardConfigurationBenefit b : bwizCat.getWizardBenefits()) {
			benIds.add(b.getBenefit().getBenefitId());
			BHRBenefit bb = new BHRBenefit(b.getBenefit());
			for (BenefitRider br : bb.getBenefitRiders())
				benIds.add(br.getRiderBenefitId());
		}

		benefitIds = new String[benIds.size()];
		int count = 0;
		for (String s : benIds)
			benefitIds[count++] = s;
	}

	public String[] getBenefitIds() {
		return benefitIds;
	}

	public void setBenefitIds(String[] benefitIds) {
		this.benefitIds = benefitIds;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BScreenOrGroup getScreen() {
		return screen;
	}

	public void setScreen(BScreenOrGroup screen) {
		this.screen = screen;
	}
}
