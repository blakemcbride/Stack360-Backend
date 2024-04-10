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

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "hr_benefit_join")
public class HrBenefitJoin extends AuditedBean implements java.io.Serializable, Comparable<HrBenefitJoin>, IHrBenefitJoinCurrent, ArahantSaveNotify {

	private static final transient ArahantLogger logger = new ArahantLogger(HrBenefitJoin.class);
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_benefit_join";
	public static final String HRBENEFIT = "hrBenefit";
	public static final String HR_BENEFIT_CATEGORY = "hrBenefitCategory";
	public static final String HR_BENEFIT_ID = "hrBenefitId";
	public static final String HR_BENEFIT_CATEGORY_ID = "hrBenefitCategoryId";
	public static final String AMOUNT_COVERED = "amountCovered";
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String RELATIONSHIP = "relationship";
	public static final String COMMENTS = "comments";
	public static final String ACCEPTED_COBRA_DATE = "acceptedDateCOBRA";
	public static final String PROJECT = "project";
	public static final String LIFE_EVENT = "lifeEvent";
	public static final String CHANGE_REASON = "benefitChangeReason";
	public static final String BENEFIT_DECLINED = "benefitDeclined";
	public static final String BENEFIT_APPROVED = "benefitApproved";
	public static final String RECORD_CHANGE_TYPE = "recordChangeType";
	public static final String RECORD_CHANGE_DATE = "recordChangeDate";
	private char amountPaidSource = 'C';
	private Person coveredPerson;
	private Person payingPerson;
	private HrEmplDependent relationship;
	private String payingPersonId;
	private HrBenefitConfig hrBenefitConfig;
	private HrBenefit hrBenefit;
	private HrBenefitCategory hrBenefitCategory;
	private String benefitJoinId;
	private String coveredPersonId;
	private String insuranceId;
	private int policyStartDate;
	private int policyEndDate;
	private int coverageChangeDate;
	private int originalCoverageDate;
	private double amountPaid;
	private double amountCovered;
	private int coverageStartDate, coverageEndDate;
	private Set<HrBeneficiary> beneficiaries = new HashSet<HrBeneficiary>(0);
	private Set<HrPhysician> physicians = new HashSet<HrPhysician>(0);
	private String changeDescription = "Internal Edit";
	private char benefitDeclined = 'N';
	private char benefitApproved = 'Y';
	private char usingCOBRA = 'N';
	private String hrBenefitConfigId, hrBenefitCategoryId, hrBenefitId;
	private String relationshipId;
	private int ppy = 1;
	private String comments = "";
	private int maxMonthsCOBRA;
	private int acceptedDateCOBRA;
	private String otherInsurance = "";
	private char otherInsurancePrimary = 'N';
	private char amountPaidType = 'F';
	public static final char TYPE_MANUAL = 'M';
	public static final char TYPE_CALCULATED = 'C';
	private Project project;
	private LifeEvent lifeEvent;
	private HrBenefitChangeReason benefitChangeReason;
	private char employeeCovered = 'Y';
	public static final String EMPLOYEE_COVERED = "employeeCovered";
	private String employeeExplanation;
	public static final String REQUESTED_COST = "requestedCost";
	public static final String REQUESTED_COST_PERIOD = "requestedCostPeriod";
	private double requestedCost = 0.0;
	private char requestedCostPeriod = 'M';

	@Column(name = "requested_cost")
	public double getRequestedCost() {
		return requestedCost;
	}

	public void setRequestedCost(double requestedCost) {
		this.requestedCost = requestedCost;
	}

	@Column(name = "requested_cost_period")
	public char getRequestedCostPeriod() {
		return requestedCostPeriod;
	}

	public void setRequestedCostPeriod(char requestedCostPeriod) {
		this.requestedCostPeriod = requestedCostPeriod;
	}

	@Column(name = "employee_explanation")
	public String getEmployeeExplanation() {
		return employeeExplanation;
	}

	public void setEmployeeExplanation(String employeeExplanation) {
		this.employeeExplanation = employeeExplanation;
	}

	@Column(name = "employee_covered")
	public char getEmployeeCovered() {
		return this.employeeCovered;
	}

	public void setEmployeeCovered(char employeeCovered) {
		this.employeeCovered = employeeCovered;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	@Transient
	public String getCalculatedCostAnnualString() {

		if (payingPerson == null)
			return "";

		if (amountPaidSource == TYPE_MANUAL)  //has cost override
			return MoneyUtils.formatMoney(amountPaid);

		return MoneyUtils.formatMoney(getCalculatedCostAnnual());
	}

	@Transient
	public double getCalculatedCostPPP() {
		return MoneyUtils.parseMoney(getCalculatedCost());
	}

	@Transient
	public double getCalculatedCostMonthly() {
		return MoneyUtils.parseMoney(getCalculatedCost()) * getPpy() / 12;
	}

	@Transient
	public double getCalculatedCostAnnual() {

		//dont do this, because getCalculatedCost rounds, then the multiplication loses pennies after the round
		//return MoneyUtils.parseMoney(getCalculatedCost()) * getPpy();

		if (hrBenefitConfig == null)
			return 0;

		String costCalcType = hrBenefitConfig.getHrBenefit().getCostCalcType() + "";
		int calcDate = 0;
		if (costCalcType.equals("C"))
			if (ArahantSession.getCalcDate() != 0)
				calcDate = ArahantSession.getCalcDate();
			else
				calcDate = DateUtils.now();
		else if (costCalcType.equals("P"))
			calcDate = policyStartDate;

		//doing it this way keeps all the fractional pennies and rounds after returning
		return BenefitCostCalculator.calculateCostNewMethodAnnual(this, calcDate);
	}

	public HrBenefitJoin() {
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "benefit_config_id")
	@Override
	public HrBenefitConfig getHrBenefitConfig() {
		return this.hrBenefitConfig;
	}

	public void setHrBenefitConfig(final HrBenefitConfig hrBenefit) {
		this.hrBenefitConfig = hrBenefit;
		setHrBenefitConfigId((hrBenefitConfig != null) ? hrBenefitConfig.getBenefitConfigId() : "");
	}

	@Override
	public String keyColumn() {

		return "benefit_join_id";
	}

	@Override
	public String tableName() {

		return TABLE_NAME;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {
		setBenefitJoinId(IDGenerator.generate(this));
		return benefitJoinId;
	}

	@Id
	@Column(name = "benefit_join_id")
	@Override
	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(final String benefitJoinId) {
		firePropertyChange("benefitJoinId", this.benefitJoinId, benefitJoinId);
		this.benefitJoinId = benefitJoinId;
	}

	@Column(name = "policy_end_date")
	@Override
	public int getPolicyEndDate() {
		return policyEndDate;
	}

	@Column(name = "insurance_id")
	public String getInsuranceId() {
		return insuranceId;
	}

	@Column(name = "amount_paid")
	@Override
	public double getAmountPaid() {
		return amountPaid;
	}

	@Column(name = "policy_start_date")
	@Override
	public int getPolicyStartDate() {
		return policyStartDate;
	}

	@Column(name = "coverage_change_date")
	public int getCoverageChangeDate() {
		return coverageChangeDate;
	}

	@Column(name = "original_coverage_date")
	public int getOriginalCoverageDate() {
		return originalCoverageDate;
	}

	@Override
	public int compareTo(final HrBenefitJoin o) {

//		primary sort on not yet approved, approved benefit, approved decline ... secondary sort on coverage configuration name

		if (benefitApproved == 'N' && o.benefitApproved == 'Y')
			return -1;

		if (benefitApproved == 'Y' && o.benefitApproved == 'N')
			return 1;

		if (benefitApproved == 'Y' && o.benefitApproved == 'Y') {
			if (hrBenefit == null && o.hrBenefit != null)
				return -1;
			if (hrBenefit != null && o.hrBenefit == null)
				return 1;
			if (hrBenefitCategory == null && o.hrBenefitCategory != null)
				return -1;
			if (hrBenefitCategory != null && o.hrBenefitCategory == null)
				return 1;
		}

		if (hrBenefitConfig != null && o.hrBenefitConfig == null)
			return -1;

		if (hrBenefitConfig == null && o.hrBenefitConfig != null)
			return 1;

		String name1 = "", name2 = "";


		if (hrBenefitConfig != null)
			name1 = hrBenefitConfig.getName();

		if (o.hrBenefitConfig != null)
			name2 = o.hrBenefitConfig.getName();

		if (hrBenefit != null)
			name1 = hrBenefit.getName();

		if (o.hrBenefit != null)
			name2 = o.hrBenefit.getName();


		if (hrBenefitCategory != null)
			name1 = hrBenefitCategory.getDescription();

		if (o.hrBenefitCategory != null)
			name2 = o.hrBenefitCategory.getDescription();

		if (name1 == null)
			name1 = "";
		if (name2 == null)
			name2 = "";
		return name1.compareTo(name2);
	}

	@Column(name = "amount_covered")
	@Override
	public double getAmountCovered() {
		return amountCovered;
	}

	@OneToMany(mappedBy = HrBeneficiary.EMPL_BENEFIT_JOIN, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBeneficiary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(final Set<HrBeneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	@OneToMany(mappedBy = HrPhysician.EMPL_BENEFIT_JOIN, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrPhysician> getPhysicians() {
		return physicians;
	}

	public void setPhysicians(final Set<HrPhysician> physicians) {
		this.physicians = physicians;
	}

	@Column(name = "change_description")
	@Override
	public String getChangeDescription() {
		return changeDescription;
	}

	@Column(name = "coverage_end_date")
	@Override
	public int getCoverageEndDate() {
		return coverageEndDate;
	}

	@Column(name = "coverage_start_date")
	@Override
	public int getCoverageStartDate() {
		return coverageStartDate;
	}

	@Transient
	public char getJoinActiveEnrollment() {
		return (coverageStartDate != 0 && coverageEndDate == 0) ? 'Y' : 'N';
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getHrBenefit() {
		return hrBenefit;
	}

	public void setHrBenefit(final HrBenefit hrBenefit) {
		this.hrBenefit = hrBenefit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_cat_id")
	public HrBenefitCategory getHrBenefitCategory() {
		return hrBenefitCategory;
	}

	public void setHrBenefitCategory(final HrBenefitCategory hrBenefitCategory) {
		this.hrBenefitCategory = hrBenefitCategory;
		setHrBenefitCategoryId((hrBenefitCategory != null) ? hrBenefitCategory.getBenefitCatId() : "");

	}

	@Override
	public ArahantHistoryBean historyObject() {

		return new HrBenefitJoinH();
	}

	/**
	 * @return Returns the benefitDeclined.
	 */
	@Column(name = "benefit_declined")
	public char getBenefitDeclined() {
		return benefitDeclined;
	}

	/**
	 * @param amountCovered The amountCovered to set.
	 */
	public void setAmountCovered(double amountCovered) {
		firePropertyChange("amountCovered", this.amountCovered, amountCovered);
		this.amountCovered = amountCovered;
	}

	/**
	 * @param amountPaid The amountPaid to set.
	 */
	public void setAmountPaid(double amountPaid) {
		firePropertyChange("amountPaid", this.amountPaid, amountPaid);
		this.amountPaid = amountPaid;
	}

	/**
	 * @param benefitDeclined The benefitDeclined to set.
	 */
	public void setBenefitDeclined(char benefitDeclined) {
		firePropertyChange("benefitDeclined", this.benefitDeclined, benefitDeclined);
		this.benefitDeclined = benefitDeclined;
	}

	/**
	 * @param changeDescription The changeDescription to set.
	 */
	public void setChangeDescription(String changeDescription) {
		firePropertyChange("changeDescription", this.changeDescription,
				changeDescription);
		this.changeDescription = changeDescription;
	}

	/**
	 * @param coverageEndDate The coverageEndDate to set.
	 */
	public void setCoverageEndDate(int coverageEndDate) {
		firePropertyChange("coverageEndDate", this.coverageEndDate, coverageEndDate);
		this.coverageEndDate = coverageEndDate;
	}

	/**
	 * @param coverageStartDate The coverageStartDate to set.
	 */
	public void setCoverageStartDate(int coverageStartDate) {
		firePropertyChange("coverageStartDate", this.coverageStartDate,
				coverageStartDate);
		if (coverageStartDate == 0 && coverageStartDate != this.coverageStartDate)
			System.out.println("This is the spot");
		this.coverageStartDate = coverageStartDate;
	}

	/**
	 * @param insuranceId The insuranceId to set.
	 */
	public void setInsuranceId(String insuranceId) {
		firePropertyChange("insuranceId", this.insuranceId, insuranceId);
		this.insuranceId = insuranceId;
	}

	/**
	 * @param policyEndDate The policyEndDate to set.
	 */
	public void setPolicyEndDate(int policyEndDate) {
		firePropertyChange("policyEndDate", this.policyEndDate, policyEndDate);
		this.policyEndDate = policyEndDate;
	}

	/**
	 * @param policyStartDate The policyStartDate to set.
	 */
	public void setPolicyStartDate(int policyStartDate) {
		firePropertyChange("policyStartDate", this.policyStartDate, policyStartDate);
		this.policyStartDate = policyStartDate;
	}

	public void setCoverageChangeDate(int coverageChangeDate) {
		this.coverageChangeDate = coverageChangeDate;
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		this.originalCoverageDate = originalCoverageDate;
	}

	/**
	 * @return Returns the hrBenefitCategoryId.
	 */
	@Column(name = "benefit_cat_id", insertable = false, updatable = false)
	public String getHrBenefitCategoryId() {
		return hrBenefitCategoryId;
	}

	/**
	 * @param hrBenefitCategoryId The hrBenefitCategoryId to set.
	 */
	public void setHrBenefitCategoryId(String hrBenefitCategoryId) {
		firePropertyChange("hrBenefitCategoryId", this.hrBenefitCategoryId,
				hrBenefitCategoryId);
		this.hrBenefitCategoryId = hrBenefitCategoryId;
	}

	/**
	 * @return Returns the hrBenefitConfigId.
	 */
	@Column(name = "benefit_config_id", insertable = false, updatable = false)
	@Override
	public String getHrBenefitConfigId() {
		return hrBenefitConfigId;
	}

	/**
	 * @param hrBenefitConfigId The hrBenefitConfigId to set.
	 */
	public void setHrBenefitConfigId(String hrBenefitConfigId) {
		firePropertyChange("hrBenefitConfigId", this.hrBenefitConfigId,
				hrBenefitConfigId);
		this.hrBenefitConfigId = hrBenefitConfigId;
	}

	/**
	 * @return Returns the benefitApproved.
	 */
	@Column(name = "benefit_approved")
	@Override
	public char getBenefitApproved() {
		return benefitApproved;
	}

	/**
	 * @param benefitApproved The benefitApproved to set.
	 */
	public void setBenefitApproved(char benefitApproved) {
		firePropertyChange("benefitApproved", this.benefitApproved, benefitApproved);
		this.benefitApproved = benefitApproved;
	}

	/**
	 * @return Returns the cobra.
	 */
	@Column(name = "cobra")
	@Override
	public char getUsingCOBRA() {
		return usingCOBRA;
	}

	/**
	 * @param usingCOBRA The usingCOBRA to set.
	 */
	public void setUsingCOBRA(char usingCOBRA) {
		firePropertyChange("usingCOBRA", this.usingCOBRA, usingCOBRA);
		this.usingCOBRA = usingCOBRA;
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getHrBenefitId() {
		return hrBenefitId;
	}

	public void setHrBenefitId(String hrBenefitId) {
		firePropertyChange("hrBenefitId", this.hrBenefitId, hrBenefitId);
		this.hrBenefitId = hrBenefitId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "covered_person")
	@Override
	public Person getCoveredPerson() {
		return coveredPerson;
	}

	public void setCoveredPerson(Person coveredPerson) {
		if (coveredPerson != null)
			setCoveredPersonId(coveredPerson.getPersonId());
		this.coveredPerson = coveredPerson;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paying_person")
	@Override
	public Person getPayingPerson() {
		return payingPerson;
	}

	public void setPayingPerson(Person payingPerson) {
		if (payingPerson != null)
			setPayingPersonId(payingPerson.getPersonId());
		this.payingPerson = payingPerson;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relationship_id")
	@Override
	public HrEmplDependent getRelationship() {
		return relationship;
	}

	public void setRelationship(HrEmplDependent relationship) {
		this.relationship = relationship;
	}

	@Column(name = "paying_person", insertable = false, updatable = false)
	@Override
	public String getPayingPersonId() {
		return payingPersonId;
	}

	public void setPayingPersonId(String payingPersonId) {
		firePropertyChange("payingPersonId", this.payingPersonId, payingPersonId);
		this.payingPersonId = payingPersonId;
	}

	@Column(name = "covered_person", insertable = false, updatable = false)
	@Override
	public String getCoveredPersonId() {
		return coveredPersonId;
	}
	private String calculatedCost;

	/**
	 * This method is called each time a bean is loaded by hibernate to populate
	 * calculatedCost. Subsequent calls to not recalculate, only return the
	 * value previously calculated.
	 *
	 * @return monthly cost for this enrollment, formatted as a string
	 * @see BenefitCostCalculator.calculateCostNewMethod
	 */
	@Transient
	@Override
	public String getCalculatedCost() {

		if (calculatedCost != null && !calculatedCost.equals("") && MoneyUtils.parseMoney(calculatedCost) >= .01)
			return calculatedCost;

		if (hrBenefitConfig == null) {
			calculatedCost = MoneyUtils.formatMoney(0);
			return calculatedCost;
		}

		String costCalcType = hrBenefitConfig.getHrBenefit().getCostCalcType() + "";
		int calcDate = 0;
		if (costCalcType.equals("C"))
			if (ArahantSession.getCalcDate() != 0)
				calcDate = ArahantSession.getCalcDate();
			else
				calcDate = DateUtils.now();
		else if (costCalcType.equals("P"))
			calcDate = policyStartDate;

		if (amountPaidSource == TYPE_MANUAL)
			return calculatedCost = MoneyUtils.formatMoney(amountPaid / BEmployee.getPPY(payingPersonId, calcDate));

		//if this is a flex, use calculated
		if (hrBenefitConfig != null && hrBenefitConfig.getHrBenefit().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE) {
			//do I have pay periods set up - if employee
			BPerson bp = new BPerson(payingPerson);
			if (bp.isEmployee()) {
				PaySchedule ps = bp.getBEmployee().getPaySchedule();
				//how many periods in the pay schedule until the end of the year?
				int periods = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class).eq(PaySchedulePeriod.PAY_SCHEDULE, ps).ge(PaySchedulePeriod.PERIOD_END, policyStartDate).le(PaySchedulePeriod.PERIOD_END, policyEndDate).count();
				if (periods > 0)
					calculatedCost = MoneyUtils.formatMoney(amountCovered / periods);
				else
					calculatedCost = MoneyUtils.formatMoney(amountCovered / BEmployee.getPPY(payingPersonId, calcDate));
			}
			return calculatedCost;
		}

		//Check if there is a cost calculation that applies to this employee, if there is, use it
		double tempCost = BenefitCostCalculator.calculateCostNewMethod(this, calcDate);

		if (!Utils.doubleEqual(tempCost, 0.0, 0.001)) {
			calculatedCost = MoneyUtils.formatMoney(tempCost);
			return calculatedCost;
		}




		int endDate = policyEndDate;
		if (policyEndDate == 0)
			endDate = DateUtils.now();


		if (calculatedCost == null || calculatedCost.equals("") || MoneyUtils.parseMoney(calculatedCost) < .01) {
			calcDate = endDate;

			if (ArahantSession.getCalcDate() != 0)
				calcDate = ArahantSession.getCalcDate();

			double newCost = BenefitCostCalculator.calculateCostNewMethod(this, calcDate);

			if (newCost < .01)
				return calculatedCost;

			calculatedCost = MoneyUtils.formatMoney(newCost);
		}
		return calculatedCost;
	}

	public void setCalculatedCost(String calculatedCost) {
		firePropertyChange("calculatedCost", this.calculatedCost, calculatedCost);
		this.calculatedCost = calculatedCost;
	}

	public void setCoveredPersonId(String coveredPersonId) {
		firePropertyChange("coveredPersonId", this.coveredPersonId, coveredPersonId);
		this.coveredPersonId = coveredPersonId;
	}

	@Override
	public boolean equals(Object o) {
		if (benefitJoinId == null && o == null)
			return true;
		if (benefitJoinId != null && o instanceof HrBenefitJoin)
			return benefitJoinId.equals(((HrBenefitJoin) o).getBenefitJoinId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitJoinId == null)
			return 0;
		return benefitJoinId.hashCode();
	}

	/*
	 * This method sometimes returns the wrong value because the wrong value is being set by setPpy()/Jess.
	 */
	@Transient
	public int getPpy() {
		//TODO: change everybody calling this to go through
		//BEmployee.  Change WmCo rules to set pay periods some other way
		if (ppy == 1 && getPayingPerson() != null)
			ppy = BEmployee.getPPY(getPayingPerson());
		return ppy;
	}

	/*
	 * The following method is only called by Jess.
	 */
	public void setPpy(int ppy) {
		try {
			firePropertyChange("ppy", this.ppy, ppy);
			this.ppy = ppy;
			if (calculatedCost != null)
				//need to recalc it
				calculatedCost = MoneyUtils.formatMoney(MoneyUtils.parseMoney(getCalculatedCostAnnualString()) / ppy);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Column(name = "relationship_id", insertable = false, updatable = false)
	@Override
	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		firePropertyChange("relationshipId", this.relationshipId, relationshipId);
		this.relationshipId = relationshipId;
	}

	/**
	 * @return
	 */
	@Transient
	public String getProviderInitials() {
		if (payingPerson != null)
			return payingPerson.getInitials();
		return "";
	}

	@Column(name = "comments")
	@Override
	public String getComments() {
		if (comments == null)
			comments = "";
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "cobra_acceptance_date")
	public int getAcceptedDateCOBRA() {
		return acceptedDateCOBRA;
	}

	public void setAcceptedDateCOBRA(int acceptedDateCOBRA) {
		this.acceptedDateCOBRA = acceptedDateCOBRA;
	}

	@Column(name = "max_months_on_cobra")
	public int getMaxMonthsCOBRA() {
		return maxMonthsCOBRA;
	}

	public void setMaxMonthsCOBRA(int maxMonthsCOBRA) {
		this.maxMonthsCOBRA = maxMonthsCOBRA;
	}

	@Column(name = "other_insurance")
	public String getOtherInsurance() {
		if (otherInsurance == null)
			otherInsurance = "";
		return otherInsurance;
	}

	public void setOtherInsurance(String otherInsurance) {
		this.otherInsurance = otherInsurance;
	}

	@Column(name = "other_insurance_is_primary")
	public char getOtherInsurancePrimary() {
		return otherInsurancePrimary;
	}

	public void setOtherInsurancePrimary(char otherInsurancePrimary) {
		this.otherInsurancePrimary = otherInsurancePrimary;
	}

	@Column(name = "amount_paid_type")
	public char getAmountPaidType() {
		return amountPaidType;
	}

	public void setAmountPaidType(char amountPaidType) {
		this.amountPaidType = amountPaidType;
	}

	@Override
	public String notifyId() {
		return benefitJoinId;
	}

	@Column(name = "amount_paid_source")
	public char getAmountPaidSource() {
		return amountPaidSource;
	}

	public void setAmountPaidSource(char amountPaidSource) {
		this.amountPaidSource = amountPaidSource;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "life_event_id")
	@Override
	public LifeEvent getLifeEvent() {
		return lifeEvent;
	}

	public void setLifeEvent(LifeEvent lifeEvent) {
		this.lifeEvent = lifeEvent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bcr_id")
	@Override
	public HrBenefitChangeReason getBenefitChangeReason() {
		return benefitChangeReason;
	}

	public void setBenefitChangeReason(HrBenefitChangeReason benefitChangeReason) {
		this.benefitChangeReason = benefitChangeReason;
	}

	@Override
	public String notifyClassName() {
		return "HrBenefitJoin";
	}

	@Column(name = "record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column(name = "record_person_id")
	@Override
	public String getRecordPersonId() {
		if (recordPersonId == null)
			if (ArahantSession.getHSU().getCurrentPerson() != null)
				recordPersonId = ArahantSession.getHSU().getCurrentPerson().getPersonId();
			else
				recordPersonId = ArahantSession.getHSU().getArahantPersonId();
		return recordPersonId;
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Override
	public String keyValue() {
		return getBenefitJoinId();
	}
}
