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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.ABCL;
import com.arahant.lisp.LispPackage;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import org.kissweb.StringUtils;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FidelityLifeCostCalculator {
//	public static final double ANNUAL = 1.00;
//	public static final double SEMIANNUAL = 0.5;
//	public static final double QUARTERLY = 0.25;
//	public static final double MONTHLY = 0.08333;
//	public static final double WEEKLY = 0.08333;
//	public static final double POLICY_FEE = 0.0;
	public static final String ANNUAL = "Annual";
	public static final String SEMIANNUAL = "Semi-Annual";
	public static final String QUARTERLY = "Quarterly";
	public static final String SEMIMONTHLY = "Semi-Monthly";
	public static final String MONTHLY = "Monthly";
	public static final String BIWEEKLY = "Bi-Weekly";
	public static final String WEEKLY = "Weekly";
	public static final double POLICY_FEE = 0.0;

	private static final HashMap<String, Integer> deductions = new HashMap<String, Integer>() {
		{
			put(ANNUAL, 1);
			put(SEMIANNUAL, 2);
			put(QUARTERLY, 4);
			put(MONTHLY, 12);
			put(WEEKLY, 52);
		}
	};

	private static final HashMap<String, Integer[]> factors = new HashMap<String, Integer[]>() {
		{
			//Billing:Deductions
			put(MONTHLY + ":" + WEEKLY, new Integer[]{13,13});
			put(WEEKLY + ":" + WEEKLY, new Integer[]{1,1});

			put(MONTHLY + ":" + BIWEEKLY, new Integer[]{13,13});
			put(BIWEEKLY + ":" + BIWEEKLY, new Integer[]{13,13});

			put(MONTHLY + ":" + SEMIMONTHLY, new Integer[]{2,2});
			put(MONTHLY + ":" + MONTHLY, new Integer[]{1,1});

			put(QUARTERLY + ":" + MONTHLY, new Integer[]{3,1});
			put(QUARTERLY + ":" + SEMIMONTHLY, new Integer[]{6,2});
			put(QUARTERLY + ":" + QUARTERLY, new Integer[]{3,1});

			put(SEMIANNUAL + ":" + MONTHLY, new Integer[]{6,1});
			put(SEMIANNUAL + ":" + SEMIMONTHLY, new Integer[]{12,2});
			put(SEMIANNUAL + ":" + SEMIANNUAL, new Integer[]{6,1});

			put(ANNUAL + ":" + MONTHLY, new Integer[]{12,1});
			put(ANNUAL + ":" + SEMIMONTHLY, new Integer[]{24,2});
			put(ANNUAL + ":" + ANNUAL, new Integer[]{12,1});
		}
	};

	private static final HashMap<String, Double> modalFactorsLBT = new HashMap<String, Double>() {
		{
			put(ANNUAL, 1.0);
			put(SEMIANNUAL, 0.5);
			put(QUARTERLY, 0.25);
			put(MONTHLY, 0.08333);
			put(WEEKLY, 0.08333);
		}
	};

	private static final HashMap<String, Double> modalFactorsGDB = new HashMap<String, Double>() {
		{
			put(ANNUAL, 1.0);
			put(SEMIANNUAL, 0.520);
			put(QUARTERLY, 0.280);
			put(MONTHLY, 0.087);
			put(WEEKLY, 0.087);
		}
	};

	private static final List<String> LBT = new ArrayList<String>() {
		{
			add("LBT100");
			add("LBT70");
			add("LBT50");
			add("LBT30");
			add("LBT0");
		}
	};

	private static final List<String> GDB = new ArrayList<String>() {
		{
			add("GDBB50");
		}
	};

	public static final List<String> MAD = new ArrayList<String>() {
		{
			add("MADIV");
			add("MADIP");
			add("MADIS");
			add("MADPV");
			add("MADPP");
			add("MADPS");
		}
	};


	private static List<String> getRiderJoinIds(BHRBenefitJoin bbj) throws ArahantException {
		List<String> beneJoins = new ArrayList<String>();
		List<String> includeBenefits = (List) ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.RIDER_BENEFIT_ID).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, bbj.getBenefitId()).list();
		beneJoins = (List) ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
						.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
						.eq(HrBenefitJoin.APPROVED, bbj.getBenefitApproved() ? 'Y' : 'N')
						.eq(HrBenefitJoin.PAYING_PERSON_ID, bbj.getPayingPersonId())
						.eqJoinedField(HrBenefitJoin.PAYING_PERSON_ID, HrBenefitJoin.COVERED_PERSON_ID)
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.in(HrBenefitConfig.HR_BENEFIT_ID, includeBenefits)
						.list();

		List<String> temp = new ArrayList<String>();
		for(String s : beneJoins)
			temp.addAll(getRiderJoinIds(new BHRBenefitJoin(s)));
		beneJoins.addAll(temp);
		return beneJoins;
	}

	public FidelityLifeCostCalculator() {}

	//Calculates total cost for base and rider benefits
	public static double calculateTotalPremium(String baseJoin, List<String> beneJoins, String modalFactor) throws Exception {
		List<BHRBenefitJoin> joins = new ArrayList<BHRBenefitJoin>();
		BHRBenefitJoin bbj = new BHRBenefitJoin(baseJoin);
		//If I come in without any rider joins (null only, not empty)
		//try to get them
		if(beneJoins == null)
		{
			HrBenefitJoin requiredJoin = bbj.getRiderJoin();
			//if this is a rider enrollment, return the cost for just this rider, but we must find the required join first
			if(requiredJoin != null)
			{
				return 0;
			}
			else
			{
				beneJoins = getRiderJoinIds(bbj);
			}
		}
		if(bbj.getRequestedCost() > 0 && bbj.getRequestedCostPeriod() == modalFactor.charAt(0))
			return bbj.getRequestedCost();
		BHRBenefit base = new BHRBenefit(bbj.getBenefitConfig().getBenefitId());
		BEmployee bemp = new BEmployee(bbj.getPayingPersonId());
		BPerson realOrChangePerson = new BPerson(bbj.getPayingPersonId());
		if(!bbj.getBenefitApproved() && realOrChangePerson.hasPending(bbj.getPayingPersonId()))
			realOrChangePerson.loadPending(bbj.getPayingPersonId());
		BPerson bp = new BPerson(realOrChangePerson.getPersonId());
		joins.add(bbj);
		for(HrBenefitJoin dbj : bbj.getDependentBenefitJoins())
		{
			joins.add(new BHRBenefitJoin(dbj));
		}
		for(String id : beneJoins) {
			BHRBenefitJoin bj = new BHRBenefitJoin(id);
			joins.add(bj);
			for(HrBenefitJoin dbj : bj.getDependentBenefitJoins())
			{
				joins.add(new BHRBenefitJoin(dbj));
			}
		}
		Integer[] fa = getFactors(MONTHLY, modalFactor);
		Integer f1 = fa[0];
		Integer f2 = fa[1];

		LispPackage lpm = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
		String lispFileName = lpm.executeLispReturnString("lisp-file-name-from-package-name", base.getLispPackageName());
		lpm.packageDone();
		final LispPackage lp = new LispPackage(base.getLispPackageName(), lispFileName);

		if(MAD.contains(base.getInternalId())) {
			int deps = 0;
			BHRBenefitConfig config = new BHRBenefitConfig(bbj.getBenefitConfigId());
			String state = "";
			String zipcode = "";
			if(bemp.getHomeAddress() != null) {
				state = bemp.getHomeAddress().getState();
				zipcode = bemp.getHomeAddress().getZip();
			}
			if(config.getCoversEmployeeSpouseOrChildren()) {
				deps = config.getMaxChildren();
				if(config.getMaxChildren() == 0 || config.getMaxChildren() > 6)
					deps = 6;
				else
					deps = config.getMaxChildren();
			}

			double premium = 0;
			try {
				premium = lp.executeLispReturnDouble(base.getLispMethodName(), base.getInternalId(), deps, state, zipcode);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}

			return roundDecimal((roundDecimal(premium, 2) * 12 + getPolicyFee(base.getInternalId())) / deductions.get(modalFactor), 2) + getMADAdminFee(modalFactor);
		}
		
		double premiumUnit = 0.0;
		double runningTotal = 0.0;
		double tempPremium;
		boolean dcobChild = false;

		String trace = modalFactor + " calc for " + base.getName() + "\n";
		for(BHRBenefitJoin j : joins) {
			if(j.getEmployeeCovered().equals("N") || j.isDecline())
				continue;
			BHRBenefit b = new BHRBenefit(j.getBenefitConfig().getBenefitId());
			if(b.getInternalId().equals("DCOB") && dcobChild)
				continue;
			if(j.isDependentBenefitJoin()) {
				if(j.getPolicyBenefitJoin() != null)
					if(j.getPolicyBenefitJoin().getEmployeeCovered() == 'N')
						bp = new BPerson(j.getCoveredPersonId());
			}
			if(StringUtils.isEmpty(b.getInternalId()) || b.getInternalId().toLowerCase().contains("ltr"))
				bp = new BPerson(j.getCoveredPersonId());
			String smoker = (bp.getTabaccoUse().equals("U") || bp.getTabaccoUse().equals("N")) ? "N" : "Y";
			int age = bp.getAgeAsOf(DateUtils.now());

			double amountCovered = j.getAmountCovered();
			try {
				premiumUnit = lp.executeLispReturnDouble(base.getLispMethodName(), b.getInternalId(), base.getInternalId(), age, smoker);
			}
			catch(Exception ex) {
				if(lp != null)
					lp.packageDone();
			}
			
			//System.out.println("Fidelity Life Cost Calc: " + premiumUnit + " = " + base.getLispMethodName() + ", " + b.getInternalId() + ", " + base.getInternalId() + ", " + age + ", " + smoker);
			double unit = amountCovered / 1000.0;
			tempPremium = roundDecimal(unit * premiumUnit, 2);
			if(b.getInternalId().equals(base.getInternalId()))
				tempPremium += getPolicyFee(base.getInternalId());
			trace += "\t" + MoneyUtils.formatMoney(premiumUnit) + " = " + b.getInternalId() + " - " + base.getInternalId() + " - " + age + " - " + smoker + " - " + MoneyUtils.formatMoney(amountCovered) + " (" + j.getBenefitJoinId() +") = " + MoneyUtils.formatMoney(tempPremium) + "\n";
			//System.out.println(b.getName() + ": " + tempPremium);
			runningTotal += tempPremium;

			if(b.getInternalId().equals("DCOB"))
				dcobChild = true;
		}
		trace += "\t--------------------------\n";
		double annualPremium = roundDecimal(runningTotal * getModalFactor(modalFactor, base.getInternalId()), 2);
		trace += "\t" + MoneyUtils.formatMoney(runningTotal) + " * " + getModalFactor(modalFactor, base.getInternalId()) + " = " + annualPremium + "\n";
		annualPremium = roundDecimal(annualPremium / f1, 2);
		trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " / " + f1 + " = " + annualPremium + "\n";
		annualPremium = roundDecimal(annualPremium * f2, 2);
		trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " * " + f2 + " = " + annualPremium + "\n";
		annualPremium = roundDecimal(annualPremium * 12, 2);
		trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " * 12" + " = " + annualPremium + "\n";
		annualPremium = roundDecimal(annualPremium / deductions.get(modalFactor), 2);
		trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " / " + deductions.get(modalFactor) + " = " + annualPremium + "\n";

		lp.packageDone();
		trace += "\t--------------------------\n";
		trace += "\t" + MoneyUtils.formatMoney(annualPremium);
		System.out.println(trace);
		if(modalFactor.equals(WEEKLY) && annualPremium < 3.0)
			annualPremium = 3.0;
		else if(modalFactor.equals(MONTHLY) && annualPremium < 13.0)
			annualPremium = 13.0;
		else if(modalFactor.equals(ANNUAL) && annualPremium < 156.0)
			annualPremium = 156.0;

		return annualPremium;
	}

	public static double calculateTotalPremiumByBenefit(String baseJoin, List<String> beneJoins, String modalFactor, boolean ltrCoverageCalc) throws Exception {
		BHRBenefitJoin bbj = new BHRBenefitJoin(baseJoin);
		//If I come in without any rider joins (null only, not empty)
		//try to get them
		if(beneJoins == null)
		{
			List<String> excludeRiders = (List)ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, bbj.getBenefitId()).eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').list();
			List<String> includeBenefits = (List)ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.RIDER_BENEFIT_ID).notIn(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID, excludeRiders).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, bbj.getBenefitId()).list();

			beneJoins = (List)ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
					.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
					.ne(HrBenefitJoin.EMPLOYEE_COVERED, 'N')
					.eq(HrBenefitJoin.PAYING_PERSON_ID, bbj.getPayingPersonId())
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.in(HrBenefitConfig.HR_BENEFIT_ID, includeBenefits)
					.list();
		}

		List<BHRBenefitJoin> joins = new ArrayList<BHRBenefitJoin>();
		BHRBenefit base = new BHRBenefit(bbj.getBenefitConfig().getBenefitId());
		BEmployee bemp = new BEmployee(bbj.getPayingPersonId());
		BPerson realOrChangePerson = new BPerson(bbj.getPayingPersonId());
		if(!bbj.getBenefitApproved() && realOrChangePerson.hasPending(bbj.getPayingPersonId()))
			realOrChangePerson.loadPending(bbj.getPayingPersonId());
		BPerson bp = new BPerson(realOrChangePerson.getPersonId());
		for(String id : beneJoins) {
			BHRBenefitJoin bj = new BHRBenefitJoin(id);
			joins.add(bj);
		}
		Integer[] fa = getFactors(MONTHLY, modalFactor);
		Integer f1 = fa[0];
		Integer f2 = fa[1];

		LispPackage lpm = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
		String lispFileName = lpm.executeLispReturnString("lisp-file-name-from-package-name", base.getLispPackageName());
		lpm.packageDone();
		LispPackage lp = new LispPackage(base.getLispPackageName(), lispFileName);
		lp = new LispPackage(base.getLispPackageName(), lispFileName);

		if(MAD.contains(base.getInternalId())) {
			int deps = 0;
			BHRBenefitConfig config = new BHRBenefitConfig(bbj.getBenefitConfigId());
			String state = "";
			String zipcode = "";
			if(bemp.getHomeAddress() != null) {
				state = bemp.getHomeAddress().getState();
				zipcode = bemp.getHomeAddress().getZip();
			}
			if(config.getCoversEmployeeSpouseOrChildren()) {
				deps = config.getMaxChildren();
				if(config.getMaxChildren() == 0 || config.getMaxChildren() > 6)
					deps = 6;
				else
					deps = config.getMaxChildren();
			}

			double premium = 0;
			try {
				premium = lp.executeLispReturnDouble(base.getLispMethodName(), base.getInternalId(), deps, state, zipcode);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}

			return roundDecimal((roundDecimal(premium, 2) * 12 + getPolicyFee(base.getInternalId())) / deductions.get(modalFactor), 2) + getMADAdminFee(modalFactor);
		}


		double premiumUnit = 0.0;
		double runningTotal = 0.0;
		double tempPremium;
		boolean dcobChild = false;
		String trace = modalFactor + " calc for " + base.getName() + " (By Benefit Method)\n";

		for(BHRBenefitJoin j : joins) {
			if(j.getEmployeeCovered().equals("N") || j.isDecline())
				continue;
			BHRBenefit b = new BHRBenefit(j.getBenefitConfig().getBenefitId());
			if(b.getInternalId().equals("DCOB") && dcobChild)
				continue;
			if(j.isDependentBenefitJoin()) {
				if(j.getPolicyBenefitJoin() != null)
					if(j.getPolicyBenefitJoin().getEmployeeCovered() == 'N')
						bp = new BPerson(j.getCoveredPersonId());
			}
			if(StringUtils.isEmpty(b.getInternalId()) || b.getInternalId().toLowerCase().contains("ltr"))
				bp = new BPerson(j.getCoveredPersonId());
			String smoker = (bp.getTabaccoUse().equals("U") || bp.getTabaccoUse().equals("N")) ? "N" : "Y";
			int age = bp.getAgeAsOf(DateUtils.now());
			double amountCovered = j.getAmountCovered();
			try {
				premiumUnit = lp.executeLispReturnDouble(base.getLispMethodName(), b.getInternalId(), base.getInternalId(), age, smoker);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}
			trace += "\t" + MoneyUtils.formatMoney(premiumUnit) + " = " + b.getInternalId() + " - " + base.getInternalId() + " - " + age + " - " + smoker + " - " + MoneyUtils.formatMoney(amountCovered) + "\n";
			//System.out.println("Fidelity Life Cost Calc: " + premiumUnit + " = " + base.getLispMethodName() + ", " + b.getInternalId() + ", " + base.getInternalId() + ", " + age + ", " + smoker);
			double unit = amountCovered / 1000.0;
			tempPremium = roundDecimal(unit * premiumUnit, 2);
			if(b.getInternalId().equals(base.getInternalId()))
				tempPremium += getPolicyFee(base.getInternalId());
			//System.out.println(b.getName() + ": " + tempPremium + " (By Benefit)");
			runningTotal += tempPremium;

			if(b.getInternalId().equals("DCOB"))
				dcobChild = true;
		}
		System.out.println("");

		double annualPremium = runningTotal;
		if(!ltrCoverageCalc) {
			annualPremium = roundDecimal(runningTotal * getModalFactor(modalFactor, base.getInternalId()), 2);
			annualPremium = roundDecimal(annualPremium / f1, 2);
			annualPremium = roundDecimal(annualPremium * f2, 2);
			annualPremium = roundDecimal(annualPremium * 12, 2);
			annualPremium = roundDecimal(annualPremium / deductions.get(modalFactor), 2);
		}
		trace += "\t--------------------------\n";
		trace += "\t" + MoneyUtils.formatMoney(annualPremium);
		System.out.println(trace);
		return annualPremium;
	}

	public static double calculateTotalPremiumByBenefit(String configId, String baseBeneIntId, String payId, List<String> covIds, double amountCovered, String modal, boolean ltrCoverageCalc) throws Exception {
		BHRBenefit bene = new BHRBenefit(new BHRBenefitConfig(configId).getBenefitId());
		BPerson bp = new BPerson(payId);
		BEmployee bemp = new BEmployee(payId);
		Integer[] fa = getFactors(MONTHLY, modal);
		Integer f1 = fa[0];
		Integer f2 = fa[1];

		LispPackage lpm = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
		String lispFileName = lpm.executeLispReturnString("lisp-file-name-from-package-name", bene.getLispPackageName());
		lpm.packageDone();
		final LispPackage lp = new LispPackage(bene.getLispPackageName(), lispFileName);

		if(MAD.contains(baseBeneIntId)) {
			int deps = 0;
			BHRBenefitConfig config = new BHRBenefitConfig(configId);
			String state = "";
			String zipcode = "";
			if(bemp.getHomeAddress() != null) {
				state = bemp.getHomeAddress().getState();
				zipcode = bemp.getHomeAddress().getZip();
			}
			if(config.getCoversEmployeeSpouseOrChildren()) {
				deps = config.getMaxChildren();
				if(config.getMaxChildren() == 0 || config.getMaxChildren() > 6)
					deps = 6;
				else
					deps = config.getMaxChildren();
			}
			
			double premium = 0;
			try {
				premium = lp.executeLispReturnDouble(bene.getLispMethodName(), baseBeneIntId, deps, state, zipcode);
//				System.out.println("Deps: " + deps + ", State: " + state + ", Premium: " + premium + ", Modal: " + modal);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}

			return roundDecimal((roundDecimal(premium, 2) * 12 + getPolicyFee(bene.getInternalId())) / deductions.get(modal), 2) + getMADAdminFee(modal);
		}

		double premiumUnit = 0.0;
		double runningTotal = 0.0;
		double tempPremium;
		boolean dcobChild = false;
		String trace = modal + " calc for " + bene.getName() + " (By Benefit Method)\n";

		for(String covId : covIds) {
			if(bene.getInternalId().equals("DCOB") && dcobChild)
				continue;
			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, payId).eq(HrBenefitJoin.COVERED_PERSON_ID, covId).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).orderBy(HrBenefitJoin.APPROVED).first();
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
			if(bj != null)
				amountCovered = bj.getAmountCovered();
			if(bbj.isDependentBenefitJoin()) {
				if(bbj.getPolicyBenefitJoin() != null)
					if(bbj.getPolicyBenefitJoin().getEmployeeCovered() == 'N')
						bp = new BPerson(bbj.getCoveredPersonId());
			}
			if(StringUtils.isEmpty(bene.getInternalId()) || bene.getInternalId().toLowerCase().contains("ltr"))
				bp = new BPerson(covId);
			String smoker = (bp.getTabaccoUse().equals("U") || bp.getTabaccoUse().equals("N")) ? "N" : "Y";
			int age = bp.getAgeAsOf(DateUtils.now());
			try {
				premiumUnit = lp.executeLispReturnDouble(bene.getLispMethodName(), bene.getInternalId(), baseBeneIntId, age, smoker);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}
//			finally {
//				if(lp != null)
//					lp.packageDone();
//			}
			
			//System.out.println("Fidelity Life Cost Calc: " + premiumUnit + " = " + base.getLispMethodName() + ", " + b.getInternalId() + ", " + base.getInternalId() + ", " + age + ", " + smoker);
			double unit = amountCovered / 1000.0;
			tempPremium = unit * premiumUnit;//roundDecimal(unit * premiumUnit, 2);
			if(bene.getInternalId().equals(baseBeneIntId))
				tempPremium += getPolicyFee(baseBeneIntId);
			trace += "\t" + MoneyUtils.formatMoney(premiumUnit) + " = " + bene.getInternalId() + " - " + baseBeneIntId + " - " + age + " - " + smoker + " - " + MoneyUtils.formatMoney(amountCovered) + " = " + MoneyUtils.formatMoney(tempPremium) + "\n";
			//System.out.println(b.getName() + ": " + tempPremium + " (By Benefit)");
			runningTotal += tempPremium;

			if(bene.getInternalId().equals("DCOB"))
				dcobChild = true;
		}

		System.out.println("");

		double annualPremium = runningTotal;
		if(!ltrCoverageCalc) {
			trace += "\t--------------------------\n";
			trace += "\t" + MoneyUtils.formatMoney(runningTotal) + " * " + getModalFactor(modal, baseBeneIntId) + "\n";
			annualPremium = runningTotal * getModalFactor(modal, baseBeneIntId);//roundDecimal(runningTotal * getModalFactor(modal), 2);
			trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " / " + f1 + "\n";
			annualPremium = annualPremium / f1;//roundDecimal(annualPremium / f1, 2);
			trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " * " + f2 + "\n";
			annualPremium = annualPremium * f2;//roundDecimal(annualPremium * f2, 2);
			trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " * 12\n";
			annualPremium = annualPremium * 12;//roundDecimal(annualPremium * 12, 2);
			trace += "\t" + MoneyUtils.formatMoney(annualPremium) + " / " + deductions.get(modal) + "\n";
			annualPremium = annualPremium / deductions.get(modal);//roundDecimal(annualPremium / deductions.get(modal), 2);
		}

		lp.packageDone();
		trace += "\t--------------------------\n";
		trace += "\t" + MoneyUtils.formatMoney(annualPremium);
		System.out.println(trace);
		return annualPremium;
	}

	public static double calculateTotalCoverageAmount(String baseJoin, List<String> joinIds, double moneyPurchase, String modalFactor) throws Exception {
		List<BHRBenefitJoin> joins = new ArrayList<BHRBenefitJoin>();
		List<String> ltrJoinIds = new ArrayList<String>();
		BHRBenefit base = new BHRBenefit(new BHRBenefitJoin(baseJoin).getBenefitConfig().getBenefitId());
		BHRBenefitJoin bbj = new BHRBenefitJoin(baseJoin);
		BEmployee bemp = new BEmployee(bbj.getPayingPersonId());
		BPerson realOrChangePerson = new BPerson(bbj.getPayingPersonId());
		if(!bbj.getBenefitApproved() && realOrChangePerson.hasPending(bbj.getPayingPersonId()))
			realOrChangePerson.loadPending(bbj.getPayingPersonId());
		BPerson bp = new BPerson(realOrChangePerson.getPersonId());

		int deduct = deductions.get(modalFactor);
		double premiumUnit = 0;
		double tempPremium;

		double sap = roundDecimal((moneyPurchase - 0.01) * deduct, 2);
		double runningTotal = 0.0;
		sap -= getPolicyFee(base.getInternalId());
		joins.add(bbj);
		for(HrBenefitJoin dbj : bbj.getDependentBenefitJoins())
		{
			joins.add(new BHRBenefitJoin(dbj));
		}
		for(String id : joinIds) {
			BHRBenefitJoin bj = new BHRBenefitJoin(id);
			joins.add(bj);
			for(HrBenefitJoin dbj : bj.getDependentBenefitJoins())
			{
				joins.add(new BHRBenefitJoin(dbj));
			}
		}
		LispPackage lpm = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
		String lispFileName = lpm.executeLispReturnString("lisp-file-name-from-package-name", base.getLispPackageName());
		lpm.packageDone();
		final LispPackage lp = new LispPackage(base.getLispPackageName(), lispFileName);
		boolean dcobChild = false;

		for(BHRBenefitJoin j : joins) {
			if(j.getEmployeeCovered().equals("N") || j.isDecline())
				continue;
			BHRBenefit b = new BHRBenefit(j.getBenefitConfig().getBenefitId());
			if(b.getInternalId().equals("DCOB") && dcobChild)
				continue;
			if(bbj.isDependentBenefitJoin()) {
				if(bbj.getPolicyBenefitJoin() != null)
					if(bbj.getPolicyBenefitJoin().getEmployeeCovered() == 'N')
						bp = new BPerson(bbj.getCoveredPersonId());
			}
			if(StringUtils.isEmpty(b.getInternalId()) || b.getInternalId().toLowerCase().contains("ltr")) {
				ltrJoinIds.add(j.getBenefitJoinId());
				continue;
			}
			String smoker = (bp.getTabaccoUse().equals("U") || bp.getTabaccoUse().equals("N")) ? "N" : "Y";
			int age = bp.getAgeAsOf(DateUtils.now());
			try {
				System.out.println("Fidelity Life Cost Calc: " + premiumUnit + " = " + base.getLispMethodName() + ", " + b.getInternalId() + ", " + base.getInternalId() + ", " + age + ", " + smoker);
				premiumUnit = lp.executeLispReturnDouble(base.getLispMethodName(), b.getInternalId(), base.getInternalId(), age, smoker);
			}
			catch (Exception ex) {
				System.err.println("Error retrieving value from LISP.");
				if(lp != null)
					lp.packageDone();
			}
//			finally {
//				if(lp != null)
//					lp.packageDone();
//			}
			tempPremium = roundDecimal(premiumUnit * getModalFactor(modalFactor, base.getInternalId()) * (int)(1/getModalFactor(modalFactor, base.getInternalId())), 2);
			runningTotal += tempPremium;

			if(b.getInternalId().equals("DCOB"))
				dcobChild = true;
		}
		sap -= FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(baseJoin, ltrJoinIds, modalFactor, true);
		double coveredAmount = (int)((sap / runningTotal) * 1000);

		lp.packageDone();
		return coveredAmount;
	}
	
	public static double roundDecimal(double value, int decimalPlaces) {
		String format = "0" + (decimalPlaces == 0 ? "" : ".");
		for(int i = 0; i < decimalPlaces; i++)
			format = format.concat("0");

		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(RoundingMode.HALF_UP);

		return Double.valueOf(df.format(value)).doubleValue();
	}

	private static Integer[] getFactors(String billing, String deductions) {
		if(factors.get(billing + ":" + deductions) == null)
			return factors.get(deductions + ":" + deductions);
		else
			return factors.get(billing + ":" + deductions);
	}

	public static double getModalFactor(String key, String baseInternalId) {
		if(LBT.contains(baseInternalId))
			return modalFactorsLBT.get(key);
		else if(GDB.contains(baseInternalId))
			return modalFactorsGDB.get(key);
		else
			return 0;
	}

	public static double getPolicyFee(String baseInternalId) {
		if(LBT.contains(baseInternalId))
			return 0;
		else if(GDB.contains(baseInternalId))
			return 75;
		else if(MAD.contains(baseInternalId))
			return 12;
		else
			return 0;
	}

	public static double getMADAdminFee(String modal) {
		if(modal.equals(MONTHLY))
			return 5;
		else if(modal.equals(QUARTERLY))
			return 7.5;
		else if(modal.equals(ANNUAL))
			return 10;
		else
			return 0;
	}

	public static void main(String[] args) {
		ABCL.init();
		ArahantSession.getHSU().setCurrentPersonToArahant();
		ArahantSession.getHSU().beginTransaction();

		HrBenefitConfig lbt100 = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.INTERNAL_ID, "LBT100").first();
//		HrBenefit lbt70 = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "LBT70").first();
//		HrBenefit lbt50 = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "LBT50").first();
//		HrBenefit lbt30 = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "LBT30").first();
//		HrBenefit lbt0 = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "LBT0").first();

		HrBenefitConfig adob = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.INTERNAL_ID, "ADOB").first();
		HrBenefitConfig ltr = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, "00001-0000000115").first();
//		HrBenefit dcob = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "DCOB").first();
		HrBenefitConfig wpob = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.INTERNAL_ID, "WPOB").first();
//		HrBenefit pwob = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "PWOB").first();
//		HrBenefit giob = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "GIOB").first();
		HrBenefitConfig ltc = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.INTERNAL_ID, "LTC").first();
//		HrBenefit ltcti = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "LTCTI").first();
//		HrBenefit ti = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.INTERNAL_ID, "TI").first();
		HrBenefitConfig wpobltr = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, "00001-0000000122").first();

//		List<HrBenefitJoin> joins = new ArrayList<HrBenefitJoin>();
		List<String> joins = new ArrayList<String>();


		/*
		 * WITH RIDERS
		 */
//		optionals.add(adob);
//		optionals.add(ltr);
//		optionals.add(wpobltr);
//		optionals.add(ltc);
//		optionals.add(wpob);

		/*
		 * UNIVERSAL (EMPLOYEE)
		 */
		BEmployee p = new BEmployee();
		p.create();
		p.setDob(19811002);
		p.setSmoker(false);
		p.setFirstName("Blah");
		p.setLastName("Blah");
		p.insert();

		/*
		 * UNIVERSAL (DEPEDENTS)
		 */
		BPerson d = new BPerson();
		d.create();
		d.setDob(19761002);
		d.setSmoker(false);
		d.setFirstName("Blah");
		d.setLastName("Blah");
		d.insert();

		/*
		 * Setting up coverage amounts
		 */
//		amountCovered.put(p.getPersonId() + lbt100.getBenefitId(), 103516);
//		amountCovered.put(p.getPersonId() + adob.getBenefitId(), 103516);
//		amountCovered.put(p.getPersonId() + lbt100.getBenefitId(), 50000);
//		System.out.println(p.getPersonId() + lbt100.getBenefitId());
//		amountCovered.put(p.getPersonId() + ltc.getBenefitId(), 50000);
//		System.out.println(p.getPersonId() + ltc.getBenefitId());
//		amountCovered.put(p.getPersonId() + wpob.getBenefitId(), 50000);
//		System.out.println(p.getPersonId() + wpob.getBenefitId());
//		amountCovered.put(d.getPersonId() + ltr.getBenefitId(), 25000);
//		System.out.println(p.getPersonId() + ltr.getBenefitId());
//		amountCovered.put(d.getPersonId() + wpobltr.getBenefitId(), 25000);
//		System.out.println(p.getPersonId() + wpobltr.getBenefitId() + "\n");

		/*
		 * WITH DEPENDENT
		 */
//		deps.add(new BPerson(empId).getPerson());
		double baseAmt = 50000;
		double ltrAmt = 25000;

		BHRBenefitJoin base = new BHRBenefitJoin();
		base.create();
		base.setHrBenefitConfig(lbt100);
		base.setAmountCovered(baseAmt);
		base.setPayingPerson(p.getPerson());
		base.setCoveredPerson(p.getPerson());
		base.insert();

//		BHRBenefitJoin adobJ = new BHRBenefitJoin();
//		adobJ.create();
//		adobJ.setHrBenefitConfig(adob);
//		adobJ.setAmountCovered(baseAmt);
//		adobJ.setPayingPerson(p.getPerson());
//		adobJ.setCoveredPerson(p.getPerson());
//		adobJ.insert();

		BHRBenefitJoin ltrJ = new BHRBenefitJoin();
		ltrJ.create();
		ltrJ.setHrBenefitConfig(ltr);
		ltrJ.setAmountCovered(ltrAmt);
		ltrJ.setPayingPerson(p.getPerson());
		ltrJ.setCoveredPerson(d.getPerson());
		ltrJ.insert();

		BHRBenefitJoin ltcJ = new BHRBenefitJoin();
		ltcJ.create();
		ltcJ.setHrBenefitConfig(ltc);
		ltcJ.setAmountCovered(baseAmt);
		ltcJ.setPayingPerson(p.getPerson());
		ltcJ.setCoveredPerson(p.getPerson());
		ltcJ.insert();

		BHRBenefitJoin wpobJ = new BHRBenefitJoin();
		wpobJ.create();
		wpobJ.setHrBenefitConfig(wpob);
		wpobJ.setAmountCovered(baseAmt);
		wpobJ.setPayingPerson(p.getPerson());
		wpobJ.setCoveredPerson(p.getPerson());
		wpobJ.insert();

		BHRBenefitJoin wpobltrJ = new BHRBenefitJoin();
		wpobltrJ.create();
		wpobltrJ.setHrBenefitConfig(wpobltr);
		wpobltrJ.setAmountCovered(ltrAmt);
		wpobltrJ.setPayingPerson(p.getPerson());
		wpobltrJ.setCoveredPerson(d.getPerson());
		wpobltrJ.insert();

//		joins.add(base.getBean());
//		joins.add(adobJ.getBean());
		joins.add(ltrJ.getBenefitJoinId());
		joins.add(ltcJ.getBenefitJoinId());
		joins.add(wpobJ.getBenefitJoinId());
		joins.add(wpobltrJ.getBenefitJoinId());
//		joins.add(adobJ.getBenefitJoinId());

		ArahantSession.getHSU().commitTransaction();
		ArahantSession.getHSU().beginTransaction();
		double premium = 0.0;
		double coverage = 0.0;
		
		try {
			premium = calculateTotalPremium(base.getBenefitJoinId(), joins, WEEKLY);
			coverage = calculateTotalCoverageAmount(base.getBenefitJoinId(), joins, 15, WEEKLY);
		} catch (Exception ex) {
			Logger.getLogger(FidelityLifeCostCalculator.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println("Premium: " + premium);
		System.out.println("Coverage: " + coverage);
//		new BEmployee(empId).delete();
//		new BHRBenefitJoin(jId).delete();
	}
}
