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
package com.arahant.services.standard.hr.benefit;

import com.arahant.beans.BenefitClass;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class ListBenefitClassesReturn extends TransmitReturnBase {

	private BeneClass[] availableClasses;
	private BeneClass[] selectedClasses;

	public void setData(BHRBenefit bb)
	{
		List<BenefitClass> available = ArahantSession.getHSU().createCriteria(BenefitClass.class).orderBy(BenefitClass.NAME).list();
		List<BenefitClass> selected = ArahantSession.getHSU().createCriteria(BenefitClass.class)
				.joinTo(BenefitClass.BENEFITS).eq(HrBenefit.BENEFITID,bb.getId()).list();

		available.removeAll(selected);

		availableClasses = new BeneClass[available.size()];
		selectedClasses = new BeneClass[selected.size()];

		int i = 0;
		for (BenefitClass bc : available)
		{
			availableClasses[i++]=new BeneClass(bc);
		}

		i = 0;
		for (BenefitClass bc : selected)
		{
			selectedClasses[i++]=new BeneClass(bc);
		}

	}

	public BeneClass[] getAvailableClasses() {
		return availableClasses;
	}

	public void setAvailableClasses(BeneClass[] availableClasses) {
		this.availableClasses = availableClasses;
	}

	public BeneClass[] getSelectedClasses() {
		return selectedClasses;
	}

	public void setSelectedClasses(BeneClass[] selectedClasses) {
		this.selectedClasses = selectedClasses;
	}

}

	
