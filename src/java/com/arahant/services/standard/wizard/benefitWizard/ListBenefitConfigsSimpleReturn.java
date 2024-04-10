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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.utils.StandardProperty;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;
import java.util.List;

public class ListBenefitConfigsSimpleReturn extends TransmitReturnBase {

	private ListBenefitConfigsSimpleReturnItem item[];
	private boolean autoEnrollSingleConfig = false;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	public boolean getAutoEnrollSingleConfig() {
		return autoEnrollSingleConfig;
	}

	public void setAutoEnrollSingleConfig(boolean autoEnrollSingleConfig) {
		this.autoEnrollSingleConfig = autoEnrollSingleConfig;
	}

	/**
	 * @return Returns the item.
	 */
	public ListBenefitConfigsSimpleReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListBenefitConfigsSimpleReturnItem[] item) {
		this.item = item;
	}

	void setItem(List<WizardConfigurationConfig> l, String empId, int date) {
		item = new ListBenefitConfigsSimpleReturnItem[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			item[loop] = new ListBenefitConfigsSimpleReturnItem(l.get(loop), empId, date);
	}

	void setItem2(BHRBenefitConfig[] l, String empId, int date) {
		item = new ListBenefitConfigsSimpleReturnItem[l.length];
		for (int loop = 0; loop < l.length; loop++)
			item[loop] = new ListBenefitConfigsSimpleReturnItem(l[loop], empId, date, true);
	}
}
