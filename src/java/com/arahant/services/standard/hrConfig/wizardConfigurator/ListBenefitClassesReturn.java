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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BBenefitClass;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;


public class ListBenefitClassesReturn extends TransmitReturnBase {

	private ListBenefitClassesReturnItem enrollmentItem[];
	private ListBenefitClassesReturnItem onboardingItem[];
	private boolean enrollmentHasNull;
	private boolean onboardingHasNull;
	private String selectedId;
	
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x) {
		cap = x;
	}
	
	public int getCap() {
		return cap;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public boolean getEnrollmentHasNull() {
		return enrollmentHasNull;
	}

	public void setEnrollmentHasNull(boolean enrollmentHasNull) {
		this.enrollmentHasNull = enrollmentHasNull;
	}

	public boolean getOnboardingHasNull() {
		return onboardingHasNull;
	}

	public void setOnboardingHasNull(boolean onboardingHasNull) {
		this.onboardingHasNull = onboardingHasNull;
	}

	public ListBenefitClassesReturnItem[] getEnrollmentItem() {
		return enrollmentItem;
	}

	public void setEnrollmentItem(ListBenefitClassesReturnItem[] enrollmentItem) {
		this.enrollmentItem = enrollmentItem;
	}

	public ListBenefitClassesReturnItem[] getOnboardingItem() {
		return onboardingItem;
	}

	public void setOnboardingItem(ListBenefitClassesReturnItem[] onboardingItem) {
		this.onboardingItem = onboardingItem;
	}

	/**
	 * @param accounts
	 */
	void setEnrollmentItem(final BBenefitClass[] a) {
		enrollmentItem=new ListBenefitClassesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			enrollmentItem[loop]=new ListBenefitClassesReturnItem(a[loop]);
	}
	void setOnboardingItem(final BBenefitClass[] a) {
		onboardingItem=new ListBenefitClassesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			onboardingItem[loop]=new ListBenefitClassesReturnItem(a[loop]);
	}
}

	
