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
package com.arahant.services.standard.crm.clientAppointment;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchEmployeesInput extends TransmitInputBase {

	@Validation (required=false)
	private String firstName;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (required=false)
	private String lastName;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	@Validation (required=false)
	private String [] excludeIds;
	@Validation (min=1,max=16,required=false)
	private String personId;
	@Validation (min=1,max=16,required=false)
	private String arrangementId;
	

	public String getFirstName()
	{
		return modifyForSearch(firstName,firstNameSearchType);
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public int getFirstNameSearchType()
	{
		return firstNameSearchType;
	}
	public void setFirstNameSearchType(int firstNameSearchType)
	{
		this.firstNameSearchType=firstNameSearchType;
	}
	public String getLastName()
	{
		return modifyForSearch(lastName,lastNameSearchType);
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public int getLastNameSearchType()
	{
		return lastNameSearchType;
	}
	public void setLastNameSearchType(int lastNameSearchType)
	{
		this.lastNameSearchType=lastNameSearchType;
	}
	public String [] getExcludeIds()
	{
		if (excludeIds==null)
			excludeIds= new String [0];
		return excludeIds;
	}
	public void setExcludeIds(String [] excludeIds)
	{
		this.excludeIds=excludeIds;
	}

	public String getArrangementId() {
		return arrangementId;
	}

	public void setArrangementId(String arrangementId) {
		this.arrangementId = arrangementId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


}

	
