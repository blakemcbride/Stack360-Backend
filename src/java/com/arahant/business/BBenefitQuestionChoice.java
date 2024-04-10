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

package com.arahant.business;

import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.BenefitQuestionChoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BBenefitQuestionChoice extends SimpleBusinessObjectBase<BenefitQuestionChoice> {

	public BBenefitQuestionChoice()
	{
		super();
	}

	public BBenefitQuestionChoice(String id)
	{
		super(id);
		load(id);
	}

	public BBenefitQuestionChoice(BenefitQuestionChoice o)
	{
		super();
		bean=o;
	}

	@Override
	public String create() throws ArahantException {
		bean=new BenefitQuestionChoice();
		return bean.generateId();
	}

	public String getId() {
		return bean.getBenefitQuestionChoiceId();
	}

	public String getName() {
		return bean.getDescription();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitQuestionChoice.class, key);
	}

	public static BBenefitQuestionChoice [] list(String questionId)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(BenefitQuestionChoice.class)
			.orderBy(BenefitQuestionChoice.DESCRIPTION)
			.joinTo(BenefitQuestionChoice.QUESTION)
			.eq(BenefitQuestion.ID,questionId)
			.list());
	}

	public static BBenefitQuestionChoice[] makeArray(List<BenefitQuestionChoice> l) {
		BBenefitQuestionChoice[]ret=new BBenefitQuestionChoice[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BBenefitQuestionChoice(l.get(loop));

		return ret;
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setQuestion(BenefitQuestion q) {
		bean.setQuestion(q);
	}

}
