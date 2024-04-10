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


package com.arahant.services.standard.hr.benefitConfigAdvancedCost;

import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BBenefitConfigCostAge;

public class NewConfigInputCost {

	private String ageCalcType;
	private String orgGroupId;
	private String statusType;
	private int firstActiveDate;
	private int lastActiveDate;
	
	private double fixedEmployeeCost;
	private double fixedEmployerCost;
	private double benefitAmount;
	private double minValue;
	private double maxValue;
	private double stepValue;
	private double maxMultipleOfSalary;
	private double ratePerUnit;
	private short rateFrequency;
	private String rateRelatesTo;
	private String salaryRoundType;
	private double salaryRoundAmount;
	private String benefitRoundType;
	private double benefitRoundAmount;
	private String capType;
	private String guaranteedIssueType;
	private double guaranteedIssueAmount;

	private String[] statusIds;
	private NewConfigInputCostAge[] age;

	public NewConfigInputCost() {
	}

	public void setData(BBenefitConfigCost bcc) {
		bcc.setOrgGroupId(orgGroupId);
		bcc.setStatusType(statusType);
		bcc.setFirstActiveDate(firstActiveDate);
		bcc.setLastActiveDate(lastActiveDate);
		bcc.setStatusIds(getStatusIds());
		bcc.setAgeCalcType(ageCalcType);
		
		bcc.setFixedEmployeeCost(fixedEmployeeCost);
		bcc.setFixedEmployerCost(fixedEmployerCost);
		bcc.setBenefitAmount(benefitAmount);
		bcc.setMinValue(minValue);
		bcc.setMaxValue(maxValue);
		bcc.setStepValue(stepValue);
		bcc.setMaxMultipleOfSalary(maxMultipleOfSalary);
		bcc.setRatePerUnit(ratePerUnit);
		bcc.setRateFrequency(rateFrequency);
		bcc.setRateRelatesTo(rateRelatesTo.charAt(0));
		bcc.setSalaryRoundType(salaryRoundType.charAt(0));
		bcc.setSalaryRoundAmount(salaryRoundAmount);
		bcc.setBenefitRoundType(benefitRoundType.charAt(0));
		bcc.setBenefitRoundAmount(benefitRoundAmount);
		bcc.setCapType(capType.charAt(0));
		bcc.setGuaranteedIssueType(guaranteedIssueType.charAt(0));
		bcc.setGuaranteedIssueAmount(guaranteedIssueAmount);

		//handle ages

		for (NewConfigInputCostAge a : getAge()) {
			BBenefitConfigCostAge ba = new BBenefitConfigCostAge();
			ba.create();
			ba.setMaxAge(a.getMaxAge());
			ba.setEeValue(a.getEeValue());
			ba.setErValue(a.getErValue());
			ba.setInsuranceId(a.getInsuranceId());
			ba.setConfigCost(bcc);
			bcc.addPendingInsert(ba);
		}
	}

	public NewConfigInputCostAge[] getAge() {
		if (age == null)
			age = new NewConfigInputCostAge[0];
		return age;
	}

	public void setAge(NewConfigInputCostAge[] age) {
		this.age = age;
	}

	public String[] getStatusIds() {
		if (statusIds == null)
			statusIds = new String[0];
		return statusIds;
	}

	public void setStatusIds(String[] statusIds) {
		this.statusIds = statusIds;
	}

	public String getAgeCalcType() {
		return ageCalcType;
	}

	public void setAgeCalcType(String ageCalcType) {
		this.ageCalcType = ageCalcType;
	}

	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public double getFixedEmployeeCost() {
		return fixedEmployeeCost;
	}

	public void setFixedEmployeeCost(double fixedEmployeeCost) {
		this.fixedEmployeeCost = fixedEmployeeCost;
	}

	public double getFixedEmployerCost() {
		return fixedEmployerCost;
	}

	public void setFixedEmployerCost(double fixedEmployerCost) {
		this.fixedEmployerCost = fixedEmployerCost;
	}

	public double getBenefitAmount() {
		return benefitAmount;
	}

	public void setBenefitAmount(double benefitAmount) {
		this.benefitAmount = benefitAmount;
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

	public double getMaxMultipleOfSalary() {
		return maxMultipleOfSalary;
	}

	public void setMaxMultipleOfSalary(double maxMultipleOfSalary) {
		this.maxMultipleOfSalary = maxMultipleOfSalary;
	}

	public double getRatePerUnit() {
		return ratePerUnit;
	}

	public void setRatePerUnit(double ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}

	public short getRateFrequency() {
		return rateFrequency;
	}

	public void setRateFrequency(short rateFrequency) {
		this.rateFrequency = rateFrequency;
	}

	public double getSalaryRoundAmount() {
		return salaryRoundAmount;
	}

	public void setSalaryRoundAmount(double salaryRoundAmount) {
		this.salaryRoundAmount = salaryRoundAmount;
	}

	public String getRateRelatesTo() {
		return rateRelatesTo;
	}

	public void setRateRelatesTo(String rateRelatesTo) {
		this.rateRelatesTo = rateRelatesTo;
	}

	public String getSalaryRoundType() {
		return salaryRoundType;
	}

	public void setSalaryRoundType(String salaryRoundType) {
		this.salaryRoundType = salaryRoundType;
	}

	public String getBenefitRoundType() {
		return benefitRoundType;
	}

	public void setBenefitRoundType(String benefitRoundType) {
		this.benefitRoundType = benefitRoundType;
	}

	public double getBenefitRoundAmount() {
		return benefitRoundAmount;
	}

	public void setBenefitRoundAmount(double benefitRoundAmount) {
		this.benefitRoundAmount = benefitRoundAmount;
	}

	public String getCapType() {
		return capType;
	}

	public void setCapType(String capType) {
		this.capType = capType;
	}

	public String getGuaranteedIssueType() {
		return guaranteedIssueType;
	}

	public void setGuaranteedIssueType(String guaranteedIssueType) {
		this.guaranteedIssueType = guaranteedIssueType;
	}

	public double getGuaranteedIssueAmount() {
		return guaranteedIssueAmount;
	}

	public void setGuaranteedIssueAmount(double guaranteedIssueAmount) {
		this.guaranteedIssueAmount = guaranteedIssueAmount;
	}

}
