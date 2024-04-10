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
package com.arahant.services.standard.hr.projectComment;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProject;
import com.arahant.business.BProjectComment;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class LoadCommentSummaryReturn extends TransmitReturnBase {
	LoadCommentSummaryReturnItem item[];

	private String summary, employeeSSN, detail;
	private String personFirstName;
	private String personLastName;
	private String personType;
	private String hrScreenGroupId;
	private String personId;
	private boolean active;

	/**
	 * @return Returns the detail.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail The detail to set.
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * @return Returns the item.
	 */
	public LoadCommentSummaryReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final LoadCommentSummaryReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BProject bp) {
		summary = bp.getDescription();
		detail = bp.getDetailDesc();
		employeeSSN = bp.getWorkDoneForPersonSSN();
		final BProjectComment[] a = bp.getProjectComments(null);
		item = new LoadCommentSummaryReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new LoadCommentSummaryReturnItem(a[loop]);
		active = bp.getActive();
		personFirstName = bp.getWorkDoneForEmployeeFirstName();
		personLastName = bp.getWorkDoneForEmployeeLastName();
		personType = bp.getWorkDoneForEmployeePersonType();
		hrScreenGroupId = BProperty.get(StandardProperty.HR_Screen_Group_ID);
		personId = bp.getWorkDoneForEmployeeId();
	}

	public String getHrScreenGroupId() {
		return hrScreenGroupId;
	}

	public void setHrScreenGroupId(String hrScreenGroupId) {
		this.hrScreenGroupId = hrScreenGroupId;
	}

	public String getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}


	/**
	 * @return Returns the employeeSSN.
	 */
	public String getEmployeeSSN() {
		return employeeSSN;
	}

	/**
	 * @param employeeSSN The employeeSSN to set.
	 */
	public void setEmployeeSSN(final String employeeSSN) {
		this.employeeSSN = employeeSSN;
	}

	/**
	 * @return Returns the summary.
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary The summary to set.
	 */
	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


}


