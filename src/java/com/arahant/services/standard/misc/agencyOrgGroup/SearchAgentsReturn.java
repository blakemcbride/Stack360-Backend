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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agencyOrgGroup;

import com.arahant.business.BAgent;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchAgentsReturn extends TransmitReturnBase {

	private Agents [] persons;
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
	public Agents[] getPersons() {
		return persons;
	}

	/**
	 * @param persons The persons to set.
	 */
	public void setPersons(final Agents[] persons) {
		this.persons = persons;
	}

	/**
	 * @param vcs
	 */
	void setPersons(final BAgent[] a, String orgGroupId) {
		persons=new Agents[a.length];
		for (int loop=0;loop<a.length;loop++)
			persons[loop]=new Agents(a[loop], orgGroupId);
	}
}

	
