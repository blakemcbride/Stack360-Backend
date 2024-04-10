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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;
import com.arahant.services.standard.misc.message.FoundPeople;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchLoginsReturn extends TransmitReturnBase {

	private FoundPeople[] logins;
	
	public SearchLoginsReturn() {
		super();
	}

	/**
	 * @return Returns the logins.
	 */
	public FoundPeople[] getLogins() {
		return logins;
	}

	/**
	 * @param logins The logins to set.
	 */
	public void setLogins(final FoundPeople[] logins) {
		this.logins = logins;
	}

	/**
	 * @param persons
	 */
	void setLogins(final BPerson[] persons) {
		logins=new FoundPeople[persons.length];
		for (int loop=0;loop<persons.length;loop++)
			logins[loop]=new FoundPeople(persons[loop]);
	}
}

	
