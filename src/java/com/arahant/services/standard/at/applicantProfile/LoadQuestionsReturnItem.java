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

package com.arahant.services.standard.at.applicantProfile;

import com.arahant.business.BApplicantQuestion;

public class LoadQuestionsReturnItem {

	private String applicantQuestionId;
	private String question;
	private boolean common;
	private String answerType;

	public LoadQuestionsReturnItem() {
	}

	LoadQuestionsReturnItem(BApplicantQuestion bc) {
		applicantQuestionId = bc.getId();
		question = bc.getQuestion();
		answerType = bc.getAnswerType();
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public String getApplicantQuestionId() {
		return applicantQuestionId;
	}

	public void setApplicantQuestionId(String applicantQuestionId) {
		this.applicantQuestionId = applicantQuestionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean getCommon() {
		return common;
	}

	public void setCommon(boolean common) {
		this.common = common;
	}
}

	
