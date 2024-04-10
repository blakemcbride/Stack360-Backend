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
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class GetQualifyingEventReturn extends TransmitReturnBase {

	private GetQualifyingEventReturnItem item[];
	private String openEnrollmentId;
	private int openEnrollmentEffectiveDate;
	private String newHireId;
	private Integer newHireEffectiveDate;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private boolean showQualifyingEvent;

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	public int getOpenEnrollmentEffectiveDate() {
		return openEnrollmentEffectiveDate;
	}

	public void setOpenEnrollmentEffectiveDate(int openEnrollmentEffectiveDate) {
		this.openEnrollmentEffectiveDate = openEnrollmentEffectiveDate;
	}

	/**
	 * @return Returns the item.
	 */
	public GetQualifyingEventReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final GetQualifyingEventReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BHRBenefitChangeReason[] a) {
		item = new GetQualifyingEventReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new GetQualifyingEventReturnItem(a[loop]);
	}

	public String getOpenEnrollmentId() {
		return openEnrollmentId;
	}

	public void setOpenEnrollmentId(String openEnrollmentId) {
		this.openEnrollmentId = openEnrollmentId;
	}

	/**
	 * @return the newHiredId
	 */
	public String getNewHireId() {
		return newHireId;
	}

	/**
	 * @param newHiredId the newHiredId to set
	 */
	public void setNewHireId(String newHiredId) {
		this.newHireId = newHiredId;
	}

	/**
	 * @return the newHireEffectiveDate
	 */
	public Integer getNewHireEffectiveDate() {
		return newHireEffectiveDate;
	}

	/**
	 * @param newHireEffectiveDate the newHireEffectiveDate to set
	 */
	public void setNewHireEffectiveDate(Integer newHireEffectiveDate) {
		this.newHireEffectiveDate = newHireEffectiveDate;
	}

	public boolean isShowQualifyingEvent() {
		return showQualifyingEvent;
	}

	public void setShowQualifyingEvent(boolean showQualifyingEvent) {
		this.showQualifyingEvent = showQualifyingEvent;
	}
	
}
