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
package com.arahant.services.standard.hr.callLogListParent;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchPersonsInput extends TransmitInputBase {

	@Validation (table="person",column="fname",required=false)
	private String firstName;
	@Validation (table="person",column="lname",required=false)
	private String lastName;
	@Validation (table="person",column="ssn",required=false)
	private String ssn;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	@Validation (min=1,max=1,required=true)
	private String searchType;

	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getFirstName()
	{
		return modifyForSearch(firstName, firstNameSearchType);
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return modifyForSearch(lastName,lastNameSearchType);
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(final String ssn)
	{
		this.ssn=ssn;
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



}

	
