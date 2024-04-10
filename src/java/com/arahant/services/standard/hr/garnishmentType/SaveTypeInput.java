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
package com.arahant.services.standard.hr.garnishmentType;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BGarnishmentType;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveTypeInput extends TransmitInputBase {

	void setData(BGarnishmentType bc)
	{
		
		bc.setWageTypeId(wageTypeId);
		bc.setDescription(description);
		bc.setLastActiveDate(inactiveDate);

	}
	
	@Validation (required=true)
	private String wageTypeId;
	@Validation (required=true, table="garnishment_type", column="description")
	private String description;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false, type="date")
	private int inactiveDate;

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}
	
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

}

	
