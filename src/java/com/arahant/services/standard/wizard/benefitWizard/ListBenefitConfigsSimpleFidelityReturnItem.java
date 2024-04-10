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

import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import org.kissweb.StringUtils;
import com.arahant.utils.Utils;

public class ListBenefitConfigsSimpleFidelityReturnItem {

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

	public ListBenefitConfigsSimpleFidelityReturnItem() {
	}

	ListBenefitConfigsSimpleFidelityReturnItem(WizardConfigurationConfig wcc, String employeeId, int date) {
		BHRBenefitConfig bc = new BHRBenefitConfig(wcc.getBenefitConfig());
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();
		String state = employee.getState();
		if (StringUtils.isEmpty(state))
			employee.getCompany().getState();

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

		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

			coverageAmount = bbj.getAmountCovered();
		} else if (Utils.doubleEqual(bc.getMaxAmount(), bc.getMinValue(), 0.001) && Utils.doubleEqual(bc.getMaxAmount(), bc.getStepValue(), 0.001) && !Utils.doubleEqual(bc.getMaxAmount(), 0, 0.001))
			coverageAmount = bc.getMaxAmount();
		else
			coverageAmount = 0;
	}

	ListBenefitConfigsSimpleFidelityReturnItem(BHRBenefitConfig bc, String employeeId, int date, boolean dependentBenefit) {
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();
		String state = employee.getState();
		if (StringUtils.isEmpty(state))
			employee.getCompany().getState();

		name = bc.getName();
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

		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			coverageAmount = bbj.getAmountCovered();
		} else if (Utils.doubleEqual(bc.getMaxAmount(), bc.getMinValue(), 0.001) && Utils.doubleEqual(bc.getMaxAmount(), bc.getStepValue(), 0.001) && !Utils.doubleEqual(bc.getMaxAmount(), 0, 0.001))
			coverageAmount = bc.getMaxAmount();
		else
			coverageAmount = 0;
	}

	ListBenefitConfigsSimpleFidelityReturnItem(BHRBenefitConfig bc, String employeeId, int date) {
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();

		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			coverageAmount = bbj.getAmountCovered();
		} else
			coverageAmount = 0;

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
