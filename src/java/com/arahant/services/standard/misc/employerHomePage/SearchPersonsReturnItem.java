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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;


/**
 * 
 *
 *
 */
public class SearchPersonsReturnItem {
	
	public SearchPersonsReturnItem()
	{
		;
	}

	SearchPersonsReturnItem (final BPerson bc) throws ArahantException
	{
		
		dob=bc.getDob();
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		middleName=bc.getMiddleName();
		sex=bc.getSex();
		ssn=bc.getSsn();
		personId=bc.getPersonId();
		
		BEmployee bemp=bc.getBEmployee();
		isEmployee="No";
		
		if (bemp!=null)
		{
			employeeStatus=bemp.getLastStatusName();
			
			isEmployee="Yes";
		}

	}
	
	private int dob;
	private String firstName;
	private String lastName;
	private String middleName;
	private String sex;
	private String ssn, personId;
	private String employeeStatus;
	private String isEmployee;

	public String getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(String isEmployee) {
		this.isEmployee = isEmployee;
	}

	/**
	 * @return Returns the status.
	 */
	public String getEmployeeStatus() {
		return employeeStatus;
	}

	/**
	 * @param status The status to set.
	 */
	public void setEmployeeStatus(final String status) {
		this.employeeStatus = status;
	}

	public int getDob()
	{
		return dob;
	}
	public void setDob(final int dob)
	{
		this.dob=dob;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(final String middleName)
	{
		this.middleName=middleName;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(final String sex)
	{
		this.sex=sex;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(final String ssn)
	{
		this.ssn=ssn;
	}

	/**
	 * @return Returns the employeeId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setPersonId(final String employeeId) {
		this.personId = employeeId;
	}

}

	
