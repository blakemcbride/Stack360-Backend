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
import com.arahant.business.BContactQuestionDetail;
import com.arahant.business.BProspectContact;
import com.arahant.services.TransmitInputBase;

public class SaveContactInput extends TransmitInputBase {

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
	private String lastName;
	@Validation(table = "person", column = "fname", required = true)
	private String firstName;
	@Validation(min = 1, max = 16, required = true)
	private String orgGroupId;
	@Validation(required = false)
	private SaveContactInputItem[] item;
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

	public SaveContactInputItem[] getItem() {
		if (item == null)
			item = new SaveContactInputItem[0];
		return item;
	}

	public void setItem(SaveContactInputItem[] item) {
		this.item = item;
	}

	void setData(BProspectContact bc) {
		bc.setFirstName(firstName);
		bc.setLastName(lastName);
		bc.setPersonalEmail(personalEmail);
		bc.setJobTitle(jobTitle);
		bc.setWorkPhone(workPhone);
		bc.setWorkFax(workFax);
		bc.setProspectType(type);

		for (SaveContactInputItem i : getItem())
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public boolean getPrimaryIndicator() {
		return primaryIndicator;
	}

	public void setPrimaryIndicator(boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
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

	public void setWorkFax(String workFax) {
		this.workFax = workFax;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
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
