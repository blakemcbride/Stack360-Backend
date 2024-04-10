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
package com.arahant.services.standard.hr.benefitClass;

import com.arahant.beans.HrBenefit;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import java.util.Set;


/**
 * 
 *
 *
 */
public class ListAssociatedBenefitsReturn extends TransmitReturnBase {
	ListAssociatedBenefitsReturnItem item[];
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);


	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public ListAssociatedBenefitsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListAssociatedBenefitsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	public void setItem(final Set<HrBenefit> set) {
		item=new ListAssociatedBenefitsReturnItem[set.size()];
                int i = 0;
		for (HrBenefit hr : set){
			item[i]=new ListAssociatedBenefitsReturnItem(hr);
                        i++;
                }
	}

   
}

	
