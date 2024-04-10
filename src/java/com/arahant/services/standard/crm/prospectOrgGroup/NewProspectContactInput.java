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
import com.arahant.annotation.Validation;


import com.arahant.business.BProspectContact;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class NewProspectContactInput extends TransmitInputBase  {
	@Validation (table="phone",column="phone_number",required=false)
	private String workPhone;
	@Validation (table="phone",column="phone_number",required=false)
	private String workFax;
	@Validation (required=false)
	private boolean primaryIndicator;
	@Validation (table="person",column="personal_email",required=false)
	private String personalEmail;
	@Validation (table="person",column="job_title",required=false)
	private String jobTitle;
	@Validation (table="person",column="lname",required=true)
	private String lname;
	@Validation (table="person",column="fname",required=true)
	private String fname;
	@Validation (min=1,max=16,required=true)
	private String orgGroupId;
	@Validation (min=1,max=5,required=true)
	private int type;
	

	/**
	 * @param bcc
	 * @throws ArahantException 
	 */
	public void makeProspectContact(final BProspectContact bcc) throws ArahantException {
		bcc.setWorkPhone(workPhone);
		bcc.setWorkFax(workFax);
		
		bcc.setPersonalEmail(personalEmail);
		bcc.setJobTitle(jobTitle);
		bcc.setLastName(lname);
		bcc.setFirstName(fname);
		bcc.setProspectType(type);
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
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}
	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
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
	 * @return Returns the primaryIndicator.
	 */
	public boolean isPrimaryIndicator() {
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

	
