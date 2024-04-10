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
import com.arahant.beans.HrEmplDependentWizard;
import com.arahant.business.BBenefitEnrollments;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.MoneyUtils;

public class ListDependentsAndEmployeeSimpleFidelityReturnItem {

	private boolean select;
	private String personId;
	private String dependentId;
	private String lastName;
	private String firstName;
	private String middleName;
	private String relationship;
	private String sex;
	private String ssn;
	private int age;
	private boolean selectPending;
	private boolean selectApproved;
	private double maxCoverage;
	private double minCoverage;
	private String coverageAmount = "$0.00";
	private String pppCostAmount = "$0.00";
	private String monthlyCostAmount = "$0.00";
	private String annualCostAmount = "$0.00";

	public ListDependentsAndEmployeeSimpleFidelityReturnItem() {
	}

	ListDependentsAndEmployeeSimpleFidelityReturnItem(HrEmplDependentWizard ew, String benefitId, String categoryId) {
		if (ew.getRecordType() == 'C') {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			dependentId = bc.getDependentId();
			personId = bc.getPerson().getPersonId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();
			select = false;
			selectPending = false;
			selectApproved = false;

			if (categoryId != null && !categoryId.equals("")) {
				selectPending = bc.enrolledInCategory(categoryId);
				selectApproved = bc.enrolledInApprovedCategory(categoryId);
			} else if (benefitId != null && !benefitId.equals("")) {
				select = bc.enrolledInBenefit(benefitId);
				if (!select)
					select = bc.enrolledInApprovedBenefit(benefitId);
				selectPending = bc.enrolledInBenefit(benefitId);
				selectApproved = bc.enrolledInApprovedBenefit(benefitId);
			}

			try {
				if (bc.getDob() > 0)
					age = Integer.parseInt(BPerson.getAge(bc.getDob()));
				else
					age = -1;
			} catch (Exception e) {
				age = -1;
			}
			maxCoverage = 25000;
			minCoverage = 1000;
			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, bc.getPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT_ID, benefitId).first();
			if (bj != null) {
				coverageAmount = MoneyUtils.formatMoney(bj.getAmountCovered());
				pppCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostPPP());
				monthlyCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostMonthly());
				annualCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostAnnual());
			}
		} else {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			personId = bc.getPerson().getPersonId();
			dependentId = bc.getDependentId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();
			select = false;
			selectPending = false;
			selectApproved = false;

			if (categoryId != null && !categoryId.equals("")) {
				selectPending = bc.enrolledInCategory(categoryId);
				selectApproved = bc.enrolledInApprovedCategory(categoryId);
			} else if (benefitId != null && !benefitId.equals("")) {
				select = bc.enrolledInBenefit(benefitId);
				if (!select)
					select = bc.enrolledInApprovedBenefit(benefitId);
				selectPending = bc.enrolledInBenefit(benefitId);
				selectApproved = bc.enrolledInApprovedBenefit(benefitId);
			}

			try {
				if (bc.getDob() > 0)
					age = Integer.parseInt(BPerson.getAge(bc.getDob()));
				else
					age = -1;
			} catch (Exception e) {
				age = -1;
			}
			maxCoverage = 25000;
			minCoverage = 1000;

			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, bc.getPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT_ID, benefitId).first();
			if (bj != null) {
				coverageAmount = MoneyUtils.formatMoney(bj.getAmountCovered());
				pppCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostPPP());
				monthlyCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostMonthly());
				annualCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostAnnual());
			}
		}
	}

	ListDependentsAndEmployeeSimpleFidelityReturnItem(BPerson bpp, BEmployee be, String benefitId, String categoryId) {
		personId = be.getPersonId();
		lastName = be.getLastName();
		firstName = be.getFirstName();
		middleName = be.getMiddleName();
		relationship = "Employee";
		sex = be.getSex();
		ssn = be.getSsn();
		select = false;
		selectPending = false;
		selectApproved = false;

		if (categoryId != null && !categoryId.equals("")) {
			selectPending = BBenefitEnrollments.enrolledInCategory(categoryId, new String[]{be.getPersonId()});
			selectApproved = BBenefitEnrollments.enrolledInApprovedCategory(categoryId, new String[]{be.getPersonId()});
		} else if (benefitId != null && !benefitId.equals("")) {
			select = BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{be.getPersonId()});
			if (!select)
				select = BBenefitEnrollments.enrolledInApprovedBenefit(benefitId, new String[]{be.getPersonId()});
			selectPending = BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{be.getPersonId()});
			selectApproved = BBenefitEnrollments.enrolledInApprovedBenefit(benefitId, new String[]{be.getPersonId()});
		}

		try {
			if (bpp.getDob() > 0)
				age = Integer.parseInt(BPerson.getAge(bpp.getDob()));
			else
				age = -1;
		} catch (Exception e) {
			age = -1;
		}
		maxCoverage = 25000;
		minCoverage = 1000;
		HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT_ID, benefitId).first();
		if (bj != null) {
			coverageAmount = MoneyUtils.formatMoney(bj.getAmountCovered());
			pppCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostPPP());
			monthlyCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostMonthly());
			annualCostAmount = MoneyUtils.formatMoney(bj.getCalculatedCostAnnual());
		}
	}

	public String getDependentId() {
		return dependentId;
	}

	public void setDependentId(String dependentId) {
		this.dependentId = dependentId;
	}

	public String getAnnualCostAmount() {
		return annualCostAmount;
	}

	public void setAnnualCostAmount(String annualCostAmount) {
		this.annualCostAmount = annualCostAmount;
	}

	public String getMonthlyCostAmount() {
		return monthlyCostAmount;
	}

	public void setMonthlyCostAmount(String monthlyCostAmount) {
		this.monthlyCostAmount = monthlyCostAmount;
	}

	public String getPppCostAmount() {
		return pppCostAmount;
	}

	public void setPppCostAmount(String pppCostAmount) {
		this.pppCostAmount = pppCostAmount;
	}

	public String getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(String coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public double getMaxCoverage() {
		return maxCoverage;
	}

	public void setMaxCoverage(double maxCoverage) {
		this.maxCoverage = maxCoverage;
	}

	public double getMinCoverage() {
		return minCoverage;
	}

	public void setMinCoverage(double minCoverage) {
		this.minCoverage = minCoverage;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean getSelectApproved() {
		return selectApproved;
	}

	public void setSelectApproved(boolean selectApproved) {
		this.selectApproved = selectApproved;
	}

	public boolean getSelectPending() {
		return selectPending;
	}

	public void setSelectPending(boolean selectPending) {
		this.selectPending = selectPending;
	}
}
