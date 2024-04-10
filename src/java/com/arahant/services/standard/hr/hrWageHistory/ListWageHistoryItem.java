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
package com.arahant.services.standard.hr.hrWageHistory;
import com.arahant.business.BHRWage;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListWageHistoryItem {

	private String wageId;
	private String wageTypeName;
	private double wageAmount;
	private String effectiveDateFormatted;
	private String positionName;
	private String notePreview;
	private boolean readOnly;
	
	// output: wage history item id, wage type formatted (Hourly, Annually or Yearly), 
	//wage amount, effective date formatted, position name, note preview (100 chars)


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

	/**
	 * @param account
	 */
	ListWageHistoryItem(final BHRWage w, final boolean owner) {
		wageId=w.getWageId();

		wageTypeName=w.getWageTypeName();

		wageAmount=w.getWageAmount();
		effectiveDateFormatted=DateUtils.getDateFormatted(w.getEffectiveDate());
		positionName=w.getPositionName();
		notePreview=w.getNotes();
		if (notePreview==null)
			notePreview="";
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
	 * @return Returns the positionName.
	 */
	public String getPositionName() {
		return positionName;
	}
	/**
	 * @param positionName The positionName to set.
	 */
	public void setPositionName(final String positionName) {
		this.positionName = positionName;
	}
	/**
	 * @return Returns the wageAmount.
	 */
	public double getWageAmount() {
		return wageAmount;
	}
	/**
	 * @param wageAmount The wageAmount to set.
	 */
	public void setWageAmount(final double wageAmount) {
		this.wageAmount = wageAmount;
	}

	public ListWageHistoryItem()
	{
		
	}
	/**
	 * @return Returns the WageId.
	 */
	public String getWageId() {
		return wageId;
	}

	/**
	 * @param WageId The WageId to set.
	 */
	public void setWageId(final String wageId) {
		this.wageId =wageId;
	}

	public String getWageTypeName() {
		return wageTypeName;
	}

	public void setWageTypeName(String wageTypeName) {
		this.wageTypeName = wageTypeName;
	}




}

	
