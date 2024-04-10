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
 *
 */
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.IPerson;
import com.arahant.beans.Person;
import com.arahant.business.*;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class GetApprovedEnrollmentReturn extends TransmitReturnBase {

	private String benefitName;
	private String configName;
	private double oldPppCost;
	private double oldMonthlyCost;
	private double oldAnnualCost;
	private double newPppCost;
	private double newMonthlyCost;
	private double newAnnualCost;
	private boolean coversEmployee;
	private boolean coversEmpSpouse;
	private boolean coversNonEmpSpouse;
	private boolean coversChildren;
	private boolean coversEmpSpouseOrChildren;
	private boolean coversNonEmpSpouseOrChildren;
	private int maxDependentsCovered;
	private boolean spouseDeclinesCoverage;
	private String benefitId;
	private boolean currentEnrollmentReplaced;
	private double coverageAmount;
	private double actualPppCost;
	private double actualMonthlyCost;
	private double actualAnnualCost;
	private boolean decline;

	public void setData(BHRBenefitJoin bj, String employeeId, int asOfDate) {
		BHRBenefitConfig bc = bj.getBenefitConfig();
		benefitName = bj.getBenefitName();
		benefitId = bj.getBenefitId();
		configName = bj.getBenefitConfigName();

		List<IPerson> p = bc.estimateCoveredPeople(new BEmployee(employeeId), true);

		oldPppCost = BenefitCostCalculator.calculateCostNewMethodWithDependencies(DateUtils.now(), bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), DateUtils.now(), 0, p, -1);
		oldMonthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthlyWithDependencies(DateUtils.now(), bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), DateUtils.now(), 0, p, -1);
		oldAnnualCost = BenefitCostCalculator.calculateCostNewMethodAnnualWithDependencies(DateUtils.now(), bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), DateUtils.now(), 0, p, -1);
		newPppCost = BenefitCostCalculator.calculateCostNewMethodWithDependencies(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), asOfDate, 0, p, -1);
		newMonthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthlyWithDependencies(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), asOfDate, 0, p, -1);
		newAnnualCost = BenefitCostCalculator.calculateCostNewMethodAnnualWithDependencies(asOfDate, bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, bj.getAmountCovered(), asOfDate, 0, p, -1);

		coversEmployee = bc.getCoversEmployee();
		coversEmpSpouse = bc.getCoversEmployeeSpouse();
		coversNonEmpSpouse = bc.getSpouseNonEmployee();
		coversChildren = bc.getCoversChildren();
		coversEmpSpouseOrChildren = bc.getCoversEmployeeSpouseOrChildren();
		coversNonEmpSpouseOrChildren = bc.getSpouseNonEmpOrChildren();
		maxDependentsCovered = bc.getMaxChildren();
		spouseDeclinesCoverage = bc.getSpouseDeclinesExternalCoverage();
		currentEnrollmentReplaced = (new BHRBenefit(bj.getBenefitId()).getEndDate() < asOfDate && new BHRBenefit(bj.getBenefitId()).getReplacingBenefit() != null);
		if (bj.getEmployeeCovered().equals("N") && bj.getDependentBenefitJoins().size() > 0)
			bj = new BHRBenefitJoin(bj.getDependentBenefitJoins().get(0));
		coverageAmount = bj.getAmountCovered();
		actualPppCost = BenefitCostCalculator.calculateCostNewMethodWithDependencies(asOfDate, bc.getBean(), new BPerson(employeeId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, p, -1);
		actualMonthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthlyWithDependencies(asOfDate, bc.getBean(), new BPerson(employeeId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, p, -1);
		actualAnnualCost = BenefitCostCalculator.calculateCostNewMethodAnnualWithDependencies(asOfDate, bc.getBean(), new BPerson(employeeId).getPerson(), false, bj.getAmountCovered(), asOfDate, 0, p, -1);
	}

	public void setData(Boolean d) {
		benefitName = "";
		benefitId = "";
		configName = "";
		oldPppCost = 0;
		oldMonthlyCost = 0;
		oldAnnualCost = 0;
		newPppCost = 0;
		newMonthlyCost = 0;
		newAnnualCost = 0;
		coversEmployee = false;
		coversEmpSpouse = false;
		coversNonEmpSpouse = false;
		coversChildren = false;
		coversEmpSpouseOrChildren = false;
		coversNonEmpSpouseOrChildren = false;
		maxDependentsCovered = 0;
		spouseDeclinesCoverage = false;
		currentEnrollmentReplaced = false;
		coverageAmount = 0;
		actualPppCost = 0;
		actualMonthlyCost = 0;
		actualAnnualCost = 0;
		decline = d;
	}

	public boolean getDecline() {
		return decline;
	}

	public void setDecline(boolean decline) {
		this.decline = decline;
	}

	public double getActualAnnualCost() {
		return actualAnnualCost;
	}

	public void setActualAnnualCost(double actualAnnualCost) {
		this.actualAnnualCost = actualAnnualCost;
	}

	public double getActualMonthlyCost() {
		return actualMonthlyCost;
	}

	public void setActualMonthlyCost(double actualMonthlyCost) {
		this.actualMonthlyCost = actualMonthlyCost;
	}

	public double getActualPppCost() {
		return actualPppCost;
	}

	public void setActualPppCost(double actualPppCost) {
		this.actualPppCost = actualPppCost;
	}

	public double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
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

	public double getOldPppCost() {
		return oldPppCost;
	}

	public void setOldPppCost(double oldPppCost) {
		this.oldPppCost = oldPppCost;
	}

	public double getOldMonthlyCost() {
		return oldMonthlyCost;
	}

	public void setOldMonthlyCost(double oldMonthlyCost) {
		this.oldMonthlyCost = oldMonthlyCost;
	}

	public double getOldAnnualCost() {
		return oldAnnualCost;
	}

	public void setOldAnnualCost(double oldAnnualCost) {
		this.oldAnnualCost = oldAnnualCost;
	}

	public double getNewPppCost() {
		return newPppCost;
	}

	public void setNewPppCost(double newPppCost) {
		this.newPppCost = newPppCost;
	}

	public double getNewMonthlyCost() {
		return newMonthlyCost;
	}

	public void setNewMonthlyCost(double newMonthlyCost) {
		this.newMonthlyCost = newMonthlyCost;
	}

	public double getNewAnnualCost() {
		return newAnnualCost;
	}

	public void setNewAnnualCost(double newAnnualCost) {
		this.newAnnualCost = newAnnualCost;
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

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public boolean getCurrentEnrollmentReplaced() {
		return currentEnrollmentReplaced;
	}

	public void setCurrentEnrollmentReplaced(boolean currentEnrollmentReplaced) {
		this.currentEnrollmentReplaced = currentEnrollmentReplaced;
	}
}
