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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "benefit_question")
public class BenefitQuestion extends AuditedBean implements Serializable {

	public static final String SEQ = "questionOrder";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String ANSWERS = "answers";
	public static final String ID = "benefitQuestionId";
	public static final String INTERNAL_USE = "internalUse";
	public static final String QUESTION = "question";
	public static final String BENEFIT = "benefit";
	public static final String BENEFIT_ID = "benefitId";
	public static final char TYPE_NUMERIC = 'N';
	public static final char TYPE_DATE = 'D';
	public static final char TYPE_STRING = 'S';
	public static final char TYPE_LIST = 'L';
	public static final char TYPE_YES_NO_UNK = 'Y';
	private String benefitQuestionId;// character(16) NOT NULL,
	private String question;// character varying(80) NOT NULL,
	private short questionOrder;// smallint NOT NULL,
	private int lastActiveDate = 0;// integer NOT NULL DEFAULT 0,
	private char dataType = 'S';
	private HrBenefit benefit;
	private String benefitId;
	private Set<BenefitAnswer> answers = new HashSet<BenefitAnswer>();
	private char includesExplanation = 'N';
	private char appliesToEmployee = 'Y';
	private char appliesToSpouse = 'Y';
	private char appliesToChildOther = 'Y';
	public static final String INCLUDES_EXPLANATION = "includesExplanation";
	public static final String APPLIES_TO_EMPLOYEE = "appliesToEmployee";
	public static final String APPLIES_TO_SPOUSE = "appliesToSpouse";
	public static final String APPLIES_TO_CHILD_OTHER = "appliesToChildOther";
	private String explanationText;
	public static final String EXPLANATION_TEXT = "explanationText";
	private String internalId;
	public static final String INTERNAL_ID = "internalId";

	@Column(name = "internal_id")
	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	@Override
	public String tableName() {
		return "benefit_question";
	}

	@Override
	public String keyColumn() {
		return "benefit_question_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return benefitQuestionId = IDGenerator.generate(this);
	}

	@OneToMany(mappedBy = BenefitAnswer.QUESTION, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<BenefitAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<BenefitAnswer> answers) {
		this.answers = answers;
	}

	@Id
	@Column(name = "benefit_question_id")
	public String getBenefitQuestionId() {
		return benefitQuestionId;
	}

	public void setBenefitQuestionId(String benefitQuestionId) {
		this.benefitQuestionId = benefitQuestionId;
	}

	@Column(name = "explanation_text")
	public String getExplanationText() {
		return explanationText;
	}

	public void setExplanationText(String explanationText) {
		this.explanationText = explanationText;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefit) {
		this.benefit = benefit;
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "question")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name = "question_order")
	public short getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(short questionOrder) {
		this.questionOrder = questionOrder;
	}

	@Column(name = "data_type")
	public char getDataType() {
		return dataType;
	}

	public void setDataType(char dataType) {
		this.dataType = dataType;
	}

	@Column(name = "applies_to_child_other")
	public char getAppliesToChildOther() {
		return appliesToChildOther;
	}

	public void setAppliesToChildOther(char appliesToChildOther) {
		this.appliesToChildOther = appliesToChildOther;
	}

	@Column(name = "applies_to_employee")
	public char getAppliesToEmployee() {
		return appliesToEmployee;
	}

	public void setAppliesToEmployee(char appliesToEmployee) {
		this.appliesToEmployee = appliesToEmployee;
	}

	@Column(name = "applies_to_spouse")
	public char getAppliesToSpouse() {
		return appliesToSpouse;
	}

	public void setAppliesToSpouse(char appliesToSpouse) {
		this.appliesToSpouse = appliesToSpouse;
	}

	@Column(name = "include_explanation")
	public char getIncludesExplanation() {
		return includesExplanation;
	}

	public void setIncludesExplanation(char includesExplanation) {
		this.includesExplanation = includesExplanation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BenefitQuestion other = (BenefitQuestion) obj;
		if (this.benefitQuestionId != other.getBenefitQuestionId() && (this.benefitQuestionId == null || !this.benefitQuestionId.equals(other.getBenefitQuestionId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + (this.benefitQuestionId != null ? this.benefitQuestionId.hashCode() : 0);
		return hash;
	}

	@Override
	public ArahantHistoryBean historyObject() {
		return new BenefitQuestionH();
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Column(name = "record_person_id")
	@Override
	public String getRecordPersonId() {
		return recordPersonId;
	}

	@Column(name = "record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Override
	public String keyValue() {
		return getBenefitQuestionId();
	}
}
