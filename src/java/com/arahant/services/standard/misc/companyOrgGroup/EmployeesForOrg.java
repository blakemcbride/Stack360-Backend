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
package com.arahant.services.standard.misc.companyOrgGroup;
import com.arahant.business.BEmployee;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class EmployeesForOrg {

	private String primary;
	private String personId;
	private String lname;
	private String fname;
	private String middleName;

        EmployeesForOrg(BEmployee bEmployee, String orgGroupId) {
             super();
            primary=bEmployee.isPrimary()?"Y":"N";
            personId=bEmployee.getPersonId();
            lname=bEmployee.getLastName();
            fname=bEmployee.getFirstName();
            middleName=bEmployee.getMiddleName();

        }
	
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public EmployeesForOrg() {

	}

	/**
	 * @param employee
	 */
	public EmployeesForOrg(final BEmployee employee) {
		super();

		primary=employee.isPrimary()?"Y":"N";
		personId=employee.getPersonId();
		lname=employee.getLastName();
		fname=employee.getFirstName();
		middleName=employee.getMiddleName();
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
	 * @return Returns the primary.
	 */
	public String getPrimary() {
		return primary;
	}

	/**
	 * @param primary The primary to set.
	 */
	public void setPrimary(final String primary) {
		this.primary = primary;
	}

	
}

	
