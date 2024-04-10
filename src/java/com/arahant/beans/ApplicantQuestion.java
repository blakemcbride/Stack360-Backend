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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "applicant_question")
public class ApplicantQuestion extends ArahantBean implements Serializable {

	public static final String SEQ = "questionOrder";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String ANSWERS = "answers";
	public static final String ID = "applicantQuestionId";
	public static final String INTERNAL_USE = "internalUse";
	public static final String QUESTION = "question";
	public static final char TYPE_NUMERIC = 'N';
	public static final char TYPE_DATE = 'D';
	public static final char TYPE_STRING = 'S';
	public static final char TYPE_LIST = 'L';
	public static final char TYPE_YES_NO_UNK = 'Y';
	private String applicantQuestionId;// character(16) NOT NULL,
	private String question;// character varying(80) NOT NULL,
	private short questionOrder;// smallint NOT NULL,
	private int lastActiveDate;// integer NOT NULL DEFAULT 0,
	private char dataType;
	private Set<ApplicantAnswer> answers = new HashSet<ApplicantAnswer>();
	private char internalUse = 'N';
	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY = "company";
	private HrPosition hrPosition;
	public static final String HRPOSITION = "hrPosition";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String tableName() {
		return "applicant_question";
	}

	@Override
	public String keyColumn() {
		return "applicant_question_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantQuestionId = IDGenerator.generate(this);
	}

	@OneToMany(mappedBy = ApplicantAnswer.QUESTION, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ApplicantAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<ApplicantAnswer> answers) {
		this.answers = answers;
	}

	@Id
	@Column(name = "applicant_question_id")
	public String getApplicantQuestionId() {
		return applicantQuestionId;
	}

	public void setApplicantQuestionId(String applicantQuestionId) {
		this.applicantQuestionId = applicantQuestionId;
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

	@Column(name = "internal_use")
	public char getInternalUse() {
		return internalUse;
	}

	public void setInternalUse(char internalUse) {
		this.internalUse = internalUse;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="position_id")
	public HrPosition getHrPosition() {
		return this.hrPosition;
	}

	public void setHrPosition(final HrPosition hrPosition) {
		this.hrPosition = hrPosition;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantQuestion other = (ApplicantQuestion) obj;
		if (this.applicantQuestionId != other.getApplicantQuestionId() && (this.applicantQuestionId == null || !this.applicantQuestionId.equals(other.getApplicantQuestionId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + (this.applicantQuestionId != null ? this.applicantQuestionId.hashCode() : 0);
		return hash;
	}
}
