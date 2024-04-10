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
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import java.util.List;

public class ListCategoryConfigsReturnItem {
	
	private String benefitName;
	private String benefitId;
	private String name;
	private String coverage;
	private double monthlyCost;
	private double pppCost;
	private double annualCost;
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

	public ListCategoryConfigsReturnItem() {
	}

	ListCategoryConfigsReturnItem(BHRBenefitConfig bc, String employeeId, int date) {
		BEmployee employee = new BEmployee(employeeId);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.getBean());

		//first see if they already have the benefit join
		HrBenefitJoin bj = hcu.first();

		//if so, get the costs from it
		if (bj != null) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			monthlyCost = MoneyUtils.parseMoney(bbj.getCalculatedCostMonthly());
		} else {
			List<IPerson> p = bc.estimateCoveredPeople(employee);
			monthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthly(date, bc.getBean(), ArahantSession.getHSU().get(Person.class, employeeId), false, 0, date, 0, p, -1, null);
		}

		pppCost = (monthlyCost * 12) / BEmployee.getPPY(employeeId);
		annualCost = monthlyCost * 12;

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
		benefitName = bc.getBenefitName();
		benefitId = bc.getBenefitId();
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
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

	public boolean isSelectPending() {
		return selectPending;
	}

	public void setSelectPending(boolean selectPending) {
		this.selectPending = selectPending;
	}
}
