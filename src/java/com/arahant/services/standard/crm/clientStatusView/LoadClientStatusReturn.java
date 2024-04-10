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

import com.arahant.business.BClientCompany;
import com.arahant.services.TransmitReturnBase;

public class LoadClientStatusReturn extends TransmitReturnBase {

	void setData(BClientCompany bc)
	{
		statusId=bc.getClientStatusId();
		lastContactDate=bc.getLastContactDate();
		comments=bc.getStatusComments();
	}
	
	private String statusId;
	private int lastContactDate;
	private String comments;
	

	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(String statusId)
	{
		this.statusId=statusId;
	}
	public int getLastContactDate()
	{
		return lastContactDate;
	}
	public void setLastContactDate(int lastContactDate)
	{
		this.lastContactDate=lastContactDate;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments=comments;
	}

}

	
