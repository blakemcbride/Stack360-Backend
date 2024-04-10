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
import com.arahant.business.*;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;

public class NewWizardConfigurationInput extends TransmitInputBase {

	@Validation(table = "wizard_configuration", column = "config_name", required = true)
	private String name;
	@Validation(table = "wizard_configuration", column = "description", required = true)
	private String description;
	@Validation(table = "wizard_configuration", column = "wizard_version", required = true)
	private String version;
	@Validation(table = "wizard_configuration", column = "wizard_type", required = true)
	private String wizardType;
	@Validation(required = false)
	private boolean fullscreen;
	@Validation(required = false)
	private boolean showInapplicable;
	@Validation(required = false)
	private boolean allowInapplicable;
	@Validation(required = false)
	private boolean rememberState;
	@Validation(required = false)
	private boolean progressPaneButtons;
	@Validation(required = false)
	private boolean skipPresentation;
	@Validation(required = false)
	private boolean useToolTips;
	@Validation(required = false)
	private boolean showDemographics;
	@Validation(required = false)
	private boolean showDependents;
	@Validation(required = false)
	private boolean allCompanies;
	@Validation(required = false)
	private boolean lockOnFinalize;
	@Validation(table = "wizard_configuration", column = "project_summary", required = true)
	private String projectSummary;
	@Validation(table = "project_status", column = "project_status_id", required = true)
	private String projectStatusId;
	@Validation(table = "project_category", column = "project_category_id", required = true)
	private String projectCategoryId;
	@Validation(table = "project_type", column = "project_type_id", required = true)
	private String projectTypeId;
	@Validation(table = "hr_benefit_class", column = "benefit_class_id", required = false)
	private String benefitClassId;
	@Validation(table = "employee", column = "person_id", required = true)
	private String hrContactId;
	@Validation(required = true)
	private String physicianSelectionMode;
	@Validation(required = false)
	private String screenId;
	@Validation(required = false)
	private String demographicInstructions;
	@Validation(required = true)
	private short reviewReport;
	@Validation(required = true)
	private short benefitReport;
	@Validation(required = false)
	private boolean autoApproveDeclines;
	@Validation(required = false)
	private boolean paymentInfo;
	@Validation(required = false)
	private boolean enableAvatars;
	@Validation(required = false)
	private String welcomeAvatar;
	@Validation(required = false)
	private String qualifyingEventAvatar;
	@Validation(required = false)
	private String demographicsAvatar;
	@Validation(required = false)
	private String dependentsAvatar;
	@Validation(required = false)
	private String reviewAvatar;
	@Validation(required = false)
	private String finishAvatar;
	@Validation(required = false)
	private boolean showAnnualCost;
	@Validation(required = false)
	private boolean showMonthlyCost;
	@Validation(required = false)
	private boolean showPPPCost;
	@Validation(required = true)
	private boolean showQualifyingEvent;
	@Validation(required = true)
	private boolean allowDemographicChanges;
	@Validation(required = true)
	private short hdeType;
	@Validation(required = false)
	private short daysBefore;
	@Validation(required = false)
	private short daysAfter;
	@Validation(required = false)
	private short firstDays;
	@Validation(required = false)
	private short firstMonths;
	@Validation(required = false)
	private short afterDays;
	@Validation(required = false)
	private short afterMonths;
	@Validation(required = false)
	private boolean allowReportFromReview;

	public boolean getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(boolean paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public short getBenefitReport() {
		return benefitReport;
	}

	public void setBenefitReport(short benefitReport) {
		this.benefitReport = benefitReport;
	}

	public short getReviewReport() {
		return reviewReport;
	}

	public void setReviewReport(short reviewReport) {
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

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
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

	public String getWizardType() {
		return wizardType;
	}

	public void setWizardType(String wizardType) {
		this.wizardType = wizardType;
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

	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}

	public String getProjectCategoryId() {
		return projectCategoryId;
	}

	public void setProjectCategoryId(String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}

	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public String getHrContactId() {
		return hrContactId;
	}

	public void setHrContactId(String hrContactId) {
		this.hrContactId = hrContactId;
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
		bc.setName(name);
		bc.setDescription(description);
		bc.setWizardVersion(version);
		bc.setWizardType(wizardType.charAt(0));
		bc.setFullscreen(fullscreen ? 'Y' : 'N');
		bc.setShowInapplicable(showInapplicable ? 'Y' : 'N');
		bc.setAllowInapplicable(allowInapplicable ? 'Y' : 'N');
		bc.setRememberState(rememberState ? 'Y' : 'N');
		bc.setProgressPaneButtons(progressPaneButtons ? 'Y' : 'N');
		bc.setSkipPresentation(skipPresentation ? 'Y' : 'N');
		bc.setUseToolTips(useToolTips ? 'Y' : 'N');
		bc.setShowDemographics(showDemographics ? 'Y' : 'N');
		bc.setShowDependents(showDependents ? 'Y' : 'N');
		bc.setLockOnFinalizeBool(lockOnFinalize);
		bc.setAllCompanies(allCompanies);
		bc.setProjectSummary(projectSummary);
		bc.setProjectStatus(new BProjectStatus(projectStatusId).getBean());
		bc.setProjectCategory(new BProjectCategory(projectCategoryId).getBean());
		bc.setProjectType(new BProjectType(projectTypeId).getBean());
		bc.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
		if(!isEmpty(benefitClassId))
			bc.setBenefitClass(new BBenefitClass(benefitClassId).getBean());
		else
			bc.setBenefitClass(null);
		if(!isEmpty(screenId))
			bc.setDemographicScreen(new BScreen(screenId).getBean());
		else
			bc.setDemographicScreen(null);
		demographicInstructions = bc.getDemographicInstructions();
		bc.setHrContactId(hrContactId);
		bc.setPhysicianSelectionMode(physicianSelectionMode);
		bc.setBenefitReport(benefitReport);
		bc.setReviewReport(reviewReport);
		bc.setAutoApproveDeclines(autoApproveDeclines);
		bc.setPaymentInfoBoolean(paymentInfo);
		bc.setEnableAvatarsBoolean(enableAvatars);
		bc.setWelcomeAvatar(welcomeAvatar);
		bc.setQualifyingEventAvatar(qualifyingEventAvatar);
		bc.setDemographicsAvatar(demographicsAvatar);
		bc.setDependentsAvatar(dependentsAvatar);
		bc.setReviewAvatar(reviewAvatar);
		bc.setFinishAvatar(finishAvatar);
		bc.setShowAnnualCost(showAnnualCost ? 'Y' : 'N');
		bc.setShowMonthlyCost(showMonthlyCost ? 'Y' : 'N');
		bc.setShowPPPCost(showPPPCost ? 'Y' : 'N');
		bc.setShowQualifyingEvent(showQualifyingEvent ? 'Y' : 'N');
		bc.setAllowDemographicChanges(allowDemographicChanges ? 'Y' : 'N');
		bc.setHdeType(hdeType);
		bc.setHdeDaysBefore(daysBefore);
		bc.setHdeDaysAfter(daysAfter);
		switch (hdeType) {
			case 2:
				bc.setHdePeriod(firstDays);
				break;
			case 3:
				bc.setHdePeriod(firstMonths);
				break;
			case 4:
				bc.setHdePeriod(afterDays);
				break;
			case 5:
				bc.setHdePeriod(afterMonths);
				break;
			default:
				bc.setHdePeriod((short) 0);
				break;
		}
		bc.setAllowReportFromReview(allowReportFromReview ? 'Y' : 'N');
	}
}

	
