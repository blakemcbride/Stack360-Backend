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

import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BBenefitRider;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.calculations.FidelityLifeCostCalculator;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.MoneyUtils;
import com.arahant.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetFidelityCostsReturn extends TransmitReturnBase {

	private double actualPppCost;
	private double actualMonthlyCost;
	private double actualAnnualCost;
	private double actualPppCoverage;
	private double actualMonthlyCoverage;
	private double actualAnnualCoverage;

	public void setAmountCoveredForRiders(BHRBenefitJoin bbj, GetFidelityCostsInput in, double requestedCost, char requestedCostPeriod) throws ArahantException {
		if (Utils.doubleEqual(actualAnnualCoverage, 0, 0.001))
			actualAnnualCoverage = in.getAnnualCoverage();
		bbj.setAmountCovered(actualAnnualCoverage);
		bbj.setRequestedCost(requestedCost);
		bbj.setRequestedCostPeriod(requestedCostPeriod);
		bbj.update();
		for (HrBenefitJoin dbp : bbj.getDependentBenefitJoins()) {
			BHRBenefitJoin bdbj = new BHRBenefitJoin(dbp);
			bdbj.setAmountCovered(actualAnnualCoverage);
			bdbj.setRequestedCost(requestedCost);
			bdbj.setRequestedCostPeriod(requestedCostPeriod);
			bdbj.update();
		}
		for (String s : in.getBenefitJoinIds()) {
			BHRBenefitJoin dj = new BHRBenefitJoin(s);
			com.arahant.beans.BenefitRider br = dj.getRiderEnrollment();
			BBenefitRider bbr = new BBenefitRider(br);
			if (bbr.getBaseBenefitId().equals(bbj.getBenefitId()) && !(bbr.getHiddenBoolean() && !bbr.getRequiredBoolean())) {
				dj.setAmountCovered(actualAnnualCoverage);
				dj.setRequestedCost(requestedCost);
				dj.setRequestedCostPeriod(requestedCostPeriod);
				dj.update();
				for (HrBenefitJoin dbp : dj.getDependentBenefitJoins()) {
					BHRBenefitJoin bdbj = new BHRBenefitJoin(dbp);
					bdbj.setAmountCovered(actualAnnualCoverage);
					bdbj.setRequestedCost(requestedCost);
					bdbj.setRequestedCostPeriod(requestedCostPeriod);
					bdbj.update();
				}
			}
		}
		ArahantSession.getHSU().flush();
	}

	public void setData(GetFidelityCostsInput in) {
		List<String> bjl = new ArrayList<String>();
		if (in.getBenefitJoinIds() != null)
			bjl.addAll(Arrays.asList(in.getBenefitJoinIds()));
		BHRBenefitJoin bbj = new BHRBenefitJoin(in.getBenefitJoinId());
		double ppy = BEmployee.getPPY(bbj.getPayingPersonId());
		boolean bubbleError = false;
		try {
			//reverse coverage calculation
			if (in.getDoReverseCosts()) {

				double LTRCost = 0;
				List<String> ltrIds = new ArrayList<String>();
				for (String s : bjl) {
					BHRBenefitJoin ltrBJ = new BHRBenefitJoin(s);
					if (new BHRBenefit(ltrBJ.getBenefitId()).getInternalId().contains("LTR")) {
						ltrIds.add(ltrBJ.getBenefitJoinId());
						for (HrBenefitJoin ltrbj : ltrBJ.getDependentBenefitJoins())
							ltrIds.add(ltrbj.getBenefitJoinId());
					}
				}

				if (!Utils.doubleEqual(in.getRequestedCostAnnually(), 0, 0.001)) {
					if (ltrIds.size() > 0)
						LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.ANNUAL, true);
					double minCost = FidelityLifeCostCalculator.roundDecimal((3.0 * 52.0) + LTRCost, 2);
					if (in.getRequestedCostAnnually() < minCost)
						throw new ArahantWarning("The minimum annual cost allowed is " + MoneyUtils.formatMoney(minCost));
					//in.setRequestedCostMonthly(in.getRequestedCostAnnually() / 12.0);
					//in.setRequestedCostPPP(in.getRequestedCostAnnually() / ppy);
					actualAnnualCoverage = FidelityLifeCostCalculator.calculateTotalCoverageAmount(in.getBenefitJoinId(), bjl, in.getRequestedCostAnnually(), FidelityLifeCostCalculator.ANNUAL);
					setAmountCoveredForRiders(bbj, in, in.getRequestedCostAnnually(), 'A');
					in.setRequestedCostMonthly(FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.MONTHLY));
					in.setRequestedCostPPP(FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.WEEKLY) * 52.0 / ppy);
				} else if (!Utils.doubleEqual(in.getRequestedCostMonthly(), 0, 0.001)) {
					if (ltrIds.size() > 0)
						LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.MONTHLY, true) / 12.0;
					double minCost = FidelityLifeCostCalculator.roundDecimal((3.0 * 52.0 / 12.0) + LTRCost, 2);
					if (in.getRequestedCostMonthly() < minCost)
						throw new ArahantWarning("The minimum monthly cost allowed is " + MoneyUtils.formatMoney(minCost));
					//in.setRequestedCostAnnually(in.getRequestedCostMonthly() * 12.0);
					//in.setRequestedCostPPP(in.getRequestedCostAnnually() / ppy);
					actualAnnualCoverage = FidelityLifeCostCalculator.calculateTotalCoverageAmount(in.getBenefitJoinId(), bjl, in.getRequestedCostMonthly(), FidelityLifeCostCalculator.MONTHLY);
					setAmountCoveredForRiders(bbj, in, in.getRequestedCostMonthly(), 'M');
					in.setRequestedCostPPP(FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.WEEKLY) * 52.0 / ppy);
					double calcAnnual = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.ANNUAL);
					in.setRequestedCostAnnually(calcAnnual < 156.0 ? 156.0 : calcAnnual);
				} else if (!Utils.doubleEqual(in.getRequestedCostPPP(), 0, 0.001)) {
					if (ltrIds.size() > 0)
						LTRCost = FidelityLifeCostCalculator.calculateTotalPremiumByBenefit(in.getBenefitJoinId(), ltrIds, FidelityLifeCostCalculator.WEEKLY, true) / ppy;
					double minCost = FidelityLifeCostCalculator.roundDecimal((3.0 * 52.0 / ppy) + LTRCost, 2);
					if (in.getRequestedCostPPP() < minCost)
						throw new ArahantWarning("The minimum cost per pay period allowed is " + MoneyUtils.formatMoney(minCost));
					//in.setRequestedCostAnnually(in.getRequestedCostPPP() * ppy);
					//in.setRequestedCostMonthly(in.getRequestedCostAnnually() / 12.0);
					double weeklyCost = in.getRequestedCostPPP() * ppy / 52.0;
					actualAnnualCoverage = FidelityLifeCostCalculator.calculateTotalCoverageAmount(in.getBenefitJoinId(), bjl, weeklyCost, FidelityLifeCostCalculator.WEEKLY);
					setAmountCoveredForRiders(bbj, in, weeklyCost, 'W');
					double calcAnnual = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.ANNUAL);
					in.setRequestedCostAnnually(calcAnnual < 156.0 ? 156.0 : calcAnnual);
					double calcMonthly = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.MONTHLY);
					in.setRequestedCostMonthly(calcMonthly < 13.0 ? 13.0 : calcMonthly);
				}

				actualAnnualCost = in.getRequestedCostAnnually();
				actualMonthlyCost = in.getRequestedCostMonthly();
				actualPppCost = in.getRequestedCostPPP();


			} else { //regular calculation
				setAmountCoveredForRiders(bbj, in, 0, 'M');
				actualAnnualCost = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.ANNUAL);
				actualPppCost = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.WEEKLY) * 52.0 / ppy;
				if (actualPppCost < 0)
					actualPppCost = 0;
				actualMonthlyCost = FidelityLifeCostCalculator.calculateTotalPremium(in.getBenefitJoinId(), bjl, FidelityLifeCostCalculator.MONTHLY);
				if (actualMonthlyCoverage < 0)
					actualMonthlyCoverage = 0;
			}
		} catch (Exception ex) {
			if (bubbleError)
				throw new ArahantWarning(ex.getMessage());
			else
				ex.printStackTrace();
		}
	}

	public double getActualAnnualCoverage() {
		return actualAnnualCoverage;
	}

	public void setActualAnnualCoverage(double actualAnnualCoverage) {
		this.actualAnnualCoverage = actualAnnualCoverage;
	}

	public double getActualMonthlyCoverage() {
		return actualMonthlyCoverage;
	}

	public void setActualMonthlyCoverage(double actualMonthlyCoverage) {
		this.actualMonthlyCoverage = actualMonthlyCoverage;
	}

	public double getActualPppCoverage() {
		return actualPppCoverage;
	}

	public void setActualPppCoverage(double actualPppCoverage) {
		this.actualPppCoverage = actualPppCoverage;
	}

	public double getActualPppCost() {
		return actualPppCost;
	}

	public void setActualPppCost(double actualPppCost) {
		this.actualPppCost = actualPppCost;
	}

	public double getActualMonthlyCost() {
		return actualMonthlyCost;
	}

	public void setActualMonthlyCost(double actualMonthlyCost) {
		this.actualMonthlyCost = actualMonthlyCost;
	}

	public double getActualAnnualCost() {
		return actualAnnualCost;
	}

	public void setActualAnnualCost(double actualAnnualCost) {
		this.actualAnnualCost = actualAnnualCost;
	}
}
