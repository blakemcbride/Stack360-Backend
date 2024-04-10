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
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListStatusHistoryItem {

	private String statusHistoryItemId;
	private String statusName;
	private String effectiveDateFormatted;
	private String notePreview;
	private boolean readOnly;
	
	// status history item id, effective date formatted, status name, note preview (100 chars)

	public ListStatusHistoryItem()
	{
		
	}

	/**
	 * @param account
	 */
	ListStatusHistoryItem(final BHREmplStatusHistory w, final boolean owner) {
		statusHistoryItemId=w.getStatusHistId();


		statusName=w.getStatusName();
		effectiveDateFormatted=DateUtils.getDateFormatted(w.getEffectiveDate());

		notePreview=w.getNotes();
		
		if (notePreview.length()>100)
			notePreview=notePreview.substring(0,100);
		if (owner)
			readOnly=false;
		else
			readOnly=w.getReadOnly();
	}

	/**
	 * @return Returns the effectiveDateFormatted.
	 */
	public String getEffectiveDateFormatted() {
		return effectiveDateFormatted;
	}

	/**
	 * @param effectiveDateFormatted The effectiveDateFormatted to set.
	 */
	public void setEffectiveDateFormatted(final String effectiveDateFormatted) {
		this.effectiveDateFormatted = effectiveDateFormatted;
	}

	/**
	 * @return Returns the notePreview.
	 */
	public String getNotePreview() {
		return notePreview;
	}

	/**
	 * @param notePreview The notePreview to set.
	 */
	public void setNotePreview(final String notePreview) {
		this.notePreview = notePreview;
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
	 * @return Returns the statusName.
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName The statusName to set.
	 */
	public void setStatusName(final String statusName) {
		this.statusName = statusName;
	}

	/**
	 * @return Returns the readOnly.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(final boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	

}

	
