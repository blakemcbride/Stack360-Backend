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
package com.arahant.services.standard.crm.clientContact;

import com.arahant.annotation.Validation;
import com.arahant.business.BClientContact;
import com.arahant.business.BContactQuestionDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class SaveClientContactInput extends TransmitInputBase {

	@Validation(table = "phone", column = "phone_number", required = false)
	private String workPhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String workFax;
	@Validation(required = false)
	private boolean primaryIndicator;
	@Validation(table = "person", column = "personal_email", required = false)
	private String personalEmail;
	@Validation(table = "person", column = "job_title", required = false)
	private String jobTitle;
	@Validation(min = 1, max = 16, required = true)
	private String personId;
	@Validation(table = "person", column = "lname", required = true)
	private String lname;
	@Validation(table = "person", column = "fname", required = true)
	private String fname;
	@Validation(table = "prophet_login", column = "user_login", required = false)
	private String login;
	@Validation(table = "prophet_login", column = "user_password", required = false)
	private String contactPassword;
	@Validation(required = false)
	private boolean canLogin;
	@Validation(min = 1, max = 16, required = false)
	private String screenGroupId;
	@Validation(min = 1, max = 16, required = true)
	private String orgGroupId;
	@Validation(min = 1, max = 16, required = false)
	private String securityGroupId;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(table = "address", column = "country_code", required = false)
	private String country;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String homePhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mobilePhone;
	@Validation(table = "address", column = "state", required = false)
	private String state;
	@Validation(table = "address", column = "zip", required = false)
	private String zip;
	@Validation(table = "address", column = "street", required = false)
	private String street;
	@Validation(table = "address", column = "street2", required = false)
	private String street2;
	@Validation(required = false)
	private SaveClientContactInputItem[] item;

	public SaveClientContactInputItem[] getItem() {
		if (item == null)
			item = new SaveClientContactInputItem[0];
		return item;
	}

	public void setItem(SaveClientContactInputItem[] item) {
		this.item = item;
	}

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
	 * @param bcc
	 * @throws ArahantException
	 */
	void makeClientContact(final BClientContact bcc) throws ArahantException {
		bcc.setWorkPhone(workPhone);
		bcc.setWorkFax(workFax);

		bcc.setPersonalEmail(personalEmail);
		bcc.setJobTitle(jobTitle);
		bcc.setPersonId(personId);
		bcc.setLastName(lname);
		bcc.setFirstName(fname);
		bcc.setUserLogin(login);
		bcc.setUserPassword(contactPassword, canLogin);

		bcc.setScreenGroupId(screenGroupId);
		bcc.setSecurityGroupId(securityGroupId);

		for (SaveClientContactInputItem i : getItem())
			if (isEmpty(i.getDetailId())) {
				if (!isEmpty(i.getResponse())) {
					BContactQuestionDetail qd = new BContactQuestionDetail();
					qd.create();
					qd.setPersonId(personId);
					qd.setResponse(i.getResponse());
					qd.setQuestionId(i.getQuestionId());
					qd.insert();
				}
			} else {
				BContactQuestionDetail qd = new BContactQuestionDetail(i.getDetailId());
				if (isEmpty(i.getResponse()))
					qd.delete();
				else {
					qd.setResponse(i.getResponse());
					qd.update();
				}
			}
		bcc.setStreet(street);
		bcc.setStreet2(street2);
		bcc.setCity(city);
		bcc.setState(state);
		bcc.setZip(zip);
		bcc.setCountry(country);
		bcc.setHomePhone(homePhone);
		bcc.setMobilePhone(mobilePhone);
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

	/**
	 * @return Returns the contactPassword.
	 */
	public String getContactPassword() {
		return contactPassword;
	}

	/**
	 * @param contactPassword The contactPassword to set.
	 */
	public void setContactPassword(final String contactPassword) {
		this.contactPassword = contactPassword;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
