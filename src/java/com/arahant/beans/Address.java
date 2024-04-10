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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;
import org.hibernate.annotations.Where;

/**
 * Address generated by hbm2java
 */
@Entity
@Table(name = "address")
@Where(clause = "record_type='R'")
public class Address extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "address";
	public static final String RECORD_TYPE = "recordType";
	// Fields    
	private String addressId;
	public static final String ADDRESSID = "addressId";
	private OrgGroup orgGroup;
	public static final String ORGGROUP = "orgGroup";
	private Person person;
	public static final String PERSON = "person";
	private String street = "";
	public static final String STREET = "street";
	private String city = "";
	public static final String CITY = "city";
	private String state = "";
	public static final String STATE = "state";
	private String zip = "";
	public static final String ZIP = "zip";
	private int addressType;
	public static final String ADDRESSTYPE = "addressType";
	private String street2 = "";
	public static final String STREET2 = "street2";
	private String country = "US";
	private String personId;
	public static final String COUNTY = "county";
	private String county = "";
	public static final String TIMEZONE_OFFSET = "timeZoneOffset";
	private short timeZoneOffset = 100;
	private char recordType = 'R';

	@Column(name = "record_type")
	public char getRecordType() {
		return recordType;
	}

	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	// Constructors
	/**
	 * @return Returns the street2.
	 */
	@Column(name = "street2")
	public String getStreet2() {
		if (street2 == null)
			return "";
		return street2;
	}

	/**
	 * @param street2 The street2 to set.
	 */
	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	/**
	 * default constructor
	 */
	public Address() {}

	// Property accessors
	@Id
	@Column(name = "address_id")
	public String getAddressId() {
		return this.addressId;
	}

	public void setAddressId(final String addressId) {
		firePropertyChange("addressId", this.addressId, addressId);
		this.addressId = addressId;
	}

	@ManyToOne()
	@JoinColumn(name = "org_group_join")
	public OrgGroup getOrgGroup() {
		return this.orgGroup;
	}

	public void setOrgGroup(final OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne()
	@JoinColumn(name = "person_join")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "street")
	public String getStreet() {
		if (street == null)
			return "";
		return this.street;
	}

	public void setStreet(final String street) {
		firePropertyChange("street", this.street, street);
		this.street = street;
	}

	@Column(name = "city")
	public String getCity() {
		if (city == null)
			return "";
		return this.city;
	}

	public void setCity(final String city) {
		firePropertyChange("city", this.city, city);
		this.city = city;
	}

	@Column(name = "state")
	public String getState() {
		if (state == null)
			return "";
		return this.state;
	}

	public void setState(final String state) {
		firePropertyChange("state", this.state, state);
		this.state = state;
	}

	@Column(name = "zip")
	public String getZip() {
		if (zip == null)
			return "";
		return this.zip;
	}

	public void setZip(final String zip) {
		firePropertyChange("zip", this.zip, zip);
		this.zip = zip;
	}

	@Column(name = "address_type")
	public int getAddressType() {
		return this.addressType;
	}

	public void setAddressType(final int addressType) {
		firePropertyChange("addressType", this.addressType, addressType);
		this.addressType = addressType;
	}

	@Column(name = "person_join", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	@Column(name = "country_code")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country == null)
			return;
		this.country = country;
	}

	@Column(name = "county")
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String keyColumn() {
		return "address_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setAddressId(IDGenerator.generate(this));
		return getAddressId();
	}

	@Override
	public boolean equals(Object o) {
		if (addressId == null && o == null)
			return true;
		if (addressId != null && o instanceof Address)
			return addressId.equals(((Address) o).getAddressId());

		return false;
	}

	@Override
	public int hashCode() {
		if (addressId == null)
			return 0;
		return addressId.hashCode();
	}

	@Override
	public String toString() {
		String addr = street + "\n";
		if (street2 != null && !street2.trim().equals(""))
			addr += street2 + "\n";

		addr += city + ", " + state + "  " + zip;

		return addr;
	}

	@Override
	public String notifyId() {
		return addressId;
	}

	@Override
	public String notifyClassName() {
		return "Address";
	}

	@Column(name = "time_zone_offset")
	public short getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(short timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
}
