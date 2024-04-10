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
import com.arahant.business.BProjectStatus;


/**
 * 
 *
 *
 */
public class ListStatusesReturnItem {
	
	public ListStatusesReturnItem()
	{
		;
	}

	ListStatusesReturnItem (final BProjectStatus bc)
	{
		
		statusCode=bc.getCode();
		statusId=bc.getProjectStatusId();
		active=bc.getActive()==1;

	}
	
	private String statusCode;
	private String statusId;
	private boolean active;

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(final String statusCode)
	{
		this.statusCode=statusCode;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(final String statusId)
	{
		this.statusId=statusId;
	}

}

	
