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


/**
 * 
 */
package com.arahant.services.standard.hr.benefitCategory;

import com.arahant.beans.HrBenefit;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHrBenefitCategoryAnswer;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;


public class ListAnswersForQuestionReturn extends TransmitReturnBase {

	ListAnswersForQuestionReturnItem item[];
	
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x) {
		cap = x;
	}
	
	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public ListAnswersForQuestionReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListAnswersForQuestionReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BHrBenefitCategoryAnswer[] a, String catId) {
		List<HrBenefit> benefitsWithAnswers = new ArrayList<HrBenefit>();

		for (BHrBenefitCategoryAnswer answer : a)
		{
			benefitsWithAnswers.add(answer.getBenefit());
		}
		List<HrBenefit> benes = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(catId).getBean()).list();
		benes.removeAll(benefitsWithAnswers);

		item=new ListAnswersForQuestionReturnItem[a.length + benes.size()];
		int count = 0;
		for(HrBenefit b : benes)
		{
			item[count]=new ListAnswersForQuestionReturnItem(new BHRBenefit(b));
			count++;
		}

		
		for (int loop=benes.size();loop<a.length+benes.size();loop++)
			item[loop]=new ListAnswersForQuestionReturnItem(a[loop - benes.size()]);
	}
}

	
