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
package com.arahant.services.standard.crm.appointment;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListAttendeeAppointmentsInput extends TransmitInputBase {

	@Validation (required=false)
	private String [] item;
	@Validation (type="date",required=true)
	private int date;
	@Validation (required=false)
	private String id;
	@Validation (required=false)
	private boolean fillFromId;
	@Validation (required=false)
	private String primaryEmployeeId;
	@Validation (required=false)
	private String primaryContactId; 

	public String [] getItem()
	{
		if (item==null)
			item= new String [0];
		return item;
	}
	public void setItem(String [] item)
	{
		this.item=item;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getFillFromId() {
		return fillFromId;
	}

	public void setFillFromId(boolean fillFromId) {
		this.fillFromId = fillFromId;
	}

	public String getPrimaryContactId() {
		return primaryContactId;
	}

	public void setPrimaryContactId(String primaryContactId) {
		this.primaryContactId = primaryContactId;
	}

	public String getPrimaryEmployeeId() {
		return primaryEmployeeId;
	}

	public void setPrimaryEmployeeId(String primaryEmployeeId) {
		this.primaryEmployeeId = primaryEmployeeId;
	}
	


}

	
