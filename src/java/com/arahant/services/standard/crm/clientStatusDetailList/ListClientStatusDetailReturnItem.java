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
package com.arahant.services.standard.crm.clientStatusDetailList;
import com.arahant.business.BClientCompany;

public class ListClientStatusDetailReturnItem {
	
	public ListClientStatusDetailReturnItem()
	{
		
	}

	ListClientStatusDetailReturnItem (BClientCompany bc)
	{
		clientName=bc.getName();
		clientId=bc.getOrgGroupId();
		lastContactDate=bc.getLastContactDate();
		comments=bc.getStatusComments();
		statusCode=bc.getClientStatus().getCode();
		statusId=bc.getClientStatusId();
	}
	
	private String clientName;
	private String clientId;
	private int lastContactDate;
	private String comments;
	private String statusCode;
	private String statusId;
	

	public String getClientName()
	{
		return clientName;
	}
	public void setClientName(String clientName)
	{
		this.clientName=clientName;
	}
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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
	public String getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(String statusCode)
	{
		this.statusCode=statusCode;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(String statusId)
	{
		this.statusId=statusId;
	}

}

	
