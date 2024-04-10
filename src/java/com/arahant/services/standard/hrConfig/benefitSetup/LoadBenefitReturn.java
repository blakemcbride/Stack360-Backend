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

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
public class LoadBenefitReturn extends TransmitReturnBase {
	private static final transient ArahantLogger logger = new ArahantLogger(LoadBenefitReturn.class);

	void setData(final BHRBenefit bc)
	{
		serviceId=bc.getProductServiceId();
		name=bc.getName();
		ruleName=bc.getRuleName();
		timeRelated=bc.getTimeRelated();
		paid=bc.getPaid();
		categoryId=bc.getCategoryId();
		logger.error("This screen is out-of-date.  It doesn't correctly handle preTax");
		preTax=bc.getPreTax() == 'Y';
		vendorId=bc.getUnderwriterId();
		policyOrGroup=bc.getGroupId();
		additionalInfo=bc.getAdditionalInfo();
		employeeSpecificCost=bc.getEmployeeIsProviderBool();
		activeDate=bc.getStartDate();
		inactiveDate=bc.getEndDate();
                contingentBeneficiaries = bc.getHasContingentBeneficiariesBool();
		hasBeneficiaries=bc.getHasBeneficiariesBool();
		plan=bc.getPlan();
		requiresDecline=bc.getRequiresDecline();
		instructions=bc.getAdditionalInstructions();
		employeeChoosesAmount=bc.getEmployeeChoosesAmount();
		coveredUnderCOBRA=bc.getCoveredUnderCOBRA();
		payerId=bc.getPayerId();
		insuranceCode=bc.getInsuranceCode();
		planName=bc.getPlanName();
		subGroupId=bc.getSubGroupId();
		wageTypeId=bc.getWageTypeId();
		description=bc.getDescription();
		eligibilityType=bc.getEligibilityType();
		eligibilityPeriod=bc.getEligibilityPeriod();
		dependentMaxAge=bc.getDependentMaxAge();
		dependentMaxAgeStudent=bc.getDependentMaxAgeStudent();
		coverageEndType=bc.getCoverageEndType();
		coverageEndPeriod=bc.getCoverageEndPeriod();
		processType=bc.getProcessType();
		avatarPath=bc.getAvatarPath();
		includeInOpenEnrollment=bc.getOpenEnrollmentWizard().equals("Y");
		includeOnboarding=bc.getOnboarding().equals("Y");
	}

	private String serviceId;
	private String name;
	private String ruleName;
	private boolean timeRelated;
	private boolean paid;
	private String categoryId;
	private boolean preTax;
	private String vendorId;
	private String policyOrGroup;
	private String additionalInfo;
	private boolean employeeSpecificCost;
	private int activeDate, inactiveDate;
        private boolean contingentBeneficiaries;
	private boolean hasBeneficiaries;
	private String plan;
	private boolean requiresDecline;
	private String instructions;
	private boolean employeeChoosesAmount;
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
	 * @return Returns the employeeChoosesAmount.
	 */
	public boolean isEmployeeChoosesAmount() {
		return employeeChoosesAmount;
	}
	/**
	 * @param employeeChoosesAmount The employeeChoosesAmount to set.
	 */
	public void setEmployeeChoosesAmount(boolean employeeChoosesAmount) {
		this.employeeChoosesAmount = employeeChoosesAmount;
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
}

	
