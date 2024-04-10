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
package com.arahant.services.standard.hr.hrParent;

import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class LoadMetaReturn extends TransmitReturnBase {

	
	private int newPersonDefaultExternalId;
	private String newEmpOpenScreenGroupId;
	private boolean multipleCompanySupport;
	private boolean canEditHicNumber;

	public boolean getCanEditHicNumber() {
		return canEditHicNumber;
	}

	public void setCanEditHicNumber(boolean canEditHicNumber) {
		this.canEditHicNumber = canEditHicNumber;
	}

	private boolean ssnRequired = !BProperty.getBoolean("SSN Not Required");

	public boolean getSsnRequired() {
		return ssnRequired;
	}

	public void setSsnRequired(boolean ssnRequired) {
		this.ssnRequired = ssnRequired;
	}
	

	public int getNewPersonDefaultExternalId()
	{
		return newPersonDefaultExternalId;
	}
	public void setNewPersonDefaultExternalId(int newPersonDefaultExternalId)
	{
		this.newPersonDefaultExternalId=newPersonDefaultExternalId;
	}

	public String getNewEmpOpenScreenGroupId() {
		return newEmpOpenScreenGroupId;
	}

	public void setNewEmpOpenScreenGroupId(String newEmpOpenScreenGroupId) {
		this.newEmpOpenScreenGroupId = newEmpOpenScreenGroupId;
	}

	public boolean getMultipleCompanySupport() {
		return multipleCompanySupport;
	}

	public void setMultipleCompanySupport(boolean multipleCompanySupport) {
		this.multipleCompanySupport = multipleCompanySupport;
	}

}

	
