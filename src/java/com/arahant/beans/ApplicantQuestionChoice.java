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

/**
 *
 */
@Entity
@Table(name = ApplicantQuestionChoice.TABLE_NAME)
public class ApplicantQuestionChoice extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "applicant_question_choice";
	public static final String QUESTION = "question";
	public static final String DESCRIPTION = "description";
	public static final String ID = "applicantQuestionChoiceId";
	public static final String ANSWERS = "answers";
	private ApplicantQuestion question;
	private String applicantQuestionChoiceId;
	private String description;
	private Set<ApplicantAnswer> answers = new HashSet<ApplicantAnswer>();

	@Id
	@Column(name = "applicant_question_choice_id")
	public String getApplicantQuestionChoiceId() {
		return applicantQuestionChoiceId;
	}

	public void setApplicantQuestionChoiceId(String applicantQuestionChoiceId) {
		this.applicantQuestionChoiceId = applicantQuestionChoiceId;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	@JoinColumn(name = "applicant_question_choice_id")
	public Set<ApplicantAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<ApplicantAnswer> answers) {
		this.answers = answers;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "applicant_question_id")
	public ApplicantQuestion getQuestion() {
		return question;
	}

	public void setQuestion(ApplicantQuestion question) {
		this.question = question;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "applicant_question_choice_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantQuestionChoiceId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantQuestionChoice other = (ApplicantQuestionChoice) obj;
		if ((this.applicantQuestionChoiceId == null) ? (other.applicantQuestionChoiceId != null) : !this.applicantQuestionChoiceId.equals(other.applicantQuestionChoiceId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 73 * hash + (this.applicantQuestionChoiceId != null ? this.applicantQuestionChoiceId.hashCode() : 0);
		return hash;
	}
}
