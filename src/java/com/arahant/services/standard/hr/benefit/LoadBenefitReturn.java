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



package com.arahant.services.standard.hr.benefit;

import com.arahant.business.BHRBenefit;
import com.arahant.business.BScreen;
import com.arahant.services.TransmitReturnBase;


public class LoadBenefitReturn extends TransmitReturnBase {

	private String serviceId;
	private String name;
	private String ruleName;
	private boolean timeRelated;
	private boolean paid;
	private String categoryId;
	private String preTax;
	private String vendorId;
	private String policyOrGroup;
	private String additionalInfo;
	private int activeDate, inactiveDate;
	private boolean contingentBeneficiaries;
	private boolean hasBeneficiaries;
	private String plan;
	private boolean requiresDecline;
	private String instructions;
	private boolean coveredUnderCOBRA;
	private String payerId;
	private String insuranceCode;
	private String planName;
	private String subGroupId;
	private String wageTypeId;
	private String description;
	private int eligibilityType;
	private int eligibilityPeriod;
	private int dependentMaxAge;
	private int dependentMaxAgeStudent;
	private int coverageEndType;
	private int coverageEndPeriod;
	private String processType;
	private String avatarPath;
	private boolean includeInOpenEnrollment;
	private boolean includeOnboarding;
	private String screenName;
	private String screenId;
	private String onboardingScreenName;
	private String onboardingScreenId;
	private String avatarLocation;
	private Boolean hasPhysicians;
	private String groupAccountId;
	private Boolean autoAssign;
	private int minAge;
	private int maxAge;
	private String internalId;
	private String costCalcType;
	private String employerCostModel;
	private String employeeCostModel;
	private String benefitAmountModel;
	private double minPay;
	private double maxPay;
	private float minHoursPerWeek;

	void setData(final BHRBenefit bc) {
		serviceId = bc.getProductServiceId();
		name = bc.getName();
		ruleName = bc.getRuleName();
		timeRelated = bc.getTimeRelated();
		paid = bc.getPaid();
		categoryId = bc.getCategoryId();
		preTax = bc.getPreTax() + "";
		vendorId = bc.getUnderwriterId();
		policyOrGroup = bc.getGroupId();
		additionalInfo = bc.getAdditionalInfo();
		activeDate = bc.getStartDate();
		inactiveDate = bc.getEndDate();
		contingentBeneficiaries = bc.getHasContingentBeneficiariesBool();
		hasBeneficiaries = bc.getHasBeneficiariesBool();
		plan = bc.getPlan();
		requiresDecline = bc.getRequiresDecline();
		instructions = bc.getAdditionalInstructions();
		coveredUnderCOBRA = bc.getCoveredUnderCOBRA();
		payerId = bc.getPayerId();
		insuranceCode = bc.getInsuranceCode();
		planName = bc.getPlanName();
		subGroupId = bc.getSubGroupId();
		wageTypeId = bc.getWageTypeId();
		description = bc.getDescription();
		eligibilityType = bc.getEligibilityType();
		eligibilityPeriod = bc.getEligibilityPeriod();
		dependentMaxAge = bc.getDependentMaxAge();
		dependentMaxAgeStudent = bc.getDependentMaxAgeStudent();
		coverageEndType = bc.getCoverageEndType();
		coverageEndPeriod = bc.getCoverageEndPeriod();
		processType = bc.getProcessType();
		avatarPath = bc.getAvatarPath();
		includeInOpenEnrollment = bc.getOpenEnrollmentWizard().equals("Y");
		includeOnboarding = bc.getOnboarding().equals("Y");
		hasPhysicians = bc.getHasPhysicians();
		groupAccountId = bc.getGroupAccountId();
		autoAssign = bc.getAutoAssignBoolean();
		minAge = bc.getMinAge();
		maxAge = bc.getMaxAge();
		internalId = bc.getInternalId();
		costCalcType = bc.getCostCalcTypeString();
		employerCostModel = bc.getEmployerCostModel() + "";
		employeeCostModel = bc.getEmployeeCostModel() + "";
		benefitAmountModel = bc.getBenefitAmountModel() + "";
		minPay = bc.getMinPay();
		maxPay = bc.getMaxPay();
		minHoursPerWeek = bc.getMinHoursPerWeek();

		if (bc.getOpenEnrollmentScreen() != null) {
			BScreen oe = new BScreen(bc.getOpenEnrollmentScreen());
			screenName = oe.getName();
			screenId = oe.getScreenId();
		}

		if (bc.getOnboardingScreen() != null) {
			BScreen ob = new BScreen(bc.getOnboardingScreen());
			onboardingScreenName = ob.getName();
			onboardingScreenId = ob.getScreenId();
		}

		avatarLocation = bc.getAvatarLocation();
	}

	public String getCostCalcType() {
		return costCalcType;
	}

	public void setCostCalcType(String costCalcType) {
		this.costCalcType = costCalcType;
	}

	public Boolean getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(Boolean autoAssign) {
		this.autoAssign = autoAssign;
	}

	public String getGroupAccountId() {
		return groupAccountId;
	}

	public void setGroupAccountId(String groupAccountId) {
		this.groupAccountId = groupAccountId;
	}

	public Boolean getHasPhysicians() {
		return hasPhysicians;
	}

	public void setHasPhysicians(Boolean hasPhysicians) {
		this.hasPhysicians = hasPhysicians;
	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}

	/**
	 * @return Returns the instructions.
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions The instructions to set.
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return Returns the requiresDecline.
	 */
	public boolean isRequiresDecline() {
		return requiresDecline;
	}

	/**
	 * @param requiresDecline The requiresDecline to set.
	 */
	public void setRequiresDecline(final boolean requiresDecline) {
		this.requiresDecline = requiresDecline;
	}

	/**
	 * @return Returns the plan.
	 */
	public String getPlan() {
		return plan;
	}

	/**
	 * @param plan The plan to set.
	 */
	public void setPlan(final String plan) {
		this.plan = plan;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}

	public boolean getTimeRelated() {
		return timeRelated;
	}

	public void setTimeRelated(final boolean timeRelated) {
		this.timeRelated = timeRelated;
	}

	public boolean getPaid() {
		return paid;
	}

	public void setPaid(final boolean paid) {
		this.paid = paid;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final String categoryId) {
		this.categoryId = categoryId;
	}

	public String getPreTax() {
		return preTax;
	}

	public void setPreTax(String preTax) {
		this.preTax = preTax;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(final String underwriter) {
		this.vendorId = underwriter;
	}

	public String getPolicyOrGroup() {
		return policyOrGroup;
	}

	public void setPolicyOrGroup(final String groupId) {
		this.policyOrGroup = groupId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(final String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public boolean getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(boolean contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	public boolean getHasBeneficiaries() {
		return hasBeneficiaries;
	}

	public void setHasBeneficiaries(final boolean hasBeneficiaries) {
		this.hasBeneficiaries = hasBeneficiaries;
	}

	/**
	 * @return Returns the activeDate.
	 */
	public int getActiveDate() {
		return activeDate;
	}

	/**
	 * @param activeDate The activeDate to set.
	 */
	public void setActiveDate(final int activeDate) {
		this.activeDate = activeDate;
	}

	/**
	 * @return Returns the inactiveDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}

	/**
	 * @param inactiveDate The inactiveDate to set.
	 */
	public void setInactiveDate(final int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	/**
	 * @return Returns the coveredUnderCOBRA.
	 */
	public boolean getCoveredUnderCOBRA() {
		return this.coveredUnderCOBRA;
	}

	/**
	 * @param coveredUnderCOBRA The coveredUnderCOBRA to set.
	 */
	public void setCoveredUnderCOBRA(boolean coveredUnderCOBRA) {
		this.coveredUnderCOBRA = coveredUnderCOBRA;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCoverageEndType() {
		return coverageEndType;
	}

	public void setCoverageEndType(int coverageEndType) {
		this.coverageEndType = coverageEndType;
	}

	public int getCoverageEndPeriod() {
		return coverageEndPeriod;
	}

	public void setCoverageEndPeriod(int coverageEndPeriod) {
		this.coverageEndPeriod = coverageEndPeriod;
	}

	public int getDependentMaxAge() {
		return dependentMaxAge;
	}

	public void setDependentMaxAge(int dependentMaxAge) {
		this.dependentMaxAge = dependentMaxAge;
	}

	public int getDependentMaxAgeStudent() {
		return dependentMaxAgeStudent;
	}

	public void setDependentMaxAgeStudent(int dependentMaxAgeStudent) {
		this.dependentMaxAgeStudent = dependentMaxAgeStudent;
	}

	public int getEligibilityPeriod() {
		return eligibilityPeriod;
	}

	public void setEligibilityPeriod(int eligibilityPeriod) {
		this.eligibilityPeriod = eligibilityPeriod;
	}

	public int getEligibilityType() {
		return eligibilityType;
	}

	public void setEligibilityType(int eligibilityType) {
		this.eligibilityType = eligibilityType;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
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

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getEmployerCostModel() {
		return employerCostModel;
	}

	public void setEmployerCostModel(String employerCostModel) {
		this.employerCostModel = employerCostModel;
	}

	public String getEmployeeCostModel() {
		return employeeCostModel;
	}

	public void setEmployeeCostModel(String employeeCostModel) {
		this.employeeCostModel = employeeCostModel;
	}

	public String getBenefitAmountModel() {
		return benefitAmountModel;
	}

	public void setBenefitAmountModel(String benefitAmountModel) {
		this.benefitAmountModel = benefitAmountModel;
	}

	public double getMinPay() {
		return minPay;
	}

	public void setMinPay(double minPay) {
		this.minPay = minPay;
	}

	public double getMaxPay() {
		return maxPay;
	}

	public void setMaxPay(double maxPay) {
		this.maxPay = maxPay;
	}

	public float getMinHoursPerWeek() {
		return minHoursPerWeek;
	}

	public void setMinHoursPerWeek(float minHoursPerWeek) {
		this.minHoursPerWeek = minHoursPerWeek;
	}

}
