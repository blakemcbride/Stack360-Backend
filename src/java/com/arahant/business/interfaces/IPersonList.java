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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.business.interfaces;

import com.arahant.beans.Person;

/**
 *
 * Created on Feb 12, 2007
 *
 */
public interface IPersonList {

	/**
	 * @return Returns the orgGroupType.
	 */
	public abstract int getOrgGroupType();

	/**
	 * @param orgGroupType The orgGroupType to set.
	 */
	public abstract void setOrgGroupType(int orgGroupType);

	/**
	 * @return Returns the hasLogin.
	 */
	public abstract String getHasLogin();

	/**
	 * @param hasLogin The hasLogin to set.
	 */
	public abstract void setHasLogin(String hasLogin);

	public abstract Person makePerson(Person p);

	public abstract String getPersonId();

	public abstract void setPersonId(String personId);

	public abstract String getLname();

	public abstract void setLname(String lname);

	public abstract String getFname();

	public abstract void setFname(String fname);
}
