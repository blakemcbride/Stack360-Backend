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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hrConfig.wizardConfigurator;
import com.arahant.beans.WizardConfigurationProjectAssignment;
import com.arahant.business.BPerson;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListAssignedPersonsItem {

	private String fname;
	private String lname;
	private String personId;
	private String personType;

	public ListAssignedPersonsItem() {
	}

	ListAssignedPersonsItem(WizardConfigurationProjectAssignment pa) {
		BPerson emp = new BPerson(pa.getPersonId());
		fname = emp.getFirstName();
		lname = emp.getLastName();
		personId = emp.getPersonId();
		if(emp.isEmployee())
			personType = "E";
		else
			personType = "V";
	}
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

}

	
