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

package com.arahant.business;

import com.arahant.beans.AIProperty;
import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.BenefitDependency;
import com.arahant.beans.BenefitRider;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBeneficiary;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.LifeEvent;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.WizardProject;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.standard.wizard.benefitWizard.Enrollee;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import com.arahant.utils.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class BHRBenefitJoin extends SimpleBusinessObjectBase<HrBenefitJoin> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static transient ArahantLogger logger = new ArahantLogger(BHRBenefitJoin.class);

	public BHRBenefitJoin() {
	}

	/**
	 * @param hrBenefitJoin
	 */
	public BHRBenefitJoin(HrBenefitJoin hrBenefitJoin) {
		bean = hrBenefitJoin;
	}

	/**
	 * @param benefitJoinId
	 * @throws ArahantException
	 */
	public BHRBenefitJoin(final String benefitJoinId) throws ArahantException {
		internalLoad(benefitJoinId);
	}

	/**
	 * @param employeeId
	 * @param benefitConfigId
	 * @throws ArahantException
	 */
	public BHRBenefitJoin(final String employeeId, final String benefitConfigId) throws ArahantException {
		final HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);

		hcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Person.PERSONID, employeeId);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId);
		hcu.orderByDesc(HrBenefitJoin.POLICY_START_DATE);

		bean = hcu.first();

		if (bean == null)
			throw new ArahantException("Could not find benefit join record.");
	}

	public void copyReason(HrBenefitJoin bBenefitJoin) {
		bean.setBenefitChangeReason(bBenefitJoin.getBenefitChangeReason());
		bean.setLifeEvent(bBenefitJoin.getLifeEvent());
		bean.setChangeDescription(bBenefitJoin.getChangeDescription());
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitJoin();
		return bean.generateId();
	}

	public String getEmployeeCovered() {
		return bean.getEmployeeCovered() + "";
	}

	public void setEmployeeCovered(String employeeCovered) {
		bean.setEmployeeCovered(employeeCovered.charAt(0));
	}

	public int getAcceptedDateCOBRA() {
		return bean.getAcceptedDateCOBRA();
	}

	public double getAmountPaidOverrideAnnual() {
		return bean.getAmountPaid();
	}

	public double getAmountPaidOverridePPP() {
		int ppy = bean.getPpy();
		if (ppy <= 1)
			try {
				BEmployee bemp = new BPerson(bean.getPayingPerson()).getBEmployee();
				ppy = bemp.getPayPeriodsPerYear();
			} catch (Exception e) {
				//use default must be dependent
			}
		return bean.getAmountPaid() / ppy;
	}

	public String getAmountPaidType() {
		return bean.getAmountPaidType() + "";
	}

	@Override
	public HrBenefitJoin getBean() {
		return bean;
	}

	public String getBenefitChangeReasonId() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getHrBenefitChangeReasonId();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
		//otherwise, nothing was set
		return null;
	}

	public int getChangeReasonDate() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getEffectiveDate();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getEventDate();
		//otherwise, nothing was set
		return 0;
	}

	public int getChangeReasonEndDate() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getEndDate();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getChangeReason().getEndDate();
		//otherwise, nothing was set
		return 0;
	}

	public int getChangeReasonType() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getType();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getChangeReason().getType();
		//otherwise, nothing was set
		return 0;
	}

	public String getCalculatedCostIfCOBRA() {
		char cobra = bean.getUsingCOBRA();
		bean.removeFromAIEngine();
		bean.setUsingCOBRA('Y');
		bean.setCalculatedCost(null);
		bean.linkToEngine();
		ArahantSession.runAI();
		String ret = bean.getCalculatedCost();
		bean.removeFromAIEngine();
		bean.setUsingCOBRA(cobra);
		bean.setCalculatedCost(null);
		bean.linkToEngine();
		ArahantSession.runAI();
		return ret;
	}

	public String getComments() {
		return bean.getComments();
	}

	public double getCalculatedCostPPP() {
		return bean.getCalculatedCostPPP();
	}

	public double getCalculatedCostAnnual() {
		return bean.getCalculatedCostAnnual();
	}

	public Person getHistoryChangePerson() {
		return ArahantSession.getHSU().get(Person.class, bean.getRecordPersonId());
	}

	public int getMaxMonthsCOBRA() {
		return bean.getMaxMonthsCOBRA();
	}

	public String getOtherInsurance() {
		return bean.getOtherInsurance();
	}

	public boolean getOtherInsurancePrimary() {
		return bean.getOtherInsurancePrimary() == 'P';
	}

	public boolean getUseAmountOverride() {
		return bean.getAmountPaidSource() == HrBenefitJoin.TYPE_MANUAL;
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitJoin.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void insert() throws ArahantException {
		this.insert(false);
	}

	/**
	 * Optionally allows validation to be bypassed on the policy benefit join,
	 * which should be RARELY used
	 *
	 * @param skipValidation
	 * @throws ArahantException
	 */
	public void insert(boolean skipValidation) throws ArahantException {
		if (isEmpty(getBenefitChangeReasonId()) && getLifeEvent() == null) //make sure any temporary joins have a reason
			setChangeReason(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).first().getHrBenefitChangeReasonId());

		this.insertOrUpdate(skipValidation);

		super.insert();
	}

	public void setAcceptedDateCOBRA(int acceptedDateCOBRA) {
		bean.setAcceptedDateCOBRA(acceptedDateCOBRA);
	}

	public void setAmountPaidType(String amountPaidType) {
		if (!isEmpty(amountPaidType))
			bean.setAmountPaidType(amountPaidType.charAt(0));
	}

	public void setBenefitJoinId(String id) {
		bean.setBenefitJoinId(id);
	}

	public void setCalculatedCost(String calculatedCost) {
		bean.setCalculatedCost(calculatedCost);
	}

	public void setComments(String comments) {
		bean.setComments(comments);
	}

	public void setEmployeeExplanation(String employeeExplanation) {
		bean.setEmployeeExplanation(employeeExplanation);
	}

	public String getEmployeeExplanation() {
		return bean.getEmployeeExplanation();
	}

	public void setDefaults() {
		//request default coverage amount
		AIProperty defaultCoverage = new AIProperty("DefaultCoverageAmount", bean.getHrBenefitConfigId(), bean.getPayingPersonId());
		setAmountCovered(defaultCoverage.getRetDblVal());

		AIProperty defaultCost = new AIProperty("DefaultBenefitCost", bean.getBenefitJoinId());
		setAmountPaid(defaultCost.getRetDblVal());
	}

	public void setMaxMonthsCOBRA(int maxMonthsCOBRA) {
		bean.setMaxMonthsCOBRA(maxMonthsCOBRA);
	}

	public void setOtherInsuanceIsPrimary(boolean otherInsurancePrimary) {
		bean.setOtherInsurancePrimary(otherInsurancePrimary ? 'P' : 'S');
	}

	public void setOtherInsurance(String otherInsurance) {
		bean.setOtherInsurance(otherInsurance);
	}

	public void setUseAmountOverride(boolean useAmountOverride) {
		bean.setAmountPaidSource(useAmountOverride ? HrBenefitJoin.TYPE_MANUAL : HrBenefitJoin.TYPE_CALCULATED);
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (HrBenefitJoin depJoin : getDependentBenefitJoins()) {
			depJoin.setAmountPaidSource(useAmountOverride ? HrBenefitJoin.TYPE_MANUAL : HrBenefitJoin.TYPE_CALCULATED);
			hsu.update(depJoin);
		}
	}

	@Override
	public void update() throws ArahantException {
		this.update(false);
	}

	/**
	 * Optionally allows validation to be bypassed on the policy benefit join,
	 * which should be RARELY used
	 *
	 * @param skipValidation
	 * @throws ArahantException
	 */
	public void update(boolean skipValidation) throws ArahantException {
		this.insertOrUpdate(skipValidation);

		super.update();
	}

	protected void insertOrUpdate(boolean skipValidation) throws ArahantException {

		// sanity check
		if (bean.getHrBenefit() == null && bean.getHrBenefitCategory() == null && bean.getHrBenefitConfig() == null)
			throw new ArahantException("Benefit join doesn't join to anything!");


		// is this a policy benefit join?
		if (this.isPolicyBenefitJoin()) {

			//if the config does not cover employee, mark the policy join accordingly
			if (bean.getHrBenefitConfig() != null && bean.getHrBenefitConfig().getEmployee() == 'N')
				bean.setEmployeeCovered('N');
			// yes, so check if we are doing validation
			if (!skipValidation) {
				this.checkPolicyEndDateAgainstCoverageDates();
				this.checkPolicyDatesAgainstExistingBenefitInCategory();
				this.checkCoverageDatesAgainstExistingBenefitInCategory();
				this.checkOverCovered();
			}
		}
		//make sure we HAVE a policy join, if we don't we should prob create it and mark employee not covered
//		else {
//			if(this.getPolicyBenefitJoin() == null && bean.getHrBenefitConfig() != null && bean.getHrBenefitConfig().getEmployee() == 'N')	{
//				BHRBenefitJoin policyJoin = new BHRBenefitJoin();
//				policyJoin.create();
//				String[] excludeFields = new String[1];
//				excludeFields[0] = HrBenefitJoin.BENEFIT_JOIN_ID;
//				HibernateSessionUtil.copyCorresponding(policyJoin.getBean(), bean, excludeFields);
//				policyJoin.setEmployeeCovered("N");
//				policyJoin.setCoverageStartDate(0);
//				policyJoin.setCoverageEndDate(0);
//				policyJoin.setAmountCovered(0.0);
//				policyJoin.setCoveredPerson(policyJoin.getPayingPerson().getPerson());
//				policyJoin.insert();
//				System.out.println("Created Policy Join for " + policyJoin.getPayingPerson().getNameFML() + " (" + policyJoin.getBenefitName() + " - " + policyJoin.getBenefitConfigName() + ")");
//			}
//		}

		//TODO shouldn't this be auto-approving all joins in the relationship?
		if (bean.getBenefitApproved() == 'Y')
			this.checkAutoApprove();

	}

	/**
	 * @return
	 */
	public String getBenefitJoinId() {
		return bean.getBenefitJoinId();
	}

	/**
	 * @param policyStartDate
	 */
	public void setPolicyStartDate(int policyStartDate) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		bean.setPolicyStartDate(policyStartDate);

		if (isPolicyBenefitJoin())
			for (HrBenefitJoin dbj : getDependentBenefitJoins()) {
				dbj.setPolicyStartDate(policyStartDate);
				hsu.saveOrUpdate(dbj);
			}
	}

	public int getPolicyStartDate() {
		return bean.getPolicyStartDate();
	}

	public void setPolicyEndDate(int policyEndDate) {
		bean.setPolicyEndDate(policyEndDate);
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (isPolicyBenefitJoin())
			for (HrBenefitJoin dbj : getDependentBenefitJoins()) {
				dbj.setPolicyEndDate(policyEndDate);
				hsu.saveOrUpdate(dbj);
			}
	}

	public int getPolicyEndDate() {
		return bean.getPolicyEndDate();
	}

	public int getCoverageChangeDate() {
		return bean.getCoverageChangeDate();
	}

	public void setCoverageChangeDate(int coverageChangeDate) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		bean.setCoverageChangeDate(coverageChangeDate);

		if (isPolicyBenefitJoin())
			for (HrBenefitJoin dbj : getDependentBenefitJoins()) {
				dbj.setCoverageChangeDate(coverageChangeDate);
				hsu.saveOrUpdate(dbj);
			}
	}

	public int getOriginalCoverageDate() {
		return bean.getOriginalCoverageDate();
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		bean.setOriginalCoverageDate(originalCoverageDate);
	}

	public void setPayingPerson(Person payingPerson) {
		bean.setPayingPerson(payingPerson);
	}

	public void setPayingPersonId(String payingPersonId) {
		Person payingPerson = ArahantSession.getHSU().get(Person.class, payingPersonId);
		bean.setPayingPerson(payingPerson);
	}

	public void setRelationship(HrEmplDependent relationship) {
		bean.setRelationship(relationship);
	}

	public BHREmplDependent getRelationship() throws ArahantException {
		if (bean.getRelationship() == null)
			return null;
		else
			return new BHREmplDependent(bean.getRelationship());
	}

	public String getRelationshipText() throws ArahantException {
		String relationship;

		if (this.isPolicyBenefitJoin()) {
			BPerson person = new BPerson(this.getPayingPersonId());

			if (person.isEmployee())
				relationship = "Employee";
			else
				relationship = "Dependent";
		} else if (bean.getRelationship() == null)
			relationship = "Employee";
		else
			relationship = this.getRelationship().getTextRelationship();

		return relationship;
	}

	public String getChangeReason() {
		try {
			if (!isEmpty(bean.getChangeDescription()))
				return bean.getChangeDescription();

			if (bean.getLifeEvent() != null)
				return bean.getLifeEvent().getChangeReason().getDescription();

			if (bean.getBenefitChangeReason() != null)
				return bean.getBenefitChangeReason().getDescription();

			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public boolean getBenefitApproved() {
		return bean.getBenefitApproved() == 'Y';
	}

	public boolean getBenefitRejected() {
		return bean.getBenefitApproved() == 'R';
	}

	public void setBenefitApproved(boolean approved) {
		bean.setBenefitApproved(approved ? 'Y' : 'N');
	}

	public void setBenefitApproved(char approved) {
		bean.setBenefitApproved(approved);
	}

	public void setPolicyApproved(boolean approved) {
		setPolicyApproved(approved, true);
	}

	public void setPolicyApproved(boolean approved, boolean doWizProjects) {
		if (!this.isPolicyBenefitJoin())
			throw new ArahantDeleteException("The benefit is not a policy benefit join");

		BHRBenefitJoin[] allBenefitJoins = this.getPolicyAndDependentBenefitJoins(false);
		for (BHRBenefitJoin bBenefitJoin : allBenefitJoins) {
			bBenefitJoin.setBenefitApproved(approved);
			if (doWizProjects)
				bBenefitJoin.setWizardProjectsCompleted();
			bBenefitJoin.update();
		}
	}

	public void setWizardProjectsCompleted() {
		for (WizardProject wp : ArahantSession.getHSU().createCriteria(WizardProject.class).eq(WizardProject.BENEFIT_JOIN, bean).eq(WizardProject.PROJECT_ACTION, 'A').eq(WizardProject.COMPLETED, 'N').list()) {
			BWizardProject bwp = new BWizardProject(wp);
			bwp.markCompleted(false);
		}
	}

	public void setBenefitDeclined(char benefitDeclined) {
		bean.setBenefitDeclined(benefitDeclined);
	}

	public void setCoveredPerson(Person coveredPerson) {
		bean.setCoveredPerson(coveredPerson);
	}

	public void setCoveredPersonId(String coveredPersonId) {
		Person coveredPerson = ArahantSession.getHSU().get(Person.class, coveredPersonId);
		bean.setCoveredPerson(coveredPerson);
	}

	public void setAmountCovered(double amountCovered) {
		bean.setAmountCovered(amountCovered);
	}

	public double getAmountCovered() {
		return bean.getAmountCovered();
	}

	public double getRequestedCost() {
		return bean.getRequestedCost();
	}

	public void setRequestedCost(double requestedCost) {
		bean.setRequestedCost(requestedCost);
	}

	public char getRequestedCostPeriod() {
		return bean.getRequestedCostPeriod();
	}

	public void setRequestedCostPeriod(char requestedCostPeriod) {
		bean.setRequestedCostPeriod(requestedCostPeriod);
	}

	public void setAmountPaid(double amountPaid) {
		// only allowed to do this for when amount is employee specific and this is a policy benefit join
		try {
//			if (this.isPolicyBenefitJoin() && bean.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y')
			if (this.isPolicyBenefitJoin() && bean.getHrBenefitConfig().getHrBenefit().getEmployeeCostModel() == 'R')
				bean.setAmountPaid(amountPaid);
		} catch (Exception e) {
			//don't set amount if not emp specific
		}
	}

	public void setChangeDescription(String changeDescription) {
		bean.setChangeDescription(changeDescription);
	}

	public void setChangeReason(String reasonId) {

		if (!isEmpty(reasonId)) {
			HrBenefitChangeReason bcr = ArahantSession.getHSU().get(HrBenefitChangeReason.class, reasonId);
			bean.setBenefitChangeReason(bcr);
			bean.setLifeEvent(null);
			bean.setChangeDescription(bcr.getDescription());
		} else {
			bean.setBenefitChangeReason(null);
			bean.setChangeDescription("");
		}
	}

	public void setCoverageEndDate(int coverageEndDate) {
		this.setCoverageEndDate(coverageEndDate, true);
	}

	public void setCoverageEndDate(int coverageEndDate, boolean skipCheck) {
		if (!skipCheck)
			this.checkCoverageEndDateAgainstBenefitRules(coverageEndDate);
		bean.setCoverageEndDate(coverageEndDate);
	}

	/**
	 * @return
	 */
	public int getCoverageEndDate() {
		return bean.getCoverageEndDate();
	}

	public void setCoverageStartDate(int coverageStartDate) {
		this.setCoverageStartDate(coverageStartDate, true);
	}

	public void setCoverageStartDate(int coverageStartDate, boolean skipCheck) {
		if (!skipCheck)
			this.checkCoverageStartDateAgainstBenefitEligibilityDate(coverageStartDate);

		bean.setCoverageStartDate(coverageStartDate);
	}

	public int getCoverageStartDate() {
		return bean.getCoverageStartDate();
	}

	public void setInsuranceId(String insuranceId) {
		bean.setInsuranceId(insuranceId);
	}

	public String getInsuranceId() {
		return bean.getInsuranceId();
	}

	public void setUsingCOBRA(char usingCOBRA) {
		bean.setUsingCOBRA(usingCOBRA);
	}

	public void setUsingCOBRA(boolean usingCOBRA) {
		bean.setUsingCOBRA(usingCOBRA ? 'Y' : 'N');
	}

	public boolean getUsingCOBRA() {
		return bean.getUsingCOBRA() == 'Y';
	}

	@Deprecated
	public boolean getAmountPaidChangeable() {
		return bean.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y';
	}

	public boolean getSupportsBeneficiaries() {
		return bean.getHrBenefitConfig().getHrBenefit().getHasBeneficiaries() == 'Y';
	}

	/**
	 * This function is incredibly costly!
	 * 
	 * @return 
	 */
	public double getAmountPaid() {
		if (bean.getHrBenefitConfig() == null)
			return 0;

		if (bean.getCalculatedCost() == null)
			return bean.getAmountPaid() / bean.getPpy();

		return MoneyUtils.parseMoney(bean.getCalculatedCost());
	}

	public double getAmountPaidAnnual() {
		if (bean.getHrBenefitConfig() == null)
			return 0;
		return MoneyUtils.parseMoney(bean.getCalculatedCostAnnualString());
	}

	public String getCalculatedCost() {
		return bean.getCalculatedCost();
	}

	public String getCalculatedCostMonthly() {
		return MoneyUtils.formatMoney(bean.getCalculatedCostMonthly());
	}
	
	/**
	 * Calculate the employee's annual cost.  written by Blake
	 * 
	 * @return 
	 */
	public double getEmployeeAnnualCost() {
		return BenefitCostCalculator.calculateEmployeeAnnualCost(bean);
	}
	
	/**
	 * Calculate the employee's per-pay-period cost.  written by Blake
	 * 
	 * @return per-pay-period cost
	 */
	public double getEmployeePPPCost() {
		BEmployee emp = new BEmployee(bean.getPayingPersonId());
		int ppy = emp.getPayPeriodsPerYear();
		if (ppy == 0)
			return 0.0;
		double annualCost = BenefitCostCalculator.calculateEmployeeAnnualCost(bean);
		return annualCost / ppy;
	}
	
	/**
	 * Calculate the employee's monthly cost.  written by Blake
	 * 
	 * @return monthly cost
	 */
	public double getEmployeeMonthlyCost() {
		return BenefitCostCalculator.calculateEmployeeAnnualCost(bean) / 12.0;
	}

	/**
	 * Sets the coverage config id of the bean, effectively making it a true
	 * benefit join
	 *
	 * @param benefitConfigId
	 */
	public void setBenefitConfigId(final String benefitConfigId) {
		if (!isEmpty(benefitConfigId)) {
			bean.setBenefitDeclined('N');
			bean.setHrBenefit(null);
			bean.setHrBenefitCategory(null);
			bean.setHrBenefitConfig(ArahantSession.getHSU().get(HrBenefitConfig.class, benefitConfigId));
		}
	}

	/**
	 * Sets the benefit category id of the bean, effectively making it a benefit
	 * level decline
	 *
	 * @param benefitId
	 */
	public void setBenefitId(final String benefitId) {
		if (!isEmpty(benefitId)) {
			bean.setBenefitDeclined('Y');
			bean.setHrBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
			bean.setHrBenefitCategory(null);
			bean.setHrBenefitConfig(null);
		}
	}

	/**
	 * Sets the benefit category id of the bean, effectively making it a
	 * category level decline
	 *
	 * @param categoryId
	 */
	public void setCategoryId(final String categoryId) {
		if (!isEmpty(categoryId)) {
			bean.setBenefitDeclined('Y');
			bean.setHrBenefit(null);
			bean.setHrBenefitCategory(ArahantSession.getHSU().get(HrBenefitCategory.class, categoryId));
			bean.setHrBenefitConfig(null);
		}
	}

	/**
	 * @param beneficiaries
	 */
	@SuppressWarnings("unchecked")
	public void setBeneficiaries(Set<HrBeneficiary> beneficiaries) {
		Set benes = new HashSet<HrBeneficiary>();
		if (beneficiaries != null)
			benes.addAll(beneficiaries);
		setBeneficiaries(benes);
	}

	public boolean isDecline() {
		return isBenefitDecline() || isBenefitCategoryDecline();
	}

	public boolean isBenefitDecline() {
		return bean.getHrBenefit() != null;
	}

	public boolean isBenefitCategoryDecline() {
		return bean.getHrBenefitCategory() != null;
	}

	public String getBenefitCategoryName() {
		try {
			if (bean.getHrBenefitConfig() != null)
				return bean.getHrBenefitConfig().getHrBenefitCategory().getDescription();
			else if (bean.getHrBenefit() != null)
				return bean.getHrBenefit().getHrBenefitCategory().getDescription();
			else
				return bean.getHrBenefitCategory().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getBenefitCategoryId() {
		try {
			if (bean.getHrBenefitConfig() != null)
				return bean.getHrBenefitConfig().getHrBenefitCategory().getBenefitCatId();
			else if (bean.getHrBenefit() != null)
				return bean.getHrBenefit().getHrBenefitCategory().getBenefitCatId();
			else
				return bean.getHrBenefitCategory().getBenefitCatId();
		} catch (final Exception e) {
			return "";
		}
	}

	public short getBenefitCategoryType() {
		try {
			if (bean.getHrBenefitConfig() != null)
				return bean.getHrBenefitConfig().getHrBenefitCategory().getBenefitType();
			else if (bean.getHrBenefit() != null)
				return bean.getHrBenefit().getHrBenefitCategory().getBenefitType();
			else
				return bean.getHrBenefitCategory().getBenefitType();
		} catch (final Exception e) {
			return -1;
		}
	}

	public String getBenefitName() {
		try {
			if (bean.getHrBenefitConfig() != null)
				return bean.getHrBenefitConfig().getHrBenefit().getName();
			else
				return bean.getHrBenefit().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getBenefitId() {
		try {
			if (bean.getHrBenefitConfig() != null)
				return bean.getHrBenefitConfig().getHrBenefit().getBenefitId();
			else
				return bean.getHrBenefit().getBenefitId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getProvidedBySSN() {
		if (this.isPolicyBenefitJoin())
			return "";
		else
			return bean.getPayingPerson().getUnencryptedSsn();
	}

	public String getProvidedByDisplayName() {
		if (this.isPolicyBenefitJoin())
			return "";
		else
			return bean.getPayingPerson().getNameLFM();
	}

	public String isActive() {
		return bean.getCoverageStartDate() > 0 && bean.getCoverageStartDate() <= DateUtils.now() && (bean.getCoverageEndDate() == 0 || bean.getCoverageEndDate() >= DateUtils.now()) ? "Y" : "N";
	}

	public void deleteBeneficiaries() throws ArahantDeleteException {
		if (!this.isPolicyBenefitJoin())
			throw new ArahantDeleteException("The benefit is not a policy benefit join");

		ArahantSession.getHSU().delete(bean.getBeneficiaries());
		bean.setBeneficiaries(null);
	}

	public HrBenefitJoin getPolicyBenefitJoin() throws ArahantException {
		if (BHRBenefitJoin.isPolicyBenefitJoin(this.bean))
			throw new ArahantException("The benefit is not a dependent benefit join");

		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, bean.getPayingPersonId()).eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPayingPersonId()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bean.getHrBenefitConfigId()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).first();
	}

	public List<HrBenefitJoin> getDependentBenefitJoins() throws ArahantException {
		//	if (!BHRBenefitJoin.isPolicyBenefitJoin(this.bean))
		//		throw new ArahantException("The benefit is not a policy benefit join");

		// check for config, benefit, and category being same in case this is a decline
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).isNotNull(HrBenefitJoin.RELATIONSHIP).eq(HrBenefitJoin.APPROVED, bean.getBenefitApproved()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).list();
	}

	public List<HrBenefitJoin> getActiveDependentBenefitJoins() throws ArahantException {
		//	if (!BHRBenefitJoin.isPolicyBenefitJoin(this.bean))
		//		throw new ArahantException("The benefit is not a policy benefit join");

		// check for config, benefit, and category being same in case this is a decline
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, bean.getBenefitApproved()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).list();
	}

	public List<HrBenefitJoin> getJoinGroup() throws ArahantException {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();
	}

	public BHRBenefitJoin[] getPolicyAndDependentBenefitJoins(List<HrBenefitJoin> allExistingBenefitJoins, boolean fakeJoinsForNonCoveredDependents) throws ArahantException {
		final List<BHRBenefitJoin> benefitJoins = new ArrayList<BHRBenefitJoin>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		// try to load the paying person as an employee
		Employee employee = hsu.get(Employee.class, bean.getPayingPerson().getPersonId());
		if (employee == null)
			// not an employee, so must be a non-employee dependent - just add this join and we are done
			benefitJoins.add(this);
		else {
			// paying person is an employee, so let's get all dependents
			@SuppressWarnings("unchecked")
			List<String> dependents = (List) hsu.createCriteria(HrEmplDependent.class).selectFields(HrEmplDependent.DEP_KEY).eq(HrEmplDependent.EMPLOYEE, employee).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

//            // get policy and all existing dependent benefit joins
//            List<HrBenefitJoin> allExistingBenefitJoins = hsu.createCriteria(HrBenefitJoin.class)
//					.eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson())
//					.eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate())
//					.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig())
//					.eq(HrBenefitJoin.APPROVED, bean.getBenefitApproved()).list();

			//System.out.println("Looking for " + bean.getPayingPersonId() + " " + bean.getPolicyStartDate() + " " + bean.getHrBenefitConfig().getBenefitConfigId());
			for (final HrBenefitJoin existingBenefitJoin : allExistingBenefitJoins) {
				// add this benefit join to the list, and remove the dependent if it happens to be a dependent benefit join
				benefitJoins.add(new BHRBenefitJoin(existingBenefitJoin));
				if (existingBenefitJoin.getRelationship() != null)
					try {
						dependents.remove(existingBenefitJoin.getRelationship().getRelationshipId());
					} catch (Exception e) {
						BPerson bpp = new BPerson();
						if (bpp.hasPending(existingBenefitJoin.getPayingPersonId())) {
							String person = existingBenefitJoin.getPayingPerson().getNameLFM();
							throw new ArahantException("Please approve pending change request for " + person + " before approving benefits.");
						} else
							throw new ArahantException("Get Policy and Dependent benefit join error: " + e.getMessage());
					}
			}

			// check if we need to create fake dependent benefit joins for those dependents that don't have one
			if (fakeJoinsForNonCoveredDependents)
				// yes, so spin through remaining dependents
				for (String depId : dependents) {
					BHREmplDependent bdependent = new BHREmplDependent(depId);
					HrEmplDependent dependent = bdependent.getEmplDependent();
					BHRBenefitJoin dbj = new BHRBenefitJoin();
					dbj.create();
					dbj.setRelationship(dependent);
					dbj.setBenefitConfigId(bean.getHrBenefitConfigId());
					dbj.setPayingPerson(bean.getPayingPerson());
					dbj.setCoveredPerson(dependent.getPerson());
					dbj.setPolicyStartDate(bean.getPolicyStartDate());
					dbj.setPolicyEndDate(bean.getPolicyEndDate());
					dbj.setCoverageChangeDate(bean.getCoverageChangeDate());
					dbj.setCoverageStartDate(0);
					dbj.setCoverageEndDate(0);
					dbj.setOriginalCoverageDate(0);
					benefitJoins.add(dbj);
				}
		}

		// convert to array for return
		final BHRBenefitJoin[] benefitJoinArray = new BHRBenefitJoin[benefitJoins.size()];
		return benefitJoins.toArray(benefitJoinArray);
	}

	/**
	 * Gets the policy and all dependent benefit joins for that are associated
	 * with the current benefit join
	 *
	 * @param fakeJoinsForNonCoveredDependents
	 * @return
	 * @throws ArahantException
	 */
	public BHRBenefitJoin[] getPolicyAndDependentBenefitJoins(boolean fakeJoinsForNonCoveredDependents) throws ArahantException {
		final List<BHRBenefitJoin> benefitJoins = new ArrayList<BHRBenefitJoin>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (this.getBean().getHrBenefit() != null || this.getBean().getHrBenefitCategory() != null) //DECLINES... don't return anything but this one
		{
			benefitJoins.add(this);
			final BHRBenefitJoin[] benefitJoinArray = new BHRBenefitJoin[benefitJoins.size()];
			return benefitJoins.toArray(benefitJoinArray);
		}
		// try to load the paying person as an employee
		Employee employee = hsu.get(Employee.class, bean.getPayingPerson().getPersonId());
		if (employee == null)
			// not an employee, so must be a non-employee dependent - just add this join and we are done
			benefitJoins.add(this);
		else {
			// paying person is an employee, so let's get all dependents
			@SuppressWarnings("unchecked")
			List<String> dependents = (List) hsu.createCriteria(HrEmplDependent.class).selectFields(HrEmplDependent.DEP_KEY).eq(HrEmplDependent.EMPLOYEE, employee).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

			// get policy and all existing dependent benefit joins
			List<HrBenefitJoin> allExistingBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.APPROVED, bean.getBenefitApproved()).list();

			//System.out.println("Looking for " + bean.getPayingPersonId() + " " + bean.getPolicyStartDate() + " " + bean.getHrBenefitConfig().getBenefitConfigId());
			for (final HrBenefitJoin existingBenefitJoin : allExistingBenefitJoins) {
				// add this benefit join to the list, and remove the dependent if it happens to be a dependent benefit join
				benefitJoins.add(new BHRBenefitJoin(existingBenefitJoin));
				try {
					dependents.remove(existingBenefitJoin.getRelationshipId());
				} catch (Exception e) {
					BPerson bpp = new BPerson();
					if (bpp.hasPending(existingBenefitJoin.getPayingPersonId())) {
						String person = existingBenefitJoin.getPayingPerson().getNameLFM();
						throw new ArahantException("Please approve pending change request for " + person + " before approving benefits.");
					} else
						throw new ArahantException("Get Policy and Dependent benefit join error: " + e.getMessage());
				}
			}

			// check if we need to create fake dependent benefit joins for those dependents that don't have one
			if (fakeJoinsForNonCoveredDependents)
				// yes, so spin through remaining dependents
				for (String depId : dependents) {
					BHREmplDependent bdependent = new BHREmplDependent(depId);
					HrEmplDependent dependent = bdependent.getEmplDependent();
					BHRBenefitJoin dbj = new BHRBenefitJoin();
					dbj.create();
					dbj.setRelationship(dependent);
					dbj.setBenefitConfigId(bean.getHrBenefitConfigId());
					dbj.setPayingPerson(bean.getPayingPerson());
					dbj.setCoveredPerson(dependent.getPerson());
					dbj.setPolicyStartDate(bean.getPolicyStartDate());
					dbj.setPolicyEndDate(bean.getPolicyEndDate());
					dbj.setCoverageChangeDate(bean.getCoverageChangeDate());
					dbj.setCoverageStartDate(0);
					dbj.setCoverageEndDate(0);
					dbj.setOriginalCoverageDate(0);
					benefitJoins.add(dbj);
				}
		}

		// convert to array for return
		final BHRBenefitJoin[] benefitJoinArray = new BHRBenefitJoin[benefitJoins.size()];
		return benefitJoins.toArray(benefitJoinArray);
	}

	public void setHrBenefitConfig(HrBenefitConfig hrBenefitConfig) {
		bean.setHrBenefitConfig(hrBenefitConfig);
	}

	public void setHrBenefit(HrBenefit hrBenefit) {
		bean.setHrBenefit(hrBenefit);
	}

	public void setHrBenefitCategory(HrBenefitCategory hrBenefitCategory) {
		bean.setHrBenefitCategory(hrBenefitCategory);
	}

	public static boolean isPolicyBenefitJoin(HrBenefitJoin benefitJoin) {
		return benefitJoin.getPayingPerson().getPersonId().equals(benefitJoin.getCoveredPerson().getPersonId());
	}

	public boolean isPolicyBenefitJoin() {
		if (this.bean.getCoveredPerson() == null)
			return false;
		try {
			return this.bean.getPayingPerson().getPersonId().equals(this.bean.getCoveredPerson().getPersonId());
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDependentBenefitJoin(HrBenefitJoin benefitJoin) {
		return !benefitJoin.getPayingPerson().getPersonId().equals(benefitJoin.getCoveredPerson().getPersonId());
	}

	public boolean isDependentBenefitJoin() {
		return !this.bean.getPayingPerson().getPersonId().equals(this.bean.getCoveredPerson().getPersonId());
	}

	public void adjustStartDate(int newBenefitStartDate, boolean adjustCoverageDates) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (this.isPolicyBenefitJoin()) {
			this.bean.setPolicyStartDate(newBenefitStartDate);
			// check if end date now needs to be moved
			if (this.bean.getPolicyEndDate() != 0 && this.bean.getPolicyEndDate() < this.bean.getPolicyStartDate())
				this.bean.setPolicyEndDate(this.bean.getPolicyStartDate());

			// optionally set coverage dates
			if (adjustCoverageDates) {
				// set the coverage start date on the policy join if it has a coverage start date
				// and it is less than the new policy start date
				if (this.bean.getCoverageStartDate() != 0 && this.bean.getCoverageStartDate() < newBenefitStartDate) {
					this.bean.setCoverageStartDate(newBenefitStartDate);
					// check if end date now needs to be moved too
					if (this.bean.getCoverageEndDate() != 0 && this.bean.getCoverageEndDate() < this.bean.getCoverageStartDate())
						this.bean.setCoverageEndDate(this.bean.getCoverageStartDate());
				}

				hsu.saveOrUpdate(this.bean);
			}

			// also set the policy start date of all dependent benefit joins and optionally the coverage dates
			List<HrBenefitJoin> dependentBenefitJoins = this.getDependentBenefitJoins();
			for (HrBenefitJoin dependentBenefitJoin : dependentBenefitJoins) {
				// dependentBenefitJoin
				dependentBenefitJoin.setPolicyStartDate(newBenefitStartDate);

				// optionally set coverage dates
				if (adjustCoverageDates)
					if (dependentBenefitJoin.getCoverageStartDate() != 0 && dependentBenefitJoin.getCoverageStartDate() < newBenefitStartDate) {
						dependentBenefitJoin.setCoverageStartDate(newBenefitStartDate);
						// check if end date now needs to be moved too
						if (dependentBenefitJoin.getCoverageEndDate() != 0 && dependentBenefitJoin.getCoverageEndDate() < dependentBenefitJoin.getCoverageStartDate())
							dependentBenefitJoin.setCoverageEndDate(dependentBenefitJoin.getCoverageStartDate());
					}

				hsu.saveOrUpdate(dependentBenefitJoin);
			}
		} else // optionally set coverage dates
		if (adjustCoverageDates) {
			// set the coverage start date on the policy join if it has a coverage start date
			// and it is less than the new policy start date
			if (this.bean.getCoverageStartDate() != 0 && this.bean.getCoverageStartDate() < newBenefitStartDate) {
				this.bean.setCoverageStartDate(newBenefitStartDate);
				// check if end date now needs to be moved too
				if (this.bean.getCoverageEndDate() != 0 && this.bean.getCoverageEndDate() < this.bean.getCoverageStartDate())
					this.bean.setCoverageEndDate(this.bean.getCoverageStartDate());
			}

			hsu.saveOrUpdate(this.bean);
		}
	}

	public String getBenefitConfigName() {
		if (bean.getHrBenefitConfig() == null)
			return "";
		return bean.getHrBenefitConfig().getName();
	}

	private void checkPolicyEndDateAgainstCoverageDates() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// if this just set an end date to non-zero, set all zero dep joins to same date
		if (bean.getPolicyEndDate() != 0) {
			if (bean.getCoverageEndDate() == 0 && bean.getCoverageStartDate() != 0)
				bean.setCoverageEndDate(bean.getPolicyEndDate());
			else if (bean.getPolicyEndDate() < bean.getCoverageEndDate())
				throw new ArahantWarning("Policy End Date is prior to Coverage End Date for " + bean.getCoveredPerson().getNameLFM() + " (" + bean.getCoveredPerson().getUnencryptedSsn() + ").");

			// process the dependent benefit joins
			for (HrBenefitJoin dbj : new BHRBenefitJoin(bean).getDependentBenefitJoins())
				if (dbj.getCoverageEndDate() == 0 && dbj.getCoverageStartDate() != 0) {
					dbj.setCoverageEndDate(bean.getPolicyEndDate());
					hsu.saveOrUpdate(dbj); //DCM: this is the second call
				} else if (bean.getPolicyEndDate() < dbj.getCoverageEndDate())
					throw new ArahantWarning("Policy End Date is prior to Coverage End Date for " + dbj.getCoveredPerson().getNameLFM() + " (" + dbj.getCoveredPerson().getUnencryptedSsn() + ").");
		}
	}

	private void checkPolicyDatesAgainstExistingBenefitInCategory() throws ArahantException {
		// determine type of this benefit
		if (bean.getHrBenefitCategory() != null) // benefit category level decline
			this.checkPolicyDatesForBenefitCategoryDecline();
		else if (bean.getHrBenefit() != null) // benefit level decline
			this.checkPolicyDatesForBenefitDecline();
		else // benefit config
			this.checkPolicyDatesForBenefitConfig();
	}

	private void checkPolicyDatesForBenefitCategoryDecline() throws ArahantException {
		// Type 1 Check - Term existing benefit as of day before start of new
		//     Cases:              Case 1      Case 2
		//     New Dates:           x x         x
		//     Existing Dates:     x           x
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();

			// look for category declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list());

			// should not have any benefit declines for which to look ...

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins)
				terminate(existingPolicyBenefitJoin);
		}

		// Type 2 Check - Term new benefit as of day before start of earliest existing found
		//     Cases:              Case 1      Case 2
		//     New Dates:          x           x
		//     Existing Dates:      x x         x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() == 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();

			// look for category declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list());

			// should not have any benefit declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0) {
				int earliestPolicyStartDate = 0;

				// find earliest start date of existings
				for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins)
					earliestPolicyStartDate = (earliestPolicyStartDate == 0 || existingPolicyBenefitJoin.getPolicyStartDate() < earliestPolicyStartDate) ? existingPolicyBenefitJoin.getPolicyStartDate() : earliestPolicyStartDate;

				// now term the new benefit as of the day before earliest start date
				bean.setPolicyEndDate(DateUtils.add(earliestPolicyStartDate, -1));
			}
		}

		// Type 3 Check - Error
		//     Cases:              Case 1      Case 2      Case 3
		//     New Dates:          x           x           x x
		//     Existing Dates:     x x         x           x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();

			// look for category declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list());

			// should not have any benefit declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}

		// Type 4 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:          x x         x x         x x         x x         x   x       x  x
		//     Existing Dates:      x x          x x        x            x          x x           x x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();

			// look for category declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list());

			// should not have any benefit declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}

		// Type 5 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:           x x          x x        x            x          x x          x x
		//     Existing Dates:     x x         x x         x x         x x         x   x       x   x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list();

			// look for category declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).list());

			// should not have any benefit declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}

	}

	private void checkPolicyDatesForBenefitDecline() throws ArahantException {
		// Type 1 Check - Term existing benefit as of day before start of new
		//     Cases:              Case 1      Case 2
		//     New Dates:           x x         x
		//     Existing Dates:     x           x
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).list());

			// should not have any category declines for which to look ...

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins)
				terminate(existingPolicyBenefitJoin);
		}

		// Type 2 Check - Term new benefit as of day before start of earliest existing found
		//     Cases:              Case 1      Case 2
		//     New Dates:          x           x
		//     Existing Dates:      x x         x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() == 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).list());

			// should not have any category declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0) {
				int earliestPolicyStartDate = 0;

				// find earliest start date of existings
				for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins)
					earliestPolicyStartDate = (earliestPolicyStartDate == 0 || existingPolicyBenefitJoin.getPolicyStartDate() < earliestPolicyStartDate) ? existingPolicyBenefitJoin.getPolicyStartDate() : earliestPolicyStartDate;

				// now term the new benefit as of the day before earliest start date
				bean.setPolicyEndDate(DateUtils.add(earliestPolicyStartDate, -1));
			}
		}

		// Type 3 Check - Error
		//     Cases:              Case 1      Case 2      Case 3
		//     New Dates:          x           x           x x
		//     Existing Dates:     x x         x           x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).list());

			// should not have any category declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}


		// Type 4 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:          x x         x x         x x         x x         x   x       x  x
		//     Existing Dates:      x x          x x        x            x          x x           x x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).list());

			// should not have any category declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}

		// Type 5 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:           x x          x x        x            x          x x          x x
		//     Existing Dates:     x x         x x         x x         x x         x   x       x   x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).list());

			// should not have any category declines for which to look ...

			if (existingPolicyBenefitJoins.size() > 0)
				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
		}
	}

	private void checkPolicyDatesForBenefitConfig() throws ArahantException {
		// Type 1 Check - Term existing benefit as of day before start of new
		//     Cases:              Case 1      Case 2
		//     New Dates:           x x         x
		//     Existing Dates:     x           x
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()).list());

			// look for category declines
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_END_DATE, 0).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list());

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins) {
				// if category allows multiple configs, this is not a decline of some type, and it is not the same config, we don't care
				if (bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()
						&& existingPolicyBenefitJoin.getHrBenefitConfig() != null
						&& !existingPolicyBenefitJoin.getHrBenefitConfig().getBenefitConfigId().equals(bean.getHrBenefitCategoryId()))
					continue;
				terminate(existingPolicyBenefitJoin);
			}
		}

		// Type 2 Check - Term new benefit as of day before start of earliest existing found
		//     Cases:              Case 1      Case 2
		//     New Dates:          x           x
		//     Existing Dates:      x x         x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() == 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()).list());

			// look for category declines
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ge(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list());

			if (existingPolicyBenefitJoins.size() > 0) {
				int earliestPolicyStartDate = 0;

				// find earliest start date of existing matches
				for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins) {
					// if category allows multiple configs, this is not a decline of some type, and it is not the same config, we don't care
					if (bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()
							&& existingPolicyBenefitJoin.getHrBenefitConfig() != null
							&& !existingPolicyBenefitJoin.getHrBenefitConfig().getBenefitConfigId().equals(bean.getHrBenefitCategoryId()))
						continue;

					earliestPolicyStartDate = (earliestPolicyStartDate == 0 || existingPolicyBenefitJoin.getPolicyStartDate() < earliestPolicyStartDate) ? existingPolicyBenefitJoin.getPolicyStartDate() : earliestPolicyStartDate;
				}

				// now term the new benefit as of the day before earliest start date, assumming we got a match
				if (earliestPolicyStartDate != 0) {
					bean.setPolicyEndDate(DateUtils.add(earliestPolicyStartDate, -1));
					bean.setCoverageEndDate(DateUtils.add(earliestPolicyStartDate, -1));
				}
			}
		}

		// Type 3 Check - Error
		//     Cases:              Case 1      Case 2      Case 3
		//     New Dates:          x           x           x x
		//     Existing Dates:     x x         x           x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()).list());

			// look for category declines
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list());

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins) {
				// if category allows multiple configs, this is not a decline of some type, and it is not the same config, we don't care
				if (bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()
						&& existingPolicyBenefitJoin.getHrBenefitConfig() != null
						&& !existingPolicyBenefitJoin.getHrBenefitConfig().getBenefitConfigId().equals(bean.getHrBenefitCategoryId()))
					continue;

				terminate(existingPolicyBenefitJoin);
				//throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
			}
		}

		// Type 4 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:          x x         x x         x x         x x         x   x       x  x
		//     Existing Dates:      x x          x x        x            x          x x           x x
		if (bean.getPolicyStartDate() != 0 && bean.getPolicyEndDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).gt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()).list());

			// look for category declines
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ge(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyEndDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list());

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins) {
				// if category allows multiple configs, this is not a decline of some type, and it is not the same config, we don't care
				if (bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()
						&& existingPolicyBenefitJoin.getHrBenefitConfig() != null
						&& !existingPolicyBenefitJoin.getHrBenefitConfig().getBenefitConfigId().equals(bean.getHrBenefitCategoryId()))
					continue;

				throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
			}
		}

		// Type 5 Check - Error
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     New Dates:           x x          x x        x            x          x x          x x
		//     Existing Dates:     x x         x x         x x         x x         x   x       x   x
		if (bean.getPolicyStartDate() != 0) {
			// look for configs...
			List<HrBenefitJoin> existingPolicyBenefitJoins = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list();

			// look for benefit declines...
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()).list());

			// look for category declines
			existingPolicyBenefitJoins.addAll(hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.POLICY_START_DATE, 0).ne(HrBenefitJoin.POLICY_END_DATE, 0).le(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).ge(HrBenefitJoin.POLICY_END_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()) // only looking for ones we pay for, not other people covering us
					.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()).list());

			for (HrBenefitJoin existingPolicyBenefitJoin : existingPolicyBenefitJoins) {
				// if category allows multiple configs, this is not a decline of some type, and it is not the same config, we don't care
				if (bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()
						&& existingPolicyBenefitJoin.getHrBenefitConfig() != null
						&& !existingPolicyBenefitJoin.getHrBenefitConfig().getBenefitConfigId().equals(bean.getHrBenefitCategoryId()))
					continue;

				terminate(existingPolicyBenefitJoin);

				//throw new ArahantWarning("Existing benefit or decline's Policy Dates overlap new decline's Policy Dates.");
			}
		}
	}

	private void checkCoverageDatesAgainstExistingBenefitInCategory() throws ArahantException {
		//
		//DOES NOT WORK BECAUSE WE LET START DATES FROM OLD POLICIES BE PRESERVED IN TO NEW POLICIES, CAUSING CONFLICTS
		//
		/*
		 * // skip declines (no coverage date information present) or configs
		 * in a category that allow multiples if
		 * (bean.getHrBenefitConfig()==null ||
		 * bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
		 * return;
		 *
		 * // note that one thing that could have screwed us up but won't is if
		 * a benefit will be auto-termed by // this benefit in the case that it
		 * is new ... if it had not been termed yet, we could potentially find
		 * // a collision of coverage date and erroneously report it, but it
		 * should have been termed by the point // we are at this call
		 *
		 * // spin through all joins of this policy (don't create ones for
		 * dependents not enrolled) BHRBenefitJoin[] bAllBenefitJoins =
		 * this.getPolicyAndDependentBenefitJoins(false); for (BHRBenefitJoin
		 * bBenefitJoin : bAllBenefitJoins) { // skip those not covered if
		 * (bBenefitJoin.getCoverageStartDate()==0) continue;
		 *
		 * // look for an existing coverage configuration in the same benefit
		 * as the one // we are creating that covers the covered person and
		 * either has no coverage end date // or a coverage end date that is
		 * greater than the new coverage start date HrBenefitJoin
		 * existingBenefitJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .ne(HrBenefitJoin.BENEFIT_JOIN_ID,
		 * bBenefitJoin.getBenefitJoinId())
		 * .ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
		 * .geOrEq(HrBenefitJoin.COVERAGE_END_DATE,
		 * bBenefitJoin.getCoverageStartDate(), 0)
		 * .eq(HrBenefitJoin.COVERED_PERSON,
		 * bBenefitJoin.getCoveredPerson().getPerson())
		 * .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
		 * .joinTo(HrBenefitConfig.HR_BENEFIT) .eq(HrBenefit.BENEFIT_CATEGORY,
		 * bBenefitJoin.bean.getHrBenefitConfig().getHrBenefitCategory())
		 * .first();
		 *
		 * if (existingBenefitJoin==null) { // look for an existing coverage
		 * configuration in the same benefit as the one // we are creating that
		 * covers the covered person and has a start date // that is less than
		 * our end date
		 * existingBenefitJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .ne(HrBenefitJoin.BENEFIT_JOIN_ID,
		 * bBenefitJoin.getBenefitJoinId())
		 * .ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
		 * .le(HrBenefitJoin.COVERAGE_START_DATE,
		 * bBenefitJoin.getCoverageEndDate()) .eq(HrBenefitJoin.COVERED_PERSON,
		 * bBenefitJoin.getCoveredPerson().getPerson())
		 * .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
		 * .joinTo(HrBenefitConfig.HR_BENEFIT) .eq(HrBenefit.BENEFIT_CATEGORY,
		 * bBenefitJoin.bean.getHrBenefitConfig().getHrBenefitCategory())
		 * .first();
		 *
		 * if (existingBenefitJoin==null &&
		 * bBenefitJoin.getCoverageEndDate()==0) { // look for an existing
		 * coverage configuration in the same benefit as the one // we are
		 * creating that covers the covered person and has a start date // that
		 * is greater than or equal to our start date (problem because we have
		 * no end date)
		 * existingBenefitJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .ne(HrBenefitJoin.BENEFIT_JOIN_ID,
		 * bBenefitJoin.getBenefitJoinId())
		 * .ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
		 * .geOrEq(HrBenefitJoin.COVERAGE_START_DATE,
		 * bBenefitJoin.getCoverageStartDate(),
		 * bBenefitJoin.getCoverageStartDate())
		 * .eq(HrBenefitJoin.COVERED_PERSON,
		 * bBenefitJoin.getCoveredPerson().getPerson())
		 * .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
		 * .joinTo(HrBenefitConfig.HR_BENEFIT) .eq(HrBenefit.BENEFIT_CATEGORY,
		 * bBenefitJoin.bean.getHrBenefitConfig().getHrBenefitCategory())
		 * .first(); } }
		 *
		 * if (existingBenefitJoin!= null) throw new ArahantWarning("Coverage
		 * Start Date for " + bBenefitJoin.getCoveredPerson().getFullName() + "
		 * (" + bBenefitJoin.getCoveredPerson().getSsn() + ") overlaps with an
		 * existing benefit's Coverage Start Date. Existing benefit policy owner
		 * is " + existingBenefitJoin.getPayingPerson().getDisplayName() + " ("
		 * + existingBenefitJoin.getPayingPerson().getSsn() + ")."); }
		 */
	}

	private void checkAutoApprove() {
		// check if already approved and return
		if (bean.getBenefitApproved() == 'Y')
			return;

		// check if a coverage config and return
		if (bean.getHrBenefitConfig() != null)
			return;

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		// check if a benefit level decline
		if (bean.getHrBenefit() != null)
			// check if they already have a policy benefit join with a coverage config and return if so
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()).exists())
				return;

		// check if a category level decline
		if (bean.getHrBenefitCategory() != null)
			// check if they already have a policy benefit join with a coverage config and return if so
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()).exists())
				return;

		// all checks passed, auto-approve whole policy
		this.setPolicyApproved(true);
	}

	private void terminate(HrBenefitJoin benefitJoin) throws ArahantException {
		// if benefit join is approved, terminate it
		if (bean.getBenefitApproved() == 'Y') {
			BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(benefitJoin);

			// set final date of the specified benefit join to be the day before our current benefit's start date
			int finalDate = DateUtils.add(bean.getPolicyStartDate(), -1);

			// check the end date - it is arguable if we should throw an exception here to the user instead
			if (finalDate < bBenefitJoin.getPolicyStartDate())
				finalDate = bBenefitJoin.getPolicyStartDate();

			//figure out the reason

			bBenefitJoin.terminate(finalDate);
		}
	}

	public void terminate(int finalDate, String changeReasonId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		//flush anything outstanding
		hsu.flush(); //this is important!  Failure to do this will mess up history

		BPerson p = new BPerson(bean.getPayingPersonId());
		if (p.isEmployee()) {
			BEmployee bemp = p.getBEmployee();

			if (isEmpty(changeReasonId))
				try {
					//figure out the reason
					AIProperty prop = new AIProperty("TerminateChangeReason", bemp.getLastStatusId());

					if (!isEmpty(prop.getValue()))
						bean.setChangeDescription(prop.getValue());
				} catch (Exception e) {
					bean.setChangeDescription("Terminated");
				}
			else {
				BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(changeReasonId);
				bean.setChangeDescription(bcr.getDescription());
				bean.setBenefitChangeReason(bcr.getBean());
			}

		}

		// determine what type of join this is
		if (this.isPolicyBenefitJoin()) {
			// it is a policy join ...

			// set the policy end date on the policy join
			this.bean.setPolicyEndDate(finalDate);


			// set the coverage end date on the policy join if it has a coverage start date and does
			// not already have an end date or the one it has is greater than the end date being given
			// (otherwise we leave the term date as it is, earlier than the date terminated)
			if (this.bean.getCoverageEndDate() > finalDate || (bean.getCoverageStartDate() != 0 && bean.getCoverageEndDate() == 0))
				this.bean.setCoverageEndDate(finalDate);

			if (bean.getCoverageStartDate() > bean.getCoverageEndDate())
				bean.setCoverageStartDate(bean.getCoverageEndDate());

			hsu.saveOrUpdate(this.bean);
//			hsu.flush();

			// also set the policy end date of all dependent benefit joins
			List<HrBenefitJoin> dependentBenefitJoins = this.getDependentBenefitJoins();
			for (HrBenefitJoin dependentBenefitJoin : dependentBenefitJoins) {
				// dependentBenefitJoin
				dependentBenefitJoin.setPolicyEndDate(finalDate);
				dependentBenefitJoin.setChangeDescription(bean.getChangeDescription());
				// set the coverage end date on the dependent join if it has a coverage start date and does
				// not already have an end date or the one it has is greater than the end date being given
				// (otherwise we leave the term date as it is, earlier than the date terminated)
				if (dependentBenefitJoin.getCoverageStartDate() != 0
						&& (dependentBenefitJoin.getCoverageEndDate() == 0 || (dependentBenefitJoin.getCoverageEndDate() != 0 && dependentBenefitJoin.getCoverageEndDate() > finalDate)))
					dependentBenefitJoin.setCoverageEndDate(finalDate);

				if (dependentBenefitJoin.getCoverageStartDate() > dependentBenefitJoin.getCoverageEndDate())
					dependentBenefitJoin.setCoverageStartDate(dependentBenefitJoin.getCoverageEndDate());

				hsu.saveOrUpdate(dependentBenefitJoin);
			}

			bean.setPolicyEndDate(finalDate);
			// now delete the policy join if the end date behind us
			if (this.bean.getPolicyEndDate() <= DateUtils.now())
				this.delete();
			bean.setPolicyEndDate(finalDate);
		} else // it is a dependent join ...
		// set the coverage end date on the dependent join if it has a coverage start date and does
		// not already have an end date or the one it has is greater than the end date being given
		// (otherwise we leave the term date as it is, earlier than the date terminated)
		    if (this.bean.getCoverageEndDate() > finalDate || (bean.getCoverageStartDate() != 0 && bean.getCoverageEndDate() == 0)) {
			    this.bean.setCoverageEndDate(finalDate);
			    hsu.saveOrUpdate(this.bean);
		    } // we leave dependent joins in place, even if they coverage end date is past so
		// they can be seen by the user when the policy is viewed
	}

	public void terminate(int finalDate) throws ArahantException {
		terminate(finalDate, null);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
//		try
//		{
//			hsu.refresh(this.bean);
//		}
//		catch (org.hibernate.UnresolvableObjectException e)
//		{
//			//it's already gone, so just return
//			return;
//		}

		if (bean.getPolicyEndDate() == 0 && bean.getBenefitApproved() == 'Y')
			throw new ArahantException("Please set policy end date before deleting record.  If they should never have had the benefit, please set the end date to before the begin date.  If you are replacing existing coverage, you must set the end dates on the existing coverage before creating the new enrollment."); //bean.setPolicyEndDate(DateUtils.now());

		if (bean.getCoverageEndDate() == 0 && bean.getCoverageStartDate() != 0 && bean.getBenefitApproved() == 'Y')
			throw new ArahantException("Please set coverage end date before deleting record.  If they should never have had the benefit, please set the end date to before the begin date."); //bean.setCoverageEndDate(DateUtils.now());

		// check if this is a policy benefit join
		if (BHRBenefitJoin.isPolicyBenefitJoin(this.bean)) {
			// it is, remove the dependent joins and beneficiaries
			try {
				for (HrBenefitJoin dbj : getDependentBenefitJoins()) {
					if (dbj.getCoverageEndDate() == 0)
						dbj.setCoverageEndDate(bean.getCoverageEndDate());
					if (dbj.getPolicyEndDate() == 0)
						dbj.setPolicyEndDate(bean.getPolicyEndDate());
					hsu.createCriteria(WizardProject.class).eq(WizardProject.BENEFIT_JOIN, dbj).delete();
					hsu.delete(dbj.getPhysicians());
					hsu.saveOrUpdate(dbj);
				}
				hsu.delete(this.getDependentBenefitJoins());
			} catch (ArahantException e) {
				throw new ArahantDeleteException(e);
			}
			hsu.delete(this.bean.getBeneficiaries());
		}
		hsu.delete(this.bean.getPhysicians());

		hsu.createCriteria(WizardProject.class).eq(WizardProject.BENEFIT_JOIN, bean).delete();

		super.delete();

		hsu.flush();
	}

	/**
	 * Will delete all benefit joins as they identified, with no regard to
	 * whether it is a dependent benefit join or policy benefit join. Be careful
	 * not to pass only a dependent benefit join when the whole policy should be
	 * deleted.
	 *
	 * @param benefitJoinIds
	 * @throws ArahantException
	 */
	public static void delete(String[] benefitJoinIds) throws ArahantException {
		ArrayList<BHRBenefitJoin> benefitJoins = new ArrayList<BHRBenefitJoin>();

		for (String benefitJoinId : benefitJoinIds)
			benefitJoins.add(new BHRBenefitJoin(benefitJoinId));

		BHRBenefitJoin.delete(benefitJoins);
	}

	/**
	 * Will delete all benefit joins as they identified, with no regard to
	 * whether it is a dependent benefit join or policy benefit join. Be careful
	 * not to pass only a dependent benefit join when the whole policy should be
	 * deleted.
	 *
	 * @param benefitJoins
	 * @param checkIsPolicyBenefitJoin
	 * @throws ArahantException
	 */
	public static void delete(List<BHRBenefitJoin> benefitJoins) throws ArahantException {
		for (BHRBenefitJoin benefitJoin : benefitJoins)
			benefitJoin.delete();
	}

	/**
	 * Delete all policy benefit joins where the specified person is covered or
	 * paying and the benefit join is termed
	 *
	 * @param person
	 * @throws ArahantException
	 */
	public static void deleteExpiredBenefitsReferencingPerson(BPerson person) throws ArahantException {
		// get the list of all benefit joins those where the person is paying (policy benefit joins)
		List<HrBenefitJoin> benefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, person.getPerson()).eq(HrBenefitJoin.COVERED_PERSON, person.getPerson()).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).list();

		// spin through them all
		for (HrBenefitJoin benefitJoin : benefitJoins)
			new BHRBenefitJoin(benefitJoin).delete();

//		 get the list of all benefit joins those where the person is covered (dependent benefit joins)
		benefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.PAYING_PERSON, person.getPerson()).eq(HrBenefitJoin.COVERED_PERSON, person.getPerson()).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).list();

		for (HrBenefitJoin benefitJoin : benefitJoins) {
			BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(benefitJoin);
			// it's a dependent benefit join, so we need to get the policy to do the delete so this clears everything
			bBenefitJoin = new BHRBenefitJoin(bBenefitJoin.getPolicyBenefitJoin());
			bBenefitJoin.delete();
		}
	}

	public BHRBenefitJoin saveDependentBenefitAssignment(final BHREmplDependent bDep,
			final int coverageStartDate,
			final int coverageEndDate,
			int originalCoverageDate,
			final double amountCovered,
			final boolean usingCOBRA,
			int newPolicyStart,
			int newPolicyEnd,
			int coverageChangeDate,
			String comments,
			String otherInsurance,
			boolean otherPrimary) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// make sure this is a policy benefit join and not called accidentally for a dependent benefit join
		if (!this.isPolicyBenefitJoin())
			if (this.getBenefitConfig() != null && !this.getBenefitConfig().getCoversEmployee())
				throw new ArahantException("Benefit doesn't cover employee.");
			else
				throw new ArahantException("The benefit is not a policy benefit join");

		// have to make sure the policy benefit join exists
		hsu.saveOrUpdate(bean);

		// try to load this dependent benefit join for the specified dependent (may not exist yet)
		final HrEmplDependent employeeDependent = bDep.bean;
		HrBenefitJoin dependentBenefitJoin = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.RELATIONSHIP, employeeDependent).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).first();

		BHRBenefitJoin bBenefitJoin;

		// sanity check the dates/amounts
		if (coverageStartDate == 0 && coverageEndDate != 0)
			throw new ArahantException(bDep.getNameLFM() + " (" + bDep.getSsn() + ") has a Coverage End Date but no Coverage Start Date.");
		else if (coverageStartDate == 0 && coverageEndDate == 0 && amountCovered != 0)
			throw new ArahantException(bDep.getNameLFM() + " (" + bDep.getSsn() + ") has an Amount but no Coverage Start Date.");


		//did they have a prior coverage that needs coverage end date
		if (coverageStartDate == 0)
			//does this benefit allow multiple assignments
			if (bean.getHrBenefitConfig() != null && !bean.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()) {
				//does not allow multiple benefits, so see if there was one
				HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.RELATIONSHIP, employeeDependent).eq(HrBenefitJoin.COVERAGE_END_DATE, 0).lt(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).orderByDesc(HrBenefitJoin.POLICY_START_DATE).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory()).first();
				if (bj != null) {
					bj.setCoverageEndDate(DateUtils.addDays(bean.getPolicyStartDate(), -1));
					hsu.saveOrUpdate(bj);
				}
			}

		if (dependentBenefitJoin == null) {
			// not yet there, it is new assignment
			bBenefitJoin = new BHRBenefitJoin();

			// if no real detail for this dependent, don't insert it
			if (coverageStartDate == 0 && coverageEndDate == 0 && amountCovered == 0)
				return null;

			// create it
			bBenefitJoin.create();
		} else
			// already there, it is an update
			bBenefitJoin = new BHRBenefitJoin(dependentBenefitJoin);

		// fill out the dependent benefit join from the policy benefit join and the employee dependent's specifics
		bBenefitJoin.setAmountCovered(amountCovered);
		bBenefitJoin.setCoverageStartDate(coverageStartDate);
		bBenefitJoin.setCoverageEndDate(coverageEndDate == 0 ? newPolicyEnd : coverageEndDate);
		bBenefitJoin.setOriginalCoverageDate(originalCoverageDate == 0 ? coverageStartDate : originalCoverageDate);
		bBenefitJoin.setFromDependent(employeeDependent); // sets covered and relationship fields
		bBenefitJoin.setFromPolicyBenefitJoin(bean); // sets most of the key fields from the paying person's hr benefit join
		bBenefitJoin.setUsingCOBRA(usingCOBRA ? 'Y' : 'N');
		bBenefitJoin.setPolicyStartDate(newPolicyStart);
		bBenefitJoin.setPolicyEndDate(newPolicyEnd);
		bBenefitJoin.setCoverageChangeDate(coverageChangeDate);
		bBenefitJoin.setComments(comments);
		bBenefitJoin.setOtherInsuanceIsPrimary(otherPrimary);
		bBenefitJoin.setOtherInsurance(otherInsurance);
		bBenefitJoin.setBenefitApproved(bean.getBenefitApproved());
		bBenefitJoin.copyReason(bean);


		// insert or update
		if (dependentBenefitJoin == null)
			bBenefitJoin.insert();
		else
			bBenefitJoin.update();



		// if no real detail for this dependent, we need to delete the existing
		if (coverageStartDate == 0 && coverageEndDate == 0 && amountCovered == 0) {
			bBenefitJoin.delete();
			return null;
		}

		return bBenefitJoin;
	}

	public void setFromPolicyBenefitJoin(HrBenefitJoin policyBenefitJoin) throws ArahantException {
		// make sure this is a policy benefit join and not called accidentally for a dependent benefit join
		if (!(new BHRBenefitJoin(policyBenefitJoin).isPolicyBenefitJoin()))
			throw new ArahantException("The benefit is not a policy benefit join");

		this.bean.setPayingPerson(policyBenefitJoin.getPayingPerson());
		this.bean.setPolicyStartDate(policyBenefitJoin.getPolicyStartDate());
		this.bean.setPolicyEndDate(policyBenefitJoin.getPolicyEndDate());
		this.bean.setCoverageChangeDate(policyBenefitJoin.getCoverageChangeDate());
		this.bean.setHrBenefit(policyBenefitJoin.getHrBenefit());
		this.bean.setHrBenefitCategory(policyBenefitJoin.getHrBenefitCategory());
		this.bean.setHrBenefitConfig(policyBenefitJoin.getHrBenefitConfig());
	}

	public void setFromDependent(HrEmplDependent dependent) {
		this.bean.setRelationship(dependent);
		this.bean.setCoveredPerson(dependent.getPerson());
	}

	/**
	 * Switch a benefit to another employee, used when employee is a dependent
	 * and is taking over the paying of the benefit
	 *
	 * @param employeeId The id of the employee who will get the benefit
	 * @throws ArahantException
	 */
	public void switchPolicyOwnerToEmployee(final String employeeId) throws ArahantException {
		// make sure this is a policy benefit join and not called accidentally for a dependent benefit join
		if (!this.isPolicyBenefitJoin())
			throw new ArahantException("The benefit is not a policy benefit join");

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		// we should only be switching to an employee, as dependents can't have dependent benefit joins
		final Employee employee = hsu.get(Employee.class, employeeId);

		// we need to insert the new policy benefit join last so validations are correct, so just
		// make a list of the dependent benefit joins first, then add the policy benefit join in last
		List<HrBenefitJoin> allBenefitJoins = this.getDependentBenefitJoins();
		allBenefitJoins.add(this.bean);

		// spin through all of the benefit joins
		int endDate = DateUtils.now();
		for (HrBenefitJoin benefitJoin : allBenefitJoins) {
			// set the policy end date
			benefitJoin.setPolicyEndDate(endDate);
			hsu.saveOrUpdate(benefitJoin);

			// clone this benefit join, minus the policy dates, and associate it to the new paying person
			BHRBenefitJoin newBenefitJoin = this.cloneJoinForNewPolicyOwner(employee, benefitJoin, endDate);

			// delete the old benefit join
			hsu.delete(benefitJoin);

			// now insert the new cloned benefit join (validation will fire for policy benefit join)
			newBenefitJoin.insert();
		}
	}

	private BHRBenefitJoin cloneJoinForNewPolicyOwner(final Person person, HrBenefitJoin oldBenefitJoin, int startDate) throws ArahantException {
		BHRBenefitJoin newBenefitJoin = new BHRBenefitJoin();

		newBenefitJoin.create();

		newBenefitJoin.setAmountCovered(oldBenefitJoin.getAmountCovered());
		newBenefitJoin.setAmountPaid(oldBenefitJoin.getAmountPaid());
		newBenefitJoin.setBenefitApproved(oldBenefitJoin.getBenefitApproved());
		newBenefitJoin.setBenefitDeclined(oldBenefitJoin.getBenefitDeclined());
		newBenefitJoin.setBeneficiaries(oldBenefitJoin.getBeneficiaries());
		newBenefitJoin.copyReason(oldBenefitJoin);
		newBenefitJoin.setCoverageEndDate(oldBenefitJoin.getCoverageEndDate());
		newBenefitJoin.setCoverageStartDate(oldBenefitJoin.getCoverageStartDate());
		newBenefitJoin.setOriginalCoverageDate(oldBenefitJoin.getOriginalCoverageDate());
		newBenefitJoin.setCoveredPerson(oldBenefitJoin.getCoveredPerson());
		newBenefitJoin.setHrBenefit(oldBenefitJoin.getHrBenefit());
		newBenefitJoin.setHrBenefitCategory(oldBenefitJoin.getHrBenefitCategory());
		newBenefitJoin.setHrBenefitConfig(oldBenefitJoin.getHrBenefitConfig());
		newBenefitJoin.setInsuranceId(oldBenefitJoin.getInsuranceId());
		newBenefitJoin.setPayingPerson(person);
		newBenefitJoin.setPolicyStartDate(startDate);
		newBenefitJoin.setPolicyEndDate(0);
		newBenefitJoin.setCoverageChangeDate(oldBenefitJoin.getCoverageChangeDate());
		newBenefitJoin.setRelationship(
				ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, person).eq(HrEmplDependent.PERSON, oldBenefitJoin.getCoveredPerson()).first());
		newBenefitJoin.setUsingCOBRA(oldBenefitJoin.getUsingCOBRA());

		return newBenefitJoin;
	}

	public void copyFrom(BHRBenefitJoin oldBenefitJoin) {
		HibernateSessionUtil.copyCorresponding(bean, oldBenefitJoin.bean, HrBenefitJoin.BENEFIT_JOIN_ID);

	}

	public void separateOutDependentBenefitJoinForPerson(final String personId) throws ArahantException {
		// our current benefit join may be a policy benefit join or a dependent benefit join, we don't care ...
		// we just need to separate out the dependent benefit join for the specified person
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final Person person = hsu.get(Person.class, personId);

		// look for dependent benefit join using the paying person of our current benefit join and the specified person
		HrBenefitJoin dependentBenefitJoin = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitCategory()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefit()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.COVERED_PERSON, person).first();

		if (dependentBenefitJoin == null)
			throw new ArahantException("Can't separate coverage as it could not be found for current benefit");

		// clone this dependent benefit join, minus the policy dates, and associate it to the new paying person
		// (will now be a policy benefit join)
		BHRBenefitJoin newBenefitJoin = this.cloneJoinForNewPolicyOwner(person, dependentBenefitJoin, DateUtils.now());

		// delete the old dependent benefit join
		hsu.delete(dependentBenefitJoin);

		// now insert the new cloned policy benefit join (validation will fire)
		newBenefitJoin.insert();
	}

	public BPerson getPayingPerson() throws ArahantException {
		if (bean.getPayingPerson() == null)
			return null;

		return new BPerson(bean.getPayingPerson());
	}

	public String getPayingPersonId() throws ArahantException {
		return bean.getPayingPersonId();
	}

	public BPerson getCoveredPerson() throws ArahantException {
		if (bean.getCoveredPerson() == null)
			return null;

		return new BPerson(bean.getCoveredPerson());
	}

	public String getCoveredPersonId() throws ArahantException {
		return bean.getCoveredPersonId();
	}

	public String getCoveredNameLFM() {
		return bean.getCoveredPerson().getNameLFM();
	}

	public String getCoveredFirstName() {
		return bean.getCoveredPerson().getFname();
	}

	public String getCoveredLastName() {
		return bean.getCoveredPerson().getLname();
	}

	public String getCoveredMiddleName() {
		return bean.getCoveredPerson().getMname();
	}

	public String getCoveredSsn() {
		return bean.getCoveredPerson().getUnencryptedSsn();
	}

	public BHRBenefitConfig getBenefitConfig() {
		if (bean.getHrBenefitConfig() == null)
			return null;
		return new BHRBenefitConfig(bean.getHrBenefitConfig());
	}

	public String getBenefitConfigId() {
		if (bean.getHrBenefitConfig() == null)
			return null;

		return bean.getHrBenefitConfigId();
	}

	/**
	 * @param l a list of benefit joins to convert to an array of biz objects
	 * @return 
	 */
	public static BHRBenefitJoin[] makeArray(final Collection<HrBenefitJoin> l) {
		final BHRBenefitJoin[] ret = new BHRBenefitJoin[l.size()];
		final Iterator<HrBenefitJoin> bjItr = l.iterator();

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitJoin(bjItr.next());

		return ret;
	}

	public void approve() throws ArahantDeleteException {
		//TODO This needs to be rechecked and revamped when we revisit the wizard
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		// are we a category level decline?
		if (bean.getHrBenefitCategory() != null) {
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitCategory()));
			bean.setBenefitApproved('Y');
			hsu.saveOrUpdate(bean);
			return;
		}

		// are we a benefit level decline?
		if (bean.getHrBenefit() != null) {
			// yes, set policy term date on old benefits
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit()));
			bean.setBenefitApproved('Y');
			hsu.saveOrUpdate(bean);
			return;
		}


		// if this benefit doesn't have any exclusiveness, just set approve flag and return
		if (bean.getHrBenefitConfig().getHrBenefit().getRequiresDecline() == 'N' && bean.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()) {
			//term any already approved versions of same config
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()));

			bean.setBenefitApproved('Y');
			hsu.saveOrUpdate(bean);
			return;
		}

		//otherwise find old benefit and terminate it
		if (bean.getHrBenefitConfig().getHrBenefit().getRequiresDecline() == 'Y') {
			//term any old decline
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).eq(HrBenefitJoin.HRBENEFIT, bean.getHrBenefitConfig().getHrBenefit()));

			//term any old config
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefitConfig().getHrBenefit()));

			bean.setBenefitApproved('Y');
			hsu.saveOrUpdate(bean);

			//end my coverage on benefits provided by somebody else
			if (bean.getCoverageStartDate() != 0) {
				HibernateCriteriaUtil<HrBenefitJoin> dbjHcu = hsu.createCriteria(HrBenefitJoin.class);
				dbjHcu.eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson());
				dbjHcu.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, bean.getCoverageStartDate());
				dbjHcu.eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefitConfig().getHrBenefit());


				for (HrBenefitJoin dbj : dbjHcu.list()) {
					dbj.setCoverageEndDate(DateUtils.add(bean.getPolicyStartDate(), -1));
					hsu.saveOrUpdate(dbj);
				}
			}
			return;
		}

		if (!bean.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean()) {
			// term any old decline
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()));

			//term any old config
			termPolicies(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory()));

			bean.setBenefitApproved('Y');
			hsu.saveOrUpdate(bean);


			// end my coverage on benefits provided by somebody else
			if (bean.getCoverageStartDate() != 0) {
				HibernateCriteriaUtil<HrBenefitJoin> dbjHcu = hsu.createCriteria(HrBenefitJoin.class);
				dbjHcu.eq(HrBenefitJoin.COVERED_PERSON, bean.getPayingPerson());
				dbjHcu.ne(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson());
				dbjHcu.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, bean.getCoverageStartDate());
				dbjHcu.eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefitCategory());


				for (HrBenefitJoin dbj : dbjHcu.list()) {
					dbj.setCoverageEndDate(DateUtils.add(bean.getPolicyStartDate(), -1));
					hsu.saveOrUpdate(dbj);
				}
			}
		}
	}

	private void termPolicies(HibernateCriteriaUtil<HrBenefitJoin> hcu) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// set policy end date on old benefit(s)
		for (HrBenefitJoin benefitJoin : hcu.list()) {
			benefitJoin.setPolicyEndDate(DateUtils.add(bean.getPolicyStartDate(), -1));
			hsu.saveOrUpdate(benefitJoin);
		}
	}

	public void setDependentStartDate(String depId, int coverageStartDate, String changeReasonId) throws ArahantException {
		if (!this.isPolicyBenefitJoin())
			throw new ArahantException("The benefit is not a policy benefit join");

		// get the associated dependent benefit join
		HrBenefitJoin dependentBenefitJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).joinTo(HrBenefitJoin.COVERED_PERSON).eq(Person.PERSONID, depId).first();
		BHRBenefitJoin bDependentBenefitJoin = dependentBenefitJoin == null ? null : new BHRBenefitJoin(dependentBenefitJoin);

		// check if the join already exists
		if (bDependentBenefitJoin == null) {
			// no, insert this
			bDependentBenefitJoin = this.saveDependentBenefitAssignment(new BHREmplDependent(bean.getPayingPersonId(), depId),
					coverageStartDate,
					0,
					coverageStartDate,
					0.0,
					false,
					bean.getPolicyStartDate(),
					bean.getPolicyEndDate(),
					bean.getCoverageChangeDate(),
					"", "", false);

			bDependentBenefitJoin.setChangeReason(changeReasonId);
		} else {
			// yes, so update the relevant fields
			bDependentBenefitJoin.setCoverageStartDate(coverageStartDate);
			bDependentBenefitJoin.setCoverageEndDate(0);
			bDependentBenefitJoin.setChangeReason(changeReasonId);
			bDependentBenefitJoin.update();
		}
	}

	public BHREmplDependent[] getAllDependents() throws ArahantException {
		if (!this.isPolicyBenefitJoin())
			throw new ArahantException("The benefit is not a policy benefit join");
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//TODO: this could be optimized to bring in the columns we need and remove query from loop
		final List<HrEmplDependent> l = hsu.createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.PERSON).joinTo(Person.HR_BENEFIT_JOINS_WHERE_COVERED).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).list();

		List<BHREmplDependent> bl1;
		if (l == null)
			bl1 = new ArrayList<BHREmplDependent>();
		else
			bl1 = new ArrayList<BHREmplDependent>(l.size());
		if (l != null)
			for (final HrEmplDependent dep : l) {
				final BHREmplDependent d = new BHREmplDependent(dep);


				final HrBenefitJoin dbj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.RELATIONSHIP, dep).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).first();

				d.setEnrolled(dbj.getCoverageStartDate() <= DateUtils.now() && dbj.getCoverageStartDate() > 0 && (dbj.getCoverageEndDate() == 0 || dbj.getCoverageEndDate() >= DateUtils.now()));
				d.setBenefitStartDate(dbj.getCoverageStartDate());
				d.setBenefitEndDate(dbj.getCoverageEndDate());
				d.setAmountCovered(dbj.getAmountCovered());
				bl1.add(d);
			}

		//now get all the dependents for the employee that weren't in that list
		//and set them as not enrolled
		final List<HrEmplDependent> l2 = hsu.createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.EMPLOYEE).eq(Employee.PERSONID, bean.getPayingPersonId()).list();
		l2.removeAll(l);

		for (final HrEmplDependent dep : l2) {
			final BHREmplDependent d = new BHREmplDependent(dep);
			d.setEnrolled(false);
			bl1.add(d);
		}

		return BHREmplDependent.sort(bl1);
	}

	/**
	 * @param dependent
	 * @throws ArahantException
	 */
	private boolean assignDependent(final HrEmplDependent dependent) throws ArahantException {
		//if the dependent is already assigned, return
		if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.RELATIONSHIP, dependent).exists())
			return false;

		final BHRBenefitJoin dbj = new BHRBenefitJoin();
		dbj.create();
		dbj.setCoverageStartDate(bean.getPolicyStartDate());
		dbj.setOriginalCoverageDate(bean.getPolicyStartDate());
		dbj.setPayingPerson(bean.getPayingPerson());
		dbj.setRelationship(dependent);
		dbj.setCoveredPerson(dependent.getPerson());
		dbj.copyReason(bean);
		dbj.insert();
		return true;
	}
	private String warning = "";

	/**
	 * @return Returns the warning.
	 */
	public String getWarning() {
		return warning;
	}

	public void copyPriorCoverages() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// check if this is a decline and return if it is (nothing to copy)
		if (bean.getHrBenefitConfig() == null)
			return;

		HrBenefitJoin ebj = null;

		if (bean.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory().getRequiresDecline() == 'Y')
			ebj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bean.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory()).first();

		if (ebj == null && bean.getHrBenefitConfig().getHrBenefit().getRequiresDecline() == 'Y')
			ebj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getCoveredPerson()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefitConfig().getHrBenefit()).first();

		if (ebj == null)
			return;

		bean.setCoverageStartDate(ebj.getCoverageStartDate());
		bean.setOriginalCoverageDate(ebj.getOriginalCoverageDate());
		//not going to copy this coverage end date because they could be trying to enroll themself

		hsu.saveOrUpdate(bean);
		hsu.flush();

		// check if this is a policy benefit join
		BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(bean);
		if (bBenefitJoin.isPolicyBenefitJoin())
			// it is, do the dependent benefit joins
			for (HrBenefitJoin dbj : bBenefitJoin.getDependentBenefitJoins()) {
				HrBenefitJoin bdbj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.RELATIONSHIP, dbj.getRelationship()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).ne(HrBenefitJoin.BENEFIT_JOIN_ID, bean.getBenefitJoinId()).first();
				if (bdbj != null) {
					bdbj.setCoverageEndDate(dbj.getCoverageEndDate());
					bdbj.setCoverageStartDate(dbj.getCoverageStartDate());
					bdbj.setOriginalCoverageDate(dbj.getOriginalCoverageDate());
					hsu.saveOrUpdate(bdbj);
					hsu.flush();
				}

			}
	}

	/**
	 * Check to see if benefit is supposed to cover more people than it is
	 *
	 */
	public void checkOverCovered() {
		//to eliminate redundancy, only run check for payer version
		//	if (!bean.getPayingPerson().getPersonId().equals(bean.getCoveredPerson().getPersonId()))
		//		return;

		if (checkOverCoverage())
			ArahantSession.AIEval("(assert (notifyOverCoverage \"" + bean.getBenefitJoinId() + "\"))");
	}

	/**
	 * Is the benefit over coverage levels?
	 * 
	 * @return true=over coverage, false=not over coverage
	 */
	public boolean checkOverCoverage() {
		//to eliminate redundancy, only run check for payer version
		//	if (!bean.getPayingPerson().getPersonId().equals(bean.getCoveredPerson().getPersonId()))
		//		return;

		//the only ones that matter are configs
		if (bean.getHrBenefitConfig() == null)
			return false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		HrBenefitConfig config = bean.getHrBenefitConfig();

		//if this is the only config for this benefit, it can't be over-covered
		if (config.getHrBenefit().getBenefitConfigs().size() == 1)
			return false;

		//if this config only covers employee, it can't be over covered
		if (config.getChildren() == 'N' && config.getSpouseEmployee() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getSpouseNonEmployee() == 'N' && config.getSpouseNonEmpOrChildren() == 'N')
			return false;

		//if this is employee + 1 type and there is no employee only type, it can't be over covered
		if ((config.getSpouseNonEmpOrChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getChildren() == 'Y') && config.getMaxChildren() == 1 && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists())
			return false;

		//if this is a family and type and there is no employee only or plus one, etc, then it can't be over-covered
		if ((config.getSpouseNonEmpOrChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0) && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists() && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE,
				HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return false;

		//check for children only type
		if ((config.getSpouseNonEmpOrChildren() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0)
				&& !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists() && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(new String[]{HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
					HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
					HrBenefitConfig.COVERS_CHILDREN}, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return false;

		List<HrBenefitJoin> bjl = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).geOrEq(HrBenefitJoin.COVERAGE_END_DATE, bean.getPolicyEndDate(), 0).eq(HrBenefitJoin.APPROVED, bean.getBenefitApproved()).list();

		if (bjl.size() == 2 && (config.getSpouseNonEmpOrChildren() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0))
			//is there an applicable employee + 1?
			return hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(new String[]{HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
						HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
						HrBenefitConfig.COVERS_CHILDREN}, 'Y').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists();


		//am I an employee +1 with just employee?
		if (bjl.size() == 1 && config.getEmployee() == 'Y' && (config.getMaxChildren() == 1 || config.getSpouseEmployee() == 'Y' || config.getSpouseNonEmployee() == 'Y'))
			return true;

		//am I a family with just one dependent AND employee +1 exists
		if (bjl.size() <= 2 && config.getMaxChildren() != 1 && (config.getChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getSpouseNonEmpOrChildren() == 'Y')
				&& hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE,
				HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return true;

		return false;
	}

	/**
	 * Is the benefit over coverage levels?
	 * 
	 * @param bjs
	 * @return true=over coverage, false=not over coverage
	 */
	public static boolean checkPotentialOverCoverage(List<BHRBenefitJoin> bjs) {
		//to eliminate redundancy, only run check for payer version
		//	if (!bean.getPayingPerson().getPersonId().equals(bean.getCoveredPerson().getPersonId()))
		//		return;
		HrBenefitJoin bj = bjs.get(0).getBean();

		//the only ones that matter are configs
		if (bj.getHrBenefitConfig() == null)
			return false;

		HrBenefitConfig config = bj.getHrBenefitConfig();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//if this is the only config for this benefit, it can't be over-covered
		if (config.getHrBenefit().getBenefitConfigs().size() == 1)
			return false;

		//if this config only covers employee, it can't be over covered
		if (config.getChildren() == 'N' && config.getSpouseEmployee() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getSpouseNonEmployee() == 'N' && config.getSpouseNonEmpOrChildren() == 'N')
			return false;

		//if this is employee + 1 type and there is no employee only type, it can't be over covered
		if ((config.getSpouseNonEmpOrChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getChildren() == 'Y') && config.getMaxChildren() == 1 && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists())
			return false;

		//if this is a family and type and there is no employee only or plus one, etc, then it can't be over-covered
		if ((config.getSpouseNonEmpOrChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0) && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists() && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE,
				HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return false;

		//check for children only type
		if ((config.getSpouseNonEmpOrChildren() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0)
				&& !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').eq(HrBenefitConfig.COVERS_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N').eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N').exists() && !hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(new String[]{HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
					HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
					HrBenefitConfig.COVERS_CHILDREN}, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return false;

		//List<HrBenefitJoin> bjl = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).geOrEq(HrBenefitJoin.COVERAGE_END_DATE, bean.getPolicyEndDate(), 0).eq(HrBenefitJoin.APPROVED, bj.getBenefitApproved()).list();

		if (bjs.size() == 2 && (config.getSpouseNonEmpOrChildren() == 'N' && config.getSpouseEmpOrChildren() == 'N' && config.getChildren() == 'Y') && (config.getMaxChildren() > 1 || config.getMaxChildren() == 0))
			//is there an applicable employee + 1?
			return hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(new String[]{HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
						HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
						HrBenefitConfig.COVERS_CHILDREN}, 'Y').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists();


		//am I an employee +1 with just employee?
		if (bjs.size() == 1 && config.getEmployee() == 'Y' && (config.getMaxChildren() == 1 || config.getSpouseEmployee() == 'Y' || config.getSpouseNonEmployee() == 'Y'))
			return true;

		//am I a family with just one dependent AND employee +1 exists
		if (bjs.size() <= 2 && config.getMaxChildren() != 1 && (config.getChildren() == 'Y' || config.getSpouseEmpOrChildren() == 'Y' || config.getSpouseNonEmpOrChildren() == 'Y')
				&& hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, config.getHrBenefit()).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y').orEq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,
				HrBenefitConfig.COVERS_CHILDREN,
				HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE,
				HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N').eq(HrBenefitConfig.MAX_DEPENDENTS, (short) 1).exists())
			return true;

		return false;
	}

	public static void deleteExpiredBenefits() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {

			//System.out.println("got hsu");
			hsu.setCurrentPersonToArahant();
			//System.out.println("set current person");
			HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.POLICY_END_DATE, 0).lt(HrBenefitJoin.POLICY_END_DATE, DateUtils.now());
			//System.out.println("did query");
			//better scroll this, could be thousands
			HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();
			//System.out.println("did scroll");
			int count = 0;
			while (hscr.next()) {
				if (++count % 50 == 0) {
					logger.info("Benefit delete count " + count);
					hsu.commitTransaction();
					hsu.beginTransaction();
				}
				try {
					new BHRBenefitJoin(hscr.get()).delete();
				} catch (Exception exc) {
					///		hsu.rollbackTransaction();
				}
				hsu.clear();
				//	hsu.beginTransaction();
			}
			hscr.close();
			hsu.commitTransaction();
			hsu.beginTransaction();
		} catch (Exception e) {
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			logger.error(e);
		}
	}

	void setProject(Project project) {
		bean.setProject(project);
	}

	void setRelationshipId(String enrollingRelationshipId) {
		bean.setRelationship(ArahantSession.getHSU().get(HrEmplDependent.class, enrollingRelationshipId));
	}

	public String getPersonName() {

		Person p = ArahantSession.getHSU().get(Person.class, bean.getRecordPersonId());
		return p.getNameWithLogin();
	}

	public LifeEvent getLifeEvent() {
		return bean.getLifeEvent();
	}

	public void setLifeEvent(LifeEvent lifeEvent) {
		bean.setLifeEvent(lifeEvent);
	}

	public String getRecordChangeType() {
		if (bean.getRecordChangeType() == 'N')
			return "New";
		else if (bean.getRecordChangeType() == 'M')
			return "Modify";
		else if (bean.getRecordChangeType() == 'D')
			return "Delete";
		else
			throw new ArahantException("Record Change Type not specified");
	}

	private void checkCoverageStartDateAgainstBenefitEligibilityDate(int startDate) {
		BHRBenefit bene = new BHRBenefit(this.getBenefitConfig().getBenefit());
		BEmployee be = new BEmployee(this.getPayingPersonId());

		Calendar cal = DateUtils.getCalendar(be.getHireDate());

		int eligibilityStartDate = 0;

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

		//start date
		//Check qualifying event date and see if it is before or after the eligibility date
		//if qe is before set it to elibility date
		if (startDate < eligibilityStartDate)
			//this.setCoverageStartDate(eligibilityStartDate); Dont set it, throw an exception and tell them what it should be
			throw new ArahantException("Employee not eligible to enroll in this benefit until" + DateUtils.getDateFormatted(eligibilityStartDate));
	}

	private void checkCoverageEndDateAgainstBenefitRules(int endDate) {
		BHRBenefit bene = new BHRBenefit(this.getBenefitConfig().getBenefit());
		BEmployee be = new BEmployee(this.getPayingPersonId());

		int eligibilityEndDate = 0;

//		if (!isEmpty(this.getBenefitChangeReasonId()))
//		{
//			BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(this.getBenefitChangeReasonId());
//			bcrDate = bcr.getEffectiveDate();
//		}
//		else if (this.getLifeEvent() != null) //check if linked to life event
//		{
//			bcrDate = this.getLifeEvent().getEventDate();
//		}
//		else
//		{
//			throw new ArahantWarning("There is no qualifying event associated with this benefit!");
//		}

		//End Date
		switch (bene.getCoverageEndType()) {
			//End of the month
			case 1: //get qe and get end of the month
				Calendar c = DateUtils.getCalendar(endDate);
				int lastDay = c.getActualMaximum(Calendar.DATE);
				c.set(Calendar.DATE, lastDay);
				eligibilityEndDate = DateUtils.getDate(c);
				break;
			//Employment End Date
			case 2: //get the employee's most recent status from the status history table, if inactive get date
				HrEmplStatusHistory esh = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).eq(HrEmplStatusHistory.EMPLOYEE, be.getEmployee()).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'N').first();

				//if null use qe date
				if (esh != null)
					eligibilityEndDate = esh.getEffectiveDate();
				else
					eligibilityEndDate = endDate;
				break;
			//X days after qualifying event
			case 3:
				eligibilityEndDate = DateUtils.addDays(endDate, bene.getCoverageEndPeriod());
		}

		//endate
		if (endDate != eligibilityEndDate)
			//this.setCoverageEndDate(eligibilityEndDate);
			throw new ArahantException("Coverage End Date must be " + DateUtils.getDateFormatted(eligibilityEndDate) + " based on the benefit rules.");

	}

	public BenefitRider getRiderEnrollment() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (BenefitRider br : hsu.createCriteria(BenefitRider.class).eq(BenefitRider.RIDER_BENEFIT_ID, this.getBenefitId()).list())
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).exists())
				return br;
			//is there an approved enrollment and no unapproved decline?
			else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).exists()
					&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, br.getBaseBenefit()).exists())
				return br;
		return null;
	}

	public HrBenefitJoin getRiderJoin() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (BenefitRider br : hsu.createCriteria(BenefitRider.class).eq(BenefitRider.RIDER_BENEFIT_ID, this.getBenefitId()).list()) {
			HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).first();
			if (bj != null)
				return bj;

			bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).first();
			if (!hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, getPayingPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, br.getBaseBenefit()).exists() && bj != null)
				return bj;
		}
		return null;
	}

	public boolean isRiderJoin() {
		return ArahantSession.getHSU().createCriteria(BenefitRider.class).eq(BenefitRider.RIDER_BENEFIT_ID, getBenefitId()).exists();
	}

	//there is also a version of this in BHRBenefit
	public List<String> autoEnrollRidersAndDependencies(String empId) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<String> ret = new ArrayList<String>();
		List<String> newBenefitJoinIds = new ArrayList<String>();
		BHRBenefitJoin bbj = this;
		BPerson bpp = bbj.getPayingPerson();
		if (bpp.hasPending(empId))
			bpp.loadPending(empId);
		else
			//otherwise, get the Real person record
			bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

		if (bbj.isBenefitDecline()) {
			BHRBenefit declineBen = new BHRBenefit(bbj.getBenefitId());
			List<BenefitDependency> bds = hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, declineBen.getBean()).list();

			//for all the benefits that depend on the one we declined
			for (BenefitDependency bd : bds) {
				//check to see that they meet another one of their requirements
				List<BenefitDependency> otherRequirements = hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, bd.getDependentBenefit()).ne(BenefitDependency.REQUIRED_BENEFIT, bd.getRequiredBenefit()).list();
				boolean meetsRequirement = false;
				for (BenefitDependency bd2 : otherRequirements)
					//do they have an unapproved enrollment?
					if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getRequiredBenefit()).exists())
						meetsRequirement = true;
					//do they have an approved enrollment and no unapproved decline?
					else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getRequiredBenefit()).exists()
							&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, bd2.getRequiredBenefit().getBenefitId()).exists())
						meetsRequirement = true;
				//if they don't meet their requirement,
				//delete their unapproved enrollments in those dependent benefits
				if (!meetsRequirement) {
					//enrollments
					hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getDependentBenefit()).delete();
					//declines
					hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, bd.getDependentBenefit()).delete();
				}
			}
		} else {
			BHRBenefit bb = new BHRBenefit(bbj.getBenefitId());
			double amountCovered = bbj.getAmountCovered();
			if (bbj.getEmployeeCovered().equals("N") || Utils.doubleEqual(amountCovered, 0, 0.001))
				for (HrBenefitJoin depJ : bbj.getDependentBenefitJoins())
					if (!Utils.doubleEqual(depJ.getAmountCovered(), 0, 0.001)) {
						amountCovered = depJ.getAmountCovered();
						break;
					}
			for (BenefitRider b : BBenefitRider.getRidersForBaseBenefit(bb)) {
				BBenefitRider bbr = new BBenefitRider(b);
				if (!bbr.getRequiredBoolean() || (bbr.getRequiredBoolean() && !bbr.getHiddenBoolean()))
					continue;
				//check if they are age eligible
				short min = bbr.getRiderBenefit().getMinAge();
				short max = bbr.getRiderBenefit().getMaxAge();
				short age = bpp.getAgeAsOf(DateUtils.now());
				if (max > 0 && age > max)
					continue;
				if (min > 0 && age < min)
					continue;
				BHRBenefit brb = new BHRBenefit(bbr.getRiderBenefit());
				HrBenefitConfig match = brb.getCorrespondingBenefitConfig(bbj.getBenefitConfig().getBean());
				if (match != null) //found a good config to enroll in
				{
					//get all the enrollees and lets carry over their coverageAmounts in case they are different per covered person
					List<Enrollee> enrollees = new ArrayList<Enrollee>();
					Enrollee[] ea;
					for (HrBenefitJoin dpj : bbj.getDependentBenefitJoins()) {
						Enrollee e = new Enrollee();
						e.setRelationshipId(dpj.getRelationship().getRelationshipId());
						e.setCoverageAmount(dpj.getAmountCovered());
						enrollees.add(e);
					}
					ea = new Enrollee[enrollees.size()];
					int count = 0;
					for (Enrollee ee : enrollees) {
						ea[count] = ee;
						count++;
					}
					newBenefitJoinIds.add(bpp.enrollInConfigMultipleCoverages(match.getBenefitConfigId(), bbj.getBenefitChangeReasonId(), bbj.getPolicyStartDate(), ea, amountCovered, true, bbj.getEmployeeExplanation()));

				}
			}

			for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bb)) {
				BBenefitDependency bbd = new BBenefitDependency(bd);
				if (!bbd.getRequiredBoolean() || (bbd.getRequiredBoolean() && !bbd.getHiddenBoolean()))
					continue;

				//check if they are age eligible
				short min = bbd.getDependentBenefit().getMinAge();
				short max = bbd.getDependentBenefit().getMaxAge();
				short age = bpp.getAgeAsOf(DateUtils.now());
				if (max > 0 && age > max)
					continue;
				if (min > 0 && age < min)
					continue;
				BHRBenefit bdb = new BHRBenefit(bd.getDependentBenefit());
				HrBenefitConfig match = bdb.getCorrespondingBenefitConfig(bbj.getBenefitConfig().getBean());
				if (match != null) //found a good config to enroll in
				{
					//get all the enrollees and lets carry over their coverageAmounts in case they are different per covered person
					List<Enrollee> enrollees = new ArrayList<Enrollee>();
					Enrollee[] ea;
					for (HrBenefitJoin dpj : bbj.getDependentBenefitJoins()) {
						Enrollee e = new Enrollee();
						e.setRelationshipId(dpj.getRelationship().getRelationshipId());
						e.setCoverageAmount(dpj.getAmountCovered());
						enrollees.add(e);
					}
					ea = new Enrollee[enrollees.size()];
					int count = 0;
					for (Enrollee ee : enrollees) {
						ea[count] = ee;
						count++;
					}
					newBenefitJoinIds.add(bpp.enrollInConfigMultipleCoverages(match.getBenefitConfigId(), bbj.getBenefitChangeReasonId(), bbj.getPolicyStartDate(), ea, amountCovered, true, bbj.getEmployeeExplanation()));
				}
			}
		}
		ret.addAll(newBenefitJoinIds);
		//Do this recursively
		for (String s : newBenefitJoinIds) {
			BHRBenefitJoin newBJ = new BHRBenefitJoin(s);
			ret.addAll(newBJ.autoEnrollRidersAndDependencies(empId));
		}
		return ret;
	}

	public String[] getCoveredPersonIdsInGroup() {
		List<HrBenefitJoin> bjl = this.getDependentBenefitJoins();
		String[] ret = new String[bjl.size()];
		int count = 0;
		for (HrBenefitJoin bj : bjl)
			ret[count++] = bj.getRelationship().getRelationshipId();
		return ret;
	}
	
	/**
	 * This method returns all the benefit join records that apply to a single benefit config, and it
	 * removes that same group from the incoming list.
	 * 
	 * @param bjs a group of benefit joins applicable to a single employee
	 * @return the subset of benefit joins that apply to the same benefit config
	 */
	public static List<HrBenefitJoin> getGroup(List<HrBenefitJoin> bjs) {
		List<HrBenefitJoin> ret = new LinkedList<HrBenefitJoin>();
		if (bjs == null  ||  bjs.isEmpty())
			return ret;
		HrBenefitJoin first = bjs.get(0);
		String configId = first.getHrBenefitConfigId();
		if (configId == null  || configId.length() == 0) {
			ret.add(first);
			bjs.remove(0);
			return ret;
		}		
		List<HrBenefitJoin> nlst = new LinkedList<HrBenefitJoin>();
		for (HrBenefitJoin bj : bjs) {
			if (configId.equals(bj.getHrBenefitConfigId()))
				ret.add(bj);
			else
				nlst.add(bj);
		}
		bjs.clear();
		bjs.addAll(nlst);
		return ret;
	}
	
	/**
	 * Takes a group of benefit join records for a single benefit config and returns the one associated to the employee.
	 * 
	 * @param bjs
	 * @return 
	 */
	public static HrBenefitJoin getPayingBenefitJoin(List<HrBenefitJoin> bjs) {
		for (HrBenefitJoin bj : bjs) {
			if (bj.getPayingPersonId().equals(bj.getCoveredPersonId()))
				return bj;
		}
		return null;
	}
}
