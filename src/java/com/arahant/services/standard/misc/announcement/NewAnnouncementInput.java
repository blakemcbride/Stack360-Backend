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
import com.arahant.annotation.Validation;

import com.arahant.business.BAnnouncement;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewAnnouncementInput extends TransmitInputBase {

	void setData(BAnnouncement bc)
	{
		
		bc.setFinalDate(finalDate);
		bc.setOrgGroupId(orgGroupId);
		bc.setMessage(message);
		bc.setStartDate(startDate);

	}
	
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int finalDate;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private String message;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int startDate;

	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
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

	
