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

import com.arahant.business.BProspectContact;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class ListProspectContactsReturn extends TransmitReturnBase {

	ListProspectContactsReturnItem [] persons;
	private int cap;

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
	 * @return Returns the persons.
	 */
	public ListProspectContactsReturnItem[] getPersons() {
		return persons;
	}

	/**
	 * @param persons The persons to set.
	 */
	public void setPersons(final ListProspectContactsReturnItem[] persons) {
		this.persons = persons;
	}

	/**
	 * @param contacts
	 */
	public void setPersons(final BProspectContact[] contacts, String groupId) {
		persons=new ListProspectContactsReturnItem[contacts.length];
		for (int loop=0;loop<persons.length;loop++)
			persons[loop]=new ListProspectContactsReturnItem(contacts[loop], groupId);
	}
}

	
