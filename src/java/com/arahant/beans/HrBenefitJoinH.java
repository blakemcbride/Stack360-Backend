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

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "hr_benefit_join_h")
public class HrBenefitJoinH extends ArahantHistoryBean implements Serializable, Comparable<HrBenefitJoinH>, IHrBenefitJoin {

	private static final transient ArahantLogger logger = new ArahantLogger(HrBenefitJoinH.class);
	public static final String TABLE_NAME = "hr_benefit_join_h";
	private String benefitJoinId;
	private String insuranceId;
	private char amountPaidSource = 'C';
	private int policyStartDate;
	private int policyEndDate;
	private int coverageChangeDate;
	private int originalCoverageDate;
	private double amountPaid;
	private double amountCovered;
	private int coverageStartDate, coverageEndDate;
	private char benefitDeclined = 'N';
	private char benefitApproved = 'Y';
	private char usingCOBRA = 'N';
	private String relationshipId;
	private HrEmplDependent relationship;
	private String changeDescription;
	private String payingPersonId, hrBenefitConfigId, hrBenefitCategoryId, hrBenefitId;
	private String coveredPersonId;
	private String comments;
	private Person payingPerson;
	private Person coveredPerson;
	private HrBenefitConfig hrBenefitConfig;
	private int maxMonthsCOBRA;
	private int acceptedDateCOBRA;
	private String otherInsurance;
	private char otherInsurancePrimary;
	private char amountPaidType;
	private LifeEvent lifeEvent;
	private Project project;
	private HrBenefitChangeReason benefitChangeReason;
	private String employeeExplanation;
	private char employeeCovered = 'Y';
	public static final String EMPLOYEE_COVERED = "employeeCovered";
	public static final String REQUESTED_COST = "requestedCost";
	public static final String REQUESTED_COST_PERIOD = "requestedCostPeriod";
	private double requestedCost = 0.0;
	private char requestedCostPeriod = 'M';
	private static final long serialVersionUID = 7769432178422454993L;
	public static final String EMPLOYEE_ID = "coveredPersonId";
	public static final String CATEGORY_ID = "hrBenefitCategoryId";
	public static final String BENEFIT_ID = "hrBenefitId";
	public static final String BENEFIT_CONFIG_ID = "hrBenefitConfigId";
	public static final String RELATIONSHIP_ID = "relationshipId";
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String RELATIONSHIP = "relationship";
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String COVERAGE_START_DATE = "coverageStartDate";
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String COVERAGE_END_DATE = "coverageEndDate";
	public static final String AMOUNT_COVERED = "amountCovered";

	public HrBenefitJoinH() {
	}

	@Id
	@Override
	public String getHistory_id() {
		return history_id;
	}

	/**
	 * @return Returns the recordChangeType.
	 */
	@Column(name = "record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column(name = "record_person_id")
	@Override
	public String getRecordPersonId() {
		return recordPersonId;
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "covered_person", insertable = false, updatable = false)
	@Override
	public Person getCoveredPerson() {
		return coveredPerson;
	}

	public void setCoveredPerson(Person coveredPerson) {
		this.coveredPerson = coveredPerson;
		if (coveredPerson != null)
			setCoveredPersonId(coveredPerson.getPersonId());
		else
			setCoveredPersonId(null);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "benefit_config_id", insertable = false, updatable = false)
	@Override
	public HrBenefitConfig getHrBenefitConfig() {
		return hrBenefitConfig;
	}

	public void setHrBenefitConfig(HrBenefitConfig hrBenefitConfig) {
		this.hrBenefitConfig = hrBenefitConfig;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paying_person", insertable = false, updatable = false)
	@Override
	public Person getPayingPerson() {
		return payingPerson;
	}

	public void setPayingPerson(Person payingPerson) {
		this.payingPerson = payingPerson;
	}

	@Override
	public String keyColumn() {
		return null;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	/**
	 * @return Returns the amountCovered.
	 */
	@Column(name = "amount_covered")
	@Override
	public double getAmountCovered() {
		return amountCovered;
	}

	/**
	 * @param amountCovered The amountCovered to set.
	 */
	public void setAmountCovered(final double amountCovered) {
		this.amountCovered = amountCovered;
	}

	/**
	 * @return Returns the amountPaid.
	 */
	@Column(name = "amount_paid")
	public double getAmountPaid() {
		return amountPaid;
	}

	/**
	 * @param amountPaid The amountPaid to set.
	 */
	public void setAmountPaid(final double amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * @return Returns the changeDescription.
	 */
	@Column(name = "change_description")
	@Override
	public String getChangeDescription() {
		return changeDescription;
	}

	/**
	 * @param changeDescription The changeDescription to set.
	 */
	public void setChangeDescription(final String changeDescription) {
		this.changeDescription = changeDescription;
	}

	/**
	 * @return Returns the coverageEndDate.
	 */
	@Column(name = "coverage_end_date")
	@Override
	public int getCoverageEndDate() {
		return coverageEndDate;
	}

	/**
	 * @param coverageEndDate The coverageEndDate to set.
	 */
	public void setCoverageEndDate(final int coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	/**
	 * @return Returns the coverageStartDate.
	 */
	@Column(name = "coverage_start_date")
	@Override
	public int getCoverageStartDate() {
		return coverageStartDate;
	}

	/**
	 * @param coverageStartDate The coverageStartDate to set.
	 */
	public void setCoverageStartDate(final int coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	/**
	 * @return Returns the benefitJoinId.
	 */
	@Column(name = "benefit_join_id")
	@Override
	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	/**
	 * @param benefitJoinId The benefitJoinId to set.
	 */
	public void setBenefitJoinId(final String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	/**
	 * @return Returns the insuranceId.
	 */
	@Column(name = "insurance_id")
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
	 * @return Returns the policyEndDate.
	 */
	@Column(name = "policy_end_date")
	@Override
	public int getPolicyEndDate() {
		return policyEndDate;
	}

	/**
	 * @param policyEndDate The policyEndDate to set.
	 */
	public void setPolicyEndDate(final int policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	/**
	 * @return Returns the policyStartDate.
	 */
	@Column(name = "policy_start_date")
	@Override
	public int getPolicyStartDate() {
		return policyStartDate;
	}

	/**
	 * @param policyStartDate The policyStartDate to set.
	 */
	public void setPolicyStartDate(final int policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	@Column(name = "coverage_change_date")
	public int getCoverageChangeDate() {
		return coverageChangeDate;
	}

	public void setCoverageChangeDate(int coverageChangeDate) {
		this.coverageChangeDate = coverageChangeDate;
	}

	@Column(name = "original_coverage_date")
	public int getOriginalCoverageDate() {
		return originalCoverageDate;
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		this.originalCoverageDate = originalCoverageDate;
	}

	/**
	 * @return Returns the employeeId.
	 */
	@Column(name = "paying_person")
	@Override
	public String getPayingPersonId() {
		return payingPersonId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setPayingPersonId(String employeeId) {
		firePropertyChange("payingPersonId", this.payingPersonId, employeeId);
		this.payingPersonId = employeeId;
	}

	/**
	 * @return Returns the hrBenefitCategoryId.
	 */
	@Column(name = "benefit_cat_id")
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
	@Column(name = "benefit_config_id")
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
	 * @return Returns the benefitDeclined.
	 */
	@Column(name = "benefit_declined")
	public char getBenefitDeclined() {
		return benefitDeclined;
	}

	/**
	 * @param benefitApproved The benefitApproved to set.
	 */
	public void setBenefitDeclined(char benefitDeclined) {
		//firePropertyChange("benefitDeclined", this.benefitDeclined, benefitDeclined);
		this.benefitDeclined = benefitDeclined;
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
	 * @return Returns the usingCOBRA.
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

	@Column(name = "benefit_id")
	public String getHrBenefitId() {
		return hrBenefitId;
	}

	public void setHrBenefitId(String hrBenefitId) {
		firePropertyChange("hrBenefitId", this.hrBenefitId, hrBenefitId);
		this.hrBenefitId = hrBenefitId;
	}

	/*
	 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HrBenefitJoinH o) {

		return o.getRecordChangeDate().compareTo(getRecordChangeDate());
	}

	@Column(name = "relationship_id", insertable = false, updatable = false)
	@Override
	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationship) {
		this.relationshipId = relationship;
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

	@Column(name = "covered_person")
	@Override
	public String getCoveredPersonId() {
		return coveredPersonId;
	}

	public void setCoveredPersonId(String coveredPersonId) {
		this.coveredPersonId = coveredPersonId;
	}

	@Column(name = "comments")
	@Override
	public String getComments() {
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

	@Override
	public void copy(IAuditedBean ab) {
		super.copy(ab);

		HrBenefitJoin bj = (HrBenefitJoin) ab;
		//need to grab the calculated cost and insert it
	/*
		 * amountPaid=MoneyUtils.parseMoney(bj.getCalculatedAnnualCost()); if
		 * (amountPaid==0) amountPaid=bj.getAmountPaid();
		 */

	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantHistoryBean#alreadyThere()
	 */
	@Override
	public boolean alreadyThere() {
		try {
			PreparedStatement stmt = ArahantSession.getHSU().getConnection().prepareStatement("select * from hr_benefit_join_h where benefit_join_id=? and record_change_date=?");

			stmt.setString(1, benefitJoinId);
			stmt.setTimestamp(2, new java.sql.Timestamp(getRecordChangeDate().getTime()));
			ResultSet rs = stmt.executeQuery();
			boolean found = rs.next();

			rs.close();
			stmt.close();

			return found;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	@Column(name = "amount_paid_type")
	public char getAmountPaidType() {
		return amountPaidType;
	}

	public void setAmountPaidType(char amountPaidType) {
		this.amountPaidType = amountPaidType;
	}

	@Transient
	@Override
	public String getCalculatedCost() {
		return "";
	}

	@Column(name = "amount_paid_source")
	public char getAmountPaidSource() {
		return amountPaidSource;
	}

	public void setAmountPaidSource(char amountPaidSource) {
		this.amountPaidSource = amountPaidSource;
	}

	@ManyToOne
	@JoinColumn(name = "life_event_id")
	@Override
	public LifeEvent getLifeEvent() {
		return lifeEvent;
	}

	public void setLifeEvent(LifeEvent lifeEvent) {
		this.lifeEvent = lifeEvent;
	}

	@ManyToOne
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne
	@JoinColumn(name = "bcr_id")
	@Override
	public HrBenefitChangeReason getBenefitChangeReason() {
		return benefitChangeReason;
	}

	public void setBenefitChangeReason(HrBenefitChangeReason benefitChangeReason) {
		this.benefitChangeReason = benefitChangeReason;
	}
}
