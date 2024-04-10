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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class LoadDemographicsReturn extends TransmitReturnBase {

	private String firstName;
	private String middleName;
	private String lastName;
	private String ssn;
	private String sex;
	private int dob;
	private String email;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zip;
	private String homePhone;
	private String workPhone;
	private String orgGroupName;
	private int statusDate;
	private String status;
	private String mobilePhone;
	private boolean smoker;
	private boolean spouseSmoker;
	private String instructions;
	private boolean allowDemographicChanges;

	void setData(final BPerson bc) {
		if (bc.getRecordType() == 'R') {
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			lastName = bc.getLastName();
			ssn = bc.getSsn();
			sex = bc.getSex();
			dob = bc.getDob();
			email = bc.getPersonalEmail();
			street1 = bc.getStreet();
			street2 = bc.getStreet2();
			city = bc.getCity();
			state = bc.getState();
			zip = bc.getZip();
			homePhone = bc.getHomePhone();
			workPhone = bc.getWorkPhoneNumber();
			mobilePhone = bc.getMobilePhone();

			orgGroupName = "";
			statusDate = 0;
			status = "";
			smoker = false;
			spouseSmoker = false;
		} else if (bc.getRecordType() == 'C') {
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			lastName = bc.getLastName();
			ssn = bc.getSsn();
			sex = bc.getSex();
			dob = bc.getDob();
			email = bc.getPersonalEmail();
			street1 = bc.getStreetPending();
			street2 = bc.getStreet2Pending();
			city = bc.getCityPending();
			state = bc.getStatePending();
			zip = bc.getZipPending();
			homePhone = bc.getHomePhonePending();
			workPhone = bc.getWorkPhoneNumberPending();
			mobilePhone = bc.getMobilePhonePending();
 
			orgGroupName = "";
			statusDate = 0;
			status = "";
			smoker = false;
			spouseSmoker = false;
		}
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	private boolean ssnRequired = !BProperty.getBoolean("SSN Not Required");

	public boolean getSsnRequired() {
		return ssnRequired;
	}

	public void setSsnRequired(boolean ssnRequired) {
		this.ssnRequired = ssnRequired;
	}

	public boolean getSpouseSmoker() {
		return spouseSmoker;
	}

	public void setSpouseSmoker(boolean spouseSmoker) {
		this.spouseSmoker = spouseSmoker;
	}

	public boolean getSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(final int dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(final String zip) {
		this.zip = zip;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(final String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(final String workPhone) {
		this.workPhone = workPhone;
	}

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(final String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}

	public int getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(final int startDate) {
		this.statusDate = startDate;
	}

	public boolean isAllowDemographicChanges() {
		return allowDemographicChanges;
	}

	public void setAllowDemographicChanges(boolean allowDemographicChanges) {
		this.allowDemographicChanges = allowDemographicChanges;
	}
	
}
