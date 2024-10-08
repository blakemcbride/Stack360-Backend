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
package com.arahant.services.standard.crm.prospectAppointment;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAppointment;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveArrangmentInput extends TransmitInputBase {

	void setData(BAppointment bc)
	{
		
		bc.setDate(date);
		bc.setPurpose(purpose);
		bc.setStatus(status);
		bc.setType("A");
		bc.setPersonId(personId);
	}
	
	@Validation (type="date",required=false)
	private int date;
	@Validation (table="appointment",column="purpose",required=true)
	private String purpose;
	@Validation (table="appointment",column="status",required=true)
	private String status;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (min=1,max=16,required=true)
	private String personId;

	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public String getPurpose()
	{
		return purpose;
	}
	public void setPurpose(String purpose)
	{
		this.purpose=purpose;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

}

	
