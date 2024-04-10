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

import com.arahant.beans.BenefitAnswer;
import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

public class BBenefitAnswer extends SimpleBusinessObjectBase<BenefitAnswer> {

	public BBenefitAnswer() {
	}

	public BBenefitAnswer(String id) {
		super(id);
		internalLoad(id);
	}

	public BBenefitAnswer(BenefitAnswer ba) {
		bean = ba;
	}

	@Override
	public String create() throws ArahantException {
		bean = new BenefitAnswer();
		return bean.generateId();
	}

	public String getId() {
		return bean.getBenefitAnswerId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitAnswer.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public void setAnswer(String answer) {
		bean.setStringAnswer(answer);
	}

	public void setAnswer(String answerType, String textBasedAnswer, double numberBasedAnswer) {
		switch (answerType.charAt(0)) {
			case BenefitQuestion.TYPE_DATE:
				bean.setDateAnswer((int) numberBasedAnswer);
				break;
			case BenefitQuestion.TYPE_NUMERIC:
			case BenefitQuestion.TYPE_YES_NO_UNK:
				bean.setNumericAnswer(numberBasedAnswer);
				break;
			case BenefitQuestion.TYPE_STRING:
				bean.setStringAnswer(textBasedAnswer);
				break;
			default:
				throw new ArahantException("Unknown answer type " + answerType);
		}
	}

	public void setPerson(Person p) {
		bean.setPerson(p);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setQuestionId(String id) {
		bean.setBenefitQuestion(ArahantSession.getHSU().get(BenefitQuestion.class, id));
	}

	public String getQuestionId() {
		return bean.getBenefitQuestion().getBenefitQuestionId();
	}

	public Integer getDateAnswer() {
		if (bean.getDateAnswer() == null)
			bean.setDateAnswer(0);
		return bean.getDateAnswer();
	}

	public void setDateAnswer(Integer dateAnswer) {
		bean.setDateAnswer(dateAnswer);
	}

	public void setDateAnswer(int dateAnswer) {
		bean.setDateAnswer(dateAnswer);
	}

	public Double getNumericAnswer() {
		if (bean.getNumericAnswer() == null)
			bean.setNumericAnswer(0.0);
		return bean.getNumericAnswer();
	}

	public void setNumericAnswer(Double numericAnswer) {
		bean.setNumericAnswer(numericAnswer);
	}

	public void setNumericAnswer(double numericAnswer) {
		bean.setNumericAnswer(numericAnswer);
	}

	public String getStringAnswer() {
		return bean.getStringAnswer();
	}

	public void setStringAnswer(String answer) {
		bean.setStringAnswer(answer);
	}
	
	/**
	 * Returns the first BenefitAnswer that matches the input parameters.
	 * 
	 * @param personId
	 * @param benefitId
	 * @param internalId
	 * @return the BenefitAnswer that matches the input
	 */
	public static BBenefitAnswer getBenefitAnswer(String personId, String benefitId, String internalId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<BenefitAnswer> crit = hsu.createCriteria(BenefitAnswer.class);
		crit.eq(BenefitAnswer.PERSON_ID, personId);
		crit.joinTo(BenefitAnswer.QUESTION).eq(BenefitQuestion.BENEFIT_ID, benefitId).eq(BenefitQuestion.INTERNAL_ID, internalId);
		crit.orderByDesc(BenefitAnswer.RECORD_CHANGE_DATE);  //  get the modt recent answer to that question
		BenefitAnswer ba = crit.first();
		return ba == null ? null : new BBenefitAnswer(ba);
	}
}
