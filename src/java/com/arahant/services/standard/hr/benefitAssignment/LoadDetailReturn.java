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


package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBeneficiary;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRPhysician;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 *
 */
public class LoadDetailReturn extends TransmitReturnBase {

	private LoadDetailReturnItem item[];
	private LoadDetailReturnPhysician physician[];
	private double amountOverrideAnnual;
	private boolean benefitSupportsBeneficiaries, amountPaidChangeable;
	/**
	 * Policy start date
	 */
	private int startDate;
	/**
	 * policy end date
	 */
	private int endDate;
	private int lastCoverageChangeDate;
	private String insuranceId;
	private String warning;
	private boolean usingCOBRA;
	private String benefitChangeReasonId;
	private int acceptedDateCOBRA;
	private int maxMonthsCOBRA;
	private LoadDetailReturnBeneficiariesItem[] primaryBeneficiaries;
	private LoadDetailReturnBeneficiariesItem[] contingentBeneficiaries;
	private String amountPaidType;
	private double amountOverridePPP;
	private double amountCalculatedAnnual;
	private double amountCalculatedPPP;
	private boolean useAmountOverride;
	private boolean benefitSupportsPhysicians;
	private String personId;
	private String employerCostModel;
	private String employeeCostModel;
	private String benefitAmountModel;
	private int minPolicyDate;
	private int maxPolicyDate;

	public double getAmountCalculatedAnnual() {
		return amountCalculatedAnnual;
	}

	public void setAmountCalculatedAnnual(double amountCalculatedAnnual) {
		this.amountCalculatedAnnual = amountCalculatedAnnual;
	}

	public double getAmountCalculatedPPP() {
		return amountCalculatedPPP;
	}

	public void setAmountCalculatedPPP(double amountCalculatedPPP) {
		this.amountCalculatedPPP = amountCalculatedPPP;
	}

	public double getAmountOverrideAnnual() {
		return amountOverrideAnnual;
	}

	public void setAmountOverrideAnnual(double amountOverrideAnnual) {
		this.amountOverrideAnnual = amountOverrideAnnual;
	}

	public double getAmountOverridePPP() {
		return amountOverridePPP;
	}

	public void setAmountOverridePPP(double amountOverridePPP) {
		this.amountOverridePPP = amountOverridePPP;
	}

	/**
	 * @return Returns the warning.
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param warning The warning to set.
	 */
	public void setWarning(final String warning) {
		this.warning = warning;
	}

	/**
	 * @return Returns the item.
	 */
	public LoadDetailReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final LoadDetailReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @return Returns the item.
	 */
	public LoadDetailReturnPhysician[] getPhysician() {
		return physician;
	}

	/**
	 * @param item The item to set.
	 */
	public void setPhysician(final LoadDetailReturnPhysician[] physician) {
		this.physician = physician;
	}

	/**
	 * @param accounts
	 * @throws ArahantException
	 */
	void setData(final BHRBenefitJoin benefitJoin) throws ArahantException {

		BHRBenefit bene = new BHRBenefit(benefitJoin.getBenefitConfig().getBenefit());
		employerCostModel = bene.getEmployerCostModel() + "";
		employeeCostModel = bene.getEmployeeCostModel() + "";
		benefitAmountModel = bene.getBenefitAmountModel() + "";
		benefitSupportsBeneficiaries = benefitJoin.getSupportsBeneficiaries();
		amountPaidChangeable = benefitJoin.getAmountPaidChangeable();
		endDate = benefitJoin.getPolicyEndDate();
		lastCoverageChangeDate = benefitJoin.getCoverageChangeDate();
		BHRBenefitConfig bbc = benefitJoin.getBenefitConfig();		
		
		minPolicyDate = bene.getStartDate();
		if (minPolicyDate < bbc.getStartDate())
			minPolicyDate = bbc.getStartDate();
		maxPolicyDate = bene.getEndDate();
		if (maxPolicyDate == 0  ||  maxPolicyDate > bbc.getEndDate()  &&  bbc.getEndDate() != 0)
			maxPolicyDate = bbc.getEndDate();
		
		int eligibilityStartDate = 0;
		if (benefitJoin.getBenefitConfig() != null && !isEmpty(benefitJoin.getBenefitChangeReasonId())) {
			BHRBenefitChangeReason b = new BHRBenefitChangeReason(benefitJoin.getBenefitChangeReasonId());
			if (b.getType() == HrBenefitChangeReason.NEW_HIRE) {
				BEmployee be = new BEmployee(benefitJoin.getPayingPersonId());
				Calendar cal = DateUtils.getCalendar(be.getHireDate());
				//Start Date
				switch (bene.getEligibilityType()) {
					//First day of employment
					case 1:
						eligibilityStartDate = be.getHireDate();
						break;
					//First day of the month following x days of employment
					case 2:
						int tempDate = DateUtils.addDays(be.getHireDate(), bene.getEligibilityPeriod());
						cal = DateUtils.getCalendar(tempDate);
						cal.add(Calendar.MONTH, 1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						eligibilityStartDate = DateUtils.getDate(cal);
						break;
					//First day of the month following x months of employment
					case 3:
						cal.add(Calendar.MONTH, (int) bene.getEligibilityPeriod());
						cal.add(Calendar.MONTH, 1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						eligibilityStartDate = DateUtils.getDate(cal);
						break;
					//After x days
					case 4:
						eligibilityStartDate = DateUtils.addDays(be.getHireDate(), (int) bene.getEligibilityPeriod());
						break;
					//After x months
					case 5:
						cal.add(Calendar.MONTH, (int) bene.getEligibilityPeriod());
						eligibilityStartDate = DateUtils.getDate(cal);
						break;
				}
			}
		}

		if (eligibilityStartDate != 0)
			startDate = eligibilityStartDate;
		if (benefitJoin.getPolicyStartDate() > eligibilityStartDate)
			startDate = benefitJoin.getPolicyStartDate();
		insuranceId = benefitJoin.getInsuranceId();
		usingCOBRA = benefitJoin.getUsingCOBRA();
		benefitChangeReasonId = benefitJoin.getBenefitChangeReasonId();

		final BHRBenefitJoin[] benefitJoins = benefitJoin.getPolicyAndDependentBenefitJoins(true);

		List<LoadDetailReturnItem> l = new LinkedList<LoadDetailReturnItem>();

		ArahantSession.runAI();

		useAmountOverride = benefitJoin.getUseAmountOverride();

		//1-5-12 Arahant
		//Some Dependent joins don't have the same value for "useAmountOverride" as their policy.
		//Not sure how/why they could be different.  Anytime they open a join to look at it, lets set
		//the value so they stay the same.
		benefitJoin.setUseAmountOverride(useAmountOverride);

		if (useAmountOverride) {
			amountOverrideAnnual = benefitJoin.getAmountPaidOverrideAnnual();
			amountOverridePPP = benefitJoin.getAmountPaidOverridePPP();
		}

		if (benefitJoin.getEmployeeCovered().equals("N") && benefitJoin.getDependentBenefitJoins().size() >= 1) {
			BHRBenefitJoin depJoin = new BHRBenefitJoin(benefitJoin.getDependentBenefitJoins().get(0));
			amountCalculatedAnnual = depJoin.getCalculatedCostAnnual();//BenefitCostCalculator.calculateCostNewMethodAnnual(depJoin.getBean(), DateUtils.now());
			amountCalculatedPPP = depJoin.getCalculatedCostPPP();//BenefitCostCalculator.calculateCostNewMethod(depJoin.getBean(), DateUtils.now());
		} else {
			amountCalculatedAnnual = benefitJoin.getCalculatedCostAnnual();
			amountCalculatedPPP = benefitJoin.getCalculatedCostPPP();
		}



		for (int loop = 0; loop < benefitJoins.length; loop++)
			l.add(new LoadDetailReturnItem(benefitJoins[loop]));

		Collections.sort(l);

		item = l.toArray(new LoadDetailReturnItem[benefitJoins.length]);
		maxMonthsCOBRA = benefitJoin.getMaxMonthsCOBRA();
		acceptedDateCOBRA = benefitJoin.getAcceptedDateCOBRA();
		amountPaidType = benefitJoin.getAmountPaidType();

		BHRPhysician[] physicians = BHRPhysician.listAllPhysiciansInGroup(benefitJoin);
		physician = new LoadDetailReturnPhysician[physicians.length];
		for (int i = 0; i < physicians.length; i++)
			physician[i] = new LoadDetailReturnPhysician(physicians[i]);

		BHRBeneficiary[] primaries = BHRBeneficiary.listPrimaries(benefitJoin);
		primaryBeneficiaries = new LoadDetailReturnBeneficiariesItem[primaries.length];

		for (int loop = 0; loop < primaryBeneficiaries.length; loop++)
			primaryBeneficiaries[loop] = new LoadDetailReturnBeneficiariesItem(primaries[loop]);

		BHRBeneficiary[] secondaries = BHRBeneficiary.listSecondaries(benefitJoin);
		contingentBeneficiaries = new LoadDetailReturnBeneficiariesItem[secondaries.length];

		for (int loop = 0; loop < contingentBeneficiaries.length; loop++)
			contingentBeneficiaries[loop] = new LoadDetailReturnBeneficiariesItem(secondaries[loop]);
	}

	/**
	 * @return Returns the amountPaidChangeable.
	 */
	public boolean isAmountPaidChangeable() {
		return amountPaidChangeable;
	}

	/**
	 * @param amountPaidChangeable The amountPaidChangeable to set.
	 */
	public void setAmountPaidChangeable(final boolean amountPaidChangeable) {
		this.amountPaidChangeable = amountPaidChangeable;
	}

	/**
	 * @return Returns the benefitSupportsBeneficiaries.
	 */
	public boolean isBenefitSupportsBeneficiaries() {
		return benefitSupportsBeneficiaries;
	}

	/**
	 * @param benefitSupportsBeneficiaries The benefitSupportsBeneficiaries to
	 * set.
	 */
	public void setBenefitSupportsBeneficiaries(final boolean benefitSupportsBeneficiaries) {
		this.benefitSupportsBeneficiaries = benefitSupportsBeneficiaries;
	}

	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}

	public int getLastCoverageChangeDate() {
		return lastCoverageChangeDate;
	}

	public void setLastCoverageChangeDate(int lastCoverageChangeDate) {
		this.lastCoverageChangeDate = lastCoverageChangeDate;
	}

	/**
	 * @return Returns the insuranceId.
	 */
	public String getInsuranceId() {
		return insuranceId;
	}

	/**
	 * @param insuranceId The insuranceId to set.
	 */
	public void setInsuranceId(final String insuranceId) {
		this.insuranceId = insuranceId;
	}

	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return Returns the usingCOBRA.
	 */
	public boolean getUsingCOBRA() {
		return usingCOBRA;
	}

	/**
	 * @param usingCOBRA The usingCOBRA to set.
	 */
	public void setUsingCOBRA(final boolean usingCOBRA) {
		this.usingCOBRA = usingCOBRA;
	}

	public int getAcceptedDateCOBRA() {
		return acceptedDateCOBRA;
	}

	public void setAcceptedDateCOBRA(int acceptedDateCOBRA) {
		this.acceptedDateCOBRA = acceptedDateCOBRA;
	}

	public int getMaxMonthsCOBRA() {
		return maxMonthsCOBRA;
	}

	public void setMaxMonthsCOBRA(int maxMonthsCOBRA) {
		this.maxMonthsCOBRA = maxMonthsCOBRA;
	}

	public String getAmountPaidType() {
		return amountPaidType;
	}

	public void setAmountPaidType(String amountPaidType) {
		this.amountPaidType = amountPaidType;
	}

	public LoadDetailReturnBeneficiariesItem[] getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(LoadDetailReturnBeneficiariesItem[] contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	public LoadDetailReturnBeneficiariesItem[] getPrimaryBeneficiaries() {
		return primaryBeneficiaries;
	}

	public void setPrimaryBeneficiaries(LoadDetailReturnBeneficiariesItem[] primaryBeneficiaries) {
		this.primaryBeneficiaries = primaryBeneficiaries;
	}

	public boolean isUseAmountOverride() {
		return useAmountOverride;
	}

	public void setUseAmountOverride(boolean useAmountOverride) {
		this.useAmountOverride = useAmountOverride;
	}

	public String getBenefitChangeReasonId() {
		return benefitChangeReasonId;
	}

	public void setBenefitChangeReasonId(String benefitChangeReasonId) {
		this.benefitChangeReasonId = benefitChangeReasonId;
	}

	public boolean getBenefitSupportsPhysicians() {
		return benefitSupportsPhysicians;
	}

	public void setBenefitSupportsPhysicians(boolean benefitSupportsPhysicians) {
		this.benefitSupportsPhysicians = benefitSupportsPhysicians;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getEmployerCostModel() {
		return employerCostModel;
	}

	public void setEmployerCostModel(String employerCostModel) {
		this.employerCostModel = employerCostModel;
	}

	public String getEmployeeCostModel() {
		return employeeCostModel;
	}

	public void setEmployeeCostModel(String employeeCostModel) {
		this.employeeCostModel = employeeCostModel;
	}

	public String getBenefitAmountModel() {
		return benefitAmountModel;
	}

	public void setBenefitAmountModel(String benefitAmountModel) {
		this.benefitAmountModel = benefitAmountModel;
	}

	public int getMinPolicyDate() {
		return minPolicyDate;
	}

	public void setMinPolicyDate(int minPolicyDate) {
		this.minPolicyDate = minPolicyDate;
	}

	public int getMaxPolicyDate() {
		return maxPolicyDate;
	}

	public void setMaxPolicyDate(int maxPolicyDate) {
		this.maxPolicyDate = maxPolicyDate;
	}
	
}
