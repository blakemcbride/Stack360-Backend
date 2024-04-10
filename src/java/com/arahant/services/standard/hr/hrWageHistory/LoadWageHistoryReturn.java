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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrWageHistory;

import com.arahant.business.BHRWage;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class LoadWageHistoryReturn extends TransmitReturnBase {

	private String wageTypeId;
	private double wageAmount;
	private int effectiveDate;
	private String positionId;
	private String note;

	

	void setData(final BHRWage w)
	{
		wageAmount=w.getWageAmount();
		wageTypeId=w.getWageTypeId();
		effectiveDate=w.getEffectiveDate();
		positionId=w.getPositionId();
		note=w.getNotes();
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
	 * @return Returns the positionId.
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId The positionId to set.
	 */
	public void setPositionId(final String positionId) {
		this.positionId = positionId;
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

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}



	public LoadWageHistoryReturn() {
		super();
	}
	
	
}

//wage type (1/2), wage amount, effective date, position id, note
	
