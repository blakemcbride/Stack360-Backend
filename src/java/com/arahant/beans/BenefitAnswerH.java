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

import com.arahant.utils.ArahantSession;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "benefit_answer_h")
public class BenefitAnswerH extends ArahantHistoryBean implements Serializable {

	public final static String PERSON = "person";
	public final static String PERSON_ID = "personId";
	public final static String QUESTION = "benefitQuestion";
	public final static String QUESTION_ID = "benefitQuestionId";
	public final static String STRING_ANSWER = "stringAnswer";
	public final static String DATE_ANSWER = "dateAnswer";
	public final static String NUMERIC_ANSWER = "numericAnswer";
	private String benefitAnswerId;
	private Person person;
	private String personId;
	private BenefitQuestion benefitQuestion;
	private String benefitQuestionId;
	private String stringAnswer;
	private Integer dateAnswer;
	private Double numericAnswer;

	public BenefitAnswerH() {
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

	@Column(name = "benefit_answer_id")
	public String getBenefitAnswerId() {
		return benefitAnswerId;
	}

	public void setBenefitAnswerId(String benefitAnswerId) {
		this.benefitAnswerId = benefitAnswerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_question_id")
	public BenefitQuestion getBenefitQuestion() {
		return benefitQuestion;
	}

	public void setBenefitQuestion(BenefitQuestion benefitQuestion) {
		this.benefitQuestion = benefitQuestion;
	}

	@Column(name = "benefit_question_id", insertable = false, updatable = false)
	public String getBenefitQuestionId() {
		return benefitQuestionId;
	}

	public void setBenefitQuestionId(String benefitQuestionId) {
		this.benefitQuestionId = benefitQuestionId;
	}

	@Override
	public String tableName() {
		return "benefit_answer_h";
	}

	@Override
	public String keyColumn() {
		return "benefit_answer_id";
	}

	@Id
	@Override
	public String getHistory_id() {
		return history_id;
	}

	@Override
	public boolean alreadyThere() {

		try {
			PreparedStatement stmt = ArahantSession.getHSU().getConnection().prepareStatement("select * from benefit_answer_h where benefit_answer_id=? and record_change_date=?");

			stmt.setString(1, benefitAnswerId);
			stmt.setTimestamp(2, new java.sql.Timestamp(getRecordChangeDate().getTime()));
			ResultSet rs = stmt.executeQuery();
			boolean found = rs.next();

			rs.close();
			stmt.close();

			return found;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
	/*
	 * benefit_answer_id character(16) NOT NULL, benefit_contact_id
	 * character(16) NOT NULL, benefit_question_id character(16) NOT NULL,
	 * stringAnswer character varying(2000) NOT NULL,
	 *
	 */
}
