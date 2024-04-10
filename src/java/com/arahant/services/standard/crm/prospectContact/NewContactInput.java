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
package com.arahant.services.standard.crm.prospectContact;

import com.arahant.annotation.Validation;
import com.arahant.business.BProspectContact;
import com.arahant.services.TransmitInputBase;

public class NewContactInput extends TransmitInputBase {

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
	@Validation(table = "person", column = "lname", required = true)
	private String lastName;
	@Validation(table = "person", column = "fname", required = true)
	private String firstName;
	@Validation(min = 1, max = 16, required = true)
	private String orgGroupId;
	@Validation(required = false)
	private NewContactInputItem[] item;
	@Validation(min = 1, max = 5, required = true)
	private int type;
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
	private String linkedIn;

	void setData(BProspectContact bc) {
		bc.setWorkPhone(workPhone);
		bc.setWorkFax(workFax);
		bc.setPersonalEmail(personalEmail);
		bc.setJobTitle(jobTitle);
		bc.setLastName(lastName);
		bc.setFirstName(firstName);
		bc.setProspectType(type);
		bc.setStreet(street);
		bc.setStreet2(street2);
		bc.setCity(city);
		bc.setState(state);
		bc.setZip(zip);
		bc.setCountry(country);
		bc.setHomePhone(homePhone);
		bc.setMobilePhone(mobilePhone);
		bc.setLinkedin(linkedIn);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public boolean getPrimaryIndicator() {
		return primaryIndicator;
	}

	public void setPrimaryIndicator(boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getWorkFax() {
		return workFax;
	}

	public void setWorkFax(String workFax) {
		this.workFax = workFax;
	}

	public NewContactInputItem[] getItem() {
		if (item == null)
			item = new NewContactInputItem[0];
		return item;
	}

	public void setItem(NewContactInputItem[] item) {
		this.item = item;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getLinkedIn() {
		return linkedIn;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}
}
