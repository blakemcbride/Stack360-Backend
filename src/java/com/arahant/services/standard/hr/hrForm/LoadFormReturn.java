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
package com.arahant.services.standard.hr.hrForm;

import com.arahant.business.BPersonForm;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadFormReturn extends TransmitReturnBase {

	void setData(final BPersonForm bc)
	{
		
		formTypeId=bc.getFormTypeId();
		date=bc.getDate();
		comments=bc.getComments();

	}
	
	private String formTypeId;
	private int date;
	private String comments;

	public String getFormTypeId()
	{
		return formTypeId;
	}
	public void setFormTypeId(final String formTypeId)
	{
		this.formTypeId=formTypeId;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(final int date)
	{
		this.date=date;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(final String comments)
	{
		this.comments=comments;
	}

}

	
