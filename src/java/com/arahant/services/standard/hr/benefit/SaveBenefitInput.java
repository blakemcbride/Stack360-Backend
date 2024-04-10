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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BHRBenefit;
import com.arahant.annotation.Validation;
import com.arahant.business.BScreen;

public class SaveBenefitInput extends TransmitInputBase {

	@Validation(required = true)
	private String benefitId;
	@Validation(required = true)
	private String name;
	@Validation(required = false)
	private String ruleName;
	@Validation(required = false)
	private boolean timeRelated;
	@Validation(required = false)
	private boolean paid;
	@Validation(required = true)
	private String categoryId;
	@Validation(required = false)
	private String preTax;
	@Validation(required = false)
	private String vendorId;
	@Validation(required = false)
	private String policyOrGroup;
	@Validation(required = false)
	private String additionalInfo;
	@Validation(type = "date", required = false)
	private int activeDate;
	@Validation(type = "date", required = false)
	private int inactiveDate;
	@Validation(required = false)
	private boolean contingentBeneficiaries;
	@Validation(required = false)
	private boolean hasBeneficiaries;
	@Validation(required = false)
	private String plan;
	@Validation(required = false)
	private boolean requiresDecline;
	@Validation(required = false)
	private String additionalInstructions;
	@Validation(required = false)
	private boolean coveredUnderCOBRA;
	@Validation(required = false)
	private String payerId;
	@Validation(required = false)
	private String insuranceCode;
	@Validation(required = false)
	private String planName;
	@Validation(required = false)
	private String subGroupId;
	@Validation(required = false)
	private String serviceId;
	@Validation(required = true)
	private String wageTypeId;
	@Validation(required = false)
	private String description;
	@Validation(required = true)
	private int eligibilityType;
	@Validation(required = false)
	private int eligibilityPeriod;
	@Validation(required = true)
	private int dependentMaxAge;
	@Validation(required = true)
	private int dependentMaxAgeStudent;
	@Validation(required = true)
	private int coverageEndType;
	@Validation(required = false)
	private int coverageEndPeriod;
	@Validation(required = true)
	private String processType;
	@Validation(required = false)
	private String avatarPath;
	@Validation(required = false)
	private boolean includeInOpenEnrollment;
	@Validation(required = false)
	private boolean includeOnboarding;
	@Validation(required = false)
	private String[] benefitClassIds;
	@Validation(required = false)
	private String screenId;
	@Validation(required = false)
	private String onboardingScreenId;
	@Validation(required = false)
	private String avatarLocation;
	@Validation(required = false)
	private String replacedByBenefitId;
	@Validation(required = false)
	private String[] replacingBenefitIds;
	@Validation(required = false)
	private Boolean hasPhysicians;
	@Validation(required = false)
	private String groupAccountId;
	@Validation(required = false)
	private boolean autoAssign;
	@Validation(required = false)
	private int minAge;
	@Validation(required = false)
	private int maxAge;
	@Validation(required = false)
	private String internalId;
	@Validation(required = false)
	private String costCalcType;
	@Validation(required = true)
	private String employerCostModel;
	@Validation(required = true)
	private String employeeCostModel;
	@Validation(required = true)
	private String benefitAmountModel;
	@Validation(required = false)
	private double minPay;
	@Validation(required = false)
	private double maxPay;
	@Validation(required = false)
	private float minHoursPerWeek;
	
	void setData(BHRBenefit bc) {
		bc.setName(name);
		bc.setRuleName(ruleName);
		bc.setTimeRelated(timeRelated);
		bc.setPaid(paid);
		bc.setCategoryId(categoryId);
		bc.setPreTax(preTax.charAt(0));
		bc.setUnderwriter(vendorId);
		bc.setGroupId(policyOrGroup);
		bc.setAdditionalInfo(additionalInfo);
		bc.setStartDate(activeDate);
		bc.setEndDate(inactiveDate);
		bc.setContingentBeneficiaries(contingentBeneficiaries);
		bc.setHasBeneficiaries(hasBeneficiaries);
		bc.setPlan(plan);
		bc.setRequiresDecline(requiresDecline);
		bc.setAdditionalInstructions(additionalInstructions);
		bc.setCoveredUnderCOBRA(coveredUnderCOBRA);
		bc.setPayerId(payerId);
		bc.setInsuranceCode(insuranceCode);
		bc.setPlanName(planName);
		bc.setSubGroupId(subGroupId);
		bc.setProductServiceId(serviceId);
		bc.setWageTypeId(wageTypeId);
		bc.setDescription(description);
		bc.setEligibilityType(eligibilityType);
		bc.setEligibilityPeriod(eligibilityPeriod);
		bc.setDependentMaxAge(dependentMaxAge);
		bc.setDependentMaxAgeStudent(dependentMaxAgeStudent);
		bc.setCoverageEndType(coverageEndType);
		bc.setCoverageEndPeriod(coverageEndPeriod);
		bc.setProcessType(processType);
		bc.setAvatarPath(avatarPath);
		bc.setOpenEnrollmentWizard(includeInOpenEnrollment ? "Y" : "N");
		bc.setOnboarding(includeOnboarding ? "Y" : "N");
		bc.setOpenEnrollmentScreen(new BScreen(screenId).getBean());
		bc.setOnboardingScreen(new BScreen(onboardingScreenId).getBean());
		bc.setAvatarLocation(avatarLocation);
		bc.setHasPhysicians(hasPhysicians);
		bc.setGroupAccountId(groupAccountId);
		bc.setAutoAssignBoolean(autoAssign);
		bc.setMinAge(minAge);
		bc.setMaxAge(maxAge);
		bc.setInternalId(internalId);
		bc.setCostCalcType(costCalcType.charAt(0));
		bc.setEmployerCostModel(employerCostModel.charAt(0));
		bc.setEmployeeCostModel(employeeCostModel.charAt(0));
		bc.setBenefitAmountModel(benefitAmountModel.charAt(0));
		bc.setMinPay(minPay);
		bc.setMaxPay(maxPay);
		bc.setMinHoursPerWeek(minHoursPerWeek);
		
		if (!isEmpty(replacedByBenefitId))
			bc.setReplacingBenefit(new BHRBenefit(replacedByBenefitId).getBean());
		else
			bc.setReplacingBenefit(null);

		//clear out existing benefit classes
		bc.getBean().getBenefitClasses().clear();
		bc.update();
		//add new ones
		if (benefitClassIds != null)
			for (String s : benefitClassIds)
				bc.associateBenefitClass(s);
	}
	
	public String getCostCalcType() {
		return costCalcType;
	}
	
	public void setCostCalcType(String costCalcType) {
		this.costCalcType = costCalcType;
	}
	
	public boolean getAutoAssign() {
		return autoAssign;
	}
	
	public void setAutoAssign(boolean autoAssign) {
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
	
	public String getBenefitId() {
		return benefitId;
	}
	
	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}
	
	public int getActiveDate() {
		return activeDate;
	}
	
	public void setActiveDate(int activeDate) {
		this.activeDate = activeDate;
	}
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String getAdditionalInstructions() {
		return additionalInstructions;
	}
	
	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public boolean isCoveredUnderCOBRA() {
		return coveredUnderCOBRA;
	}
	
	public void setCoveredUnderCOBRA(boolean coveredUnderCOBRA) {
		this.coveredUnderCOBRA = coveredUnderCOBRA;
	}
	
	public boolean getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}
	
	public void setContingentBeneficiaries(boolean contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}
	
	public boolean isHasBeneficiaries() {
		return hasBeneficiaries;
	}
	
	public void setHasBeneficiaries(boolean hasBeneficiaries) {
		this.hasBeneficiaries = hasBeneficiaries;
	}
	
	public int getInactiveDate() {
		return inactiveDate;
	}
	
	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	
	public String getInsuranceCode() {
		return insuranceCode;
	}
	
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isPaid() {
		return paid;
	}
	
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	
	public String getPayerId() {
		return payerId;
	}
	
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	
	public String getPlan() {
		return plan;
	}
	
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public String getPlanName() {
		return planName;
	}
	
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	
	public String getPolicyOrGroup() {
		return policyOrGroup;
	}
	
	public void setPolicyOrGroup(String policyOrGroup) {
		this.policyOrGroup = policyOrGroup;
	}
	
	public String getPreTax() {
		return preTax;
	}
	
	public void setPreTax(String preTax) {
		this.preTax = preTax;
	}
	
	public boolean isRequiresDecline() {
		return requiresDecline;
	}
	
	public void setRequiresDecline(boolean requiresDecline) {
		this.requiresDecline = requiresDecline;
	}
	
	public String getRuleName() {
		return ruleName;
	}
	
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getSubGroupId() {
		return subGroupId;
	}
	
	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}
	
	public boolean isTimeRelated() {
		return timeRelated;
	}
	
	public void setTimeRelated(boolean timeRelated) {
		this.timeRelated = timeRelated;
	}
	
	public String getVendorId() {
		return vendorId;
	}
	
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
	public String getWageTypeId() {
		return wageTypeId;
	}
	
	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
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
	
	public String[] getBenefitClassIds() {
		return benefitClassIds;
	}
	
	public void setBenefitClassIds(String[] benefitClassIds) {
		this.benefitClassIds = benefitClassIds;
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
	
	public String getReplacedByBenefitId() {
		return replacedByBenefitId;
	}
	
	public void setReplacedByBenefitId(String replacedByBenefitId) {
		this.replacedByBenefitId = replacedByBenefitId;
	}
	
	public String[] getReplacingBenefitIds() {
		return replacingBenefitIds;
	}
	
	public void setReplacingBenefitIds(String[] replacingBenefitIds) {
		this.replacingBenefitIds = replacingBenefitIds;
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
