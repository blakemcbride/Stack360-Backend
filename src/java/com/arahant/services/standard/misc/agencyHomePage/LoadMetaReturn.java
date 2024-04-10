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
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class LoadMetaReturn extends TransmitReturnBase {

	
	private int newPersonDefaultExternalId;
	private String newEmpOpenScreenGroupId;
	

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

}

	
