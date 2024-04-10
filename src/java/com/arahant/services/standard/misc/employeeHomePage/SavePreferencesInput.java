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
package com.arahant.services.standard.misc.employeeHomePage;
import com.arahant.annotation.Validation;

import com.arahant.beans.UserAttribute;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPerson;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SavePreferencesInput extends TransmitInputBase {

	void setData(BPerson bc)
	{
		
		bc.savePreference(UserAttribute.EMAIL_ADDRESSES,emailAddresses);
		bc.savePreference(UserAttribute.INCLUDE_MESSAGE_BODY,includeMessageBody);
		
		for (int loop=0;loop<getItem().length;loop++)
		{
			bc.savePreference("Exclude-"+item[loop].getId(), item[loop].getExclude());
		}

	}
	
	@Validation (table="person",column="personal_email",required=false)
	private String emailAddresses;
	@Validation (required=false)
	private boolean includeMessageBody;
	@Validation (required=false)
	private SavePreferencesInputItem []item;
	

	public String getEmailAddresses()
	{
		return emailAddresses;
	}
	public void setEmailAddresses(String emailAddresses)
	{
		this.emailAddresses=emailAddresses;
	}
	public boolean getIncludeMessageBody()
	{
		return includeMessageBody;
	}
	public void setIncludeMessageBody(boolean includeMessageBody)
	{
		this.includeMessageBody=includeMessageBody;
	}

	public SavePreferencesInputItem[] getItem() {
		if (item==null)
			item=new SavePreferencesInputItem[0];
		return item;
	}

	public void setItem(SavePreferencesInputItem[] item) {
		this.item = item;
	}
	
	

}

	
