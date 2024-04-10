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
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BScreen;


/**
 * 
 *
 *
 */
public class ListCategoriesReturnItem {
	
	public ListCategoriesReturnItem()
	{
		
	}

	ListCategoriesReturnItem (final BHRBenefitCategory bc)
	{
		categoryId=bc.getCategoryId();
		description=bc.getDescription();
		typeId=bc.getTypeId();
		typeDescription=bc.getTypeName();
		allowsMultipleBenefits=bc.getAllowsMultipleBenefits();
		allowsMultipleBenefitsFormatted=bc.getAllowsMultipleBenefitsFormatted();
		requiresDecline=bc.getRequiresDecline();
		avatarPath=bc.getAvatarPath();
		hasAvatar = (avatarPath != null && !avatarPath.equals(""))?"Yes":"No";
		includeInOpenEnrollment = bc.getOpenEnrollmentWizard().equals("Y")?"Yes":"No";
		includeOnboarding=bc.getOnboarding().equals("Y")?"Yes":"No";
		instructions=bc.getInstructions();

		if (bc.getOpenEnrollmentScreen() != null)
		{
			BScreen oe = new BScreen(bc.getOpenEnrollmentScreen());
			screenId=oe.getScreenId();
			screenName=oe.getName();
		}
		else
		{
			screenId="";
			screenName="";
		}

		if (bc.getOnboardingScreen() != null)
		{
			BScreen ob = new BScreen(bc.getOnboardingScreen());
			onboardingScreenId=ob.getScreenId();
			onboardingScreenName=ob.getName();
		}
		else
		{
			onboardingScreenId="";
			onboardingScreenName="";
		}
		
		avatarLocation=bc.getAvatarLocation();
	}
	
	private String categoryId;
	private String description;
	private int typeId;
	private String typeDescription;
	private boolean allowsMultipleBenefits;
	private String allowsMultipleBenefitsFormatted;
	private boolean requiresDecline;
	private String hasAvatar;
	private String avatarPath;
	private String includeInOpenEnrollment;
	private String includeOnboarding;
	private String instructions;
	private String screenId;
	private String screenName;
	private String onboardingScreenId;
	private String onboardingScreenName;
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
	 * @return Returns the allowsMultipleBenefitsFormatted.
	 */
	public String getAllowsMultipleBenefitsFormatted() {
		return allowsMultipleBenefitsFormatted;
	}

	/**
	 * @param allowsMultipleBenefitsFormatted The allowsMultipleBenefitsFormatted to set.
	 */
	public void setAllowsMultipleBenefitsFormatted(
			final String allowsMultipleBenefitsFormatted) {
		this.allowsMultipleBenefitsFormatted = allowsMultipleBenefitsFormatted;
	}

	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(final String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(final String description)
	{
		this.description=description;
	}

	/**
	 * @return Returns the typeDescription.
	 */
	public String getTypeDescription() {
		return typeDescription;
	}

	/**
	 * @param typeDescription The typeDescription to set.
	 */
	public void setTypeDescription(final String typeDescription) {
		this.typeDescription = typeDescription;
	}

	/**
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId The typeId to set.
	 */
	public void setTypeId(final int typeId) {
		this.typeId = typeId;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getHasAvatar() {
		return hasAvatar;
	}

	public void setHasAvatar(String hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	public String getIncludeInOpenEnrollment() {
		return includeInOpenEnrollment;
	}

	public void setIncludeInOpenEnrollment(String includeInOpenEnrollment) {
		this.includeInOpenEnrollment = includeInOpenEnrollment;
	}

	public String getIncludeOnboarding() {
		return includeOnboarding;
	}

	public void setIncludeOnboarding(String includeOnboarding) {
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

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
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

	public String getOnboardingScreenName() {
		return onboardingScreenName;
	}

	public void setOnboardingScreenName(String onboardingScreenName) {
		this.onboardingScreenName = onboardingScreenName;
	}
}

	
