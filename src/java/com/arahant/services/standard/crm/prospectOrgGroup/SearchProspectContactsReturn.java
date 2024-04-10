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
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.crm.prospectOrgGroup;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProspectContact;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class SearchProspectContactsReturn extends TransmitReturnBase {
	
	private Contacts [] contacts;
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}

	/**
	 * @return Returns the contacts.
	 */
	public Contacts[] getContacts() {
		return contacts;
	}

	/**
	 * @param contacts The contacts to set.
	 */
	public void setContacts(final Contacts[] contacts) {
		this.contacts = contacts;
	}

	public void setContacts(final BProspectContact[] c) {
		contacts=new Contacts[c.length];
		for (int loop=0;loop<c.length;loop++)
			contacts[loop]=new Contacts(c[loop]);
	}

}

	
