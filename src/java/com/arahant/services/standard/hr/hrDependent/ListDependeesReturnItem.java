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
package com.arahant.services.standard.hr.hrDependent;
import com.arahant.business.BHREmplDependent;


/**
 * 
 *
 *
 */
public class ListDependeesReturnItem {
	private String firstName;
	private String lastName;
	private String middleName;
	private String sex;
	private String relationship;
	private int dob;
	private String ssn;
	private String status;

	public ListDependeesReturnItem()
	{
		;
	}
	ListDependeesReturnItem (BHREmplDependent ed)
	{
		firstName=ed.getEmployeeFName();
		lastName=ed.getEmployeeLName();
		middleName=ed.getEmployeeMName();
		sex=ed.getEmployeeSex()+"";
		dob=ed.getEmployeeDob();
		relationship=ed.getTextRelationship();
		ssn=ed.getEmployeeSSN();		
		status=ed.getStatus();
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(final String status) {
		this.status = status;
	}
	/**
	 * @return Returns the dob.
	 */
	public int getDob() {
		return dob;
	}
	/**
	 * @param dob The dob to set.
	 */
	public void setDob(final int dob) {
		this.dob = dob;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
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

	/**
	 * @return Returns the relationship.
	 */
	public String getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship The relationship to set.
	 */
	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return Returns the sex.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex The sex to set.
	 */
	public void setSex(final String sex) {
		this.sex = sex;
	}
	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}
}

	
