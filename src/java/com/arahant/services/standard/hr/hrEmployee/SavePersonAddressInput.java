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
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class SavePersonAddressInput extends TransmitInputBase {

	@Validation(required = true)
    private String personId;

	@Validation(table = "phone", column = "phone_number", required = false)
    private String homePhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String mobilePhone;
    @Validation(table = "address", column = "street", required = false)
    private String addressLine1;
    @Validation(table = "address", column = "street2", required = false)
    private String addressLine2;
    @Validation(table = "address", column = "city", required = false)
    private String city;
    @Validation(table = "address", column = "country_code", required = false)
    private String country;
    @Validation(table = "address", column = "county", required = false)
    private String county;
    @Validation(table = "address", column = "state", required = false)
    private String stateProvince;
    @Validation(table = "address", column = "zip", required = false)
    private String zipPostalCode;
	@Validation(table = "phone", column = "phone_number", required = false)
    private String workPhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String workFax;

	void makeEmployee(final BEmployee emp) throws ArahantException
	{
		emp.setWorkPhone(workPhone);
        emp.setWorkFax(workFax);
		emp.setHomePhone(homePhone);
        emp.setMobilePhone(mobilePhone);
        emp.setStreet(addressLine1);
        emp.setCity(city);
        emp.setState(stateProvince);
        emp.setZip(zipPostalCode);
        emp.setCountry(country);
        emp.setCounty(county);
		emp.setStreet2(addressLine2);

	}

	void makePerson(BPerson per) throws ArahantException
	{
		per.setWorkPhone(workPhone);
        per.setWorkFax(workFax);
		per.setHomePhone(homePhone);
        per.setMobilePhone(mobilePhone);
        per.setStreet(addressLine1);
        per.setCity(city);
        per.setState(stateProvince);
        per.setZip(zipPostalCode);
        per.setCountry(country);
        per.setCounty(county);
        per.setStreet2(addressLine2);
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
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

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}

}

	
