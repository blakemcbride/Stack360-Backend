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

import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;

/**
 *
 *
 *
 */
public class ListFlexBenefitsReturnItem {

	private String benefitName;
	private String configName;
	private boolean employeeEntersAmount;
	private String configId;
	private double pppCost;
	private double monthlyCost;
	private double annualCost;
	private double maxPppCost;
	private double maxMonthlyCost;
	private double maxAnnualCost;
	private String instructions;
	private boolean select;
	private double payPeriods;

	public ListFlexBenefitsReturnItem() {
	}

	ListFlexBenefitsReturnItem(WizardConfigurationConfig wcc, String empId, int asOfDate) {
		BHRBenefitConfig bc = new BHRBenefitConfig(wcc.getBenefitConfig());

		benefitName = wcc.getWizardConfigurationBenefit().getName();
		configName = wcc.getName();
		employeeEntersAmount = (bc.getBenefit().deprecatedGetEmployeeChoosesAmount() == 'Y');
		configId = bc.getConfigId();

		BPerson bpp = new BPerson();
		bpp.loadPending(empId);
		select = bpp.enrolledInConfig(bc.getConfigId());

		BHRBenefitJoin bj = new BHRBenefitJoin(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bc.getConfigId()).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).first());

		if (select) {
			annualCost = bj.getAmountCovered(); //bc.getEmployeeCostAnnual(empId, asOfDate);
			pppCost = bj.getAmountCovered() / BEmployee.getPPY(empId, asOfDate);//bc.getEmployeeCostPPP(empId, asOfDate);
			monthlyCost = bj.getAmountCovered() / 12; //bc.getEmployeeCostMonthly(empId, asOfDate);
		} else {
			annualCost = 0;
			pppCost = 0;
			monthlyCost = 0;
		}

		int ppy = BEmployee.getPPY(empId, asOfDate);
		if (ppy > 0)
			maxPppCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 / ppy;
		else
			maxPppCost = 0.0;
		maxAnnualCost = Math.round(BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 * 1000.00) / 1000.00;
		maxMonthlyCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0);
		instructions = wcc.getWizardConfigurationBenefit().getInstructions();
		payPeriods = BEmployee.getPPY(empId, asOfDate);
	}

	ListFlexBenefitsReturnItem(BHRBenefitConfig bc, String empId, Integer asOfDate) {
		benefitName = bc.getBenefit().getName();
		configName = bc.getConfigName();
		employeeEntersAmount = (bc.getBenefit().deprecatedGetEmployeeChoosesAmount() == 'Y');
		configId = bc.getConfigId();

		BPerson bpp = new BPerson();
		bpp.loadPending(empId);
		select = bpp.enrolledInConfig(bc.getConfigId());

		BHRBenefitJoin bj = new BHRBenefitJoin(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bc.getConfigId()).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).first());

		if (select) {
			annualCost = bj.getAmountCovered(); //bc.getEmployeeCostAnnual(empId, asOfDate);
			pppCost = bj.getAmountCovered() / BEmployee.getPPY(empId, asOfDate);//bc.getEmployeeCostPPP(empId, asOfDate);
			monthlyCost = bj.getAmountCovered() / 12; //bc.getEmployeeCostMonthly(empId, asOfDate);
		} else {
			annualCost = 0;
			pppCost = 0;
			monthlyCost = 0;
		}

		int ppy = BEmployee.getPPY(empId, asOfDate);
		if (ppy > 0)
			maxPppCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 / ppy;
		else
			maxPppCost = 0.0;
		maxPppCost = Math.round(maxPppCost * 100.0) / 100.0;
		maxAnnualCost = Math.round(BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 * 1000.00) / 1000.00;
		maxMonthlyCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0);
		payPeriods = BEmployee.getPPY(empId, asOfDate);


		instructions = bc.getInstructions();
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public boolean getEmployeeEntersAmount() {
		return employeeEntersAmount;
	}

	public void setEmployeeEntersAmount(boolean employeeEntersAmount) {
		this.employeeEntersAmount = employeeEntersAmount;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public double getAnnualCost() {
		return annualCost;
	}

	public void setAnnualCost(double annualCost) {
		this.annualCost = annualCost;
	}

	public double getMaxAnnualCost() {
		return maxAnnualCost;
	}

	public void setMaxAnnualCost(double maxAnnualCost) {
		this.maxAnnualCost = maxAnnualCost;
	}

	public double getMaxMonthlyCost() {
		return maxMonthlyCost;
	}

	public void setMaxMonthlyCost(double maxMonthlyCost) {
		this.maxMonthlyCost = maxMonthlyCost;
	}

	public double getMaxPppCost() {
		return maxPppCost;
	}

	public void setMaxPppCost(double maxPppCost) {
		this.maxPppCost = maxPppCost;
	}

	public double getMonthlyCost() {
		return monthlyCost;
	}

	public void setMonthlyCost(double monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	public double getPppCost() {
		return pppCost;
	}

	public void setPppCost(double pppCost) {
		this.pppCost = pppCost;
	}

	public double getPayPeriods() {
		return payPeriods;
	}

	public void setPayPeriods(double payPeriods) {
		this.payPeriods = payPeriods;
	}
}
