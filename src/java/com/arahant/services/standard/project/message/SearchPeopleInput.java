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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.message;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchPeopleInput extends TransmitInputBase {

	private String firstName; // (like clause)
	private String lastName; // (like clause)

	private int firstNameSearchType;
	private int lastNameSearchType;

	private String projectId;
	private String shiftId;


	public SearchPeopleInput() {
		super();
	}

	/**
	 * @return Returns the firstNameSearchType.
	 */
	public int getFirstNameSearchType() {
		return firstNameSearchType;
	}

	/**
	 * @param firstNameSearchType The firstNameSearchType to set.
	 */
	public void setFirstNameSearchType(final int firstNameSearchType) {
		this.firstNameSearchType = firstNameSearchType;
	}

	/**
	 * @return Returns the lastNameSearchType.
	 */
	public int getLastNameSearchType() {
		return lastNameSearchType;
	}

	/**
	 * @param lastNameSearchType The lastNameSearchType to set.
	 */
	public void setLastNameSearchType(final int lastNameSearchType) {
		this.lastNameSearchType = lastNameSearchType;
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return modifyForSearch(firstName, firstNameSearchType);
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return modifyForSearch(lastName, lastNameSearchType);
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
}

	
