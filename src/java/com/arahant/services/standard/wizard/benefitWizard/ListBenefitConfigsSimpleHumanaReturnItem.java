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

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class ListBenefitConfigsSimpleHumanaReturnItem {

	private String name;
	private String coverage;
	private double maxChooseAmount;
	private double minChooseAmount;
	private double maxChooseAmountStep;
	private double coverageAmount;
	private double monthlyCost;
	private double pppCost;
	private double annualCost;
	private double employerMonthlyCost;
	private double employerPppCost;
	private double employerAnnualCost;
	private String id;
	private boolean select;
	private boolean coversEmployee;
	private boolean coversEmpSpouse;
	private boolean coversNonEmpSpouse;
	private boolean coversChildren;
	private boolean coversEmpSpouseOrChildren;
	private boolean coversNonEmpSpouseOrChildren;
	private int maxDependentsCovered;
	private boolean spouseDeclinesCoverage;
	private boolean selectPending;
	private boolean selectCheckBox;

	public ListBenefitConfigsSimpleHumanaReturnItem() {
	}

	ListBenefitConfigsSimpleHumanaReturnItem(WizardConfigurationConfig wcc, String employeeId, int date) {
		BHRBenefitConfig bc = new BHRBenefitConfig(wcc.getBenefitConfig());
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();
		String state = employee.getState();
		if (StringUtils.isEmpty(state))
			employee.getCompany().getState();
		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			pppCost = 0.0;//MoneyUtils.parseMoney(bbj.getPayPeriodAmount());
			monthlyCost = 0.0;// BEmployee.getPPY(employeeId) * pppCost / 12;
			annualCost = 0.0;//monthlyCost * 12;
			employerPppCost = bbj.getBenefitConfig().getEmployerCost() / BEmployee.getPPY(bbj.getPayingPersonId());
			employerMonthlyCost = bbj.getBenefitConfig().getEmployerCost() / 12;
			employerAnnualCost = bbj.getBenefitConfig().getEmployerCost();
			coverageAmount = bbj.getAmountCovered();
		} else {
			List<IPerson> p = bc.estimateCoveredPeople(employee);

			pppCost = 0.0;//bc.getEmployeeCostPPP(employeeId, date, p);
			monthlyCost = 0.0;//BEmployee.getPPY(employeeId) * pppCost / 12;
			annualCost = 0.0;//monthlyCost * 12;
			employerPppCost = bc.getEmployerCost() / BEmployee.getPPY(employeeId);
			employerMonthlyCost = bc.getEmployerCost() / 12;
			employerAnnualCost = bc.getEmployerCost();
			coverageAmount = 0;
		}

		BHRBenefit bb = new BHRBenefit(bc.getBenefit());
		BWizardConfiguration bz = employee.getWizardConfiguration("E");
		//check if there are any dependent benefits that need to be auto enrolled that aren't in the wizard (the wizard takes care of auto enrolls too)
		List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, bb.getBean()).list();

		List<String> dependentBenefitIds = new ArrayList<String>();
		List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
		for (BenefitDependency bd : bds) {
			dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
			dependentBenefits.add(bd.getDependentBenefit());
		}

		//get the matching benefits that are already in the wizard
//		List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class)
//																.in(HrBenefit.BENEFITID, dependentBenefitIds)
//																.joinTo(HrBenefit.WIZARD_CONF_BENEFITS)
//																.joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY)
//																.eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean())
//																.list();
//
//		//remove those because the wizard will take care of these auto enrolls
//		dependentBenefits.removeAll(benefitsInWizard);
//
//		for (HrBenefit hrb : dependentBenefits)
//		{
//			BHRBenefit bb2 = new BHRBenefit(hrb);
//			HrBenefitConfig match = null;
//			List<HrBenefitConfig> hrbcl = bb2.getConfigs();
//			if(hrbcl.size() == 1)
//			{
//				match = hrbcl.get(0);
//			}
//			else
//			{
//				for(HrBenefitConfig newConfig : hrb.getBenefitConfigs())
//				{
//					if(bc.getBean().getEmployee() == newConfig.getEmployee() &&
//					   bc.getBean().getChildren() == newConfig.getChildren() &&
//					   bc.getBean().getSpouseEmployee() == newConfig.getSpouseEmployee() &&
//					   bc.getBean().getSpouseNonEmployee() == newConfig.getSpouseNonEmployee() &&
//					   bc.getBean().getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren() &&
//					   bc.getBean().getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren() &&
//					   bc.getBean().getMaxChildren() == newConfig.getMaxChildren())
//					{
//						match = newConfig;
//						break;
//					}
//				}
//			}
//			if(match != null)
//			{
//				//if this config covers more than just the employee, then go ahead and include all the enrollees, else only the employee
//				if(match.getChildren() == 'Y' || match.getSpouseEmployee() == 'Y' || match.getSpouseNonEmployee() == 'Y' ||
//				   match.getSpouseNonEmployee() == 'Y' || match.getSpouseEmpOrChildren() == 'Y' || match.getSpouseNonEmpOrChildren() == 'Y')
//				{
//					List<IPerson> plist = new BHRBenefitConfig(match).estimateCoveredPeople(employee);
//					pppCost += BenefitCostCalculator.calculateCostNewMethod(date, match, employee.getPerson(), false, 0, date, 0, plist);
//					monthlyCost += BenefitCostCalculator.calculateCostNewMethodMonthly(date, match, employee.getPerson(), false, 0, date, 0, plist);
//					annualCost += BenefitCostCalculator.calculateCostNewMethodAnnual(date, match, employee.getPerson(), false, 0, date, 0, plist);
//				}
//			}
//		}

		name = wcc.getName();
		coverage = bc.getCoverage();
		id = bc.getConfigId();
		BPerson bpp = new BPerson();
		bpp.loadPending(employeeId);
		select = bpp.enrolledInApprovedConfig(bc.getConfigId());
		selectPending = bpp.enrolledInConfig(bc.getConfigId());
		coversEmployee = bc.getCoversEmployee();
		coversEmpSpouse = bc.getCoversEmployeeSpouse();
		coversNonEmpSpouse = bc.getSpouseNonEmployee();
		coversChildren = bc.getCoversChildren();
		coversEmpSpouseOrChildren = bc.getCoversEmployeeSpouseOrChildren();
		coversNonEmpSpouseOrChildren = bc.getSpouseNonEmpOrChildren();
		maxDependentsCovered = bc.getMaxChildren();
		spouseDeclinesCoverage = bc.getSpouseDeclinesExternalCoverage();
		maxChooseAmount = bc.getMaxAmount();
		minChooseAmount = bc.getMinValue();
		maxChooseAmountStep = bc.getStepValue();
		selectCheckBox = false;
	}

	ListBenefitConfigsSimpleHumanaReturnItem(BHRBenefitConfig bc, String employeeId, int date) {
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();

		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			pppCost = MoneyUtils.parseMoney(bbj.getCalculatedCost());
			monthlyCost = BEmployee.getPPY(employeeId) * pppCost / 12;
//			monthlyCost=MoneyUtils.parseMoney(bbj.getMonthlyAmount());
			annualCost = monthlyCost * 12;
//			annualCost=bbj.getAmountPaidAnnual();
			employerPppCost = bbj.getBenefitConfig().getEmployerCost() / BEmployee.getPPY(bbj.getPayingPersonId());
			employerMonthlyCost = bbj.getBenefitConfig().getEmployerCost() / 12;
			employerAnnualCost = bbj.getBenefitConfig().getEmployerCost();
			coverageAmount = bbj.getAmountCovered();
		} else {
			List<IPerson> p = bc.estimateCoveredPeople(employee);

			pppCost = BenefitCostCalculator.calculateCostNewMethod(date, bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, 0, date, 0, p, -1, null);
			monthlyCost = BEmployee.getPPY(employeeId) * pppCost / 12;
//			monthlyCost=bc.getEmployeeCostMonthly(employeeId, date, p);
			annualCost = monthlyCost * 12;
//			annualCost=bc.getEmployeeCostAnnual(employeeId, date, p);
			employerPppCost = bc.getEmployerCost() / BEmployee.getPPY(employeeId);
			employerMonthlyCost = bc.getEmployerCost() / 12;
			employerAnnualCost = bc.getEmployerCost();
			coverageAmount = 0;
		}

		name = bc.getConfigName();
		coverage = bc.getCoverage();
		id = bc.getConfigId();
		BPerson bpp = new BPerson();
		bpp.loadPending(employeeId);
		select = bpp.enrolledInApprovedConfig(bc.getConfigId());
		selectPending = bpp.enrolledInConfig(bc.getConfigId());
		coversEmployee = bc.getCoversEmployee();
		coversEmpSpouse = bc.getCoversEmployeeSpouse();
		coversNonEmpSpouse = bc.getSpouseNonEmployee();
		coversChildren = bc.getCoversChildren();
		coversEmpSpouseOrChildren = bc.getCoversEmployeeSpouseOrChildren();
		coversNonEmpSpouseOrChildren = bc.getSpouseNonEmpOrChildren();
		maxDependentsCovered = bc.getMaxChildren();
		spouseDeclinesCoverage = bc.getSpouseDeclinesExternalCoverage();
		maxChooseAmount = bc.getMaxAmount();
		minChooseAmount = bc.getMinValue();
		maxChooseAmountStep = bc.getStepValue();
		selectCheckBox = false;
	}

	public double getMinChooseAmount() {
		return minChooseAmount;
	}

	public void setMinChooseAmount(double minChooseAmount) {
		this.minChooseAmount = minChooseAmount;
	}

	public double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public double getMaxChooseAmountStep() {
		return maxChooseAmountStep;
	}

	public void setMaxChooseAmountStep(double maxChooseAmountStep) {
		this.maxChooseAmountStep = maxChooseAmountStep;
	}

	public double getMaxChooseAmount() {
		return maxChooseAmount;
	}

	public void setMaxChooseAmount(double maxChooseAmount) {
		this.maxChooseAmount = maxChooseAmount;
	}

	public double getEmployerAnnualCost() {
		return employerAnnualCost;
	}

	public void setEmployerAnnualCost(double employerAnnualCost) {
		this.employerAnnualCost = employerAnnualCost;
	}

	public double getEmployerMonthlyCost() {
		return employerMonthlyCost;
	}

	public void setEmployerMonthlyCost(double employerMonthlyCost) {
		this.employerMonthlyCost = employerMonthlyCost;
	}

	public double getEmployerPppCost() {
		return employerPppCost;
	}

	public void setEmployerPppCost(double employerPppCost) {
		this.employerPppCost = employerPppCost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public double getMonthlyCost() {
		return monthlyCost;
	}

	public void setMonthlyCost(double monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean getCoversEmployee() {
		return coversEmployee;
	}

	public void setCoversEmployee(boolean coversEmployee) {
		this.coversEmployee = coversEmployee;
	}

	public boolean getCoversChildren() {
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

	public double getAnnualCost() {
		return annualCost;
	}

	public void setAnnualCost(double annualCost) {
		this.annualCost = annualCost;
	}

	public double getPppCost() {
		return pppCost;
	}

	public void setPppCost(double pppCost) {
		this.pppCost = pppCost;
	}

	public boolean enrolledInConfig(BHRBenefitConfig config, String employeeId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, config.getConfigId()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).exists();
	}

	public boolean getSelectPending() {
		return selectPending;
	}

	public void setSelectPending(boolean selectPending) {
		this.selectPending = selectPending;
	}

	public boolean getSelectCheckBox() {
		return selectCheckBox;
	}

	public void setSelectCheckBox(boolean selectCheckBox) {
		this.selectCheckBox = selectCheckBox;
	}
}
