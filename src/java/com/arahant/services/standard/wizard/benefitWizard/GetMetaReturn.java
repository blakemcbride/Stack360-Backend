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

package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.Employee;
import com.arahant.business.BCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.business.BWizardConfiguration;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;

public class GetMetaReturn extends TransmitReturnBase {

	private boolean ssnRequired = !BProperty.getBoolean("SSN Not Required");
	private String defaultBcrId = BProperty.get("DefaultWizardBcrId");
	private int defaultBcrDate = BProperty.getInt("DefaultWizardBcrDate");
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private boolean fullScreenMode;
	private boolean allowInapplicable;
	private boolean skipPresentation;
	private boolean rememberState;
	private boolean textAsButtons;
	private boolean useToolTips;
	private boolean paymentInfo;
	private String welcomeAvatar;
	private String qualifyingEventAvatar;
	private String demographicsAvatar;
	private String dependentsAvatar;
	private String reviewAvatar;
	private String finishAvatar;
	private boolean enableAvatars;
	private boolean hasSpouse;  // true if employee has a real or change record spouse
	private boolean showAnnualCost;
	private boolean showMonthlyCost;
	private boolean showPPPCost;
	private boolean allowReportFromReview;

	public void setData(BCompany bc, BWizardConfiguration bWizConf, String empId) {
		BEmployee be = new BEmployee(empId);
		hasSpouse = be.getSpouse() != null;
		if (bWizConf.getHrContact() != null) {
			BEmployee cbe = new BEmployee(bWizConf.getHrContact());
			firstName = cbe.getFirstName();
			lastName = cbe.getLastName();
			phoneNumber = cbe.getWorkPhoneNumber();
			email = cbe.getPersonalEmail();
		} else {
			firstName = "HR";
			lastName = "Representative";
			phoneNumber = "work phone";
			email = "email";
		}
		fullScreenMode = bWizConf.getFullscreen() == 'Y';//BProperty.getBoolean("wizardFullScreenMode");
		allowInapplicable = bWizConf.getAllowInapplicable() == 'Y';//BProperty.getBoolean("wizardAllowInapplicable");
		skipPresentation = bWizConf.getSkipPresentation() == 'Y';//BProperty.getBoolean("wizardSkipPresentation");
		rememberState = bWizConf.getRememberState() == 'Y';
		textAsButtons = bWizConf.getProgressPaneButtons() == 'Y';
		useToolTips = bWizConf.getUseToolTips() == 'Y';
		paymentInfo = bWizConf.getPaymentInfoBoolean();
		welcomeAvatar = bWizConf.getWelcomeAvatar();
		qualifyingEventAvatar = bWizConf.getQualifyingEventAvatar();
		demographicsAvatar = bWizConf.getDemographicsAvatar();
		dependentsAvatar = bWizConf.getDependentsAvatar();
		reviewAvatar = bWizConf.getReviewAvatar();
		finishAvatar = bWizConf.getFinishAvatar();
		enableAvatars = bWizConf.getEnableAvatarsBoolean();
		showAnnualCost = bWizConf.getShowAnnualCost() == 'Y';
		showMonthlyCost = bWizConf.getShowMonthlyCost() == 'Y';
		showPPPCost = bWizConf.getShowPPPCost() == 'Y';
		allowReportFromReview = bWizConf.getAllowReportFromReview() == 'Y';
	}

	public void setData(BCompany bc, String empId) {
		BEmployee be = new BEmployee(empId);
		hasSpouse = be.getSpouse() != null;
		try {
			Employee e = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.HR_ADMIN, 'Y').first();
			if (e != null) {
				BEmployee cbe = new BEmployee(e);
				firstName = cbe.getFirstName();
				lastName = cbe.getLastName();
				phoneNumber = cbe.getWorkPhone();
				email = cbe.getPersonalEmail();
			} else {
				firstName = "HR";
				lastName = "Admin";
			}
		} catch (Exception e) {
			//just dont want this to fail for any reason
		}
		fullScreenMode = BProperty.getBoolean("wizardFullScreenMode");
		allowInapplicable = BProperty.getBoolean("wizardAllowInapplicable");
		skipPresentation = BProperty.getBoolean("wizardSkipPresentation");
		textAsButtons = BProperty.getBoolean("wizardTextAsButtons");
		useToolTips = false;
		rememberState = false;
		showAnnualCost = true;
		showMonthlyCost = true;
		showPPPCost = true;
		allowReportFromReview = true;
	}

	public int getDefaultBcrDate() {
		return defaultBcrDate;
	}

	public void setDefaultBcrDate(int defaultBcrDate) {
		this.defaultBcrDate = defaultBcrDate;
	}

	public String getDefaultBcrId() {
		return defaultBcrId;
	}

	public void setDefaultBcrId(String defaultBcrId) {
		this.defaultBcrId = defaultBcrId;
	}

	public boolean getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(boolean paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getRememberState() {
		return rememberState;
	}

	public void setRememberState(boolean rememberState) {
		this.rememberState = rememberState;
	}

	public boolean getTextAsButtons() {
		return textAsButtons;
	}

	public void setTextAsButtons(boolean textAsButtons) {
		this.textAsButtons = textAsButtons;
	}

	public boolean getUseToolTips() {
		return useToolTips;
	}

	public void setUseToolTips(boolean useToolTips) {
		this.useToolTips = useToolTips;
	}

	public boolean getSsnRequired() {
		return ssnRequired;
	}

	public void setSsnRequired(boolean ssnRequired) {
		this.ssnRequired = ssnRequired;
	}

	public boolean getSkipPresentation() {
		return skipPresentation;
	}

	public void setSkipPresentation(boolean skipPresentation) {
		this.skipPresentation = skipPresentation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean getFullScreenMode() {
		return fullScreenMode;
	}

	public void setFullScreenMode(boolean fullScreenMode) {
		this.fullScreenMode = fullScreenMode;
	}

	public boolean getAllowInapplicable() {
		return allowInapplicable;
	}

	public void setAllowInapplicable(boolean allowInapplicable) {
		this.allowInapplicable = allowInapplicable;
	}

	public String getDemographicsAvatar() {
		return demographicsAvatar;
	}

	public void setDemographicsAvatar(String demographicsAvatar) {
		this.demographicsAvatar = demographicsAvatar;
	}

	public String getDependentsAvatar() {
		return dependentsAvatar;
	}

	public void setDependentsAvatar(String dependentsAvatar) {
		this.dependentsAvatar = dependentsAvatar;
	}

	public boolean getEnableAvatars() {
		return enableAvatars;
	}

	public void setEnableAvatars(boolean enableAvatars) {
		this.enableAvatars = enableAvatars;
	}

	public String getFinishAvatar() {
		return finishAvatar;
	}

	public void setFinishAvatar(String finishAvatar) {
		this.finishAvatar = finishAvatar;
	}

	public String getQualifyingEventAvatar() {
		return qualifyingEventAvatar;
	}

	public void setQualifyingEventAvatar(String qualifyingEventAvatar) {
		this.qualifyingEventAvatar = qualifyingEventAvatar;
	}

	public String getReviewAvatar() {
		return reviewAvatar;
	}

	public void setReviewAvatar(String reviewAvatar) {
		this.reviewAvatar = reviewAvatar;
	}

	public String getWelcomeAvatar() {
		return welcomeAvatar;
	}

	public void setWelcomeAvatar(String welcomeAvatar) {
		this.welcomeAvatar = welcomeAvatar;
	}
	
	public boolean getHasSpouse() {
		return hasSpouse;
	}

	public void setHasSpouse(boolean hasSpouse) {
		this.hasSpouse = hasSpouse;
	}

	public boolean isShowAnnualCost() {
		return showAnnualCost;
	}

	public void setShowAnnualCost(boolean showAnnualCost) {
		this.showAnnualCost = showAnnualCost;
	}

	public boolean isShowMonthlyCost() {
		return showMonthlyCost;
	}

	public void setShowMonthlyCost(boolean showMonthlyCost) {
		this.showMonthlyCost = showMonthlyCost;
	}

	public boolean isShowPPPCost() {
		return showPPPCost;
	}

	public void setShowPPPCost(boolean showPPPCost) {
		this.showPPPCost = showPPPCost;
	}

	public boolean isAllowReportFromReview() {
		return allowReportFromReview;
	}

	public void setAllowReportFromReview(boolean allowReportFromReview) {
		this.allowReportFromReview = allowReportFromReview;
	}
	
}
