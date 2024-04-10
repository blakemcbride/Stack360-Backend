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


package com.arahant.beans;

import com.arahant.business.*;
import com.arahant.calculations.FidelityLifeCostCalculator;
import com.arahant.calculations.HumanaCostCalculator;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.lisp.LispPackage;
import com.arahant.utils.*;
import org.kissweb.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @see HumanaCostCalculator
 */
public class BenefitCostCalculator {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitCostCalculator.class);

	/**
	 * Finds the appropriate BenefitConfigCost record that applies to this
	 * Employee, and returns the maximum allowed monthly amount
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @return	Returns the max monthly allowed coverage amount
	 */
	public static double getMaxMonthlyAmount(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate) {
		BPerson bp = new BPerson(payingPerson);
		BenefitConfigCost c = null;

		if (usingCobra)
			c = findCobraCost(asOfDate, hrBenefitConfig);

		if (c == null) {

			List<OrgGroup> orgs = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, payingPerson).list();

			if (!orgs.contains(bp.getCompany().getBean()))
				orgs.add(bp.getCompany().getBean());

			//look for costs for my status
			if (bp.isEmployee()) {
				BEmployee bemp = bp.getBEmployee();
				HrEmplStatusHistory hist = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, bemp.getEmployee()).le(HrEmplStatusHistory.EFFECTIVEDATE, asOfDate).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

				if (hist != null)
					c = findStatusSpecificCost(orgs, asOfDate, hist.getHrEmployeeStatus(), hrBenefitConfig);
			}
			if (c == null)
				c = findCost(orgs, asOfDate, hrBenefitConfig);
		}
		if (c == null)
			return 0;
		return c.getConfig().deprecatedGetMaxAmount() / 12;
	}

	/**
	 * Calculates the pay period cost of the Benefit Configuration and includes
	 * the cost of all auto-enrolled benefit dependencies that are not in the
	 * wizard as well
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	benefit change reason type
	 * @return	Returns pay period cost
	 */
	public static double calculateCostNewMethodWithDependencies(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {

		if (true)
			return calculateCostNewMethodWithHiddenEnrollments(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType);
		BEmployee bemp = new BEmployee(payingPerson.getPersonId());
		BWizardConfiguration bz = bemp.getWizardConfiguration("E");
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());
		List<HrBenefit> reqs = BBenefitDependency.getRequirements(bb);
		double cost = 0;
		if (reqs.isEmpty()) {
			//cost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(hrBenefitConfig, hrBenefitConfig.getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
			cost = calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
			List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, bb.getBean()).list();
			List<String> dependentBenefitIds = new ArrayList<String>();
			List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
			for (BenefitDependency bd : bds) {
				dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
				dependentBenefits.add(bd.getDependentBenefit());
			}
			//get the matching benefits that are already in the wizard
			List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();
			//remove those because the wizard will take care of these auto enrolls
			dependentBenefits.removeAll(benefitsInWizard);
			for (HrBenefit b : dependentBenefits) {
				BHRBenefit bb2 = new BHRBenefit(b);
				HrBenefitConfig match = null;
				List<HrBenefitConfig> hrbcl = bb2.getConfigs();
				if (hrbcl.size() == 1)
					match = hrbcl.get(0);
				else
					for (HrBenefitConfig newConfig : b.getBenefitConfigs())
						if (hrBenefitConfig.getEmployee() == newConfig.getEmployee() && hrBenefitConfig.getChildren() == newConfig.getChildren() && hrBenefitConfig.getSpouseEmployee() == newConfig.getSpouseEmployee() && hrBenefitConfig.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee() && hrBenefitConfig.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren() && hrBenefitConfig.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren() && hrBenefitConfig.getMaxChildren() == newConfig.getMaxChildren()) {
							match = newConfig;
							break;
						}
				if (match != null) {
					//double humanaCost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(match, bb.getBenefitId(), payingPerson.getPersonId(), new BHRBenefitConfig(match).estimateCoveredPeople(bemp), amountCovered);
					double humanaCost = calculateCostNewMethod(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
					if (!Utils.doubleEqual(humanaCost, 0.0, .0001))
						cost += humanaCost;
				}
			}
		} else {
			HrBenefitJoin bj = null;
			for (HrBenefit b : reqs)
				if (bj == null)
					bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();
			if (bj == null && !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.HRBENEFIT, reqs).exists() && !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, reqs.get(0).getHrBenefitCategory()).exists())
				bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).in(HrBenefitConfig.HR_BENEFIT, reqs).first();
			if (bj != null) {
				List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT_ID, bj.getHrBenefitConfig().getBenefitId()).list();
				List<String> dependentBenefitIds = new ArrayList<String>();
				for (BenefitDependency bd : bds)
					dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
				//get the matching benefits that are already in the wizard
				List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();
				if (benefitsInWizard.contains(hrBenefitConfig.getHrBenefit()))
					//cost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(hrBenefitConfig, bj.getHrBenefitConfig().getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
					cost = calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bj.getHrBenefitConfig().getHrBenefit().getInternalId());
			}
		}
		if (!Utils.doubleEqual(cost, 0, 0.001))
			return cost;
		return calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, null);
	}

	public static double calculateCostNewMethodWithHiddenEnrollments(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {

		BEmployee bemp = new BEmployee(payingPerson.getPersonId());
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());
		double cost = 0;
		BenefitRider br = bb.getRiderEnrollment(payingPerson);
		String grandParentBenefitInternalId = null;
		if (br != null) {
			grandParentBenefitInternalId = br.getBaseBenefit().getInternalId();
			if (br.getRequired() == 'N') //Required Costs are included in base benefit
			{
				double tempCost = 0;
				tempCost += calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
				if (Utils.doubleEqual(tempCost, 0, 0.001)) {
					BHRBenefit bb2 = new BHRBenefit(br.getBaseBenefit());
					br = bb2.getRiderEnrollment(payingPerson);
					if (br != null && br.getRequired() == 'N')
						cost += calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				} else
					cost += tempCost;
			}
		} else {
			BenefitDependency bd = bb.getDependencyEnrollment(payingPerson);
			if (bd != null) {
				grandParentBenefitInternalId = bd.getRequiredBenefit().getInternalId();
				if (bd.getRequired() == 'N') //Required Costs are included in base benefit
				{
					double tempCost = 0;
					tempCost += calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
					if (Utils.doubleEqual(tempCost, 0, 0.001)) {
						BHRBenefit bb2 = new BHRBenefit(bd.getRequiredBenefit());
						bd = bb2.getDependencyEnrollment(payingPerson);
						if (bd != null && bd.getRequired() == 'N')
							cost += calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					} else
						cost += tempCost;
				}
			} else
				cost += calculateCostNewMethod(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
		}

		for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bb)) {
			BBenefitDependency bbd = new BBenefitDependency(bd);
			HrBenefit b = bbd.getDependentBenefit();
			int payerAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
			//only include in calculations if this is required and hidden (those are the auto-enrolls)
			if (!bbd.getRequiredBoolean() || (b.getMaxAge() != 0 && (payerAge > b.getMaxAge() || payerAge < b.getMinAge())))
				continue;
			BHRBenefit bb2 = new BHRBenefit(bd.getDependentBenefit());
			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethod(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethod(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}
		for (BenefitRider br2 : bb.getBenefitRiders()) {
			BBenefitRider bbr = new BBenefitRider(br2);
			//only include in calculations if this is required and hidden (those are the auto-enrolls)
			if (!bbr.getRequiredBoolean())
				continue;
			BHRBenefit bb2 = new BHRBenefit(bbr.getRiderBenefit());

			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethod(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethod(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}
		return cost;
	}

	public static double calculateCostNewMethod(HrBenefitJoin hrBenefitJoin, int asOfDate) {
		if (hrBenefitJoin.getHrBenefitConfig() == null)
			return 0;

		@SuppressWarnings("unchecked")
		List<IPerson> coveredPeople = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.PAYING_PERSON, hrBenefitJoin.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, hrBenefitJoin.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitJoin.getHrBenefitConfig()).eq(HrBenefitJoin.APPROVED, hrBenefitJoin.getBenefitApproved()).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).eq(HrBenefitJoin.EMPLOYEE_COVERED, 'Y').list();

		if (coveredPeople.isEmpty())
			coveredPeople.add(hrBenefitJoin.getPayingPerson());
		//return calculateCostNewMethod(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA() == 'Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1, null);
		return calculateCostNewMethodWithHiddenEnrollments(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA() == 'Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1);
	}

	/**
	 * Method that calculates the pay period cost for a real or proposed
	 * enrollment
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	Benefit Change Reason type
	 * @return	Returns pay period cost
	 */
	public static double calculateCostNewMethod(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType, String requiredBenefitInternalId) {

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (hrBenefitConfig == null)
			return 0;

		int ppy = BEmployee.getPPY(payingPerson.getPersonId(), asOfDate);
		int customCalc = BProperty.getInt("CustomCalculations");
		switch (customCalc) {
			case 1:
				if (!StringUtils.isEmpty(requiredBenefitInternalId)) {
					double cost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(hrBenefitConfig, requiredBenefitInternalId, payingPerson.getPersonId(), coveredPeople, amountCovered) * 52 / ppy;
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost;
				}
				break;
			case 2:
				//if(!StringUtils.isEmpty(hrBenefitConfig.getHrBenefit().getInternalId()) && hrBenefitConfig.getHrBenefit().getInternalId().contains("LBT"))
				//{
				HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
				List<String> peopleIds = new ArrayList<String>();
				for (IPerson p : coveredPeople)
					peopleIds.add(p.getPersonId());
				try {
					double cost;
					if (FidelityLifeCostCalculator.MAD.contains(requiredBenefitInternalId))
						cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.WEEKLY, false);
					else
						cost = FidelityLifeCostCalculator.calculateTotalPremium(bj.getBenefitJoinId(), null, FidelityLifeCostCalculator.WEEKLY);
					//double cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.WEEKLY, false);
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost * 52.0 / ppy;
				} catch (Exception ex) {
					return 0;
				}
				//}
				break;
		}

		//handle custom costs for humana
//		BHRBenefitConfig bbc = new BHRBenefitConfig(hrBenefitConfig);
//		HrBenefitJoin requiredJoin = null;
//		for(HrBenefit b : BBenefitDependency.getRequirements(new BHRBenefit(bbc.getBenefitId())))
//		{
//			//get the unapproved enrollment
//			requiredJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();
//			//if that doesnt work, get the approved enrollment if there is no decline for the benefit or category
//			if(requiredJoin == null && !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HRBENEFIT, b).exists()
//									&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY_ID, b.getHrBenefitCategoryId()).exists())
//				requiredJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();
//		}
//		if(requiredJoin != null)
//		{
//			double humanaCost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(hrBenefitConfig, requiredJoin.getHrBenefitConfig().getHrBenefit().getInternalId(), payingPerson.getPersonId(),coveredPeople, amountCovered);
//			if(!Utils.doubleEqual(humanaCost, 0.0, .0001))
//			{
//				int ppy = BEmployee.getPPY(payingPerson.getPersonId(), asOfDate);
//				return humanaCost * (52.0/ppy);
//			}
//		}
//		else
//		{
//			double humanaCost = HumanaCostCalculator.calculateWeeklyPlanCostByBenefit(hrBenefitConfig, hrBenefitConfig.getHrBenefit().getInternalId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
//			if(!Utils.doubleEqual(humanaCost, 0.0, .0001))
//			{
//				int ppy = BEmployee.getPPY(payingPerson.getPersonId(), asOfDate);
//				return humanaCost * (52.0/ppy);
//			}
//		}

		if (hrBenefitConfig.getHrBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE)
			//HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
			if ((bcrType == HrBenefitChangeReason.QUALIFYING_EVENT || bcrType == HrBenefitChangeReason.NEW_HIRE) && DateUtils.getCalendar(policyStartDate).get(Calendar.YEAR) == DateUtils.getCalendar(DateUtils.now()).get(Calendar.YEAR))
				return amountCovered / new BEmployee(payingPerson.getPersonId()).getPPYremaining(policyStartDate);  //FLEX is always amount covered
			else
				return amountCovered / BEmployee.getPPY(payingPerson);

		if (asOfDate == 0)  //could be making a subtle bug, if you forget and needed a specific date
			asOfDate = DateUtils.now();

		if (coveredPeople == null)
			coveredPeople = new ArrayList<IPerson>();

		double cost = 0;

		//get base amount


		//work my way up my org group hierarchy

		BPerson bp = new BPerson(payingPerson);


		BenefitConfigCost bcc = null;



		if (usingCobra)
			bcc = findCobraCost(asOfDate, hrBenefitConfig);

		if (bcc == null) {
			List<OrgGroup> orgs;
			orgs = hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, payingPerson).list();
//			Set<OrgGroupAssociation> ogaSet = bp.getOrgGroupAssociations();
//			for(OrgGroupAssociation oga : ogaSet)
//				orgs.add(oga.getOrgGroup());

			if (orgs.isEmpty())
				orgs.add(payingPerson.getCompanyBase());

			//look for costs for my status
			if (bp.isEmployee()) {
				BEmployee bemp = bp.getBEmployee();
				HrEmplStatusHistory hist = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, bemp.getEmployee()).le(HrEmplStatusHistory.EFFECTIVEDATE, asOfDate).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

//				if (hist==null)
//					System.out.println("History not found for "+bemp.getEmployee().getPersonId()+" on date "+asOfDate);
//				else
//					System.out.println("History found for "+bemp.getEmployee().getPersonId()+" on date "+asOfDate);

				if (hist != null)
					bcc = findStatusSpecificCost(orgs, asOfDate, hist.getHrEmployeeStatus(), hrBenefitConfig);
			}
			if (bcc == null)
				bcc = findCost(orgs, asOfDate, hrBenefitConfig);
		}

		///////////////////////////////////////////////////////////////////////////////////
		/////////////////////// custom WMCO cost need to move later ///////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		if (customCalc == 3 && new BPerson(payingPerson).isEmployee()) {
			BEmployee be = new BEmployee(payingPerson.getPersonId());
			HrBenefitJoin tempBJ = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
			short tempAge = be.getAgeAsOf(asOfDate);
			int salaryAsOfDate = asOfDate;
			if (tempBJ != null) {
				tempAge = be.getAgeAsOf(tempBJ.getCoverageStartDate());
				salaryAsOfDate = tempBJ.getCoverageStartDate();
			}
			double customCost = getSTDCost(be.getSalaryAsOf(salaryAsOfDate), tempAge, hrBenefitConfig.getBenefitConfigId());
			if (!Utils.doubleEqual(customCost, 0, 0.001))
				//System.out.println("Returned STD PPP = " + MoneyUtils.formatMoney(customCost) + " for " + be.getNameFML());
				return customCost * 12.0 / BEmployee.getPPY(payingPerson);
		}

		///////////////////////////////////////////////////////////////////////////////////
		/////////////////////// custom WMCO cost //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////


		if (bcc == null)
			return 0;

		//now I found the one that applies, lets see what the base is
		
		
		//  Blake
		if (ppy == 0)
			return 0.0;
		List<HrBenefitJoin> bj2 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).list();
		double coverageAmount = 0.0;
		for (HrBenefitJoin bj3 : bj2)
			coverageAmount += bj3.getAmountCovered();
		cost = calculateEmployeeAnnualCost(new BHRBenefitConfig(hrBenefitConfig), new BEmployee(payingPerson.getPersonId()), policyStartDate, policyStartDate, coverageAmount);
		if (true)
			return Utils.round(cost / ppy, 2);
		
		

		switch (bcc.deprecatedGetBaseAmountSource()) {
			case BenefitConfigCost.SOURCE_BASE:
				cost = bcc.deprecatedGetBaseAmount();
				break;
			case BenefitConfigCost.SOURCE_COVERAGE:
				//if there is a coverage amount, use it
				if (!Utils.doubleEqual(amountCovered, 0, 0.001))
					cost = amountCovered;
				else //is this a spouse or child only coverage?  need to find that coverage amount
				{
					Character empCovered = hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.EMPLOYEE_COVERED).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).charVal();
					if (empCovered == 'N')
						for (IPerson cp : coveredPeople) {
							amountCovered = hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.AMOUNT_COVERED).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, cp.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).doubleVal();
							if (!Utils.doubleEqual(amountCovered, 0, 0.001)) {
								cost = amountCovered;
								break;
							}
						}
				}
				break;
			case BenefitConfigCost.SOURCE_ANNUAL_PAY: {

				if (bp.isEmployee()) {
					BEmployee bemp = bp.getBEmployee();
					cost = bemp.getSalaryAsOf(asOfDate);
				} else
					throw new ArahantWarning("Benefit cost for " + hrBenefitConfig.getName() + " is calculated based on annual pay,\n"
							+ "but " + bp.getNameLFM() + " is not an employee.");
				break;
			}
			default:
				throw new ArahantException("Unknown benefit source type found.");
		}

		//cap it
		if (bcc.deprecatedGetBaseCapAmount() > 0 && cost > bcc.deprecatedGetBaseCapAmount())
			cost = bcc.deprecatedGetBaseCapAmount();


		//now I need to round that to nearest?

		if (bcc.deprecatedGetBaseRoundAmount() > .01) {
			long v = (long) ((cost + .005) * 100);
			v = (long) (v / (bcc.deprecatedGetBaseRoundAmount() * 100));
			v = (long) (v * (bcc.deprecatedGetBaseRoundAmount() * 100));
			cost = v / 100.0;
		}

		//handle multiplier
		switch (bcc.deprecatedGetMultiplierSource()) {
			case BenefitConfigCost.MULTIPLIER_COLUMN:
				cost *= bcc.deprecatedGetMultiplier();
				if (bcc.deprecatedGetCostPerEnrollee() == 'Y')
					cost *= coveredPeople.size();
				break;
			case BenefitConfigCost.MULTIPLIER_AGE_TABLE: {
				//loop for everyone enrolled 
				double base = cost;
				cost = 0;
				for (IPerson cp : coveredPeople) {
					HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, cp.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
					BenefitConfigCostAge age = null;
					Person p = hsu.get(Person.class, cp.getPersonId());
					if (p != null) {
						BPerson bcp = new BPerson(p);
						short personAge = bcp.getAgeAsOf(asOfDate);
						if (bj != null && bcc.getAgeCalcType() == 'S')
							personAge = bcp.getAgeAsOf(bj.getCoverageStartDate());
						else if (bcc.getAgeCalcType() == 'J') {
							Calendar janFirst = DateUtils.getCalendar(asOfDate);
							janFirst.set(Calendar.MONTH, Calendar.JANUARY);
							janFirst.set(Calendar.DAY_OF_MONTH, 1);
							personAge = bcp.getAgeAsOf(DateUtils.getDate(janFirst));
						}
						age = hsu.createCriteria(BenefitConfigCostAge.class).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, bcc).gt(BenefitConfigCostAge.AGE, personAge).orderBy(BenefitConfigCostAge.AGE).first();
						if (age == null)
							throw new ArahantWarning("Benefit " + hrBenefitConfig.getName() + " uses age in cost calculation.\n"
									+ "Age " + bcp.getAge() + " of " + bcp.getNameLFM() + " exceeds all ages in cost age table.");

					} else {
						Person pp = hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, cp.getPersonId()).first();
						if (pp != null) {
							BPerson bcp = new BPerson(pp);
							short personAge = bcp.getAgeAsOf(asOfDate);
							if (bj != null && bcc.getAgeCalcType() == 'S')
								personAge = bcp.getAgeAsOf(bj.getCoverageStartDate());
							else if (bcc.getAgeCalcType() == 'J') {
								Calendar janFirst = DateUtils.getCalendar(asOfDate);
								janFirst.set(Calendar.MONTH, Calendar.JANUARY);
								janFirst.set(Calendar.DAY_OF_MONTH, 1);
								personAge = bcp.getAgeAsOf(DateUtils.getDate(janFirst));
							}
							age = hsu.createCriteria(BenefitConfigCostAge.class).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, bcc).gt(BenefitConfigCostAge.AGE, personAge).orderBy(BenefitConfigCostAge.AGE).first();
							if (age == null)
								throw new ArahantWarning("Benefit " + hrBenefitConfig.getName() + " uses age in cost calculation.\n"
										+ "Age " + bcp.getAgeAsOf(asOfDate) + " of " + bcp.getNameLFM() + " exceeds all ages in cost age table.");
						}

					}
					cost += getOverrideAgeCost(bj, base, age);

				}
				break;
			}
			default:
				throw new ArahantException("Unknown multiplier source found.");
		}

		//handle divider
		cost = cost / bcc.deprecatedGetDivider();

		//if this is a flex type, use pay period table to determine division
		if (hrBenefitConfig.getHrBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE) {
			//do I have pay periods set up - if employee
			if (bp.isEmployee()) {
				PaySchedule ps = bp.getBEmployee().getPaySchedule();
				//how many periods in the pay schedule until the end of the year?
				int periods = hsu.createCriteria(PaySchedulePeriod.class).eq(PaySchedulePeriod.PAY_SCHEDULE, ps).ge(PaySchedulePeriod.PERIOD_END, policyStartDate).le(PaySchedulePeriod.PERIOD_END, policyEndDate).count();
				if (periods > 0)
					return amountCovered / periods;
			}
			return amountCovered / ppy;
		}


		//return pay period cost
		if (usingCobra || ppy == 12) //prevent rounding issues
		
			return cost;

		if (ppy > 0) //prevent dividing by zero
			return cost * 12 / ppy;
		return 0;

	}

	/**
	 * Calculates the annual cost of the Benefit Configuration and includes the
	 * cost of all auto-enrolled benefit dependencies that are not in the wizard
	 * as well
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	benefit change reason type
	 * @return	Returns annual cost
	 */
	public static double calculateCostNewMethodAnnualWithDependencies(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {

		if (true)
			return calculateCostNewMethodAnnualWithHiddenEnrollments(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType);
		BEmployee bemp = new BEmployee(payingPerson.getPersonId());
		BWizardConfiguration bz = bemp.getWizardConfiguration("E");
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());

		List<HrBenefit> reqs = BBenefitDependency.getRequirements(bb);
		double cost = 0;
		if (reqs.isEmpty()) { //im a req benefit
			//cost = HumanaCostCalculator.calculateAnnualPlanCostByBenefit(hrBenefitConfig, hrBenefitConfig.getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
			cost = calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
			List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, bb.getBean()).list();

			List<String> dependentBenefitIds = new ArrayList<String>();
			List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
			for (BenefitDependency bd : bds) {
				dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
				dependentBenefits.add(bd.getDependentBenefit());
			}

			//get the matching benefits that are already in the wizard
			List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();

			//remove those because the wizard will take care of these auto enrolls
			dependentBenefits.removeAll(benefitsInWizard);
			for (HrBenefit b : dependentBenefits) {
				BHRBenefit bb2 = new BHRBenefit(b);
				HrBenefitConfig match = null;
				List<HrBenefitConfig> hrbcl = bb2.getConfigs();
				if (hrbcl.size() == 1)
					match = hrbcl.get(0);
				else
					for (HrBenefitConfig newConfig : b.getBenefitConfigs())
						if (hrBenefitConfig.getEmployee() == newConfig.getEmployee()
								&& hrBenefitConfig.getChildren() == newConfig.getChildren()
								&& hrBenefitConfig.getSpouseEmployee() == newConfig.getSpouseEmployee()
								&& hrBenefitConfig.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
								&& hrBenefitConfig.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
								&& hrBenefitConfig.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
								&& hrBenefitConfig.getMaxChildren() == newConfig.getMaxChildren()) {
							match = newConfig;
							break;
						}
				if (match != null) {
					//double humanaCost = HumanaCostCalculator.calculateAnnualPlanCostByBenefit(match, bb.getBenefitId(), payingPerson.getPersonId(), new BHRBenefitConfig(match).estimateCoveredPeople(bemp), amountCovered);
					double humanaCost = calculateCostNewMethodAnnual(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
					if (!Utils.doubleEqual(humanaCost, 0.0, .0001))
						cost += humanaCost;
				}
			}
		} else //im a dep benefit
		{
			HrBenefitJoin bj = null;
			for (HrBenefit b : reqs)
				if (bj == null)
					bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();
			if (bj == null && !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.HRBENEFIT, reqs).exists()
					&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, reqs.get(0).getHrBenefitCategory()).exists())
				bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).in(HrBenefitConfig.HR_BENEFIT, reqs).first();

			if (bj != null) {
				List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT_ID, bj.getHrBenefitConfig().getBenefitId()).list();

				List<String> dependentBenefitIds = new ArrayList<String>();
				for (BenefitDependency bd : bds)
					dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());

				//get the matching benefits that are already in the wizard
				List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();
				if (benefitsInWizard.contains(hrBenefitConfig.getHrBenefit()))
					//cost = HumanaCostCalculator.calculateAnnualPlanCostByBenefit(hrBenefitConfig, bj.getHrBenefitConfig().getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
					cost = calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bj.getHrBenefitConfig().getHrBenefit().getInternalId());
			}
		}
		if (!Utils.doubleEqual(cost, 0, 0.001))
			return cost;
		return calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, null);
	}

	public static double calculateCostNewMethodAnnualWithHiddenEnrollments(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {

		BEmployee bemp;
		try {
			bemp = new BEmployee(payingPerson.getPersonId());
		} catch (Throwable ee) {
			bemp = null;  // paying person was not an employee (probably a dependent for COBRA benefits)
		}
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());
		double cost = 0;
		String grandParentBenefitInternalId = null;
		BenefitRider br = bb.getRiderEnrollment(payingPerson);
		if (br != null) {
			grandParentBenefitInternalId = br.getBaseBenefit().getInternalId();
			if (br.getRequired() == 'N') //Required Costs are included in base benefit
			{
				double tempCost = 0;
				tempCost += calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
				if (Utils.doubleEqual(tempCost, 0, 0.001)) {
					BHRBenefit bb2 = new BHRBenefit(br.getBaseBenefit());
					br = bb2.getRiderEnrollment(payingPerson);
					if (br != null && br.getRequired() == 'N')
						cost += calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				} else
					cost += tempCost;
			}
		} else {
			BenefitDependency bd = bb.getDependencyEnrollment(payingPerson);
			if (bd != null) {
				if (bd.getRequired() == 'N') //Required Costs are included in base benefit
				{
					double tempCost = 0;
					tempCost += calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
					if (Utils.doubleEqual(tempCost, 0, 0.001)) {
						BHRBenefit bb2 = new BHRBenefit(bd.getRequiredBenefit());
						bd = bb2.getDependencyEnrollment(payingPerson);
						if (bd != null && bd.getRequired() == 'N')
							cost += calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					} else
						cost += tempCost;
				}
			} else
				cost += calculateCostNewMethodAnnual(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
		}

		for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bb)) {
			BBenefitDependency bbd = new BBenefitDependency(bd);
			int payerAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
			//only include in calculations if this is required and hidden (those are the auto-enrolls)
			if (!bbd.getRequiredBoolean() || (bb.getMaxAge() != 0 && (payerAge > bb.getMaxAge() || payerAge < bb.getMinAge())))
				continue;
			BHRBenefit bb2 = new BHRBenefit(bd.getDependentBenefit());
			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethodAnnual(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethodAnnual(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}
		for (BenefitRider br2 : bb.getBenefitRiders()) {
			BBenefitRider bbr = new BBenefitRider(br2);
			//only include in calculations if this is required and hidden (those are the auto-enrolls)
			if (!bbr.getRequiredBoolean())
				continue;
			BHRBenefit bb2 = new BHRBenefit(bbr.getRiderBenefit());
			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethodAnnual(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethodAnnual(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}
		return cost;
	}

	public static double calculateCostNewMethodAnnual(HrBenefitJoin hrBenefitJoin, int asOfDate) {
		if (hrBenefitJoin.getHrBenefitConfig() == null)
			return 0;

		@SuppressWarnings("unchecked")
		List<IPerson> coveredPeople = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.APPROVED, hrBenefitJoin.getBenefitApproved()).eq(HrBenefitJoin.PAYING_PERSON, hrBenefitJoin.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, hrBenefitJoin.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitJoin.getHrBenefitConfig()).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).eq(HrBenefitJoin.EMPLOYEE_COVERED, 'Y').list();

		//return calculateCostNewMethodAnnual(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA()=='Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1, null);
		return calculateCostNewMethodAnnualWithHiddenEnrollments(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA() == 'Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1);

	}

	/**
	 * Method that calculates the annual cost for a real or proposed enrollment
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	Benefit Change Reason type
	 * @return	Returns annual cost
	 */
	public static double calculateCostNewMethodAnnual(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType, String requiredBenefitInternalId) {


		int customCalc = BProperty.getInt("CustomCalculations");
		switch (customCalc) {
			case 1:
				if (!StringUtils.isEmpty(requiredBenefitInternalId)) {
					double cost = HumanaCostCalculator.calculateAnnualPlanCostByBenefit(hrBenefitConfig, requiredBenefitInternalId, payingPerson.getPersonId(), coveredPeople, amountCovered);
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost;
				}
				break;
			case 2:
				List<String> peopleIds = new ArrayList<String>();
				for (IPerson p : coveredPeople)
					peopleIds.add(p.getPersonId());
				try {
					HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
					double cost;
					if (FidelityLifeCostCalculator.MAD.contains(requiredBenefitInternalId))
						cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.ANNUAL, false);
					else
						cost = FidelityLifeCostCalculator.calculateTotalPremium(bj.getBenefitJoinId(), null, FidelityLifeCostCalculator.ANNUAL);
					//double cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.ANNUAL, false);
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost;
				} catch (Exception ex) {
					return 0;
				}
//				HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
//				try {
//					double cost = FidelityLifeCostCalculator.calculateTotalPremium(bj.getBenefitJoinId(), null, FidelityLifeCostCalculator.ANNUAL);
//					if(!Utils.doubleEqual(cost, 0, 0.001))
//						return cost;
//				} catch (Exception ex) {
//					//continue
//				}
				break;
		}

		double ret = calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, requiredBenefitInternalId);
		Calendar c = DateUtils.getCalendar(policyStartDate);
		if (hrBenefitConfig != null && hrBenefitConfig.getHrBenefit().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE
				&& (bcrType == HrBenefitChangeReason.QUALIFYING_EVENT || bcrType == HrBenefitChangeReason.NEW_HIRE) && c.get(Calendar.YEAR) == DateUtils.getCalendar(DateUtils.now()).get(Calendar.YEAR))
			return ret * (12 - c.get(Calendar.MONTH));
		return ret * 12;
	}

	/**
	 * Calculates the monthly of the Benefit Configuration and includes the cost
	 * of all auto-enrolled benefit dependencies that are not in the wizard as
	 * well
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	benefit change reason type
	 * @return	Returns monthly
	 */
	public static double calculateCostNewMethodMonthlyWithDependencies(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {

		if (true)
			return calculateCostNewMethodMonthlyWithHiddenEnrollments(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType);
		BEmployee bemp = new BEmployee(payingPerson.getPersonId());
		BWizardConfiguration bz = bemp.getWizardConfiguration("E");
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());
		List<HrBenefit> reqs = BBenefitDependency.getRequirements(bb);
		double cost = 0;
		if (reqs.isEmpty()) //im a req benefit
		{
			//cost = HumanaCostCalculator.calculateMonthlyPlanCostByBenefit(hrBenefitConfig, hrBenefitConfig.getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
			cost = calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
			List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, bb.getBean()).list();

			List<String> dependentBenefitIds = new ArrayList<String>();
			List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
			for (BenefitDependency bd : bds) {
				dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
				dependentBenefits.add(bd.getDependentBenefit());
			}

			//get the matching benefits that are already in the wizard
			List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();

			//remove those because the wizard will take care of these auto enrolls
			dependentBenefits.removeAll(benefitsInWizard);
			for (HrBenefit b : dependentBenefits) {
				BHRBenefit bb2 = new BHRBenefit(b);
				HrBenefitConfig match = null;
				List<HrBenefitConfig> hrbcl = bb2.getConfigs();
				if (hrbcl.size() == 1)
					match = hrbcl.get(0);
				else
					for (HrBenefitConfig newConfig : b.getBenefitConfigs())
						if (hrBenefitConfig.getEmployee() == newConfig.getEmployee()
								&& hrBenefitConfig.getChildren() == newConfig.getChildren()
								&& hrBenefitConfig.getSpouseEmployee() == newConfig.getSpouseEmployee()
								&& hrBenefitConfig.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
								&& hrBenefitConfig.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
								&& hrBenefitConfig.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
								&& hrBenefitConfig.getMaxChildren() == newConfig.getMaxChildren()) {
							match = newConfig;
							break;
						}
				if (match != null) {
					//double humanaCost = HumanaCostCalculator.calculateMonthlyPlanCostByBenefit(match, bb.getBenefitId(), payingPerson.getPersonId(), new BHRBenefitConfig(match).estimateCoveredPeople(bemp), amountCovered);
					double humanaCost = calculateCostNewMethodMonthly(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
					if (!Utils.doubleEqual(humanaCost, 0.0, .0001))
						cost += (humanaCost);
				}
			}
		} else //im a dep benefit
		{
			HrBenefitJoin bj = null;
			for (HrBenefit b : reqs)
				if (bj == null)
					bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();
			if (bj == null && !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.HRBENEFIT, reqs).exists()
					&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, reqs.get(0).getHrBenefitCategory()).exists())
				bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).in(HrBenefitConfig.HR_BENEFIT, reqs).first();

			if (bj != null) {
				List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT_ID, bj.getHrBenefitConfig().getBenefitId()).list();

				List<String> dependentBenefitIds = new ArrayList<String>();
				for (BenefitDependency bd : bds)
					dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());

				//get the matching benefits that are already in the wizard
				List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();
				if (benefitsInWizard.contains(hrBenefitConfig.getHrBenefit()))
					//cost = HumanaCostCalculator.calculateMonthlyPlanCostByBenefit(hrBenefitConfig, bj.getHrBenefitConfig().getBenefitId(), payingPerson.getPersonId(), coveredPeople, amountCovered);
					cost = calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bj.getHrBenefitConfig().getHrBenefit().getInternalId());
			}
		}

		if (!Utils.doubleEqual(cost, 0, 0.001))
			return cost;
		return calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, null);
	}

	public static double calculateCostNewMethodMonthlyWithHiddenEnrollments(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType) {
		if (hrBenefitConfig == null)
			return 0;
		BEmployee bemp = new BEmployee(payingPerson.getPersonId());
		BHRBenefit bb = new BHRBenefit(hrBenefitConfig.getBenefitId());
		String grandParentBenefitInternalId = null;
		//Check to see if I am a Rider, or Dependency, if not, just try base calculation
		double cost = 0;
		BenefitRider br = bb.getRiderEnrollment(payingPerson);
		if (br != null) {
			grandParentBenefitInternalId = br.getBaseBenefit().getInternalId();
			if (br.getRequired() == 'N') //Required Costs are included in base benefit
			{
				double tempCost = 0;
				tempCost += calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
				if (Utils.doubleEqual(tempCost, 0, 0.001)) {
					BHRBenefit bb2 = new BHRBenefit(br.getBaseBenefit());
					br = bb2.getRiderEnrollment(payingPerson);
					if (br != null && br.getRequired() == 'N')
						cost += calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, br.getBaseBenefit().getInternalId());
				} else
					cost += tempCost;
			}
		} else {
			BenefitDependency bd = bb.getDependencyEnrollment(payingPerson);
			if (bd != null) {
				grandParentBenefitInternalId = bd.getRequiredBenefit().getInternalId();
				if (bd.getRequired() == 'N') //Required Costs are included in base benefit
				{
					double tempCost = 0;
					tempCost += calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					//if we have a requirement but dont return a cost, go up a level to get the grandparent base benefit
					if (Utils.doubleEqual(tempCost, 0, 0.001)) {
						BHRBenefit bb2 = new BHRBenefit(bd.getRequiredBenefit());
						bd = bb2.getDependencyEnrollment(payingPerson);
						if (bd != null && bd.getRequired() == 'N')
							cost += calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bd.getRequiredBenefit().getInternalId());
					} else
						cost += tempCost;
				}
			} else
				cost += calculateCostNewMethodMonthly(asOfDate, hrBenefitConfig, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, coveredPeople, bcrType, bb.getInternalId());
		}

		//lets loop through dependent benefits and calc the required ones (auto-enrolls)
		for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bb)) {
			BBenefitDependency bbd = new BBenefitDependency(bd);
			int payerAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
			//only include in calculations if this is required and hidden (those are the auto-enrolls)
			if (!bbd.getRequiredBoolean() || (bb.getMaxAge() != 0 && (payerAge > bb.getMaxAge() || payerAge < bb.getMinAge())))
				continue;
			BHRBenefit bb2 = new BHRBenefit(bd.getDependentBenefit());
			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethodMonthly(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethodMonthly(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}

		//lets loop through riders benefits and calc the required ones (auto-enrolls)
		for (BenefitRider br2 : bb.getBenefitRiders()) {
			BBenefitRider bbr = new BBenefitRider(br2);
			//only include in calculations if this is required
			if (!bbr.getRequiredBoolean())
				continue;
			BHRBenefit bb2 = new BHRBenefit(bbr.getRiderBenefit());
			HrBenefitConfig match = bb2.getCorrespondingBenefitConfig(hrBenefitConfig);
			if (match != null) {
				double tempCost = calculateCostNewMethodMonthly(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, bb.getInternalId());
				if (!Utils.doubleEqual(tempCost, 0.0, .0001))
					cost += (tempCost);
				else if (!StringUtils.isEmpty(grandParentBenefitInternalId))
					cost += calculateCostNewMethodMonthly(asOfDate, match, payingPerson, usingCobra, amountCovered, policyStartDate, policyEndDate, new BHRBenefitConfig(match).estimateCoveredPeople(bemp), bcrType, grandParentBenefitInternalId);
			}
		}
		return cost;
	}

	public static double calculateCostNewMethodMonthly(HrBenefitJoin hrBenefitJoin, int asOfDate) {
		if (hrBenefitJoin.getHrBenefitConfig() == null)
			return 0;

		@SuppressWarnings("unchecked")
		List<IPerson> coveredPeople = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.APPROVED, hrBenefitJoin.getBenefitApproved()).eq(HrBenefitJoin.PAYING_PERSON, hrBenefitJoin.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, hrBenefitJoin.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitJoin.getHrBenefitConfig()).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).eq(HrBenefitJoin.EMPLOYEE_COVERED, 'Y').list();

		//return calculateCostNewMethodMonthly(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA()=='Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1, null);
		return calculateCostNewMethodMonthlyWithHiddenEnrollments(asOfDate, hrBenefitJoin.getHrBenefitConfig(), hrBenefitJoin.getPayingPerson(), hrBenefitJoin.getUsingCOBRA() == 'Y', hrBenefitJoin.getAmountCovered(), hrBenefitJoin.getPolicyStartDate(), hrBenefitJoin.getPolicyEndDate(), coveredPeople, -1);

	}

	/**
	 * Method that calculates the monthly cost for a real or proposed enrollment
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @param payingPerson	The employee paying for the enrollment
	 * @param usingCobra	Is this a Cobra enrollment for a terminated employee?
	 * @param amountCovered	For benefits where you elect a coverage amount
	 * @param policyStartDate	The date the policy becomes active
	 * @param policyEndDate	The date the policy is terminated
	 * @param coveredPeople	Enrollees being covered
	 * @param bcrType	Benefit Change Reason type
	 * @return	Returns monthly cost
	 */
	public static double calculateCostNewMethodMonthly(int asOfDate, HrBenefitConfig hrBenefitConfig, Person payingPerson,
			boolean usingCobra, double amountCovered, int policyStartDate, int policyEndDate, List<IPerson> coveredPeople, int bcrType, String requiredBenefitInternalId) {

		if (hrBenefitConfig == null)
			return 0;

		double cost = 0.0;
		int customCalc = BProperty.getInt("CustomCalculations");
		switch (customCalc) {
			case 1:
				if (!StringUtils.isEmpty(requiredBenefitInternalId)) {
					cost = HumanaCostCalculator.calculateMonthlyPlanCostByBenefit(hrBenefitConfig, requiredBenefitInternalId, payingPerson.getPersonId(), coveredPeople, amountCovered);
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost;
				}
				break;
			case 2:
				List<String> peopleIds = new ArrayList<String>();
				for (IPerson p : coveredPeople)
					peopleIds.add(p.getPersonId());
				try {
					HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
					if (FidelityLifeCostCalculator.MAD.contains(requiredBenefitInternalId))
						cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.MONTHLY, false);
					else
						cost = FidelityLifeCostCalculator.calculateTotalPremium(bj.getBenefitJoinId(), null, FidelityLifeCostCalculator.MONTHLY);
					//cost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(hrBenefitConfig.getBenefitConfigId(), requiredBenefitInternalId, payingPerson.getPersonId(), peopleIds, amountCovered, FidelityLifeCostCalculator.MONTHLY, false);
					if (!Utils.doubleEqual(cost, 0, 0.001))
						return cost;
				} catch (Exception ex) {
					return 0;
				}
//				HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
//				try {
//					cost = FidelityLifeCostCalculator.calculateTotalPremium(bj.getBenefitJoinId(), null, FidelityLifeCostCalculator.MONTHLY);
//					if(!Utils.doubleEqual(cost, 0, 0.001))
//						return cost;
//				} catch (Exception ex) {
//					//continue
//				}
				break;
		}



		if (asOfDate == 0)
			asOfDate = DateUtils.now();

		if (hrBenefitConfig.getHrBenefit().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE) {
			Calendar c = DateUtils.getCalendar(policyStartDate);
			if ((bcrType == HrBenefitChangeReason.QUALIFYING_EVENT || bcrType == HrBenefitChangeReason.NEW_HIRE) && c.get(Calendar.YEAR) == DateUtils.getCalendar(DateUtils.now()).get(Calendar.YEAR))
				return amountCovered / (12 - c.get(Calendar.MONTH));  //FLEX is always amount covered
			else
				return amountCovered / 12;  //FLEX is always amount covered
		}

		if (coveredPeople == null)
			coveredPeople = new ArrayList<IPerson>();

		//get base amount


		//work my way up my org group hierarchy

		BPerson bp = new BPerson(payingPerson);


		BenefitConfigCost c = null;



		if (usingCobra)
			c = findCobraCost(asOfDate, hrBenefitConfig);

		if (c == null) {

			List<OrgGroup> orgs = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, payingPerson).list();

			if (orgs.isEmpty())
				orgs.add(payingPerson.getCompanyBase());

			//look for costs for my status
			if (bp.isEmployee()) {
				BEmployee bemp = bp.getBEmployee();
				HrEmplStatusHistory hist = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, bemp.getEmployee()).le(HrEmplStatusHistory.EFFECTIVEDATE, asOfDate).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

				if (hist != null)
					c = findStatusSpecificCost(orgs, asOfDate, hist.getHrEmployeeStatus(), hrBenefitConfig);
			}
			if (c == null)
				c = findCost(orgs, asOfDate, hrBenefitConfig);
		}

		///////////////////////////////////////////////////////////////////////////////////
		/////////////////////// custom WMCO cost need to move later ///////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		if (customCalc == 3 && new BPerson(payingPerson).isEmployee()) {
			BEmployee be = new BEmployee(payingPerson.getPersonId());
			HrBenefitJoin tempBJ = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, payingPerson.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
			short tempAge = be.getAgeAsOf(asOfDate);
			int salaryAsOfDate = asOfDate;
			if (tempBJ != null) {
				tempAge = be.getAgeAsOf(tempBJ.getCoverageStartDate());
				salaryAsOfDate = tempBJ.getCoverageStartDate();
			}
			double customCost = getSTDCost(be.getSalaryAsOf(salaryAsOfDate), tempAge, hrBenefitConfig.getBenefitConfigId());
			if (!Utils.doubleEqual(customCost, 0, 0.001))
				//System.out.println("Returned STD Monthly = " + MoneyUtils.formatMoney(customCost) + " for " + be.getNameFML());
				return customCost;
		}

		///////////////////////////////////////////////////////////////////////////////////
		/////////////////////// custom WMCO cost //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////


		if (c == null)
			return 0;

		//now I found the one that applies, lets see what the base is

		switch (c.deprecatedGetBaseAmountSource()) {
			case BenefitConfigCost.SOURCE_BASE:
				cost = c.deprecatedGetBaseAmount();
				break;
			case BenefitConfigCost.SOURCE_COVERAGE:
				cost = amountCovered;
				break;
			case BenefitConfigCost.SOURCE_ANNUAL_PAY: {

				if (bp.isEmployee()) {
					BEmployee bemp = bp.getBEmployee();
					cost = bemp.getCurrentSalary();
					if (bemp.getCurrentSalaryType() == WageType.TYPE_REGULAR)
						cost *= 2080;
				} else
					throw new ArahantWarning("Benefit cost for " + hrBenefitConfig.getName() + " is calculated based on annual pay,\n"
							+ "but " + bp.getNameLFM() + " is not an employee.");
				break;
			}
			default:
				throw new ArahantException("Unknown benefit source type found.");
		}

		//cap it
		if (c.deprecatedGetBaseCapAmount() > 0 && cost > c.deprecatedGetBaseCapAmount())
			cost = c.deprecatedGetBaseCapAmount();


		//now I need to round that to nearest?

		if (c.deprecatedGetBaseRoundAmount() > .01) {
			long v = (long) ((cost + .005) * 100);
			v = (long) (v / (c.deprecatedGetBaseRoundAmount() * 100));
			v = (long) (v * (c.deprecatedGetBaseRoundAmount() * 100));
			cost = v / 100.0;
		}

		//handle multiplier
		switch (c.deprecatedGetMultiplierSource()) {
			case BenefitConfigCost.MULTIPLIER_COLUMN:
				cost *= c.deprecatedGetMultiplier();
				if (c.deprecatedGetCostPerEnrollee() == 'Y')
					cost *= coveredPeople.size();
				break;
			case BenefitConfigCost.MULTIPLIER_AGE_TABLE: {
				double base = cost;
				cost = 0;
				//loop for everyone enrolled
				for (IPerson cp : coveredPeople) {
					HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.COVERED_PERSON_ID, cp.getPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hrBenefitConfig).first();
					BenefitConfigCostAge age = null;
					Person p = ArahantSession.getHSU().get(Person.class, cp.getPersonId());
					if (p != null) {
						BPerson bcp = new BPerson(p);
						short personAge = bcp.getAgeAsOf(asOfDate);
						if (bj != null && c.getAgeCalcType() == 'S')
							personAge = bcp.getAgeAsOf(bj.getCoverageStartDate());
						else if (c.getAgeCalcType() == 'J') {
							Calendar janFirst = DateUtils.getCalendar(asOfDate);
							janFirst.set(Calendar.MONTH, Calendar.JANUARY);
							janFirst.set(Calendar.DAY_OF_MONTH, 1);
							personAge = bcp.getAgeAsOf(DateUtils.getDate(janFirst));
						}
						age = ArahantSession.getHSU().createCriteria(BenefitConfigCostAge.class).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, c).gt(BenefitConfigCostAge.AGE, personAge).orderBy(BenefitConfigCostAge.AGE).first();
						if (age == null)
							throw new ArahantWarning("Benefit " + hrBenefitConfig.getName() + " uses age in cost calculation.\n"
									+ "Age " + personAge + " of " + bcp.getNameLFM() + " exceeds all ages in cost age table.");
					} else {
						Person pp = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, cp.getPersonId()).first();
						if (pp != null) {
							BPerson bcp = new BPerson(pp);
							short personAge = bcp.getAgeAsOf(asOfDate);
							if (bj != null && c.getAgeCalcType() == 'S')
								personAge = bcp.getAgeAsOf(bj.getCoverageStartDate());
							else if (c.getAgeCalcType() == 'J') {
								Calendar janFirst = Calendar.getInstance();
								janFirst.set(Calendar.MONTH, Calendar.JANUARY);
								janFirst.set(Calendar.DAY_OF_MONTH, 1);
								personAge = bcp.getAgeAsOf(DateUtils.getDate(janFirst));
							}
							age = ArahantSession.getHSU().createCriteria(BenefitConfigCostAge.class).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, c).gt(BenefitConfigCostAge.AGE, personAge).orderBy(BenefitConfigCostAge.AGE).first();
							if (age == null)
								throw new ArahantWarning("Benefit " + hrBenefitConfig.getName() + " uses age in cost calculation.\n"
										+ "Age " + personAge + " of " + bcp.getNameLFM() + " exceeds all ages in cost age table.");

						}
					}

					cost += getOverrideAgeCost(bj, base, age);

				}
				break;
			}
			default:
				throw new ArahantException("Unknown multiplier source found.");
		}

		//handle divider
		cost = cost / c.deprecatedGetDivider();
//System.out.println("calc monthly time was "+((new Date().getTime()-now.getTime())/1000));
		return cost;
	}

	/**
	 * Recursively finds the BenefitConfigCost records to use in the
	 * calculations for this employee based on status
	 *
	 * @param ogls	List of OrgGroupAssociations for the employee
	 * @param asOfDate	Date of the enrollment
	 * @param stat	The Employee's status
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @return	Returns the cost record to use in calculations
	 */
	static BenefitConfigCost findStatusSpecificCost(List<OrgGroup> ogls, int asOfDate, HrEmployeeStatus stat, HrBenefitConfig hrBenefitConfig) {
		if (ogls.isEmpty())
			return null;

		//	System.out.println(stat.getStatusId());

		BenefitConfigCost c = ArahantSession.getHSU().createCriteria(BenefitConfigCost.class).eq(BenefitConfigCost.BENEFIT_CONFIG, hrBenefitConfig).dateInside(BenefitConfigCost.FIRSTACTIVEDATE, BenefitConfigCost.LASTACTIVEDATE, asOfDate).eq(BenefitConfigCost.APPLIES_TO, BenefitConfigCost.APPLIES_TABLE).in(BenefitConfigCost.ORG_GROUP, ogls).joinTo(BenefitConfigCost.STATUSES).eq(BenefitConfigCostStatus.STATUS, stat).first();

		if (c == null)
			//see if there are any parents
			return findStatusSpecificCost(ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID).in(OrgGroupHierarchy.CHILD, ogls).list(), asOfDate, stat, hrBenefitConfig);

		return c;
	}

	/**
	 * Finds the BenefitConfigCost records to use in the calculations for this
	 * employee's cobra enrollment
	 *
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @return	Returns the cost record to use in calculations
	 */
	static BenefitConfigCost findCobraCost(int asOfDate, HrBenefitConfig hrBenefitConfig) {
		BenefitConfigCost c = ArahantSession.getHSU().createCriteria(BenefitConfigCost.class).eq(BenefitConfigCost.BENEFIT_CONFIG, hrBenefitConfig).dateInside(BenefitConfigCost.FIRSTACTIVEDATE, BenefitConfigCost.LASTACTIVEDATE, asOfDate) //.isNull(BenefitConfigCost.ORG_GROUP)
				.eq(BenefitConfigCost.APPLIES_TO, BenefitConfigCost.APPLIES_COBRA).first();
		/*
		 * if (c==null) { //see if there are any parents return
		 * findCobraCost(ArahantSession.getHSU().createCriteria(OrgGroup.class)
		 * .joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID)
		 * .in(OrgGroupHierarchy.CHILD,ogls) .list(),asOfDate);
		 *
		 * }
		 */
		return c;
	}

	/**
	 * Recursively finds the BenefitConfigCost records to use in the
	 * calculations for this employee
	 *
	 * @param ogls	List of OrgGroupAssociations for the employee
	 * @param asOfDate	Date of the enrollment
	 * @param hrBenefitConfig	Benefit Configuration they are trying to enroll in
	 * @return	Returns the cost record to use in calculations
	 */
	static BenefitConfigCost findCost(List<OrgGroup> ogls, int asOfDate, HrBenefitConfig hrBenefitConfig) {
		if (ogls.isEmpty())
			return null;

		BenefitConfigCost c = ArahantSession.getHSU().createCriteria(BenefitConfigCost.class).eq(BenefitConfigCost.BENEFIT_CONFIG, hrBenefitConfig).dateInside(BenefitConfigCost.FIRSTACTIVEDATE, BenefitConfigCost.LASTACTIVEDATE, asOfDate).eq(BenefitConfigCost.APPLIES_TO, BenefitConfigCost.APPLIES_ALL).in(BenefitConfigCost.ORG_GROUP, ogls).first();

		if (c == null)
			//see if there are any parents
			return findCost(ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID).in(OrgGroupHierarchy.CHILD, ogls).list(), asOfDate, hrBenefitConfig);

		return c;
	}
	private static boolean showError = true;

	/**
	 * Goes to LISP to find if there is a custom calculation for the employee's
	 * age specific cost
	 *
	 * @param bj	The HrBenefitJoin record we are calculating for
	 * @param base	The base amount in the calculation
	 * @param age	The age record used for the calculation
	 * @return	Returns a custom age calculated cost
	 */
	private static double getOverrideAgeCost(HrBenefitJoin bj, double base, BenefitConfigCostAge age) {
		double addCost = 0.0;

		if (BProperty.getInt("CustomCalculations") != 3)  // old method (jess)
//			if(bj != null && bj.overrideAgeCost() > 0.001)
//				addCost = bj.overrideAgeCost();
//			else
			addCost = base * age.deprecatedGetMultiplier();
		else {  // new method (lisp)
			boolean useJava = true;

			if (bj != null) {
				LispPackage lp = null;
				double overrideAgeCost;

				try {
					//								lp = new LispPackage("BenefitCostCalculator", "com/arahant/lisp/WmCo-BenefitCostCalculator");
					lp = new LispPackage("WmCoBenefitCostCalculator", "WmCo-BenefitCostCalculator");
					if (bj.getHrBenefitConfigId() != null)
						overrideAgeCost = lp.executeLispReturnDouble("CalculatedGCICost", bj.getCoveredPersonId(), bj.getPayingPersonId(), bj.getHrBenefitConfigId(), bj);
					else
						overrideAgeCost = 0.0;
					if (overrideAgeCost > 0.001) {
						addCost = overrideAgeCost;
						useJava = false;
					}
				} catch (Throwable t) {
					//  couldn't find lisp file
					if (showError) {
						logger.error(t);
						showError = false;  //  only show the message once
					}
				} finally {
					if (lp != null)
						lp.packageDone();
				}
			}
			if (useJava)
				addCost = base * age.deprecatedGetMultiplier();
		}
		return addCost;
	}

	private static double getSTDCost(double salary, int age, String configId) {
		LispPackage lp = null;
		double stdCost = 0.0;

		try {
			lp = new LispPackage("WmCoBenefitCostCalculator", "WmCo-BenefitCostCalculator");
			stdCost = lp.executeLispReturnDouble("CalculateShortTermDisabilityCost", configId, salary, age);
		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
				showError = false;  //  only show the message once
			}
		} finally {
			if (lp != null)
				lp.packageDone();
		}
		return stdCost;
	}
	
	/**
	 * This is the main method used to find the applicable BenefitConfigCost record to use given
	 * the BenefitConfig and certain employee info. (written by Blake)
	 * 
	 * @param bc the BenefitConfig record it relates to
	 * @param employeeStatusID the employee's status ID (or null to ignore)
	 * @param employeeOrgGroup the org group the employee is a member of
	 * @param date the date the config needs to be valid (or 0 to ignore)
	 * @return the applicable BBenefitConfigCost record
	 */	
	public static BBenefitConfigCost findConfigCost(BHRBenefitConfig bc, String employeeStatusID, BOrgGroup employeeOrgGroup, int date) {
		Set<BOrgGroup> eog = new HashSet<BOrgGroup>();
		eog.add(employeeOrgGroup);
		return findConfigCost(bc, employeeStatusID, eog, date);
	}
	
	/**
	 * This is the main method used to find the applicable BenefitConfigCost record to use given
	 * the BenefitConfig and certain employee info. (written by Blake)
	 * 
	 * @param bc the BenefitConfig record it relates to
	 * @param employeeStatusID the employee's status ID (or null to ignore)
	 * @param employeeOrgGroups the set of org groups the employee is a member of
	 * @param dateValid the date the config needs to be valid (or 0 to ignore)
	 * @return the applicable BBenefitConfigCost record
	 */
	public static BBenefitConfigCost findConfigCost(BHRBenefitConfig bc, String employeeStatusID, Set<BOrgGroup> employeeOrgGroups, int dateValid) {
		char benefitType = ' ';  //  dummy value to get past compiler warnings
		if (employeeStatusID != null) {
			benefitType = (new BHREmployeeStatus(employeeStatusID)).getBenefitType();
			if (benefitType == 'N')
				return null;  //  no benefits
		}
		BBenefitConfigCost [] gcca = bc.getBenefitCosts();
		int minLevel = -1;
		BBenefitConfigCost selectedCost = null;
		for (int i=0 ; i < gcca.length ; i++) {  //  loop over BenefitCost's
			BBenefitConfigCost bcc = gcca[i];
			
			// validate the date
			if (dateValid != 0) {
				int firstActiveDate = bcc.getFirstActiveDate();
				int lastActiveDate = bcc.getLastActiveDate();
				if (firstActiveDate != 0  &&  firstActiveDate > dateValid)
					continue;
				if (lastActiveDate != 0  &&  lastActiveDate < dateValid)
					continue;
			}
			
			// validate the status
			if (employeeStatusID != null) {
				char appliesToStatus = bcc.getStatusType().charAt(0);
				if (appliesToStatus == 'S') {  // status table
					boolean found = false;
					for (BBenefitConfigCostStatus status : bcc.getStatuses())
						if (status.getStatusId().equals(employeeStatusID)) {
							found = true;
							break;
						}
					if (!found)
						continue;
				} else if (appliesToStatus == 'C'  &&  benefitType != 'C')
					continue;
			}
			
			
			int level = (new BOrgGroup(bcc.getOrgGroupId())).getMinimumMemberLevel(employeeOrgGroups);
			if (level == -1)  // not in that group
				continue;				
			if (minLevel == -1  ||  level < minLevel) {
				minLevel = level;
				selectedCost = bcc;
			}
		}
		return selectedCost;
	}
	
	/**
	 * Finds a person's age at the correct time.
	 * 
	 * @param bcc
	 * @param per
	 * @param coverageStartDate (0 = today parameter)
	 * @param asOfDate (0 = today's date)
	 * @return 
	 */
	public static int findAge(BBenefitConfigCost bcc, BPerson per, int coverageStartDate, int asOfDate) {
		if (asOfDate == 0)
			asOfDate = DateUtils.today();
		if (coverageStartDate == 0)
			coverageStartDate = asOfDate;
		char ageCalcType = bcc.getAgeCalcType().charAt(0);
		if (ageCalcType == 'S')
			return per.getAgeAsOf(coverageStartDate);
		else if (ageCalcType == 'C')
			return per.getAgeAsOf(asOfDate);
		else if (ageCalcType == 'J') {
			Calendar janFirst = DateUtils.getCalendar(asOfDate);
			janFirst.set(Calendar.MONTH, Calendar.JANUARY);
			janFirst.set(Calendar.DAY_OF_MONTH, 1);
			return per.getAgeAsOf(DateUtils.getDate(janFirst));			
		}
		return -1;
	}
	
	/**
	 * Calculate the coverage start date. written by Blake
	 *
	 * @param asOfDate
	 * @return the coverage start date
	 */
	public static int calcCoverageStartDate(BBenefitConfigCost bcc, int asOfDate) {
		int startDate;
		HrBenefitConfig config = bcc.getBean().getConfig();
		int benStartDate = config.getHrBenefit().getStartDate();
		int configStartDate = config.getStartDate();
		int costStartDate = bcc.getFirstActiveDate();
		
		startDate = asOfDate;
		if (benStartDate > startDate)
			startDate = benStartDate;
		if (configStartDate > startDate)
			startDate = configStartDate;
		if (costStartDate > startDate)
			startDate = costStartDate;
		
		return startDate;
	}
		
	/**
	 * Finds the applicable BenefitConfigCostAge record.  written by Blake
	 * 
	 * @param bcc the relevant BenefitConfigCost (or null)
	 * @param bcp the person
	 * @param coverageStartDate date coverage is to start (0 = calculate it)
	 * @param asOfDate current date for age calc purposes (0 = today's date)
	 * @return the applicable BBenefitConfigCostAge record
	 */
	public static BBenefitConfigCostAge findBenefitConfigCostAge(BBenefitConfigCost bcc, BPerson bcp, int coverageStartDate, int asOfDate) {
		if (bcc == null)
			return null;
		if (bcc.getAgeCalcType().equals("N"))
			return null;
		BBenefitConfigCostAge [] ages = bcc.getAges();
		if (ages == null  ||  ages.length == 0)
			return null;
		
		if (coverageStartDate == 0)
			coverageStartDate = calcCoverageStartDate(bcc, asOfDate);
		
		int personAge = findAge(bcc, bcp, coverageStartDate, asOfDate);
		if (personAge == -1)
			return null;

		for (int i=0 ; i < ages.length ; i++)
			if (personAge <= ages[i].getMaxAge())
				return ages[i];
	
		return null;
	}
	
	/**
	 * Calculate benefit amount if not selected by employee.  written by Blake
	 * 
	 * @param emp
	 * @param ben
	 * @param bcc
	 * @return 
	 */
	public static double calculateCoverageAmount(BEmployee emp, BHRBenefit ben, BBenefitConfigCost bcc) {
		char benefitAmountModel = ben.getBenefitAmountModel();
		if (benefitAmountModel == 'F')
			return bcc.getBenefitAmount();
		else if (benefitAmountModel == 'S') {
			// if type is 'S' then all numbers are in units of rateFrequency
			short rateFreq = bcc.getRateFrequency();
			double percentOfSalary = bcc.getMaxMultipleOfSalary();
			double annualSalary = emp.getCurrentSalary();
			double minBenefitAmount = bcc.getMinValue();
			double maxBenefitAmount = bcc.getMaxValue();
			int roundPlaces = roundPlaces(bcc.getSalaryRoundAmount());
			if (annualSalary < 1000.0)  //  must be hourly, covert to annual
				annualSalary *= (40.0 * 52.0);
			switch (bcc.getSalaryRoundType()) {
				case 'N':
					annualSalary = Utils.round(annualSalary, roundPlaces);
					break;
				case 'U':
					annualSalary = Utils.roundUp(annualSalary, roundPlaces);
					break;
				case 'D':
					annualSalary = Utils.roundDown(annualSalary, roundPlaces);
					break;
			}

			//  make sure the benefit doesn't go beyond the declared min and max benefit amounts
			//  by adjusting the salary amount to create benefit within the defined range
			double minAnnualSalary = (rateFreq * 100.0 * minBenefitAmount) / percentOfSalary;
			double maxAnnualSalary = (rateFreq * 100.0 * maxBenefitAmount) / percentOfSalary;
			if (annualSalary < minAnnualSalary)
				annualSalary = minAnnualSalary;
			if (maxAnnualSalary > 500.0  &&  annualSalary > maxAnnualSalary)
				annualSalary = maxAnnualSalary;
			// calc amount per rateFrequency
			return  Utils.round((annualSalary * percentOfSalary) / (100.0 * rateFreq), 2);
		} else if (benefitAmountModel == 'M') {
			double annualSalary = emp.getCurrentSalary();
			double multOfSalary = bcc.getMaxMultipleOfSalary();
			double minBenefitAmount = bcc.getMinValue();
			double maxBenefitAmount = bcc.getMaxValue();
			short rateFreq = bcc.getRateFrequency();
			if (multOfSalary < .01)
				return 0.0;
			if (annualSalary < 1000.0)  //  must be hourly
				annualSalary *= (40.0 * 52.0);
			int roundPlaces = roundPlaces(bcc.getSalaryRoundAmount());
			switch (bcc.getSalaryRoundType()) {
				case 'N':
					annualSalary = Utils.round(annualSalary, roundPlaces);
					break;
				case 'U':
					annualSalary = Utils.roundUp(annualSalary, roundPlaces);
					break;
				case 'D':
					annualSalary = Utils.roundDown(annualSalary, roundPlaces);
					break;
			}

			//  make sure the benefit doesn't go beyond the declared min and max benefit amounts
			//  by adjusting the salary amount to create benefit within the defined range
			double minAnnualSalary = minBenefitAmount / multOfSalary;
			double maxAnnualSalary = maxBenefitAmount / multOfSalary;
			roundPlaces = roundPlaces(bcc.getBenefitRoundAmount());
			if (annualSalary < minAnnualSalary)
				annualSalary = minAnnualSalary;
			if (maxAnnualSalary > 500.0  &&  annualSalary > maxAnnualSalary)
				annualSalary = maxAnnualSalary;

			double benefit = annualSalary * multOfSalary;
			switch (bcc.getBenefitRoundType()) {
				case 'N':
					benefit = Utils.round(benefit, roundPlaces);
					break;
				case 'U':
					benefit = Utils.roundUp(benefit, roundPlaces);
					break;
				case 'D':
					benefit = Utils.roundDown(benefit, roundPlaces);
					break;
			}
			if (maxBenefitAmount > 2.0  &&  benefit > maxBenefitAmount)
				benefit = maxBenefitAmount;
			if (benefit < minBenefitAmount)
				benefit = minBenefitAmount;
			return Utils.round(benefit, 2);			
		}
		return 0.0;
	}
	
	/**
	 * Calculates the employer's annual cost.  written by Blake
	 * 
	 * @param ben
	 * @param bcc
	 * @param bcca
	 * @param coverageAmount
	 * @return the annual employer cost
	 */
	public static double calculateEmployerAnnualCost(BHRBenefit ben, BBenefitConfigCost bcc, BBenefitConfigCostAge bcca, double coverageAmount) {
		char employerCostModel = ben.getEmployerCostModel();
		if (employerCostModel == 'Z'  ||  bcc == null)  // no employer cost or we have none
			return 0.0;
		if (employerCostModel == 'F') {  //  fixed cost
			return Utils.round(bcc.getFixedEmployerCost() * bcc.getRateFrequency(), 2);
		}
		if (employerCostModel == 'A') {  //  age banded fixed cost
			if (bcca == null)
				return 0.0;
			return Utils.round(bcca.getErValue() * bcc.getRateFrequency(), 2);
		}
		if (employerCostModel == 'B') {  //  X percent of benefit based on age
			if (bcca == null)
				return 0.0;
			return Utils.round(bcca.getErValue() * bcc.getRateFrequency() * coverageAmount, 2);
		}
		return 0.0;
	}
	
	private static int roundPlaces(double num)
	{
		if (num < .01)
			return 2;
		return -(int) Math.log10(num);
	}
	
	/**
	 * Calculates the employee's annual cost.  written by Blake
	 * 
	 * @param emp
	 * @param ben
	 * @param bcc
	 * @param bcca
	 * @param coverageAmount
	 * @return the annual employee cost
	 */
	public static double calculateEmployeeAnnualCost(BEmployee emp, BHRBenefit ben, BBenefitConfigCost bcc, BBenefitConfigCostAge bcca, double coverageAmount) {
		char employeeCostModel = ben.getEmployeeCostModel();
		char benefitAmountModel = ben.getBenefitAmountModel();
		if (employeeCostModel == 'Z'  ||  bcc == null)  // no employer cost or we have none
			return 0.0;
		if (employeeCostModel == 'F') {  //  fixed cost
			return Utils.round(bcc.getFixedEmployeeCost() * bcc.getRateFrequency(), 2);
		}
		if (employeeCostModel == 'A') {  //  age banded fixed cost
			if (bcca == null)
				return 0.0;
			if (benefitAmountModel == 'R') {
				double rpu = bcc.getRatePerUnit();
				if (rpu < .001)
					return 0.0;
				return Utils.round(bcca.getEeValue() * bcc.getRateFrequency() * coverageAmount / rpu, 2);
			} else if (benefitAmountModel == 'S') {
				// if type is 'S' then all numbers are in units of rateFrequency
				short rateFreq = bcc.getRateFrequency();
				double percentOfSalary = bcc.getMaxMultipleOfSalary();
				double annualSalary = emp.getCurrentSalary();
				double minBenefitAmount = bcc.getMinValue();
				double maxBenefitAmount = bcc.getMaxValue();
				int roundPlaces = roundPlaces(bcc.getSalaryRoundAmount());
				double rpu = bcc.getRatePerUnit();
				if (rpu < .001  ||  rateFreq < 1)
					return 0.0;
				if (annualSalary < 1000.0)  //  must be hourly, covert to annual
					annualSalary *= (40.0 * 52.0);
				switch (bcc.getSalaryRoundType()) {
					case 'N':
						annualSalary = Utils.round(annualSalary, roundPlaces);
						break;
					case 'U':
						annualSalary = Utils.roundUp(annualSalary, roundPlaces);
						break;
					case 'D':
						annualSalary = Utils.roundDown(annualSalary, roundPlaces);
						break;
				}
				
				//  make sure the benefit doesn't go beyond the declared min and max benefit amounts
				//  by adjusting the salary amount to create benefit within the defined range
				double minAnnualSalary = (rateFreq * 100.0 * minBenefitAmount) / percentOfSalary;
				double maxAnnualSalary = (rateFreq * 100.0 * maxBenefitAmount) / percentOfSalary;
				if (annualSalary < minAnnualSalary)
					annualSalary = minAnnualSalary;
				if (maxAnnualSalary > 500.0  &&  annualSalary > maxAnnualSalary)
					annualSalary = maxAnnualSalary;
				// calc amount per rateFrequency
				double total =  Utils.round((annualSalary * percentOfSalary * bcca.getEeValue()) / (100.0 * rpu * rateFreq), 2);
				//  convert to annual
				return Utils.round(total * rateFreq, 2); 
			} else if (benefitAmountModel == 'M') {
				double annualSalary = emp.getCurrentSalary();
				double multOfSalary = bcc.getMaxMultipleOfSalary();
				double minBenefitAmount = bcc.getMinValue();
				double maxBenefitAmount = bcc.getMaxValue();
				short rateFreq = bcc.getRateFrequency();
				if (multOfSalary < .01)
					return 0.0;
				double rpu = bcc.getRatePerUnit();
				if (rpu < .001)
					return 0.0;
				if (annualSalary < 1000.0)  //  must be hourly
					annualSalary *= (40.0 * 52.0);
				int roundPlaces = roundPlaces(bcc.getSalaryRoundAmount());
				switch (bcc.getSalaryRoundType()) {
					case 'N':
						annualSalary = Utils.round(annualSalary, roundPlaces);
						break;
					case 'U':
						annualSalary = Utils.roundUp(annualSalary, roundPlaces);
						break;
					case 'D':
						annualSalary = Utils.roundDown(annualSalary, roundPlaces);
						break;
				}
				
				//  make sure the benefit doesn't go beyond the declared min and max benefit amounts
				//  by adjusting the salary amount to create benefit within the defined range
				double minAnnualSalary = minBenefitAmount / multOfSalary;
				double maxAnnualSalary = maxBenefitAmount / multOfSalary;
				roundPlaces = roundPlaces(bcc.getBenefitRoundAmount());
				if (annualSalary < minAnnualSalary)
					annualSalary = minAnnualSalary;
				if (maxAnnualSalary > 500.0  &&  annualSalary > maxAnnualSalary)
					annualSalary = maxAnnualSalary;
				
				double benefit = annualSalary * multOfSalary;
				switch (bcc.getBenefitRoundType()) {
					case 'N':
						benefit = Utils.round(benefit, roundPlaces);
						break;
					case 'U':
						benefit = Utils.roundUp(benefit, roundPlaces);
						break;
					case 'D':
						benefit = Utils.roundDown(benefit, roundPlaces);
						break;
				}
				if (maxBenefitAmount > 2.0  &&  benefit > maxBenefitAmount)
					benefit = maxBenefitAmount;
				if (benefit < minBenefitAmount)
					benefit = minBenefitAmount;
				
				
				return Utils.round((benefit * bcca.getEeValue() * rateFreq) / rpu, 2);
			} else
				return Utils.round(bcca.getEeValue() * bcc.getRateFrequency(), 2);
		}
		if (employeeCostModel == 'R') {  //  fixed range
			if (benefitAmountModel == 'N')
				return Utils.round(coverageAmount * bcc.getRateFrequency(), 2);
			if (bcca == null)
				return Utils.round(bcc.getFixedEmployeeCost() * bcc.getRateFrequency() * coverageAmount, 2);
			else
				return Utils.round(bcca.getEeValue() * bcc.getRateFrequency() * coverageAmount, 2);
		}
		if (employeeCostModel == 'B') {  //  X percent of benefit based on age
			if (bcca == null)
				return 0.0;
			return Utils.round(bcca.getEeValue() * bcc.getRateFrequency() * coverageAmount, 2);
		}
		return 0.0;
	}

	/**
	 * Calculate the employee's annual cost.  written by Blake
	 * 
	 * This method combines a number of sub-calculation into one routine for easy use.
	 * If employer cost is being calculated this method should not be used because
	 * it unnecessarily duplicated the sub-calculations and is therefore not efficient.
	 * 
	 * @param bbc
	 * @param employee
	 * @param coverageStartDate (0= calculate it)
	 * @param asOfDate
	 * @return annual employee cost
	 */
	public static double calculateEmployeeAnnualCost(BHRBenefitConfig bbc, BEmployee employee, int coverageStartDate, int asOfDate, double coverageAmount) {
		BHRBenefit ben = new BHRBenefit(bbc.getBenefit());
		BBenefitConfigCost bcc = findConfigCost(bbc, employee.getStatusId(), employee.getOrgGroups(), asOfDate);
		BBenefitConfigCostAge bcca = findBenefitConfigCostAge(bcc, employee, coverageStartDate, asOfDate);
		return calculateEmployeeAnnualCost(employee, ben, bcc, bcca, coverageAmount);
	}
	
	/**
	 * Calculate the employee's annual cost.  written by Blake
	 * 
	 * @param bj
	 * @return annual employee cost
	 */
	public static double calculateEmployeeAnnualCost(HrBenefitJoin bj) {
		// if passed the employee benefit join record but someone else is covered, we have to find their record
		HrBenefitJoin coveredBenefitJoin = bj;
		if (bj.getCoverageStartDate() == 0) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			coveredBenefitJoin = hsu.createCriteria(HrBenefitJoin.class)
					.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bj.getHrBenefitConfigId())
					.eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId())
					.eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate())
					.eq(HrBenefitJoin.BENEFIT_APPROVED, bj.getBenefitApproved())
					.ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
					.first();
			if (coveredBenefitJoin == null)
				coveredBenefitJoin = bj;
		}
		
		
		BHRBenefitConfig bbc = new BHRBenefitConfig(bj.getHrBenefitConfig());
		BEmployee employee = new BEmployee(coveredBenefitJoin.getPayingPersonId());
		return calculateEmployeeAnnualCost(bbc, employee, coveredBenefitJoin.getCoverageStartDate(), coveredBenefitJoin.getCoverageStartDate(), coveredBenefitJoin.getAmountCovered());
	}
	
//	private static double getOverrideFlexCost(BHRBenefitJoin bj) {
//
//		BEmployee be = new BEmployee(bj.getPayingPersonId());
//		int remainingPayPeriods = be.getPPYremaining(bj.getCoverageStartDate());
//		if(false) //old way
//			return bj.getAmountCovered() / BEmployee.getPPY(be.getPerson());
//		else
//		{
//			LispPackage lp;
//			double overrideFlexCost;
//
//			try {
////								lp = new LispPackage("BenefitCostCalculator", "com/arahant/lisp/WmCo-BenefitCostCalculator");
//				lp = new LispPackage("WmCoBenefitCostCalculator", "WmCo-BenefitCostCalculator");
//				overrideFlexCost = lp.executeLispReturnDouble("CalculatedFlexCost", bj.getBenefitChangeReasonId(), remainingPayPeriods, bj.getBenefitConfig().getBenefitConfigId());
//				lp.packageDone();
//
//				if (overrideFlexCost > 0.001) {
//					return overrideFlexCost;
//				}
//			} catch (Throwable t) {
//				//  couldn't find lisp file
//				if (showError) {
//					logger.error(t);
//					showError = false;  //  only show the message once
//				}
//			}
//
//			return bj.getAmountCovered() / BEmployee.getPPY(be.getPerson());
//		}
//	}
//	public static void main(String[] args) {
//
//		ABCL.init();
//
//		List<String> configIds = new ArrayList<String>();
//		configIds.add("00001-0000000073");
//		configIds.add("00001-0000000075");
//		configIds.add("00001-0000000076");
//		LispPackage lp = new LispPackage("WmCoBenefitCostCalculator", "../../Configurations/WilliamsonCounty/WmCo-BenefitCostCalculator");
//		HibernateScrollUtil<HrBenefitJoin> scr = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).scroll();
//		while (scr.next()) {
//			HrBenefitJoin j = scr.get();
//			List<IPerson> coveredPeople = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.PAYING_PERSON, j.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, j.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, j.getHrBenefitConfig()).eq(HrBenefitJoin.APPROVED, j.getBenefitApproved()).ne(HrBenefitJoin.COVERAGE_START_DATE, 0) //    .dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, asOfDate)
//					.list();
//
//			System.out.println("jess val = " + StringUtils.sprintf("%.2f", j.overrideAgeCost()) + "  " + j.getBenefitJoinId());
//
//			if (coveredPeople.size() == 0)
//				coveredPeople.add(j.getPayingPerson());
//
//
//			double lres;
//			double jres = j.overrideAgeCost();
//			double diff;
//
//			if (j.getHrBenefitConfigId() != null)
//				lres = lp.executeLispReturnDouble("CalculatedGCICost", j.getCoveredPersonId(), j.getPayingPersonId(), j.getHrBenefitConfigId(), j);
//			else
//				lres = 0.0;
//
//			System.out.println("jess = " + StringUtils.sprintf("%.2f", jres) + ", lisp = " + StringUtils.sprintf("%.2f", lres));
//
//			diff = lres - jres;
//			if (diff < 0.0)
//				diff = -diff;
//			if (diff > .001) {
//				System.out.println("Values are different!");
//				break;
//			}
//
//		}
//		lp.packageDone();
//	}
}
