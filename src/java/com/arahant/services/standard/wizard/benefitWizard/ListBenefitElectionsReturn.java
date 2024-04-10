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
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;
import java.util.ArrayList;
import java.util.List;

public class ListBenefitElectionsReturn extends TransmitReturnBase {

	private ListBenefitElectionsReturnItem item[];
	private String benefitWizardStatus;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private boolean lockAfterFinalize;

	public boolean getLockAfterFinalize() {
		return lockAfterFinalize;
	}

	public void setLockAfterFinalize(boolean lockAfterFinalize) {
		this.lockAfterFinalize = lockAfterFinalize;
	}

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	public String getBenefitWizardStatus() {
		return benefitWizardStatus;
	}

	public void setBenefitWizardStatus(String benefitWizardStatus) {
		this.benefitWizardStatus = benefitWizardStatus;
	}

	public ListBenefitElectionsReturnItem[] getItem() {
		return item;
	}

	public void setItem(final ListBenefitElectionsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BHRBenefitJoin[] a, final BPerson bp, final String empId, final int date) {
		List<String> allBenefitIds = new ArrayList<String>();
		for (BHRBenefitJoin bj : a)
			if (!bj.isBenefitCategoryDecline())
				allBenefitIds.add(bj.getBenefitId());

		item = new ListBenefitElectionsReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new ListBenefitElectionsReturnItem(a[loop], bp, empId, date, allBenefitIds);
	}
}
