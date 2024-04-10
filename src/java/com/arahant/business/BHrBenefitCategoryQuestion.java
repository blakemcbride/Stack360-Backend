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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitCategoryAnswer;
import com.arahant.beans.HrBenefitCategoryQuestion;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.List;

public class BHrBenefitCategoryQuestion extends SimpleBusinessObjectBase<HrBenefitCategoryQuestion> {

	public static void delete(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BHRBenefitCategory bCat = new BHRBenefitCategory();
		for (String id : ids) {
			BHrBenefitCategoryQuestion question = new BHrBenefitCategoryQuestion(id);
			bCat = new BHRBenefitCategory(question.getBenefitCat());
			hsu.delete(hsu.createCriteria(HrBenefitCategoryAnswer.class).eq(HrBenefitCategoryAnswer.QUESTION, question.getBean()).list());
			hsu.flush();
			question.delete();
		}
		hsu.flush();

		int tempSeqNo = 1000;
		for (HrBenefitCategoryQuestion q : hsu.createCriteria(HrBenefitCategoryQuestion.class).orderBy(HrBenefitCategoryQuestion.SEQ).eq(HrBenefitCategoryQuestion.BENEFIT_CAT, bCat.getBean()).list()) {
			q.setSequenceNumber(tempSeqNo++);
			hsu.update(q);
		}
		hsu.flush();
		for (HrBenefitCategoryQuestion q : hsu.createCriteria(HrBenefitCategoryQuestion.class).orderBy(HrBenefitCategoryQuestion.SEQ).eq(HrBenefitCategoryQuestion.BENEFIT_CAT, bCat.getBean()).list()) {
			q.setSequenceNumber(tempSeqNo++);
			hsu.update(q);
		}
	}

	public BHrBenefitCategoryQuestion() {
	}

	/**
	 * @param questionId
	 * @throws ArahantException
	 */
	public BHrBenefitCategoryQuestion(final String questionId) throws ArahantException {
		internalLoad(questionId);
	}

	/**
	 * @param question
	 */
	public BHrBenefitCategoryQuestion(final HrBenefitCategoryQuestion question) {
		bean = question;
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitCategoryQuestion.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @return
	 */
	public static BHrBenefitCategoryQuestion[] list(String categoryId) {
		HibernateCriteriaUtil<HrBenefitCategoryQuestion> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategoryQuestion.class)
				.orderBy(HrBenefitCategoryQuestion.SEQ)
				.eq(HrBenefitCategoryQuestion.BENEFIT_CAT, new BHRBenefitCategory(categoryId).getBean());

		return makeArray(hcu.list());
	}

	public static BHrBenefitCategoryQuestion[] makeArray(final List<HrBenefitCategoryQuestion> l) {
		final BHrBenefitCategoryQuestion[] ret = new BHrBenefitCategoryQuestion[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHrBenefitCategoryQuestion(l.get(loop));

		return ret;
	}
	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */

	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitCategoryQuestion();
		return bean.generateId();
	}

	public String getQuestionId() {
		return bean.getQuestionId();
	}

	public void setQuestionId(String questionId) {
		bean.setQuestionId(questionId);
	}

	public HrBenefitCategory getBenefitCat() {
		return bean.getBenefitCat();
	}

	public void setBenefitCat(HrBenefitCategory benefitCat) {
		bean.setBenefitCat(benefitCat);
	}

	public int getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(int sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	public String getQuestion() {
		return bean.getQuestion();
	}

	public void setQuestion(String question) {
		bean.setQuestion(question);
	}

	public BHrBenefitCategoryAnswer[] listAnswers(int cap) {
		return BHrBenefitCategoryAnswer.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitCategoryAnswer.class).eq(HrBenefitCategoryAnswer.QUESTION, getBean()).setMaxResults(cap).list());
	}

	public BHRBenefit[] listAvailableBenefits() {
		List<HrBenefit> benefitsInCategory = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, getBenefitCat()).orderBy(HrBenefit.SEQ).list();
		List<HrBenefit> benefitsWithAnswer = new ArrayList<HrBenefit>();
		for (BHrBenefitCategoryAnswer answer : listAnswers(100))
			benefitsWithAnswer.add(answer.getBenefit());

		benefitsInCategory.removeAll(benefitsWithAnswer);
		return BHRBenefit.makeArray(benefitsInCategory);
	}
}
