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
package com.arahant.services.standard.hr.benefitCategory;
import com.arahant.annotation.Validation;

import com.arahant.beans.HrBenefitCategory;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BScreen;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewCategoryInput extends TransmitInputBase {

	void setData(final BHRBenefitCategory bc) throws ArahantWarning
	{
		bc.setDescription(description);
		bc.setTypeId(typeId);
		bc.setAllowsMultipleBenefits(allowsMultipleBenefits); 
		bc.setRequiresDecline(requiresDecline);
		bc.setAvatarPath(avatarPath);
		bc.setOpenEnrollmentWizard(includeInOpenEnrollment?"Y":"N");
		bc.setOnboarding(includeOnboarding?"Y":"N");
		bc.setInstructions(instructions);
		bc.setSequence(ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.COMPANY, ArahantSession.getHSU().getCurrentCompany()).count());
		bc.setOpenEnrollmentScreen(new BScreen(screenId).getBean());
		bc.setOnboardingScreen(new BScreen(onboardingScreenId).getBean());
		bc.setAvatarLocation(avatarLocation);
	}
	
	@Validation (table="hr_benefit_category",column="description",required=true)
	private String description;
	@Validation (min=0,max=100,required=false)
	private int typeId;
	@Validation (required=true)
	private boolean allowsMultipleBenefits;
	@Validation (required=false)
	private boolean requiresDecline;
	@Validation(required=false)
	private String avatarPath;
	@Validation(required=false)
	private boolean includeInOpenEnrollment;
	@Validation(required=false)
	private boolean includeOnboarding;
	@Validation(required=false)
	private String instructions;
	@Validation(table="hr_benefit_category", column="onboarding_screen_id", required=false)
	private String screenId;
	@Validation(required=false)
	private String onboardingScreenId;
	@Validation(required=false)
	private String avatarLocation;

	/**
	 * @return Returns the requiresDecline.
	 */
	public boolean getRequiresDecline() {
		return requiresDecline;
	}
	/**
	 * @param requiresDecline The requiresDecline to set.
	 */
	public void setRequiresDecline(final boolean requiresDecline) {
		this.requiresDecline = requiresDecline;
	}
	/**
	 * @return Returns the allowsMultipleBenefits.
	 */
	public boolean getAllowsMultipleBenefits() {
		return allowsMultipleBenefits;
	}
	/**
	 * @param allowsMultipleBenefits The allowsMultipleBenefits to set.
	 */
	public void setAllowsMultipleBenefits(final boolean allowsMultipleBenefits) {
		this.allowsMultipleBenefits = allowsMultipleBenefits;
	}
	/**
	 * @return Returns the type.
	 */
	public int getTypeId() {
		return typeId;
	}
	/**
	 * @param type The type to set.
	 */
	public void setTypeId(final int type) {
		this.typeId = type;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(final String description)
	{
		this.description=description;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public boolean getIncludeInOpenEnrollment() {
		return includeInOpenEnrollment;
	}

	public void setIncludeInOpenEnrollment(boolean includeInOpenEnrollment) {
		this.includeInOpenEnrollment = includeInOpenEnrollment;
	}

	public boolean getIncludeOnboarding() {
		return includeOnboarding;
	}

	public void setIncludeOnboarding(boolean includeOnboarding) {
		this.includeOnboarding = includeOnboarding;
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

	public String getAvatarLocation() {
		return avatarLocation;
	}

	public void setAvatarLocation(String avatarLocation) {
		this.avatarLocation = avatarLocation;
	}

	public String getOnboardingScreenId() {
		return onboardingScreenId;
	}

	public void setOnboardingScreenId(String onboardingScreenId) {
		this.onboardingScreenId = onboardingScreenId;
	}
}

	
