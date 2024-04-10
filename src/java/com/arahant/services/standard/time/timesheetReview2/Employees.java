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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.business.BEmployee;

public class Employees {

	private String personId;
	private String lname;
	private String fname;
	private String active;
	private String hasTimeReadyForApproval;
	private String middleName;

	public Employees() {
	}

	/**
	 * @param employee
	 */
	Employees(final BEmployee e) {
		personId = e.getPersonId();
		lname = e.getLastName();
		fname = e.getFirstName();
		active = e.isActive() < 1 ? "Y" : "N";
		hasTimeReadyForApproval = e.getHasTimeReadyForApproval() ? "Yes" : "No";
		middleName = e.getMiddleName();
	}

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return Returns the hasTimeReadyForApproval.
	 */
	public String getHasTimeReadyForApproval() {
		return hasTimeReadyForApproval;
	}

	/**
	 * @param hasTimeReadyForApproval The hasTimeReadyForApproval to set.
	 */
	public void setHasTimeReadyForApproval(final String hasTimeReadyForApproval) {
		this.hasTimeReadyForApproval = hasTimeReadyForApproval;
	}

	/**
	 * @return Returns the active.
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final String active) {
		this.active = active;
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
