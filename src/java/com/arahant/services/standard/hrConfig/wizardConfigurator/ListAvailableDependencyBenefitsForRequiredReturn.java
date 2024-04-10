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
 * @authorArahant
 */
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;


public class ListAvailableDependencyBenefitsForRequiredReturn extends TransmitReturnBase {

	private ListAvailableDependencyBenefitsReturnItem[] item;
	private ListAvailableDependencyBenefitsReturnItem selectedItem;

	public ListAvailableDependencyBenefitsReturnItem[] getItem() {
		return item;
	}

	public void setItem(ListAvailableDependencyBenefitsReturnItem[] item) {
		this.item = item;
	}

	public void setItem(BHRBenefit[] d){
		item = new ListAvailableDependencyBenefitsReturnItem[d.length];
		for(int loop = 0; loop < d.length; loop++) {
			item[loop] = new ListAvailableDependencyBenefitsReturnItem(d[loop]);
		}
	}

	public ListAvailableDependencyBenefitsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ListAvailableDependencyBenefitsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

}

	
