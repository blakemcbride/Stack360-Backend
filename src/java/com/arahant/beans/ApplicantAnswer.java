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
import javax.persistence.*;


@Entity
@Table(name = "applicant_answer")
public class ApplicantAnswer extends ArahantBean implements Serializable {

	public final static String APPLICANT = "applicant";
	public final static String QUESTION = "applicantQuestion";
	public final static String STRING_ANSWER = "stringAnswer";
	public final static String DATE_ANSWER = "dateAnswer";
	public final static String NUMERIC_ANSWER = "numericAnswer";
	public final static String QUESTION_CHOICE = "listAnswer";
	private String applicantAnswerId;
	private Applicant applicant;
	private ApplicantQuestion applicantQuestion;
	private String stringAnswer;
	private Integer dateAnswer;
	private Double numericAnswer;
	private ApplicantQuestionChoice listAnswer;
	private String personId;  // person_id for the applicant
	public static final String PERSON_ID = "personId";

	@ManyToOne
	@JoinColumn(name = "applicant_question_choice_id")
	public ApplicantQuestionChoice getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(ApplicantQuestionChoice listAnswer) {
		this.listAnswer = listAnswer;
	}

	@Column(name = "date_answer")
	public Integer getDateAnswer() {
		if (dateAnswer == null)
			dateAnswer = 0;
		return dateAnswer;
	}

	public void setDateAnswer(Integer dateAnswer) {
		this.dateAnswer = dateAnswer;
	}

	public void setDateAnswer(int dateAnswer) {
		this.dateAnswer = dateAnswer;
	}

	@Column(name = "numeric_answer")
	public Double getNumericAnswer() {
		if (numericAnswer == null)
			numericAnswer = 0.0;
		return numericAnswer;
	}

	public void setNumericAnswer(Double numericAnswer) {
		this.numericAnswer = numericAnswer;
	}

	public void setNumericAnswer(double numericAnswer) {
		this.numericAnswer = numericAnswer;
	}

	@Column(name = "string_answer")
	public String getStringAnswer() {
		return stringAnswer;
	}

	public void setStringAnswer(String answer) {
		this.stringAnswer = answer;
	}

	@Id
	@Column(name = "applicant_answer_id")
	public String getApplicantAnswerId() {
		return applicantAnswerId;
	}

	public void setApplicantAnswerId(String applicantAnswerId) {
		this.applicantAnswerId = applicantAnswerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_question_id")
	public ApplicantQuestion getApplicantQuestion() {
		return applicantQuestion;
	}

	public void setApplicantQuestion(ApplicantQuestion applicantQuestion) {
		this.applicantQuestion = applicantQuestion;
	}

	@Override
	public String tableName() {
		return "applicant_answer";
	}

	@Override
	public String keyColumn() {
		return "applicant_answer_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantAnswerId = IDGenerator.generate(this);
	}
	/*
	 * applicant_answer_id character(16) NOT NULL, applicant_contact_id
	 * character(16) NOT NULL, applicant_question_id character(16) NOT NULL,
	 * stringAnswer character varying(2000) NOT NULL,
	 *
	 */
}
