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

import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.BenefitQuestionChoice;
import com.arahant.business.BBenefitAnswer;
import com.arahant.business.BBenefitQuestion;
import java.util.List;

/**
 *
 * Arahant
 */
public class PersonAnswerItem {

	private String question;
	private String questionId;
	private String answerType;
	private ListAnswerItem[] listAnswers;
	private String textAnswer;
	private double numberAnswer;
	private int dateAnswer;

	public PersonAnswerItem() {
	}

	public PersonAnswerItem(BBenefitQuestion bbq) {
		this.question = bbq.getQuestion();
		this.questionId = bbq.getId();
		this.answerType = bbq.getAnswerType();
		setListAnswerItem(bbq.getListOptions());
	}

	public PersonAnswerItem(BBenefitAnswer bba) {
		BBenefitQuestion bbq = new BBenefitQuestion(bba.getQuestionId());
		this.question = bbq.getQuestion();
		this.questionId = bbq.getId();
		this.answerType = bbq.getAnswerType();
		if(answerType.equals(BenefitQuestion.TYPE_DATE + ""))
			dateAnswer = bba.getDateAnswer();
		else if(answerType.equals(BenefitQuestion.TYPE_NUMERIC + ""))
			numberAnswer = bba.getNumericAnswer();
		else
			textAnswer = bba.getStringAnswer();
		setListAnswerItem(bbq.getListOptions());
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public int getDateAnswer() {
		return dateAnswer;
	}

	public void setDateAnswer(int dateAnswer) {
		this.dateAnswer = dateAnswer;
	}

	public ListAnswerItem[] getListAnswers() {
		return listAnswers;
	}

	public void setListAnswers(ListAnswerItem[] listAnswers) {
		this.listAnswers = listAnswers;
	}

	public double getNumberAnswer() {
		return numberAnswer;
	}

	public void setNumberAnswer(double numberAnswer) {
		this.numberAnswer = numberAnswer;
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

	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}

	private void setListAnswerItem(List<BenefitQuestionChoice> listOptions) {
		int count = 0;
		listAnswers = new ListAnswerItem[listOptions.size()];
		for(BenefitQuestionChoice bqc : listOptions) {
			listAnswers[count] = new ListAnswerItem(listOptions.get(count));
			count++ ;
		}
	}

}
