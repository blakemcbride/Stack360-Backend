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


package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.beans.AIProperty;
import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;

public class ListAssignedBenefitConfigurationsReturnItem {

	private String personConfigId;
	private String categoryId;
	private String categoryName;
	private String benefitId;
	private String benefitName;
	private String configId;
	private String configName;
	private int policyStartDate;
	private String providedBySSN;
	private String providedByDisplayName;
	private boolean supportsBeneficiaries;
	private String active;
	private int endDate;
//	private String benefitChangeReason;
	private String benefitChangeReasonId;
	private boolean readOnly;
	private String approved;
	private String description;
	
	// from benefit_config_cost
	private double minValue;
	private double maxValue;
	private double stepValue;

	public ListAssignedBenefitConfigurationsReturnItem() {
	}

	ListAssignedBenefitConfigurationsReturnItem(final BHRBenefitJoin ebj, final BPerson emp) throws ArahantException {

		personConfigId = ebj.getBenefitJoinId();

		if (ebj.getBenefitConfig() != null) {
			final BHRBenefitConfig bc = ebj.getBenefitConfig();
			categoryId = bc.getCategoryId();
			categoryName = bc.getCategoryName();
			benefitId = bc.getBenefitId();
			benefitName = bc.getBenefitName();
			configId = bc.getConfigId();
			BEmployee employee = new BEmployee(emp);
			BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(new BHRBenefitConfig(bc.getBean()), employee.getStatusId(), employee.getOrgGroups(), 0);
			if (bcc != null) {
				minValue = bcc.getMinValue();
				maxValue = bcc.getMaxValue();
				stepValue = bcc.getStepValue();
			}

			configName = bc.getConfigName();
			policyStartDate = ebj.getPolicyStartDate();
			providedBySSN = ebj.getProvidedBySSN();
			providedByDisplayName = ebj.getProvidedByDisplayName();
			supportsBeneficiaries = bc.getSupportsBeneficiaries();
			active = ebj.isActive();
			endDate = ebj.getPolicyEndDate();
			approved = ebj.getBenefitApproved() ? "Yes" : "No";
			//benefitChangeReason=ebj.getChangeReason();
			description = getDetailDesc(ebj);
		} else if (ebj.isBenefitCategoryDecline()) {
			categoryId = ebj.getBenefitCategoryId();
			categoryName = ebj.getBenefitCategoryName();
			benefitName = "DECLINE";
			configName = "DECLINE";
			policyStartDate = ebj.getPolicyStartDate();
			providedBySSN = "";
			providedByDisplayName = "";
			supportsBeneficiaries = false;
			active = "No";
			endDate = ebj.getPolicyEndDate();
			approved = ebj.getBenefitApproved() ? "Yes" : "No";
			//benefitChangeReason=ebj.getChangeReason();
			description = "Coverage Not Elected.";
		} else if (ebj.isBenefitDecline()) {
			categoryId = ebj.getBenefitCategoryId();
			categoryName = ebj.getBenefitCategoryName();
			benefitName = ebj.getBenefitName();
			benefitId = ebj.getBenefitId();
			configName = "DECLINE";
			policyStartDate = ebj.getPolicyStartDate();
			providedBySSN = "";
			providedByDisplayName = "";
			supportsBeneficiaries = false;
			active = "No";
			endDate = ebj.getPolicyEndDate();
			approved = ebj.getBenefitApproved() ? "Yes" : "No";
			//benefitChangeReason=ebj.getChangeReason();
			description = "Coverage Not Elected.";
		}
		benefitChangeReasonId = ebj.getBenefitChangeReasonId();
		readOnly = AIProperty.getBoolean("BenefitReadOnly", categoryId);
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getBenefitChangeReasonId() {
		return benefitChangeReasonId;
	}

	public void setBenefitChangeReasonId(String benefitChangeReasonId) {
		this.benefitChangeReasonId = benefitChangeReasonId;
	}

	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Returns the active.
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final String active) {
		this.active = active;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(final String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(final String benefitId) {
		this.benefitId = benefitId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(final String benefitName) {
		this.benefitName = benefitName;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(final String configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(final String configName) {
		this.configName = configName;
	}

	public int getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(final int policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getProvidedBySSN() {
		return providedBySSN;
	}

	public void setProvidedBySSN(final String providedBySSN) {
		this.providedBySSN = providedBySSN;
	}

	public String getProvidedByDisplayName() {
		return providedByDisplayName;
	}

	public void setProvidedByDisplayName(final String providedByDisplayName) {
		this.providedByDisplayName = providedByDisplayName;
	}

	public boolean getSupportsBeneficiaries() {
		return supportsBeneficiaries;
	}

	public void setSupportsBeneficiaries(final boolean supportsBeneficiaries) {
		this.supportsBeneficiaries = supportsBeneficiaries;
	}

	/**
	 * @return Returns the employeeConfigId.
	 */
	public String getPersonConfigId() {
		return personConfigId;
	}

	/**
	 * @param employeeConfigId The employeeConfigId to set.
	 */
	public void setPersonConfigId(String employeeConfigId) {
		this.personConfigId = employeeConfigId;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getStepValue() {
		return stepValue;
	}

	public void setStepValue(double stepValue) {
		this.stepValue = stepValue;
	}

	private String getDetailDesc(BHRBenefitJoin hbj) {
		String ret = "";

		ret += "Benefit: " + hbj.getBenefitName() + "\n";
		ret += "Config: " + hbj.getBenefitConfigName() + "\n";
		ret += "Coverage Start Date: " + (hbj.getCoverageStartDate() == 0 ? "" : DateUtils.getDateFormatted(hbj.getCoverageStartDate())) + "\n";
		ret += "Coverage End Date: " + (hbj.getCoverageEndDate() == 0 ? "" : DateUtils.getDateFormatted(hbj.getCoverageEndDate())) + "\n";

		for (Person p : ArahantSession.getHSU().createCriteria(Person.class)
				.joinTo(Person.HR_BENEFIT_JOINS_WHERE_COVERED)
				.eq(HrBenefitJoin.PAYING_PERSON, hbj.getPayingPerson().getPerson())
				.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hbj.getBenefitConfig().getBean())
				.eq(HrBenefitJoin.COVERAGE_START_DATE, hbj.getCoverageStartDate())
				.list())
			ret += "Covered Person: " + p.getNameLFM() + "\n";
		return ret;
	}
}
