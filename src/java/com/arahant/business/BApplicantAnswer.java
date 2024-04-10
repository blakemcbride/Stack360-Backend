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



package com.arahant.business;

import com.arahant.beans.ApplicantAnswer;
import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;


public class BApplicantAnswer extends SimpleBusinessObjectBase<ApplicantAnswer> {

	public BApplicantAnswer() {
	}

	@Override
	public String create() throws ArahantException {
		bean = new ApplicantAnswer();
		return bean.generateId();
	}

	public String getId() {
		return bean.getApplicantAnswerId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ApplicantAnswer.class, key);
	}

	public void setAnswer(String answer) {
		bean.setStringAnswer(answer);
	}

	public void setAnswer(String answerType, String textBasedAnswer, double numberBasedAnswer, String listId) {
		switch (answerType.charAt(0)) {
			case ApplicantQuestion.TYPE_LIST:
				bean.setListAnswer(ArahantSession.getHSU().get(ApplicantQuestionChoice.class, listId));
				break;
			case ApplicantQuestion.TYPE_DATE:
				bean.setDateAnswer((int) numberBasedAnswer);
				break;
			case ApplicantQuestion.TYPE_NUMERIC:
			case ApplicantQuestion.TYPE_YES_NO_UNK:
				bean.setNumericAnswer(numberBasedAnswer);
				break;
			case ApplicantQuestion.TYPE_STRING:
				bean.setStringAnswer(textBasedAnswer);
				break;
			default:
				throw new ArahantException("Unknown answer type " + answerType);
		}
	}

	public void setApplicant(BApplicant bc) {
		bean.setApplicant(bc.applicant);
	}

	public void setQuestionId(String id) {
		bean.setApplicantQuestion(ArahantSession.getHSU().get(ApplicantQuestion.class, id));
	}

	public String getQuestionId() {
		return bean.getApplicantQuestion().getApplicantQuestionId();
	}
}
