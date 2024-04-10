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

import com.arahant.business.BClientContact;
import com.arahant.services.TransmitReturnBase;

public class LoadClientContactReturn extends TransmitReturnBase   {

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
	private short contactType;

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getSecurityGroupName() {
		return securityGroupName;
	}

	public void setSecurityGroupName(final String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}

	public LoadClientContactReturn(final BClientContact contact) {
		super();
		workPhone = contact.getWorkPhoneNumber();
		workFax = contact.getWorkFaxNumber();
		primaryIndicator = contact.isPrimary();
		personalEmail = contact.getPersonalEmail();
		jobTitle = contact.getJobTitle();
		personId = contact.getPersonId();
		lname = contact.getLastName();
		fname = contact.getFirstName();
		login = contact.getUserLogin();
		password = contact.getUserPassword();
		canLogin = contact.getCanLogin() == 'Y';
		screenGroupId = contact.getScreenGroupId();
		screenGroupName = contact.getScreenGroupName();
		screenGroupExtId = contact.getScreenGroupExtId();
		securityGroupId = contact.getSecurityGroupId();
		securityGroupName = contact.getSecurityGroupName();
		contactType = contact.getContactType();
	}

	public LoadClientContactReturn() {
		super();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public boolean getPrimaryIndicator() {
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

	public String getScreenGroupName() {
		return screenGroupName;
	}

	public void setScreenGroupName(final String screenGroupName) {
		this.screenGroupName = screenGroupName;
	}

	public String getScreenGroupExtId() {
		return screenGroupExtId;
	}

	public void setScreenGroupExtId(final String screenGroupExtId) {
		this.screenGroupExtId = screenGroupExtId;
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

	public short getContactType() {
		return contactType;
	}

	public void setContactType(short contactType) {
		this.contactType = contactType;
	}
}

	
