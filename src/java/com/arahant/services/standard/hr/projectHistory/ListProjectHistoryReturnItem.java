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

package com.arahant.services.standard.hr.projectHistory;

import com.arahant.business.BProjectHistory;

public class ListProjectHistoryReturnItem {

	private String historyId;
	private String toLastName;
	private String toFirstName;
	private String dateTimeFormatted;
	private String byLastName;
	private String byFirstName;

	public ListProjectHistoryReturnItem()
	{
	}

	ListProjectHistoryReturnItem (final BProjectHistory bc) {
		historyId=bc.getHistoryId();
		toLastName="";
		toFirstName="";
		dateTimeFormatted=bc.getDateTimeFormatted();
		byLastName=bc.getByLastName();
		byFirstName=bc.getByFirstName();
	}

	public String getHistoryId()
	{
		return historyId;
	}
	public void setHistoryId(final String historyId)
	{
		this.historyId=historyId;
	}
	public String getToLastName()
	{
		return toLastName;
	}
	public void setToLastName(final String toLastName)
	{
		this.toLastName=toLastName;
	}
	public String getToFirstName()
	{
		return toFirstName;
	}
	public void setToFirstName(final String toFirstName)
	{
		this.toFirstName=toFirstName;
	}
	public String getDateTimeFormatted()
	{
		return dateTimeFormatted;
	}
	public void setDateTimeFormatted(final String dateTimeFormatted)
	{
		this.dateTimeFormatted=dateTimeFormatted;
	}
	public String getByLastName()
	{
		return byLastName;
	}
	public void setByLastName(final String byLastName)
	{
		this.byLastName=byLastName;
	}
	public String getByFirstName()
	{
		return byFirstName;
	}
	public void setByFirstName(final String byFirstName)
	{
		this.byFirstName=byFirstName;
	}

}

	
