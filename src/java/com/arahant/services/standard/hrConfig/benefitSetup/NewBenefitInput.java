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
package com.arahant.services.standard.hrConfig.benefitSetup;
import com.arahant.annotation.Validation;

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantLogger;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewBenefitInput extends TransmitInputBase {
	
	private static final transient ArahantLogger logger = new ArahantLogger(NewBenefitInput.class);

	void setData(final BHRBenefit bc)
	{
		bc.setName(name);
		bc.setRuleName(ruleName);
		bc.setTimeRelated(timeRelated);
		bc.setPaid(paid);
		bc.setCategoryId(categoryId);
		logger.error("This screen is out-of-date.  It doesn't correctly handle preTax");
		bc.setPreTax(preTax?'Y':'N');
		bc.setUnderwriter(vendorId);
		bc.setGroupId(policyOrGroup);
		bc.setAdditionalInfo(additionalInfo);
		bc.setEmployeeIsProvider(employeeSpecificCost);
		bc.setStartDate(activeDate);
		bc.setEndDate(inactiveDate);
                bc.setContingentBeneficiaries(contingentBeneficiaries);
		bc.setHasBeneficiaries(hasBeneficiaries);
		bc.setPlan(plan);
		bc.setRequiresDecline(requiresDecline);
		bc.setAdditionalInstructions(additionalInstructions);
		bc.setEmployeeChoosesAmount(employeeChoosesAmount);
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
		bc.setOpenEnrollmentWizard(includeInOpenEnrollment?"Y":"N");
		bc.setOnboarding(includeOnboarding?"Y":"N");
	}
	
	@Validation (required=true)
	private String name;
	@Validation (required=false)
	private String ruleName;
	@Validation (required=false)
	private boolean timeRelated;
	@Validation (required=false)
	private boolean paid;
	@Validation (required=true)
	private String categoryId;
	@Validation (required=false)
	private boolean preTax;
	@Validation (required=false)
	private String vendorId;
	@Validation (required=false)
	private String policyOrGroup;
	@Validation (required=false)
	private String additionalInfo;
	@Validation (required=false)
	private boolean employeeSpecificCost;
	@Validation (type="date",required=false)
	private int activeDate;
	@Validation (type="date",required=false)
	private int inactiveDate;
	@Validation (required=false)
	private boolean contingentBeneficiaries;
	@Validation (required=false)
	private boolean hasBeneficiaries;
	@Validation (required=false)
	private String plan;
	@Validation (required=false)
	private boolean requiresDecline;
	@Validation (required=false)
	private String additionalInstructions;
	@Validation (required=false)
	private boolean employeeChoosesAmount;
	@Validation (required=false)
	private boolean coveredUnderCOBRA;
	@Validation (required=false)
	private String payerId;
	@Validation (required=false)
	private String insuranceCode;
	@Validation (required=false)
	private String planName;
	@Validation (required=false)
	private String subGroupId;
	@Validation (required=false)
	private String serviceId;
	@Validation (required=true)
	private String wageTypeId;
	@Validation (required=false)
	private String description;
	@Validation (required=true)
	private int eligibilityType;
	@Validation (required=false)
	private int eligibilityPeriod;
	@Validation (required=true)
	private int dependentMaxAge;
	@Validation (required=true)
	private int dependentMaxAgeStudent;
	@Validation (required=true)
	private int coverageEndType;
	@Validation (required=false)
	private int coverageEndPeriod;
	@Validation (required=true)
	private String processType;
	@Validation (required=false)
	private String avatarPath;
	@Validation (required=false)
	private boolean includeInOpenEnrollment;
	@Validation (required=false)
	private boolean includeOnboarding;

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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	 * @return Returns the additionalInstructions.
	 */
	public String getAdditionalInstructions() {
		return additionalInstructions;
	}
	/**
	 * @param additionalInstructions The additionalInstructions to set.
	 */
	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
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
	public String getName()
	{
		return name;
	}
	public void setName(final String name)
	{
		this.name=name;
	}
	public String getRuleName()
	{
		return ruleName;
	}
	public void setRuleName(final String ruleName)
	{
		this.ruleName=ruleName;
	}
	public boolean getTimeRelated()
	{
		return timeRelated;
	}
	public void setTimeRelated(final boolean timeRelated)
	{
		this.timeRelated=timeRelated;
	}
	public boolean getPaid()
	{
		return paid;
	}
	public void setPaid(final boolean paid)
	{
		this.paid=paid;
	}
	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(final String categoryId)
	{
		this.categoryId=categoryId;
	}
	public boolean getPreTax()
	{
		return preTax;
	}
	public void setPreTax(final boolean preTax)
	{
		this.preTax=preTax;
	}
	public String getVendorId()
	{
		return vendorId;
	}
	public void setVendorId(final String underwriter)
	{
		this.vendorId=underwriter;
	}
	public String getPolicyOrGroup()
	{
		return policyOrGroup;
	}
	public void setPolicyOrGroup(final String groupId)
	{
		this.policyOrGroup=groupId;
	}
	public String getAdditionalInfo()
	{
		return additionalInfo;
	}
	public void setAdditionalInfo(final String additionalInfo)
	{
		this.additionalInfo=additionalInfo;
	}
	public boolean getEmployeeSpecificCost()
	{
		return employeeSpecificCost;
	}
	public void setEmployeeSpecificCost(final boolean employeeIsProvider)
	{
		this.employeeSpecificCost=employeeIsProvider;
	}

        public boolean getContingentBeneficiaries() {
            return contingentBeneficiaries;
        }

        public void setContingentBeneficiaries(boolean contingentBeneficiaries) {
            this.contingentBeneficiaries = contingentBeneficiaries;
        }

	public boolean getHasBeneficiaries()
	{
		return hasBeneficiaries;
	}
	public void setHasBeneficiaries(final boolean hasBeneficiaries)
	{
		this.hasBeneficiaries=hasBeneficiaries;
	}
	/**
	 * @return Returns the endDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setInactiveDate(final int endDate) {
		this.inactiveDate = endDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getActiveDate() {
		return activeDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setActiveDate(final int startDate) {
		this.activeDate = startDate;
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
	public boolean isEmployeeChoosesAmount() {
		return employeeChoosesAmount;
	}
	public void setEmployeeChoosesAmount(boolean employeeChoosesAmount) {
		this.employeeChoosesAmount = employeeChoosesAmount;
	}

	public boolean getCoveredUnderCOBRA() {
		return this.coveredUnderCOBRA;
	}
	public void setCoveredUnderCOBRA(boolean coveredUnderCOBRA) {
		this.coveredUnderCOBRA = coveredUnderCOBRA;
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
}

	
