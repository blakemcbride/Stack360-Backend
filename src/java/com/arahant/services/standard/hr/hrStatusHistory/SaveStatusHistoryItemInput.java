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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrStatusHistory;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class SaveStatusHistoryItemInput extends TransmitInputBase {

	@Validation (required=false)
	private boolean confirmed;
	@Validation (type="date",required=true)
	private int effectiveDate;
	@Validation (table="hr_empl_status_history",column="notes",required=false)
	private String note;
	@Validation (required=true)
	private String statusId;
	@Validation (required=true)
	private String statusHistoryItemId;



	/**
	 * @param x
	 */
	void setData(final BHREmplStatusHistory x) {
		x.setStatusId(statusId);
		x.setEffectiveDate(effectiveDate);
		x.setNotes(note);
	}
	/**
	 * @return Returns the effectiveDate.
	 */
	public int getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate The effectiveDate to set.
	 */
	public void setEffectiveDate(final int effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note The note to set.
	 */
	public void setNote(final String note) {
		this.note = note;
	}
	

	/**
	 * @return Returns the statusHistoryItemId.
	 */
	public String getStatusHistoryItemId() {
		return statusHistoryItemId;
	}
	/**
	 * @param statusHistoryItemId The statusHistoryItemId to set.
	 */
	public void setStatusHistoryItemId(final String statusHistoryItemId) {
		this.statusHistoryItemId = statusHistoryItemId;
	}
	/**
	 * @return Returns the statusId.
	 */
	public String getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId The statusId to set.
	 */
	public void setStatusId(final String statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return Returns the confirmed flag.
	 */
	public Boolean getConfirmed() {
		return confirmed;
	}

	/**
	 * @param confirmed The confirmed flag to set.
	 */
	public void setConfirmed(final Boolean confirmed) {
		this.confirmed = confirmed;
	}
}

	
