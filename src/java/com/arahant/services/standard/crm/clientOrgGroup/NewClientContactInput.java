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
 *
 * Created on Feb 5, 2007
*/

package com.arahant.services.standard.crm.clientOrgGroup;

import com.arahant.annotation.Validation;
import com.arahant.business.BClientContact;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class NewClientContactInput extends TransmitInputBase  {
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
	@Validation (table="prophet_login",column="user_login",required=false)
	private String login;
	@Validation (table="prophet_login",column="user_password",required=false)
	private String contactPassword;
	@Validation (required=false)
	private boolean canLogin;
	@Validation (required=false)
	private String screenGroupId;
	@Validation (required=true)
	private String orgGroupId;
	@Validation (required=false)
	private String securityGroupId;
	private int contactType;

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public void makeClientContact(final BClientContact bcc) throws ArahantException {
		bcc.setWorkPhone(workPhone);
		bcc.setWorkFax(workFax);
		
		bcc.setPersonalEmail(personalEmail);
		bcc.setJobTitle(jobTitle);
		bcc.setLastName(lname);
		bcc.setFirstName(fname);
		bcc.setUserLogin(login);
		bcc.setUserPassword(contactPassword, canLogin);
		bcc.setScreenGroupId(screenGroupId);
		bcc.setSecurityGroupId(securityGroupId);
		bcc.setContactType((short)contactType);
	}

	public boolean getCanLogin() {
		return canLogin;
	}

	public void setCanLogin(final boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(final String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(final String lname) {
		this.lname = lname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getContactPassword() {
		return contactPassword;
	}

	public void setContactPassword(final String password) {
		this.contactPassword = password;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public boolean isPrimaryIndicator() {
		return primaryIndicator;
	}

	public void setPrimaryIndicator(final boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(final String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getWorkFax() {
		return workFax;
	}

	public void setWorkFax(final String workFax) {
		this.workFax = workFax;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(final String workPhone) {
		this.workPhone = workPhone;
	}

	public int getContactType() {
		return contactType;
	}

	public void setContactType(int contactType) {
		this.contactType = contactType;
	}
}

	
