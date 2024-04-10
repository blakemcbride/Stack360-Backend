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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.misc.employeeHomePage;
import com.arahant.business.BPerson;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class FoundLogins  {



	public FoundLogins() {

	}
	
	private String personId;
	private String lname;
	private String fname;
	private String userLogin;
	


	/**
	 * @param person
	 */
	public FoundLogins(final BPerson p) {
		super();
		personId=p.getPersonId();
		lname=p.getLastName();
		fname=p.getFirstName();
		userLogin=p.getUserLogin();
	}



	/**
	 * @return Returns the fname.
	 */
	public String getFname() {
		return fname;
	}



	/**
	 * @param fname The fname to set.
	 */
	public void setFname(final String fname) {
		this.fname = fname;
	}



	/**
	 * @return Returns the lname.
	 */
	public String getLname() {
		return lname;
	}



	/**
	 * @param lname The lname to set.
	 */
	public void setLname(final String lname) {
		this.lname = lname;
	}



	/**
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}



	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}



	/**
	 * @return Returns the userLogin.
	 */
	public String getUserLogin() {
		return userLogin;
	}



	/**
	 * @param userLogin The userLogin to set.
	 */
	public void setUserLogin(final String userLogin) {
		this.userLogin = userLogin;
	}

}

	
