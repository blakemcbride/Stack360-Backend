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
package com.arahant.services.standard.crm.clientStatusView;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BClientCompany;

public class SaveClientStatusInput extends TransmitInputBase {

	void setData(BClientCompany bc)
	{
		bc.setLastContactDate(lastContactDate);
		bc.setClientStatusId(statusId);
		bc.setStatusComments(comments);
	}
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=true)
	private String statusId;
	@Validation (required=false)
	private String comments;
	@Validation (type="date",required=true)
	private int lastContactDate;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(String statusId)
	{
		this.statusId=statusId;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments=comments;
	}
	public int getLastContactDate()
	{
		return lastContactDate;
	}
	public void setLastContactDate(int lastContactDate)
	{
		this.lastContactDate=lastContactDate;
	}
}

	
