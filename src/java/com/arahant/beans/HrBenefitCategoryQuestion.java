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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 */
@Entity
@Table(name = "hr_benefit_category_question")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrBenefitCategoryQuestion extends ArahantBean implements java.io.Serializable {

	private String questionId;
	public static final String QUESTION_ID = "questionId";
	private HrBenefitCategory benefitCat;
	public static final String BENEFIT_CAT = "benefitCat";
	private int sequenceNumber;
	public static final String SEQ = "sequenceNumber";
	private String question;
	public static final String QUESTION = "question";

    @Override
    public String generateId() throws ArahantException {
        questionId = IDGenerator.generate(this);
        return questionId;
    }

	@Override
    public String keyColumn() {

        return "question_id";
    }

	@Override
    public String tableName() {
        return "hr_benefit_category_question";
    }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_cat_id")
	public HrBenefitCategory getBenefitCat() {
		return benefitCat;
	}

	public void setBenefitCat(HrBenefitCategory benefitCatId) {
		this.benefitCat = benefitCatId;
	}
	
	@Id
	@Column(name = "question_id")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	@Column(name = "question")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name = "seqno")
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HrBenefitCategoryQuestion other = (HrBenefitCategoryQuestion) obj;
		if ((this.questionId == null) ? (other.questionId != null) : !this.questionId.equals(other.questionId))
			return false;
		return true;
	}

	@Override
    public int hashCode() {
        if (questionId == null)
            return 0;
        return questionId.hashCode();
    }
}
