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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = WizardConfiguration.TABLE_NAME)
public class WizardConfiguration extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "wizard_configuration";
	public static final String COMPANY_ID = "companyId";
	public static final String WIZARD_TYPE = "wizardType";
	public static final String BENEFIT_CLASS = "benefitClass";
	public static final String HR_CONTACT = "hrContact";
	public static final String HR_CONTACT_ID = "hrContactId";
	public static final String AUTO_APPROVE_DECLINES = "autoApproveDeclines";
	private static final long serialVersionUID = 1L;
	private String wizardConfigurationId;
	private String name;
	private String description;
	private char wizardType = ' ';
	private char fullscreen = 'N';
	private char showInapplicable = 'Y';
	private char allowInapplicable = 'N';
	private char rememberState = 'Y';
	private char progressPaneButtons = 'Y';
	private char skipPresentation = 'Y';
	private char useToolTips = 'Y';
	private char showDemographics = 'Y';
	private char showDependents = 'Y';
	private char lockOnFinalize = 'Y';
	private char physicianSelectionMode = 'I';
	private char paymentInfo = 'N';
	private String companyId;
	private BenefitClass benefitClass;
	private String projectSummary;
	private ProjectStatus projectStatus;
	private ProjectCategory projectCategory;
	private ProjectType projectType;
	private String wizardVersion;
	private Employee hrContact;
	private String hrContactId;
	private Screen demographicScreen;
	private String demographicScreenId;
	private String demographicInstructions;
	private short reviewReport = 0;
	private short benefitReport = 0;
	private char autoApproveDeclines = 'Y';
	private String welcomeAvatar;
	private String qualifyingEventAvatar;
	private String demographicsAvatar;
	private String dependentsAvatar;
	private String reviewAvatar;
	private String finishAvatar;
	private char enableAvatars = 'Y';
	private char showAnnualCost = 'N';
	private char showMonthlyCost = 'N';
	private char showPPPCost = 'Y';
	private char showQualifyingEvent = 'Y';
	private char allowDemographicChanges = 'Y';
	private short hdeType = 1;
	private short hdePeriod = 0;
	private short hdeDaysBefore = 0;
	private short hdeDaysAfter = 30;
	private char allowReportFromReview = 'Y';

	public WizardConfiguration() {
	}

	@Column(name = "payment_info")
	public char getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(char paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	@Column(name = "benefit_report")
	public short getBenefitReport() {
		return benefitReport;
	}

	public void setBenefitReport(short benefitReport) {
		this.benefitReport = benefitReport;
	}

	@Column(name = "demographic_instructions")
	public String getDemographicInstructions() {
		return demographicInstructions;
	}

	public void setDemographicInstructions(String demographicInstructions) {
		this.demographicInstructions = demographicInstructions;
	}

	@ManyToOne
	@JoinColumn(name = "demographic_screen_id")
	public Screen getDemographicScreen() {
		return demographicScreen;
	}

	public void setDemographicScreen(Screen demographicScreen) {
		this.demographicScreen = demographicScreen;
	}

	@Column(name = "demographic_screen_id", insertable = false, updatable = false)
	public String getDemographicScreenId() {
		return demographicScreenId;
	}

	public void setDemographicScreenId(String demographicScreenId) {
		this.demographicScreenId = demographicScreenId;
	}

	@Column(name = "review_report")
	public short getReviewReport() {
		return reviewReport;
	}

	public void setReviewReport(short reviewReport) {
		this.reviewReport = reviewReport;
	}

	@Column(name = "physician_selection_mode")
	public char getPhysicianSelectionMode() {
		return physicianSelectionMode;
	}

	public void setPhysicianSelectionMode(char physicianSelectionMode) {
		this.physicianSelectionMode = physicianSelectionMode;
	}

	@Column(name = "lock_on_finalize")
	public char getLockOnFinalize() {
		return lockOnFinalize;
	}

	public void setLockOnFinalize(char lockOnFinalize) {
		this.lockOnFinalize = lockOnFinalize;
	}

	@Column(name = "allow_inapplicable")
	public char getAllowInapplicable() {
		return allowInapplicable;
	}

	public void setAllowInapplicable(char allowInapplicable) {
		this.allowInapplicable = allowInapplicable;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_class_id")
	public BenefitClass getBenefitClass() {
		return benefitClass;
	}

	public void setBenefitClass(BenefitClass benefitClass) {
		this.benefitClass = benefitClass;
	}

	@Column(name = "company_id")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "fullscreen")
	public char getFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(char fullscreen) {
		this.fullscreen = fullscreen;
	}

	@Column(name = "config_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "progress_pane_buttons")
	public char getProgressPaneButtons() {
		return progressPaneButtons;
	}

	public void setProgressPaneButtons(char progressPaneButtons) {
		this.progressPaneButtons = progressPaneButtons;
	}

	@ManyToOne
	@JoinColumn(name = "project_category_id")
	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	@ManyToOne
	@JoinColumn(name = "project_status_id")
	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	@Column(name = "project_summary")
	public String getProjectSummary() {
		return projectSummary;
	}

	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	@ManyToOne
	@JoinColumn(name = "project_type_id")
	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	@Column(name = "remember_state")
	public char getRememberState() {
		return rememberState;
	}

	public void setRememberState(char rememberState) {
		this.rememberState = rememberState;
	}

	@Column(name = "show_demographics")
	public char getShowDemographics() {
		return showDemographics;
	}

	public void setShowDemographics(char showDemographics) {
		this.showDemographics = showDemographics;
	}

	@Column(name = "show_dependents")
	public char getShowDependents() {
		return showDependents;
	}

	public void setShowDependents(char showDependents) {
		this.showDependents = showDependents;
	}

	@Column(name = "show_inapplicable")
	public char getShowInapplicable() {
		return showInapplicable;
	}

	public void setShowInapplicable(char showInapplicable) {
		this.showInapplicable = showInapplicable;
	}

	@Column(name = "skip_presentation")
	public char getSkipPresentation() {
		return skipPresentation;
	}

	public void setSkipPresentation(char skipPresentation) {
		this.skipPresentation = skipPresentation;
	}

	@Column(name = "use_tool_tips")
	public char getUseToolTips() {
		return useToolTips;
	}

	public void setUseToolTips(char useToolTips) {
		this.useToolTips = useToolTips;
	}

	@Id
	@Column(name = "wizard_configuration_id")
	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}

	@Column(name = "wizard_type")
	public char getWizardType() {
		return wizardType;
	}

	public void setWizardType(char wizardType) {
		this.wizardType = wizardType;
	}

	@Column(name = "wizard_version")
	public String getWizardVersion() {
		return wizardVersion;
	}

	public void setWizardVersion(String wizardVersion) {
		this.wizardVersion = wizardVersion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hr_contact_id")
	public Employee getHrContact() {
		return hrContact;
	}

	public void setHrContact(Employee hrContact) {
		this.hrContact = hrContact;
	}

	@Column(name = "hr_contact_id", updatable = false, insertable = false)
	public String getHrContactId() {
		return hrContactId;
	}

	public void setHrContactId(String hrContactId) {
		this.hrContactId = hrContactId;
	}

	@Column(name = "auto_approve_declines")
	public char getAutoApproveDeclines() {
		return autoApproveDeclines;
	}

	public void setAutoApproveDeclines(char autoApproveDeclines) {
		this.autoApproveDeclines = autoApproveDeclines;
	}

	@Column(name = "demographics_avatar")
	public String getDemographicsAvatar() {
		return demographicsAvatar;
	}

	public void setDemographicsAvatar(String demographicsAvatar) {
		this.demographicsAvatar = demographicsAvatar;
	}

	@Column(name = "dependents_avatar")
	public String getDependentsAvatar() {
		return dependentsAvatar;
	}

	public void setDependentsAvatar(String dependentsAvatar) {
		this.dependentsAvatar = dependentsAvatar;
	}

	@Column(name = "enable_avatars")
	public char getEnableAvatars() {
		return enableAvatars;
	}

	public void setEnableAvatars(char enableAvatars) {
		this.enableAvatars = enableAvatars;
	}

	@Column(name = "finish_avatar")
	public String getFinishAvatar() {
		return finishAvatar;
	}

	public void setFinishAvatar(String finishAvatar) {
		this.finishAvatar = finishAvatar;
	}

	@Column(name = "qualifying_event_avatar")
	public String getQualifyingEventAvatar() {
		return qualifyingEventAvatar;
	}

	public void setQualifyingEventAvatar(String qualifyingEventAvatar) {
		this.qualifyingEventAvatar = qualifyingEventAvatar;
	}

	@Column(name = "review_avatar")
	public String getReviewAvatar() {
		return reviewAvatar;
	}

	public void setReviewAvatar(String reviewAvatar) {
		this.reviewAvatar = reviewAvatar;
	}

	@Column(name = "welcome_avatar")
	public String getWelcomeAvatar() {
		return welcomeAvatar;
	}

	public void setWelcomeAvatar(String welcomeAvatar) {
		this.welcomeAvatar = welcomeAvatar;
	}

	@Column(name = "show_annual_cost")
	public char getShowAnnualCost() {
		return showAnnualCost;
	}

	public void setShowAnnualCost(char show_annual_cost) {
		this.showAnnualCost = show_annual_cost;
	}

	@Column(name = "show_monthly_cost")
	public char getShowMonthlyCost() {
		return showMonthlyCost;
	}

	public void setShowMonthlyCost(char show_monthly_cost) {
		this.showMonthlyCost = show_monthly_cost;
	}

	@Column(name = "show_ppp_cost")
	public char getShowPPPCost() {
		return showPPPCost;
	}

	public void setShowPPPCost(char show_ppp_cost) {
		this.showPPPCost = show_ppp_cost;
	}

	@Column(name = "show_qualifying_event")
	public char getShowQualifyingEvent() {
		return showQualifyingEvent;
	}

	public void setShowQualifyingEvent(char showQualifyingEvent) {
		this.showQualifyingEvent = showQualifyingEvent;
	}

	@Column(name = "allow_demographic_changes")
	public char getAllowDemographicChanges() {
		return allowDemographicChanges;
	}

	public void setAllowDemographicChanges(char allowDemographicChanges) {
		this.allowDemographicChanges = allowDemographicChanges;
	}

	@Column(name = "hde_type")
	public short getHdeType() {
		return hdeType;
	}

	public void setHdeType(short hdeType) {
		this.hdeType = hdeType;
	}

	@Column(name = "hde_period")
	public short getHdePeriod() {
		return hdePeriod;
	}

	public void setHdePeriod(short hdePeriod) {
		this.hdePeriod = hdePeriod;
	}

	@Column(name = "hde_days_before")
	public short getHdeDaysBefore() {
		return hdeDaysBefore;
	}

	public void setHdeDaysBefore(short hdeDaysBefore) {
		this.hdeDaysBefore = hdeDaysBefore;
	}

	@Column(name = "hde_days_after")
	public short getHdeDaysAfter() {
		return hdeDaysAfter;
	}

	public void setHdeDaysAfter(short hdeDaysAfter) {
		this.hdeDaysAfter = hdeDaysAfter;
	}

	@Column(name = "allow_report_from_review")
	public char getAllowReportFromReview() {
		return allowReportFromReview;
	}

	public void setAllowReportFromReview(char allowReportFromReview) {
		this.allowReportFromReview = allowReportFromReview;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "wizard_configuration_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setWizardConfigurationId(IDGenerator.generate(this));
		return getWizardConfigurationId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WizardConfiguration other = (WizardConfiguration) obj;
		if ((this.wizardConfigurationId == null) ? (other.wizardConfigurationId != null) : !this.wizardConfigurationId.equals(other.wizardConfigurationId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 41 * hash + (this.wizardConfigurationId != null ? this.wizardConfigurationId.hashCode() : 0);
		return hash;
	}
}
