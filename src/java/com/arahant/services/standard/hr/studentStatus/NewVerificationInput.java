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
package com.arahant.services.standard.hr.studentStatus;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BStudentVerification;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewVerificationInput extends TransmitInputBase {

	void setData(BStudentVerification bc)
	{
		
		bc.setTerm(term);
		bc.setYear(year);
		bc.setPersonId(personId);
	}
	
	@Validation (required=true)
	private String term;
	@Validation (min=1900,max=3000,required=true)
	private int year;
	@Validation (required=true)
	private String personId;

	public String getTerm()
	{
		return term;
	}
	public void setTerm(String term)
	{
		this.term=term;
	}
	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year=year;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

}

	
