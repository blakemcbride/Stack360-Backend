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
package com.arahant.services.standard.hr.benefitWizardStatus;

import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.PersonCR;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BEmployee;
import java.util.HashMap;
import java.util.List;


public class SearchEmployeeWizardStatusesReturn extends TransmitReturnBase {

	SearchEmployeeWizardStatusesReturnItem item[];
	
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
	public SearchEmployeeWizardStatusesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchEmployeeWizardStatusesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BEmployee[] a, HashMap<String, List<HrBenefitJoin>> empJoinMap, HashMap<String, PersonCR> empCRMap,  HashMap<String, List<PersonCR>> empDepMap) {
		item=new SearchEmployeeWizardStatusesReturnItem[a.length];

		for (int loop=0;loop<a.length;loop++)
		{
			List<HrBenefitJoin> empJoins = empJoinMap.get(a[loop].getPersonId());
			PersonCR empCR = empCRMap.get(a[loop].getPersonId());
			List<PersonCR> empDeps = empDepMap.get(a[loop].getPersonId());
			item[loop]=new SearchEmployeeWizardStatusesReturnItem(a[loop], empJoins, empCR, empDeps);
		}
	}
}

	
