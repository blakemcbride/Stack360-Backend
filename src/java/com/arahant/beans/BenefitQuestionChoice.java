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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = BenefitQuestionChoice.TABLE_NAME)
public class BenefitQuestionChoice extends ArahantBean implements Serializable {

    public static final String TABLE_NAME = "benefit_question_choice";
    public static final String DESCRIPTION = "description";
    public static final String ID = "benefitQuestionChoiceId";
    public static final String QUESTION = "question";
    public static final String QUESTION_ID = "questionId";
    private BenefitQuestion question;
    private String questionId;
    private String benefitQuestionChoiceId;
    private String description;

    @Id
    @Column(name = "benefit_question_choice_id")
    public String getBenefitQuestionChoiceId() {
        return benefitQuestionChoiceId;
    }

    public void setBenefitQuestionChoiceId(String benefitQuestionChoiceId) {
        this.benefitQuestionChoiceId = benefitQuestionChoiceId;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Column(name = "benefit_question_id", insertable=false, updatable=false)
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

    @ManyToOne
    @JoinColumn(name = "benefit_question_id")
    public BenefitQuestion getQuestion() {
        return question;
    }

    public void setQuestion(BenefitQuestion question) {
        this.question = question;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "benefit_question_choice_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return benefitQuestionChoiceId = IDGenerator.generate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BenefitQuestionChoice other = (BenefitQuestionChoice) obj;
        if ((this.benefitQuestionChoiceId == null) ? (other.benefitQuestionChoiceId != null) : !this.benefitQuestionChoiceId.equals(other.benefitQuestionChoiceId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.benefitQuestionChoiceId != null ? this.benefitQuestionChoiceId.hashCode() : 0);
        return hash;
    }
}
