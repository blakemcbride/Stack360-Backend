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
package com.arahant.services.standard.hr.benefitWizardStatus;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class SearchEmployeeWizardStatusesInput extends TransmitInputBase {

	@Validation (required = false)
	private String lastName;
	@Validation (min = 2,max = 5,required = false)
	private int lastNameSearchType;
	@Validation (required = false)
	private String firstName;
	@Validation (min = 2,max = 5,required = false)
	private int firstNameSearchType;
	@Validation (type = "date",required = false)
	private int fromDate;
	@Validation (type = "date",required = false)
	private int toDate;
	@Validation (required = false)
	private String wizardStatus;
	

	public String getLastName()
	{
		return modifyForSearch(lastName,lastNameSearchType);
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	public int getLastNameSearchType()
	{
		return lastNameSearchType;
	}
	public void setLastNameSearchType(int lastNameSearchType)
	{
		this.lastNameSearchType = lastNameSearchType;
	}
	public String getFirstName()
	{
		return modifyForSearch(firstName,firstNameSearchType);
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public int getFirstNameSearchType()
	{
		return firstNameSearchType;
	}
	public void setFirstNameSearchType(int firstNameSearchType)
	{
		this.firstNameSearchType = firstNameSearchType;
	}
	public int getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(int fromDate)
	{
		this.fromDate = fromDate;
	}
	public int getToDate()
	{
		return toDate;
	}
	public void setToDate(int toDate)
	{
		this.toDate = toDate;
	}
	public String getWizardStatus()
	{
		return wizardStatus;
	}
	public void setWizardStatus(String wizardStatus)
	{
		this.wizardStatus = wizardStatus;
	}


}

	
