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
package com.arahant.services.standard.hr.benefitExport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchBenefitExportsInput extends TransmitInputBase {

	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (type="date",required=false)
private int toDate;
	@Validation (required=false)
private String vendorId;
;

	public int getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(int fromDate)
	{
		this.fromDate=fromDate;
	}
	public int getToDate()
	{
		return toDate;
	}
	public void setToDate(int toDate)
	{
		this.toDate=toDate;
	}
	public String getVendorId()
	{
		return vendorId;
	}
	public void setVendorId(String vendorId)
	{
		this.vendorId=vendorId;
	}


}

	
