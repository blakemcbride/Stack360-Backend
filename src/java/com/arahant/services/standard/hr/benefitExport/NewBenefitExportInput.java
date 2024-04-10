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
public class NewBenefitExportInput extends TransmitInputBase {

	
	@Validation (required=true)
	private String vendorId;
	@Validation (required=true)
	private String ediFileType;
	@Validation (required=true)
	private String ediFileStatus;

	public String getEdiFileStatus() {
		return ediFileStatus;
	}

	public void setEdiFileStatus(String ediFileStatus) {
		this.ediFileStatus = ediFileStatus;
	}

	public String getEdiFileType() {
		return ediFileType;
	}

	public void setEdiFileType(String ediFileType) {
		this.ediFileType = ediFileType;
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

	
