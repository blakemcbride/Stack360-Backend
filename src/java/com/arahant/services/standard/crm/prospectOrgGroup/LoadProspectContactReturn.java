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
public class LoadProspectContactReturn extends TransmitReturnBase   {

	private String workPhone;
	private String workFax;
	private boolean primaryIndicator;
	private String personalEmail;
	private String jobTitle;
	private String personId;
	private String lname;
	private String fname;
	private int type;




	/**
	 * @param contact
	 */
	public LoadProspectContactReturn(final BProspectContact contact, String orgGroupId) {
		super();
		workPhone=contact.getWorkPhoneNumber();
		workFax=contact.getWorkFaxNumber();
		primaryIndicator=contact.isPrimary(orgGroupId);
		personalEmail=contact.getPersonalEmail();
		jobTitle=contact.getJobTitle();
		personId=contact.getPersonId();
		lname=contact.getLastName();
		fname=contact.getFirstName();
		type=contact.getProspectType();
	}

	public LoadProspectContactReturn() {
		super();
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
	 * @return Returns the personalEmail.
	 */
	public String getPersonalEmail() {
		return personalEmail;
	}




	/**
	 * @param personalEmail The personalEmail to set.
	 */
	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
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
	 * @return Returns the primaryIndicator.
	 */
	public boolean getPrimaryIndicator() {
		return primaryIndicator;
	}




	/**
	 * @param primaryIndicator The primaryIndicator to set.
	 */
	public void setPrimaryIndicator(final boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}




	/**
	 * @return Returns the workFax.
	 */
	public String getWorkFax() {
		return workFax;
	}




	/**
	 * @param workFax The workFax to set.
	 */
	public void setWorkFax(final String workFax) {
		this.workFax = workFax;
	}




	/**
	 * @return Returns the workPhone.
	 */
	public String getWorkPhone() {
		return workPhone;
	}




	/**
	 * @param workPhone The workPhone to set.
	 */
	public void setWorkPhone(final String workPhone) {
		this.workPhone = workPhone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	
}

	
