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
import com.arahant.beans.HrBenefitCategoryAnswer;
import com.arahant.beans.HrBenefitCategoryQuestion;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BHrBenefitCategoryAnswer extends SimpleBusinessObjectBase<HrBenefitCategoryAnswer> {

	public BHrBenefitCategoryAnswer() {
	}

	/**
	 * @param questionId
	 * @throws ArahantException
	 */
	public BHrBenefitCategoryAnswer(final String answerId) throws ArahantException {
		internalLoad(answerId);
	}

	/**
	 * @param question
	 */
	public BHrBenefitCategoryAnswer(final HrBenefitCategoryAnswer answer) {
		bean = answer;
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitCategoryAnswer.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitCategoryAnswer();
		return bean.generateId();
	}

	/**
	 * @return
	 */
	public static BHrBenefitCategoryAnswer[] list() {
		HibernateCriteriaUtil<HrBenefitCategoryAnswer> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategoryAnswer.class);

		return makeArray(hcu.list());
	}

	static BHrBenefitCategoryAnswer[] makeArray(final List<HrBenefitCategoryAnswer> l) {
		final BHrBenefitCategoryAnswer[] ret = new BHrBenefitCategoryAnswer[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHrBenefitCategoryAnswer(l.get(loop));

		return ret;
	}

	public static void delete(String[] ids) {
		ArahantSession.getHSU().createCriteria(HrBenefitCategoryAnswer.class).in(HrBenefitCategoryAnswer.ANSWER_ID, ids).delete();
	}

	public String getAnswerId() {
		return bean.getAnswerId();
	}

	public void setAnswerId(String answerId) {
		bean.setAnswerId(answerId);
	}

	public HrBenefitCategoryQuestion getQuestion() {
		return bean.getQuestion();
	}

	public void setQuestionId(HrBenefitCategoryQuestion question) {
		bean.setQuestion(question);
	}

	public HrBenefit getBenefit() {
		return bean.getBenefit();
	}

	public void setBenefit(HrBenefit benefit) {
		bean.setBenefit(benefit);
	}

	public String getAnswer() {
		return bean.getAnswer();
	}

	public void setAnswer(String answer) {
		bean.setAnswer(answer);
	}
}
