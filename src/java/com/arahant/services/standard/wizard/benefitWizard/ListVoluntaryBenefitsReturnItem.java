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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.IPerson;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 *
 *
 */
public class ListVoluntaryBenefitsReturnItem {

	private String coverage;
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
	private boolean coversEmployee;
	private boolean coversEmpSpouse;
	private boolean coversNonEmpSpouse;
	private boolean coversChildren;
	private boolean coversEmpSpouseOrChildren;
	private boolean coversNonEmpSpouseOrChildren;
	private int maxDependentsCovered;
	private boolean spouseDeclinesCoverage;
	private double coveragePpp;
	private double coverageMonthly;
	private double coverageAnnual;

	public ListVoluntaryBenefitsReturnItem() {
	}

	ListVoluntaryBenefitsReturnItem(BHRBenefitConfig bc, String empId, int asOfDate) {
		coverage = bc.getCoverage();
		configName = bc.getConfigName();
		employeeEntersAmount = (bc.getBenefit().deprecatedGetEmployeeChoosesAmount() == 'Y');
		configId = bc.getConfigId();

		BPerson bpp = new BPerson();
		bpp.loadPending(empId);
		select = bpp.enrolledInConfig(bc.getConfigId());

		if (!select)
			select = bpp.enrolledInApprovedConfig(bc.getConfigId());

		BHRBenefitJoin bj = new BHRBenefitJoin(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bc.getConfigId()).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).first());

		if (select) {
			List<IPerson> coveredPeople = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson().getPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bj.getBenefitConfig().getBean()).eq(HrBenefitJoin.APPROVED, bj.getBenefitApproved() ? 'Y' : 'N').dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, asOfDate).list();

			coverageAnnual = bj.getAmountCovered(); //bc.getEmployeeCostAnnual(empId, asOfDate);
			coveragePpp = bj.getAmountCovered();//bc.getEmployeeCostPPP(empId, asOfDate);
			coverageMonthly = bj.getAmountCovered(); //bc.getEmployeeCostMonthly(empId, asOfDate);
			pppCost = BenefitCostCalculator.calculateCostNewMethod(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1, null);
			monthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthly(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1, null);
			annualCost = BenefitCostCalculator.calculateCostNewMethodAnnual(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1, null);
			if (pppCost == Double.NaN)
				pppCost = 0;
		} else {
			List<IPerson> coveredPeople = bc.estimateCoveredPeople(new BEmployee(empId));
			pppCost = BenefitCostCalculator.calculateCostNewMethod(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, 0, asOfDate, 0, coveredPeople, -1, null);
			monthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthly(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, 0, asOfDate, 0, coveredPeople, -1, null);
			annualCost = BenefitCostCalculator.calculateCostNewMethodAnnual(asOfDate, bc.getBean(), new BPerson(empId).getPerson(), false, 0, asOfDate, 0, coveredPeople, -1, null);
			if (pppCost == Double.NaN)
				pppCost = 0;

			coveragePpp = 0;
			coverageMonthly = 0;
			coverageAnnual = 0;
		}
		int ppy = BEmployee.getPPY(empId, asOfDate);
		if (ppy > 0)
			maxPppCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 / ppy;
		else
			maxPppCost = 0.0;
		maxAnnualCost = Math.round(BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0) * 12 * 1000.00) / 1000.00;
		maxMonthlyCost = BenefitCostCalculator.getMaxMonthlyAmount(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, empId), false, 0, asOfDate, 0);
		instructions = bc.getInstructions();
		payPeriods = BEmployee.getPPY(empId, asOfDate);
		coversEmployee = bc.getCoversEmployee();
		coversEmpSpouse = bc.getCoversEmployeeSpouse();
		coversNonEmpSpouse = bc.getSpouseNonEmployee();
		coversChildren = bc.getCoversChildren();
		coversEmpSpouseOrChildren = bc.getCoversEmployeeSpouseOrChildren();
		coversNonEmpSpouseOrChildren = bc.getSpouseNonEmpOrChildren();
		maxDependentsCovered = bc.getMaxChildren();
		spouseDeclinesCoverage = bc.getSpouseDeclinesExternalCoverage();

	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
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

	public boolean isCoversChildren() {
		return coversChildren;
	}

	public void setCoversChildren(boolean coversChildren) {
		this.coversChildren = coversChildren;
	}

	public boolean isCoversEmpSpouse() {
		return coversEmpSpouse;
	}

	public void setCoversEmpSpouse(boolean coversEmpSpouse) {
		this.coversEmpSpouse = coversEmpSpouse;
	}

	public boolean isCoversEmpSpouseOrChildren() {
		return coversEmpSpouseOrChildren;
	}

	public void setCoversEmpSpouseOrChildren(boolean coversEmpSpouseOrChildren) {
		this.coversEmpSpouseOrChildren = coversEmpSpouseOrChildren;
	}

	public boolean isCoversEmployee() {
		return coversEmployee;
	}

	public void setCoversEmployee(boolean coversEmployee) {
		this.coversEmployee = coversEmployee;
	}

	public boolean isCoversNonEmpSpouse() {
		return coversNonEmpSpouse;
	}

	public void setCoversNonEmpSpouse(boolean coversNonEmpSpouse) {
		this.coversNonEmpSpouse = coversNonEmpSpouse;
	}

	public boolean isCoversNonEmpSpouseOrChildren() {
		return coversNonEmpSpouseOrChildren;
	}

	public void setCoversNonEmpSpouseOrChildren(boolean coversNonEmpSpouseOrChildren) {
		this.coversNonEmpSpouseOrChildren = coversNonEmpSpouseOrChildren;
	}

	public int getMaxDependentsCovered() {
		return maxDependentsCovered;
	}

	public void setMaxDependentsCovered(int maxDependentsCovered) {
		this.maxDependentsCovered = maxDependentsCovered;
	}

	public boolean isSpouseDeclinesCoverage() {
		return spouseDeclinesCoverage;
	}

	public void setSpouseDeclinesCoverage(boolean spouseDeclinesCoverage) {
		this.spouseDeclinesCoverage = spouseDeclinesCoverage;
	}

	public double getCoverageAnnual() {
		return coverageAnnual;
	}

	public void setCoverageAnnual(double coverageAnnual) {
		this.coverageAnnual = coverageAnnual;
	}

	public double getCoverageMonthly() {
		return coverageMonthly;
	}

	public void setCoverageMonthly(double coverageMonthly) {
		this.coverageMonthly = coverageMonthly;
	}

	public double getCoveragePpp() {
		return coveragePpp;
	}

	public void setCoveragePpp(double coveragePpp) {
		this.coveragePpp = coveragePpp;
	}
}
