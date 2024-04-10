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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.calculations;

import com.arahant.beans.BenefitAnswer;
import com.arahant.beans.BenefitDependency;
import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.BenefitRider;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.IPerson;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.LispPackage;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import org.kissweb.StringUtils;
import com.arahant.utils.Utils;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 */
public class HumanaCostCalculator {
	public static final double ANNUAL = 1.00;
	public static final double SEMIANNUAL = 0.51;
	public static final double QUARTERLY = 0.26;
	public static final double MONTHLY = 0.085;
	public static final double WEEKLY = 0.0196154;
	public static final double ANNUAL_FEE = 25.0;


	public HumanaCostCalculator() {}

	public static double calculatePlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered, double modalFactor) {
		BEmployee bemp = new BEmployee(empId);
		BHRBenefit bb = new BHRBenefit(c.getBenefitId());
		boolean child = false;
		final LispPackage lp;
		try {
			LispPackage lpm = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
			String lispFileName = lpm.executeLispReturnString("lisp-file-name-from-package-name", bb.getLispPackageName());
			lpm.packageDone();
			if(bb.getInternalId().equals("MAM"))
				bb.getActive();
			lp = new LispPackage(bb.getLispPackageName(), lispFileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		
		double totalPremium = 0.0;
		
		String state = bemp.getHomeAddress().getState();
		if(StringUtils.isEmpty(state))
			state = bemp.getCompany().getState();

		for(IPerson p : dependents) {
			BPerson bp = new BPerson(p.getPersonId());
//			String smoker = (bp.getTabaccoUse().equals("U") || bp.getTabaccoUse().equals("N")) ? "N" : "Y";
			String smoker = ArahantSession.getHSU().createCriteria(BenefitAnswer.class).eq(BenefitAnswer.PERSON_ID, bp.getPersonId())
																					   .selectFields(BenefitAnswer.STRING_ANSWER)
																					   .joinTo(BenefitAnswer.QUESTION)
																					   .eq(BenefitQuestion.INTERNAL_ID, "1649_A")
																					   .stringVal();
			if(StringUtils.isEmpty(smoker))
				smoker = "N";
			int age = bp.getAgeAsOf(DateUtils.now());
			String relationship = null;
			double tempAmountCovered = amountCovered;
			double unit = (tempAmountCovered / 1000.0);
			HrEmplDependent rel = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bemp.getEmployee())
																							   .eq(HrEmplDependent.PERSON, bp.getPerson())
																							   .first();
			if((bb.getMaxAge() != 0) && (age > bb.getMaxAge() || age < bb.getMinAge()))
				continue;
			if(rel != null)
			{
				relationship = rel.getRelationshipType() + "";
				if(relationship.equals("C")) {
					if(child)
						continue;
					else
						child = true;
				}
				if(!bb.getInternalId().equals("AI") && !bb.getInternalId().equals("HSB"))
				{
					if(relationship.equals("S"))
						tempAmountCovered = tempAmountCovered / 2.0;
					else
					{
						tempAmountCovered = tempAmountCovered / 2.0;
						if(tempAmountCovered > 5000.00)
							tempAmountCovered = 5000.0;
					}
					unit = (tempAmountCovered / 1000.0);
				}
			}
			if(StringUtils.isEmpty(relationship))
				relationship = "E";

			List<BenefitDependency> bds = ArahantSession.getHSU().createCriteria(BenefitDependency.class)
																 .eq(BenefitDependency.REQUIRED_BENEFIT, bb.getBean())
																 .list();

			String intId = bb.getInternalId();
			if(bds.isEmpty()) {
				if(intId.equals("HSB")) {
					tempAmountCovered = c.deprecatedGetMaxEmployeeAmount();
					unit = (tempAmountCovered / 50.0);
				}

				if(intId.equals("AI") || intId.equals("MAM"))
					unit = 1.0;
			}

			DecimalFormat df = new DecimalFormat("0.00");
			df.setRoundingMode(RoundingMode.HALF_UP);

			double lispValue = lp.executeLispReturnDouble(bb.getLispMethodName(), bb.getInternalId(), internalId, relationship, smoker, state, age);
			double currentTotal = Double.valueOf(df.format(Double.valueOf(df.format(lispValue * modalFactor)).doubleValue() * unit)).doubleValue();
			totalPremium += currentTotal;

			System.out.println("HumanaCostCalculator (" + bb.getName() + ") " + totalPremium + " <<< " + "(lp.executeLispReturnDouble(\"CALC-BENEFIT-COST\", " + bb.getInternalId() + ", " + internalId + ", " + relationship + ", " + smoker + ", " + state + ", " + age + ") = " + lispValue + " * " + modalFactor + ") * " + unit + " = " + currentTotal);
		}

		lp.packageDone();
		if(!Utils.doubleEqual(totalPremium, 0, 0.001))
		{
			if(new BHRBenefitConfig(c).getBenefitCategoryCategoryId().equals("00001-0000000020"))
			{
				totalPremium += (HumanaCostCalculator.getAnnualPolicyFeeByState(state) * modalFactor);
			}
		}
		return totalPremium;
	}

	public static double calculateAnnualPlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered) {
		return calculatePlanCostByBenefit(c, internalId, empId, dependents, amountCovered, HumanaCostCalculator.ANNUAL);
	}

	public static double calculateSemiAnnualPlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered) {
		return calculatePlanCostByBenefit(c, internalId, empId, dependents, amountCovered, HumanaCostCalculator.SEMIANNUAL);
	}

	public static double calculateQuarterlyPlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered) {
		return calculatePlanCostByBenefit(c, internalId, empId, dependents, amountCovered, HumanaCostCalculator.QUARTERLY);
	}

	public static double calculateMonthlyPlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered) {
		return calculatePlanCostByBenefit(c, internalId, empId, dependents, amountCovered, HumanaCostCalculator.MONTHLY);
	}

	public static double calculateWeeklyPlanCostByBenefit(HrBenefitConfig c, String internalId, String empId, List<IPerson> dependents, double amountCovered) {
		return calculatePlanCostByBenefit(c, internalId, empId, dependents, amountCovered, HumanaCostCalculator.WEEKLY);
	}

	public static double calculatePlanCostByModalFactor(BHRBenefitJoin bbj, double modalFactor, LispPackage lp) {
		BHRBenefit bene = new BHRBenefit(bbj.getBenefitConfig().getBenefit());
		BEmployee emp = new BEmployee(bbj.getPayingPersonId());
		BPerson dep = new BPerson(bbj.getCoveredPersonId());
//		if(child.getLastName().equals("Subject") && child.getFirstName().equals("Spouse"))
//			emp.getActive();
		BHREmplDependent emplDep = bbj.getRelationship();
//		String smoker = dep.getTabaccoUse().equals("U") ? "N" : bbj.getCoveredPerson().getTabaccoUse();
		String smoker = ArahantSession.getHSU().createCriteria(BenefitAnswer.class).eq(BenefitAnswer.PERSON_ID, dep.getPersonId())
																				   .selectFields(BenefitAnswer.STRING_ANSWER)
																				   .joinTo(BenefitAnswer.QUESTION)
																				   .eq(BenefitQuestion.INTERNAL_ID, "1649_A")
																				   .stringVal();
		if(StringUtils.isEmpty(smoker))
			smoker = "N";
		String locationGroupId = "";
		String state = "";
		if(!StringUtils.isEmpty(emp.getOrgGroupId()))
			locationGroupId = getSuperGroup(emp.getOrgGroupId());
		if(!StringUtils.isEmpty(locationGroupId))
			state = StringUtils.isEmpty(new BOrgGroup(locationGroupId).getState()) ? emp.getHomeAddress().getState() : new BOrgGroup(locationGroupId).getState();
		else
			state = emp.getHomeAddress().getState();
		String relationship = "E";
		int age = bbj.getCoveredPerson().getAgeAsOf(DateUtils.now());
		double units = (bbj.getAmountCovered() / 1000.0);

		if(emplDep != null)
			relationship = emplDep.getRelationshipType();

//		List<HrBenefit> planReqs = (List)ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, bene.getBean())
//																										.selectFields(BenefitDependency.REQUIRED_BENEFIT)
//																										.list();
		List<HrBenefit> planReqs = ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_BASE_BENEFIT)
																						  .eq(BenefitRider.RIDER_BENEFIT, bene.getBean())
																						  .list();
		double modalPremium = 0.0;
		double premium = 0.0;
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);

		if(planReqs.isEmpty()) {
//			List<HrBenefit> autoEnrolled = (List)ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
//																		.eq(HrBenefitJoin.COVERED_PERSON_ID, bbj.getCoveredPersonId())
//																		.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
//																		.in(HrBenefitConfig.HR_BENEFIT, planReqs)
//																		.selectFields(HrBenefitConfig.HR_BENEFIT)
//																		.joinTo((HrBenefitConfig.HR_BENEFIT))
//																		.eq(HrBenefit.BENEFIT_PROVIDER, bene.getProvider())
//																		.joinTo(HrBenefit.BENEFIT_CATEGORY)
//																		.eq(HrBenefitCategory.EXCLUSIVE, 'N')
//																		.list();
//			autoEnrolled.add(bene.getBean());
//			for(HrBenefit b : autoEnrolled) {
				modalPremium = lp.executeLispReturnDouble(bene.getLispMethodName(), bene.getInternalId(), bene.getInternalId(), relationship, smoker, state, age);
				premium = Double.valueOf(df.format(Double.valueOf(df.format(modalPremium * modalFactor)).doubleValue() * units)).doubleValue();
//			}
		}
		else {
			HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																		  .eq(HrBenefitJoin.COVERED_PERSON_ID, bbj.getCoveredPersonId());
			if(!BProperty.getBoolean("WizardDemoMode"))
				hcu.eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
			List<HrBenefit> basePlan = (List)hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
												.gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0)
												.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
												.in(HrBenefitConfig.HR_BENEFIT, planReqs)
												.selectFields(HrBenefitConfig.HR_BENEFIT)
												.joinTo(HrBenefitConfig.HR_BENEFIT)
												.joinTo(HrBenefit.RIDERS_ON_BASE_BENEFIT)
												.eq(BenefitRider.RIDER_BENEFIT, bene.getBean())
												.list();
			if(!basePlan.isEmpty()) {
				if(bene.getInternalId().equals("HSB"))
					units = (bbj.getAmountCovered() / 50.0);
				else if(bene.getInternalId().equals("AI") || bene.getInternalId().equals("MAM"))
					units = 1.0;
//				System.out.println(bene.getLispMethodName()+"|||"+bene.getInternalId()+"|||"+basePlan.get(0).getInternalId()+"|||"+relationship+"|||"+smoker+"|||"+state+"|||"+age+"|||"+units);
				modalPremium = lp.executeLispReturnDouble(bene.getLispMethodName(), bene.getInternalId(), basePlan.get(0).getInternalId(), relationship, smoker, state, age);
				premium = Double.valueOf(df.format(Double.valueOf(df.format(modalPremium * modalFactor)).doubleValue() * units)).doubleValue();
//				System.out.println("                      " + modalPremium + " = " + premium);
			}
			else
				throw new ArahantException("Person is enrolled in an optional benefit but not its required benefit.");
		}

		return premium;
	}

	public static double calculatePlanCostByModalFactor(HrBenefitJoin bj, double modalFactor, LispPackage lp) {
		return calculatePlanCostByModalFactor(new BHRBenefitJoin(bj), modalFactor, lp);
	}

	public static double calculateAnnualPlanCost(HrBenefitJoin bj, LispPackage lp) {
		return calculatePlanCostByModalFactor(bj, HumanaCostCalculator.ANNUAL, lp);
	}

	public static double calculateSemiAnnualPlanCost(HrBenefitJoin bj, LispPackage lp) {
		return calculatePlanCostByModalFactor(bj, HumanaCostCalculator.SEMIANNUAL, lp);
	}

	public static double calculateQuarterlyPlanCost(HrBenefitJoin bj, LispPackage lp) {
		return calculatePlanCostByModalFactor(bj, HumanaCostCalculator.QUARTERLY, lp);
	}

	public static double calculateMonthlyPlanCost(HrBenefitJoin bj, LispPackage lp) {
		return calculatePlanCostByModalFactor(bj, HumanaCostCalculator.MONTHLY, lp);
	}

	public static double calculateWeeklyPlanCost(HrBenefitJoin bj, LispPackage lp) {
		return calculatePlanCostByModalFactor(bj, HumanaCostCalculator.WEEKLY, lp);
	}

	public static double getAnnualPolicyFeeByState(String state) {
		double fee = HumanaCostCalculator.ANNUAL_FEE;

		if(state.equals("MS"))
			fee = 6.00;

		return fee;
	}

	private static String getSuperGroup(String org) {
		try {
			BOrgGroup bog = new BOrgGroup(org);

			if(StringUtils.isEmpty(bog.getState()))
				if(bog.getParent() != null && !StringUtils.isEmpty(bog.getParent().getOrgGroupId()))
					return getSuperGroup(bog.getParent().getOrgGroupId());

			return org;
		}
		catch(Exception e) {
			return "";
		}
    }
}
