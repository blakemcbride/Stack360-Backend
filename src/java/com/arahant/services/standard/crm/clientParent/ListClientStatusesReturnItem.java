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
 * 
 */
package com.arahant.services.standard.crm.clientParent;
import com.arahant.business.BClientStatus;

public class ListClientStatusesReturnItem {
	
	public ListClientStatusesReturnItem()
	{
		
	}

	ListClientStatusesReturnItem (BClientStatus bc)
	{
		id=bc.getClientStatusId();
		code=bc.getCode();
		description=bc.getDescription();
		lastActiveDate=bc.getLastActiveDate();
		sequence=bc.getSeqNo();
		active=bc.getActive() + "";
	}
	
	private String id;
	private String code;
	private String description;
	private int lastActiveDate;
	private int sequence;
	private String active;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getLastActiveDate() {
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
}

	
