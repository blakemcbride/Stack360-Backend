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
package com.arahant.services.standard.crm.prospectListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private boolean address;
	@Validation (required=false)
	private boolean companyPhoneNumber;
	@Validation (required=false)
	private boolean identifier;
	@Validation (required=false)
	private boolean primaryContactName;
	@Validation (required=false)
	private boolean sortAsc;
	@Validation (min=0,max=1,required=false)
	private int sortType;


	public boolean getAddress()
	{
		return address;
	}
	public void setAddress(boolean address)
	{
		this.address=address;
	}
	public boolean getCompanyPhoneNumber()
	{
		return companyPhoneNumber;
	}
	public void setCompanyPhoneNumber(boolean companyPhoneNumber)
	{
		this.companyPhoneNumber=companyPhoneNumber;
	}
	public boolean getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(boolean identifier)
	{
		this.identifier=identifier;
	}
	public boolean getPrimaryContactName()
	{
		return primaryContactName;
	}
	public void setPrimaryContactName(boolean primaryContactName)
	{
		this.primaryContactName=primaryContactName;
	}
	public boolean getSortAsc()
	{
		return sortAsc;
	}
	public void setSortAsc(boolean sortAsc)
	{
		this.sortAsc=sortAsc;
	}
	public int getSortType()
	{
		return sortType;
	}
	public void setSortType(int sortType)
	{
		this.sortType=sortType;
	}


}

	
