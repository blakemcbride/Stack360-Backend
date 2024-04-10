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
package com.arahant.services.standard.at.applicantProfile;
import com.arahant.business.BApplicantStatus;


/**
 * 
 *
 *
 */
public class SearchApplicantStatusesReturnItem {
	
	public SearchApplicantStatusesReturnItem()
	{
		
	}

	SearchApplicantStatusesReturnItem (BApplicantStatus bc)
	{
		id=bc.getId();
		name=bc.getName();
		considerForHire=bc.getConsiderForHire();
		sendEmail=bc.getSendEmail()=='Y'?true:false;
	}
	
	private String id;
	private String name;
	private boolean considerForHire;
	private boolean sendEmail;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public boolean getConsiderForHire()
	{
		return considerForHire;
	}
	public void setConsiderForHire(boolean considerForHire)
	{
		this.considerForHire=considerForHire;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
}

	
