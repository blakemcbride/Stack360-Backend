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
package com.arahant.services.standard.hr.spousalVerification;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BSpousalVerification;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveVerificationInput extends TransmitInputBase {

	void setData(BSpousalVerification bc)
	{
		if(yearHalf > 0)
		{
			year = (year * 10) + yearHalf;
		}
		bc.setYear(year);
		bc.setDateReceived(dateReceived);

	}
	
	@Validation (min=1900,max=3000,required=true)
	private int year;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (min=0,max=2,required=false)
	private int yearHalf;
	@Validation (type="date", required=true)
	private int dateReceived;

	public int getYearHalf() {
		return yearHalf;
	}

	public void setYearHalf(int yearHalf) {
		this.yearHalf = yearHalf;
	}
	public int getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(int dateReceived) {
		this.dateReceived = dateReceived;
	}

	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year=year;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
