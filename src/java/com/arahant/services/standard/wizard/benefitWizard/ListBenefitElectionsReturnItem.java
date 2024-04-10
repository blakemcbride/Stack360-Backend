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
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import com.arahant.utils.Utils;
import java.util.Calendar;
import java.util.List;

public class ListBenefitElectionsReturnItem {
	private String benefitName;
	private String selectedConfigName;
	private String effectiveDate;
	private double pppCost;
	private double monthlyCost;
	private double annualCost;
	private double coverageAnnual;
	private double coveragePpp;
	private double coverageMonthly;
	private String benefitId;
	private String dependentBenefitGroup;
	private boolean optional;

	public ListBenefitElectionsReturnItem() {
	}

	ListBenefitElectionsReturnItem(BHRBenefitJoin bj, BPerson bp, String empId, int date, List<String> allBenefitIds) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		benefitId = bj.getBenefitId();
		optional = true;
		//if this benefit is marked as a required rider for someone, you can't change it
		if (hsu.createCriteria(com.arahant.beans.BenefitRider.class).eq(com.arahant.beans.BenefitRider.RIDER_BENEFIT_ID, benefitId).eq(com.arahant.beans.BenefitRider.REQUIRED, 'Y').in(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, allBenefitIds).exists())
			optional = false;
		//if this benefit is marked as a required dependency for someone, you can't change it
		else if (hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT_ID, benefitId).eq(BenefitDependency.REQUIRED, 'Y').in(BenefitDependency.REQUIRED_BENEFIT_ID, allBenefitIds).exists())
			optional = false;

		if (!bj.isDecline()) {
			benefitName = bj.getBenefitName();
			BHRBenefitConfig bbc = bj.getBenefitConfig();

			BHRBenefit bene = new BHRBenefit(bj.getBenefitId());
			if (bene.getBean().getShowAllCoverages() == 'Y')
				selectedConfigName = bj.getCoveredNameLFM();
			else
				selectedConfigName = bj.getBenefitConfigName();

			//BWizardConfiguration bwc = bp.getBEmployee().getWizardConfiguration("E");
//			List<HrBenefit> benesInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.WIZARD_CONF_BENEFITS)
//																	.joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY)
//																	.eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bwc.getBean())
//																	.list();

			int effDate = bj.getPolicyStartDate();
			int today = DateUtils.today();
			int coverageStartDate = bj.getCoverageStartDate();
			if (bj.getEmployeeCovered().equals("N"))
				for (HrBenefitJoin depbj : bj.getDependentBenefitJoins())
					coverageStartDate = depbj.getCoverageStartDate();
			BEmployee employee = new BEmployee(empId);
			int hireDate = employee.getCurrentHiredDate();
			Calendar cal = DateUtils.getCalendar(hireDate);
			if (!bj.getBenefitApproved()) {
				switch (bene.getEligibilityType()) {
					case 1:
						effDate = coverageStartDate > today ? coverageStartDate : today;
						break;
					case 2:
						effDate = DateUtils.getStartOfMonthAfter(hireDate, bene.getEligibilityPeriod());
						break;
					case 3:
						cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + bene.getEligibilityPeriod());
						effDate = DateUtils.getStartOfMonthAfter(DateUtils.getDate(cal), 0);
						break;
				}
				if (effDate > coverageStartDate)
					effectiveDate = DateUtils.getDateFormatted(effDate);
				else
					effectiveDate = DateUtils.getDateFormatted(coverageStartDate);
			} else
				effectiveDate = "";

			//type 0=ppp, 1=monthly, 2=yearly
			//		pppCost=bp.getPendingBenefitTotalCost(benefitName, 0, date);
			//		monthlyCost=bp.getPendingBenefitTotalCost(benefitName, 1, date);
			//		annualCost=bp.getPendingBenefitTotalCost(benefitName, 2, date);

			@SuppressWarnings("unchecked")
			List<IPerson> coveredPeople = (List) hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson().getPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bbc.getBean()).eq(HrBenefitJoin.APPROVED, bj.getBean().getBenefitApproved()).ne(HrBenefitJoin.COVERAGE_START_DATE, 0) //.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, date)
					.list();

			if (coveredPeople.isEmpty()) //TODO: WHY does this happen?
				coveredPeople.add(bj.getCoveredPerson().getPerson());

			if (bj.getEmployeeCovered().equals("N") && bj.getDependentBenefitJoins().size() > 0)
				bj = new BHRBenefitJoin(bj.getDependentBenefitJoins().get(0));
			
			
			
			annualCost = BenefitCostCalculator.calculateEmployeeAnnualCost(bbc, employee, 0, today, bj.getAmountCovered());
			int ppy = BEmployee.getPPY(bj.getPayingPersonId());
			pppCost = annualCost / ppy;
			monthlyCost = annualCost / 12;		
			coverageAnnual = bj.getAmountCovered();
			coveragePpp = 0;//bj.getAmountCovered() / BEmployee.getPPY(empId, date);
			coverageMonthly = 0;//bj.getAmountCovered() / 12;
		} else {
			benefitName = bj.getBenefitName();
			selectedConfigName = "DECLINED";
			pppCost = 0;
			monthlyCost = 0;
			annualCost = 0;
			coverageAnnual = 0;
			coveragePpp = 0;
			coverageMonthly = 0;
			effectiveDate = "";
		}

		if (!StringUtils.isEmpty(bj.getBenefitId())) {  //if an enrollment or benefit decline
			//am i a benefit that is dependent on another?
			for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereDependent(new BHRBenefit(bj.getBenefitId())))
				//do i have an unapproved enrollment in this requirement?
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).exists())
					dependentBenefitGroup = bd.getRequiredBenefit().getName();
				//do i have an approved enrollment and not an unapproved decline for my requirement?
				else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).exists()
						&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.HRBENEFIT, bd.getRequiredBenefit()).exists())
					dependentBenefitGroup = bd.getRequiredBenefit().getName();
			if (StringUtils.isEmpty(dependentBenefitGroup))
				//lets check if I have any benefits that require me
				for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(new BHRBenefit(bj.getBenefitId())))
					//do i have an unapproved enrollment in my dependent benefit?
					if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getDependentBenefit()).exists())
						dependentBenefitGroup = bd.getRequiredBenefit().getName();
					//do i have an approved enrollment and not an unapproved decline for my requirement?
					else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getDependentBenefit()).exists()
							&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.HRBENEFIT, bd.getDependentBenefit()).exists())
						dependentBenefitGroup = bd.getRequiredBenefit().getName();

			if (StringUtils.isEmpty(dependentBenefitGroup)) {
				HrBenefitJoin br = bj.getRiderJoin();
				if (br != null)
					dependentBenefitGroup = new BHRBenefitJoin(br).getBenefitName();
				else
					for (com.arahant.beans.BenefitRider bd : BBenefitRider.getRidersForBaseBenefit(new BHRBenefit(bj.getBenefitId())))
						//do i have an unapproved enrollment in my dependent benefit?
						if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRiderBenefit()).exists())
							dependentBenefitGroup = bd.getBaseBenefit().getName();
						//do i have an approved enrollment and not an unapproved decline for my requirement?
						else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRiderBenefit()).exists()
								&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.HRBENEFIT, bd.getRiderBenefit()).exists())
							dependentBenefitGroup = bd.getBaseBenefit().getName();
			}
		} else { //it must be a category decline
			BHRBenefitCategory bcat = new BHRBenefitCategory(bj.getBenefitCategoryId());
			dependentBenefitGroup = bcat.getDescription();

			BWizardConfiguration bwc = new BEmployee(bj.getPayingPersonId()).getWizardConfiguration("E");
			for (WizardConfigurationCategory wcc : bwc.getWizardCategories())
				if (wcc.getBenefitCategory().getBenefitCatId().equals(bcat.getCategoryId())) {
					BWizardConfigurationCategory bwizCat = new BWizardConfigurationCategory(wcc);
					if (bwizCat.getWizardBenefits().size() > 0)
						benefitId = bwizCat.getWizardBenefits().get(0).getBenefit().getBenefitId();
				}
		}
	}

	public boolean getOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getDependentBenefitGroup() {
		return dependentBenefitGroup;
	}

	public void setDependentBenefitGroup(String dependentBenefitGroup) {
		this.dependentBenefitGroup = dependentBenefitGroup;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getSelectedConfigName() {
		return selectedConfigName;
	}

	public void setSelectedConfigName(String selectedConfigName) {
		this.selectedConfigName = selectedConfigName;
	}

	public double getPppCost() {
		return pppCost;
	}

	public void setPppCost(double pppCost) {
		this.pppCost = pppCost;
	}

	public double getMonthlyCost() {
		return monthlyCost;
	}

	public void setMonthlyCost(double monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	public double getAnnualCost() {
		return annualCost;
	}

	public void setAnnualCost(double annualCost) {
		this.annualCost = annualCost;
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

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
