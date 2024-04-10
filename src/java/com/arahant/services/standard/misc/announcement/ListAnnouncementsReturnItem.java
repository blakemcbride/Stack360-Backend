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
package com.arahant.services.standard.misc.announcement;
import com.arahant.business.BAnnouncement;


/**
 * 
 *
 *
 */
public class ListAnnouncementsReturnItem {
	
	public ListAnnouncementsReturnItem()
	{
		;
	}

	ListAnnouncementsReturnItem (BAnnouncement bc)
	{
		
		announcementId=bc.getAnnouncementId();
		finalDate=bc.getFinalDate();
		message=bc.getMessage();
		startDate=bc.getStartDate();

	}
	
	private String announcementId;
	private int finalDate;
	private String message;
	private int startDate;

	public String getAnnouncementId()
	{
		return announcementId;
	}
	public void setAnnouncementId(String announcementId)
	{
		this.announcementId=announcementId;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}

}

	
