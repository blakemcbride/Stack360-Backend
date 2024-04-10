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
 *
 */
package com.arahant.services.standard.hr.benefitParent;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;

public class ListReplacingBenefitsReturn extends TransmitReturnBase {

	ListReplacingBenefitsReturnItem replacingBenefits[];

	ListReplacingBenefitsReturnItem notReplacingBenefits[];
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	public ListReplacingBenefitsReturnItem[] getNotReplacingBenefits() {
		return notReplacingBenefits;
	}

	public void setNotReplacingBenefits(ListReplacingBenefitsReturnItem[] notReplacingBenefits) {
		this.notReplacingBenefits = notReplacingBenefits;
	}

	public ListReplacingBenefitsReturnItem[] getReplacingBenefits() {
		return replacingBenefits;
	}
   
	public void setReplacingBenefits(ListReplacingBenefitsReturnItem[] replacingBenefits) {
		this.replacingBenefits = replacingBenefits;
	}

	void setNotReplacingBenefits(final BHRBenefit[] a) {
		notReplacingBenefits=new ListReplacingBenefitsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			notReplacingBenefits[loop]=new ListReplacingBenefitsReturnItem(a[loop]);
	}

	void setReplacingBenefits(final BHRBenefit[] a) {
		replacingBenefits=new ListReplacingBenefitsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			replacingBenefits[loop]=new ListReplacingBenefitsReturnItem(a[loop]);
	}
}

	
