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

import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BApplicantQuestionChoice extends SimpleBusinessObjectBase<ApplicantQuestionChoice> {

	public BApplicantQuestionChoice()
	{
	}

	public BApplicantQuestionChoice(String id)
	{
		super(id);
	}

	public BApplicantQuestionChoice(ApplicantQuestionChoice o)
	{
		bean=o;
	}

	/* Do not use until Java 6 @Override */
	@Override
	public String create() throws ArahantException {
		bean=new ApplicantQuestionChoice();
		return bean.generateId();
	}

	public String getId() {
		return bean.getApplicantQuestionChoiceId();
	}

	public String getName() {
		return bean.getDescription();
	}

	/* Do not use until Java 6 @Override */
	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ApplicantQuestionChoice.class, key);
	}

	public static BApplicantQuestionChoice [] list(String questionId)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
			.orderBy(ApplicantQuestionChoice.ID)
			.joinTo(ApplicantQuestionChoice.QUESTION)
			.eq(ApplicantQuestion.ID,questionId)
			.list());
	}

	public static BApplicantQuestionChoice[] makeArray(List<ApplicantQuestionChoice> l) {
		BApplicantQuestionChoice[]ret=new BApplicantQuestionChoice[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BApplicantQuestionChoice(l.get(loop));

		return ret;
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setQuestion(ApplicantQuestion q) {
		bean.setQuestion(q);
	}

}
