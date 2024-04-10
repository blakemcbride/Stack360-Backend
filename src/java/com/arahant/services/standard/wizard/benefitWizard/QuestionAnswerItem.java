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


package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.HrBenefitCategoryAnswer;
import com.arahant.beans.HrBenefitCategoryQuestion;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 * Arahant
 */
public class QuestionAnswerItem {

	private String question;
	private String questionId;
	private AnswerItem[] answers;

	public QuestionAnswerItem() {
	}

	public AnswerItem[] getAnswers() {
		return answers;
	}

	public void setAnswers(AnswerItem[] answers) {
		this.answers = answers;
	}

	public final void setAnswers(List<HrBenefitCategoryAnswer> l) {
		this.answers = new AnswerItem[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			this.answers[loop] = new AnswerItem(l.get(loop));
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public QuestionAnswerItem(HrBenefitCategoryQuestion q) {
		question = q.getQuestion();
		questionId = q.getQuestionId();

		setAnswers(ArahantSession.getHSU().createCriteria(HrBenefitCategoryAnswer.class).eq(HrBenefitCategoryAnswer.QUESTION, q).list());
	}
}
