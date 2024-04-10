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
package com.arahant.services.standard.time.timesheetReview;

import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;

public final class FinalizedList extends TransmitReturnBase {

	private String loginId;
	private String lastEnteredTimeDate;
	private double hoursLastEntered;
	private String finalizationDate;
	private String personId;
	private String lname;
	private String fname;

	public FinalizedList() {
	}

	/**
	 * @param emp
	 */
	public FinalizedList(final Person emp) {
	}

	/**
	 * @param person
	 */
	FinalizedList(final BEmployee e) {
		setFinalizationDate(e.getFinalizeDate());
		setFname(e.getFirstName());
		setHoursLastEntered(e.getHoursLastEntered());
		setLastEnteredTimeDate(e.getLastEnteredTimeDate());
		setLname(e.getLastName());
		setLoginId(e.getUserLogin());
		setPersonId(e.getPersonId());
	}

	/**
	 * @return Returns the finalizationDate.
	 */
	public String getFinalizationDate() {
		return finalizationDate;
	}

	/**
	 * @param finalizationDate The finalizationDate to set.
	 */
	public void setFinalizationDate(final String finalizationDate) {
		this.finalizationDate = finalizationDate;
	}

	/**
	 * @return Returns the fname.
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname The fname to set.
	 */
	public void setFname(final String fname) {
		this.fname = fname;
	}

	/**
	 * @return Returns the hoursLastEntered.
	 */
	public double getHoursLastEntered() {
		return hoursLastEntered;
	}

	/**
	 * @param hoursLastEntered The hoursLastEntered to set.
	 */
	public void setHoursLastEntered(final double hoursLastEntered) {
		this.hoursLastEntered = hoursLastEntered;
	}

	/**
	 * @return Returns the lastEnteredTimeDate.
	 */
	public String getLastEnteredTimeDate() {
		return lastEnteredTimeDate;
	}

	/**
	 * @param lastEnteredTimeDate The lastEnteredTimeDate to set.
	 */
	public void setLastEnteredTimeDate(final String lastEnteredTimeDate) {
		this.lastEnteredTimeDate = lastEnteredTimeDate;
	}

	/**
	 * @return Returns the lname.
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname The lname to set.
	 */
	public void setLname(final String lname) {
		this.lname = lname;
	}

	/**
	 * @return Returns the loginId.
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId The loginId to set.
	 */
	public void setLoginId(final String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}
}
