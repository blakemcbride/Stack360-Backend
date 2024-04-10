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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitInputBase;

public class SaveDemographicsInput extends TransmitInputBase {

	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(type = "date", required = false)
	private int dob;
	@Validation(table = "person", column = "personal_email", required = false, min = 5)  //TODO: temporary for RLG
	private String email;
	@Validation(table = "person", column = "fname", required = true)
	private String firstName;
	@Validation(table = "person", column = "lname", required = true)
	private String lastName;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String homePhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mobilePhone;
	@Validation(table = "person", column = "mname", required = false)
	private String middleName;
	@Validation(table = "employee", column = "sex", required = false)
	private String sex;
	@Validation(table = "employee", column = "ssn", required = false)
	private String ssn;
	@Validation(table = "address", column = "state", required = false)
	private String state;
	@Validation(table = "address", column = "street", required = false)
	private String street1;
	@Validation(table = "address", column = "street2", required = false)
	private String street2;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String workPhone;
	@Validation(table = "address", column = "zip", required = false)
	private String zip;
	@Validation(required = false)
	private String employeeId;

	void setData(BPerson bc) {
		bc.setCityPending(city);
		bc.setDob(dob);
		bc.setPersonalEmail(email);
		bc.setFirstName(firstName);
		bc.setLastName(lastName);
		bc.setHomePhonePending(homePhone);
		bc.setMiddleName(middleName);
		bc.setSex(sex);
		bc.setSsn(ssn);
		bc.setStatePending(state);
		bc.setStreetPending(street1);
		bc.setStreet2Pending(street2);
		bc.setWorkPhonePending(workPhone);
		bc.setZipPending(zip);
		bc.setMobilePhonePending(mobilePhone);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
}
