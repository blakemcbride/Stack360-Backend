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

import com.arahant.beans.WizardConfigurationProjectAssignment;
import com.arahant.business.BWizardConfiguration;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class LoadWizardConfigurationReturn extends TransmitReturnBase {

	private String name;
	private String description;
	private String version;
	private String type;
	private boolean fullscreen;
	private boolean showInapplicable;
	private boolean allowInapplicable;
	private boolean rememberState;
	private boolean progressPaneButtons;
	private boolean skipPresentation;
	private boolean useToolTips;
	private boolean showDemographics;
	private boolean showDependents;
	private boolean canSeeAllCompanies;
	private boolean allCompanies;
	private boolean lockOnFinalize;
	private String projectSummary;
	private String hrId;
	private String physicianSelectionMode;
	private ListAssignedPersonsItem[] assignedPersons;
	private String screenId;
	private String screenName;
	private String demographicInstructions;
	private int reviewReport;
	private int benefitReport;
	private boolean autoApproveDeclines;
	private boolean paymentInfo;
	private boolean enableAvatars;
	private String welcomeAvatar;
	private String qualifyingEventAvatar;
	private String demographicsAvatar;
	private String dependentsAvatar;
	private String reviewAvatar;
	private String finishAvatar;
	private boolean showAnnualCost;
	private boolean showMonthlyCost;
	private boolean showPPPCost;
	private boolean showQualifyingEvent;
	private boolean allowDemographicChanges;
	private short hdeType;
	private short daysBefore;
	private short daysAfter;
	private short firstDays;
	private short firstMonths;
	private short afterDays;
	private short afterMonths;
	private boolean allowReportFromReview;

	public boolean getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(boolean paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public int getBenefitReport() {
		return benefitReport;
	}

	public void setBenefitReport(int benefitReport) {
		this.benefitReport = benefitReport;
	}

	public int getReviewReport() {
		return reviewReport;
	}

	public void setReviewReport(int reviewReport) {
		this.reviewReport = reviewReport;
	}

	public String getDemographicInstructions() {
		return demographicInstructions;
	}

	public void setDemographicInstructions(String demographicInstructions) {
		this.demographicInstructions = demographicInstructions;
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

	public ListAssignedPersonsItem[] getAssignedPersons() {
		return assignedPersons;
	}

	public void setAssignedPersons(ListAssignedPersonsItem[] assignedPersons) {
		this.assignedPersons = assignedPersons;
	}

	public String getPhysicianSelectionMode() {
		return physicianSelectionMode;
	}

	public void setPhysicianSelectionMode(String physicianSelectionMode) {
		this.physicianSelectionMode = physicianSelectionMode;
	}

	public boolean getLockOnFinalize() {
		return lockOnFinalize;
	}

	public void setLockOnFinalize(boolean lockOnFinalize) {
		this.lockOnFinalize = lockOnFinalize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public boolean getShowInapplicable() {
		return showInapplicable;
	}

	public void setShowInapplicable(boolean showInapplicable) {
		this.showInapplicable = showInapplicable;
	}

	public boolean getAllowInapplicable() {
		return allowInapplicable;
	}

	public void setAllowInapplicable(boolean allowInapplicable) {
		this.allowInapplicable = allowInapplicable;
	}

	public boolean getRememberState() {
		return rememberState;
	}

	public void setRememberState(boolean rememberState) {
		this.rememberState = rememberState;
	}

	public boolean getProgressPaneButtons() {
		return progressPaneButtons;
	}

	public void setProgressPaneButtons(boolean progressPaneButtons) {
		this.progressPaneButtons = progressPaneButtons;
	}

	public boolean getSkipPresentation() {
		return skipPresentation;
	}

	public void setSkipPresentation(boolean skipPresentation) {
		this.skipPresentation = skipPresentation;
	}

	public boolean getUseToolTips() {
		return useToolTips;
	}

	public void setUseToolTips(boolean useToolTips) {
		this.useToolTips = useToolTips;
	}

	public boolean getShowDemographics() {
		return showDemographics;
	}

	public void setShowDemographics(boolean showDemographics) {
		this.showDemographics = showDemographics;
	}

	public boolean getShowDependents() {
		return showDependents;
	}

	public void setShowDependents(boolean showDependents) {
		this.showDependents = showDependents;
	}

	public boolean getCanSeeAllCompanies() {
		return canSeeAllCompanies;
	}

	public void setCanSeeAllCompanies(boolean canSeeAllCompanies) {
		this.canSeeAllCompanies = canSeeAllCompanies;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public String getProjectSummary() {
		return projectSummary;
	}

	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	public String getHrId() {
		return hrId;
	}

	public void setHrId(String hrId) {
		this.hrId = hrId;
	}

	public boolean getAutoApproveDeclines() {
		return autoApproveDeclines;
	}

	public void setAutoApproveDeclines(boolean autoApproveDeclines) {
		this.autoApproveDeclines = autoApproveDeclines;
	}

	public boolean getEnableAvatars() {
		return enableAvatars;
	}

	public void setEnableAvatars(boolean enableAvatars) {
		this.enableAvatars = enableAvatars;
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

	public boolean isShowQualifyingEvent() {
		return showQualifyingEvent;
	}

	public void setShowQualifyingEvent(boolean showQualifyingEvent) {
		this.showQualifyingEvent = showQualifyingEvent;
	}

	public boolean isAllowDemographicChanges() {
		return allowDemographicChanges;
	}

	public void setAllowDemographicChanges(boolean allowDemographicChanges) {
		this.allowDemographicChanges = allowDemographicChanges;
	}

	public short getHdeType() {
		return hdeType;
	}

	public void setHdeType(short hdeType) {
		this.hdeType = hdeType;
	}

	public short getDaysBefore() {
		return daysBefore;
	}

	public void setDaysBefore(short daysBefore) {
		this.daysBefore = daysBefore;
	}

	public short getDaysAfter() {
		return daysAfter;
	}

	public void setDaysAfter(short daysAfter) {
		this.daysAfter = daysAfter;
	}

	public short getFirstDays() {
		return firstDays;
	}

	public void setFirstDays(short firstDays) {
		this.firstDays = firstDays;
	}

	public short getFirstMonths() {
		return firstMonths;
	}

	public void setFirstMonths(short firstMonths) {
		this.firstMonths = firstMonths;
	}

	public short getAfterDays() {
		return afterDays;
	}

	public void setAfterDays(short afterDays) {
		this.afterDays = afterDays;
	}

	public short getAfterMonths() {
		return afterMonths;
	}

	public void setAfterMonths(short afterMonths) {
		this.afterMonths = afterMonths;
	}

	public boolean isAllowReportFromReview() {
		return allowReportFromReview;
	}

	public void setAllowReportFromReview(boolean allowReportFromReview) {
		this.allowReportFromReview = allowReportFromReview;
	}

	void setData(BWizardConfiguration bc) {
		name = bc.getName();
		description = bc.getDescription();
		version = bc.getWizardVersion();
		type = bc.getWizardType() + "";
		fullscreen = bc.getFullscreen() == 'Y';
		showInapplicable = bc.getShowInapplicable() == 'Y';
		allowInapplicable = bc.getAllowInapplicable() == 'Y';
		rememberState = bc.getRememberState() == 'Y';
		progressPaneButtons = bc.getProgressPaneButtons() == 'Y';
		skipPresentation = bc.getSkipPresentation() == 'Y';
		useToolTips = bc.getUseToolTips() == 'Y';
		showDemographics = bc.getShowDemographics() == 'Y';
		showDependents = bc.getShowDependents() == 'Y';
		//canSeeAllCompanies = ArahantSession.getHSU().getCurrentPerson().;
		allCompanies = bc.getAllCompanies();
		projectSummary = bc.getProjectSummary();
		lockOnFinalize = bc.getLockOnFinalizeBool();
		hrId = bc.getHrContactId();
		physicianSelectionMode = bc.getPhysicianSelectionMode() + "";
		if (bc.getDemographicScreen() != null) {
			screenName = bc.getDemographicScreen().getName();
			screenId = bc.getDemographicScreenId();
		}
		demographicInstructions = bc.getDemographicInstructions();
		benefitReport = bc.getBenefitReport();
		reviewReport = bc.getReviewReport();
		setAssignedPersons(ArahantSession.getHSU().createCriteria(WizardConfigurationProjectAssignment.class).eq(WizardConfigurationProjectAssignment.WIZARD_CONFIG, bc.getBean()).list());
		autoApproveDeclines = bc.getAutoApproveDeclines();
		paymentInfo = bc.getPaymentInfoBoolean();
		enableAvatars = bc.getEnableAvatarsBoolean();
		welcomeAvatar = bc.getWelcomeAvatar();
		qualifyingEventAvatar = bc.getQualifyingEventAvatar();
		demographicsAvatar = bc.getDemographicsAvatar();
		dependentsAvatar = bc.getDependentsAvatar();
		reviewAvatar = bc.getReviewAvatar();
		finishAvatar = bc.getFinishAvatar();
		showAnnualCost = bc.getShowAnnualCost() == 'Y';
		showMonthlyCost = bc.getShowMonthlyCost() == 'Y';
		showPPPCost = bc.getShowPPPCost() == 'Y';
		showQualifyingEvent = bc.getShowQualifyingEvent() == 'Y';
		allowDemographicChanges = bc.getAllowDemographicChanges() == 'Y';
		hdeType = bc.getHdeType();
		daysBefore = bc.getHdeDaysBefore();
		daysAfter = bc.getHdeDaysAfter();
		firstDays = firstMonths = afterDays = afterMonths = 0;
		switch (hdeType) {
			case 2:
				firstDays = bc.getHdePeriod();
				break;
			case 3:
				firstMonths = bc.getHdePeriod();
				break;
			case 4:
				afterDays = bc.getHdePeriod();
				break;
			case 5:
				afterMonths = bc.getHdePeriod();
				break;
		}
		allowReportFromReview = bc.getAllowReportFromReview() == 'Y';
	}

	public void setAssignedPersons(List<WizardConfigurationProjectAssignment> l) {
		assignedPersons = new ListAssignedPersonsItem[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			assignedPersons[loop] = new ListAssignedPersonsItem(l.get(loop));
	}
}
