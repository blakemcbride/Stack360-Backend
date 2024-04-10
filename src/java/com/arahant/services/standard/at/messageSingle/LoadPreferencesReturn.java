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
package com.arahant.services.standard.at.messageSingle;

import com.arahant.beans.Message;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.UserAttribute;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadPreferencesReturn extends TransmitReturnBase {
	private LoadPreferencesReturnItem item[];
	private String emailAddresses;
	private boolean includeMessageBody;
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public LoadPreferencesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final LoadPreferencesReturnItem[] item) {
		this.item = item;
	}

	public String getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public boolean getIncludeMessageBody() {
		return includeMessageBody;
	}

	public void setIncludeMessageBody(boolean includeMessageBody) {
		this.includeMessageBody = includeMessageBody;
	}

	public LoadPreferencesReturn()
	{
		
	}
	
	void setData(BPerson p)
	{
		emailAddresses=p.getStringPreference(UserAttribute.EMAIL_ADDRESSES);
		includeMessageBody=p.getBooleanPreference(UserAttribute.INCLUDE_MESSAGE_BODY);
		
		item=new LoadPreferencesReturnItem[Message.TYPE_IDS.length];
		for (int loop=0;loop<Message.TYPE_IDS.length;loop++)
			item[loop]=new LoadPreferencesReturnItem(p, Message.TYPE_IDS[loop], Message.TYPE_DESCRIPTIONS[loop]);
		
	}
	
}

	
