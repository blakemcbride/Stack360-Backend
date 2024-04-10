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
package com.arahant.services.standard.misc.vendorOrgGroup;

import com.arahant.business.BVendorContact;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class LoadVendorContactReturn extends TransmitReturnBase  {

	private String workPhone;
	private String workFax;
	private boolean primaryIndicator;
	private String personalEmail;
	private String jobTitle;
	private String personId;
	private String lname;
	private String fname;
	private String login;
	private String password;
	private boolean canLogin;
	private String screenGroupId;
	private String screenGroupName;
	private String screenGroupExtId;
	private String securityGroupId;
    private String securityGroupName;

	/**
	 * @return Returns the securityGroupId.
	 */
	public String getSecurityGroupId() {
		return securityGroupId;
	}
	/**
	 * @param securityGroupId The securityGroupId to set.
	 */
	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	/**
	 * @return Returns the securityGroupName.
	 */
	public String getSecurityGroupName() {
		return securityGroupName;
	}
	/**
	 * @param securityGroupName The securityGroupName to set.
	 */
	public void setSecurityGroupName(final String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}
	/**
	 * @param contact
	 */
	void setData(final BVendorContact contact) {
		workPhone=contact.getWorkPhoneNumber();
		workFax=contact.getWorkFaxNumber();
		primaryIndicator=contact.isPrimary();
		personalEmail=contact.getPersonalEmail();
		jobTitle=contact.getJobTitle();
		personId=contact.getPersonId();
		lname=contact.getLastName();
		fname=contact.getFirstName();
		login=contact.getUserLogin();
		password=contact.getUserPassword();
		canLogin=contact.getCanLogin()=='Y';
		screenGroupId=contact.getScreenGroupId();
		screenGroupName=contact.getScreenGroupName();
		screenGroupExtId=contact.getScreenGroupExtId();
		securityGroupId=contact.getSecurityGroupId();
		securityGroupName=contact.getSecurityGroupName();
	}
	/**
	 * @return Returns the canLogin.
	 */
	public boolean isCanLogin() {
		return canLogin;
	}
	/**
	 * @param canLogin The canLogin to set.
	 */
	public void setCanLogin(final boolean canLogin) {
		this.canLogin = canLogin;
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
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(final String login) {
		this.login = login;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(final String password) {
		this.password = password;
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
	public boolean isPrimaryIndicator() {
		return primaryIndicator;
	}
	/**
	 * @param primaryIndicator The primaryIndicator to set.
	 */
	public void setPrimaryIndicator(final boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}
	/**
	 * @return Returns the screenGroupId.
	 */
	public String getScreenGroupId() {
		return screenGroupId;
	}
	/**
	 * @param screenGroupId The screenGroupId to set.
	 */
	public void setScreenGroupId(final String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}
	/**
	 * @return Returns the screenGroupName.
	 */
	public String getScreenGroupName() {
		return screenGroupName;
	}
	/**
	 * @param screenGroupName The screenGroupName to set.
	 */
	public void setScreenGroupName(final String screenGroupName) {
		this.screenGroupName = screenGroupName;
	}
	/**
	 * @return Returns the screenGroupExtId.
	 */
	public String getScreenGroupExtId() {
		return screenGroupExtId;
	}
	/**
	 * @param screenGroupExtId The screenGroupExtId to set.
	 */
	public void setScreenGroupExtId(final String screenGroupExtId) {
		this.screenGroupExtId = screenGroupExtId;
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
}

	
