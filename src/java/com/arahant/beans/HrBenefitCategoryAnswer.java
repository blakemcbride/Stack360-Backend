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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 */
@Entity
@Table(name = "hr_benefit_category_answer")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrBenefitCategoryAnswer extends ArahantBean implements java.io.Serializable {

	private String answerId;
	public static final String ANSWER_ID = "answerId";
	private HrBenefitCategoryQuestion question;
	public static final String QUESTION = "question";
	private HrBenefit benefit;
	public static final String BENEFIT_ID = "benefit";
	private String answer;
	public static final String ANSWER = "answer";

	public HrBenefitCategoryAnswer() {
	}

	@Override
	public String generateId() throws ArahantException {
		answerId = IDGenerator.generate(this);
		return answerId;
	}

	@Override
	public String keyColumn() {

		return "answer_id";
	}

	@Override
	public String tableName() {
		return "hr_benefit_category_answer";
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	public HrBenefitCategoryQuestion getQuestion() {
		return question;
	}

	public void setQuestion(HrBenefitCategoryQuestion questionId) {
		this.question = questionId;
	}

	@Id
	@Column(name = "answer_id")
	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	@Column(name = "answer")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefitId) {
		this.benefit = benefitId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HrBenefitCategoryAnswer other = (HrBenefitCategoryAnswer) obj;
		if ((this.answerId == null) ? (other.answerId != null) : !this.answerId.equals(other.answerId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (answerId == null)
			return 0;
		return answerId.hashCode();
	}
}
