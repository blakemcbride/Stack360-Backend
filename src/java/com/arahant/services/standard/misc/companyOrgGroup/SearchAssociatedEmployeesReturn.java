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
package com.arahant.services.standard.misc.companyOrgGroup;

import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchAssociatedEmployeesReturn extends TransmitReturnBase {

	private EmployeesForOrg [] persons;
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
	public EmployeesForOrg[] getPersons() {
		return persons;
	}

	/**
	 * @param persons The persons to set.
	 */
	public void setPersons(final EmployeesForOrg[] persons) {
		this.persons = persons;
	}

	/**
	 * @param vcs
	 */
	void setPersons(final BEmployee[] a, String orgGroupId) {
		persons=new EmployeesForOrg[a.length];
		for (int loop=0;loop<a.length;loop++)
			persons[loop]=new EmployeesForOrg(a[loop], orgGroupId);
	}


}

	
