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
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.misc.developerHomepage;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class SearchEmployeesInput extends TransmitInputBase  {
	@Validation (table="employee",column="ssn",required=false)
	private String ssn;

	@Validation (table="person",column="fname",required=false)
	private String firstName;
	@Validation (table="person",column="lname",required=false)
	private String lastName;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	

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
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(final String ssn) {
		this.ssn = ssn;
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

	
	
}

	
