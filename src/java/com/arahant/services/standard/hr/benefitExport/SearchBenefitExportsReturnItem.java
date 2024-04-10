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
import com.arahant.business.BEDITransaction;


/**
 * 
 *
 *
 */
public class SearchBenefitExportsReturnItem {
	
	public SearchBenefitExportsReturnItem()
	{
		;
	}

	SearchBenefitExportsReturnItem (BEDITransaction bc)
	{
		
		vendorName=bc.getVendorName();
		controlNumber=bc.getControlNumber();
		dateTimeFormatted=bc.getDateTime();
		status=bc.getStatus();

	}
	
	private String vendorName;
private String controlNumber;
private String dateTimeFormatted;
private String status;
;

	public String getVendorName()
	{
		return vendorName;
	}
	public void setVendorName(String vendorName)
	{
		this.vendorName=vendorName;
	}
	public String getControlNumber()
	{
		return controlNumber;
	}
	public void setControlNumber(String controlNumber)
	{
		this.controlNumber=controlNumber;
	}

	public String getDateTimeFormatted() {
		return dateTimeFormatted;
	}

	public void setDateTimeFormatted(String dateTimeFormatted) {
		this.dateTimeFormatted = dateTimeFormatted;
	}
	
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}

}

	
