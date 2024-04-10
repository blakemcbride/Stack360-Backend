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
import com.arahant.utils.Formatting;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "phone")
@Where(clause = "record_type='R'")
public class Phone extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "phone";
	public static final String RECORD_TYPE = "recordType";
	// Fields    
	private String phoneId;
	public static final String PHONEID = "phoneId";
	private OrgGroup orgGroup;
	public static final String ORGGROUP = "orgGroup";
	private Person person;
	public static final String PERSON = "person";
	private String phoneNumber;
	public static final String PHONENUMBER = "phoneNumber";
	private int phoneType;
	public static final String PHONETYPE = "phoneType";
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
	 * default constructor
	 */
	public Phone() {
	}

	// Property accessors
	@Id
	@Column(name = "phone_id")
	public String getPhoneId() {
		return this.phoneId;
	}

	public void setPhoneId(final String phoneId) {
		this.phoneId = phoneId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_join")
	public OrgGroup getOrgGroup() {
		return this.orgGroup;
	}

	public void setOrgGroup(final OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_join")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "phone_number")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = Formatting.formatPhoneNumber(phoneNumber);
	}

	@Column(name = "phone_type")
	public int getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(final int phoneType) {
		this.phoneType = phoneType;
	}

	@Override
	public String keyColumn() {
		return "phone_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setPhoneId(IDGenerator.generate(this));
		return phoneId;
	}

	@Override
	public boolean equals(Object o) {
		if (phoneId == null && o == null)
			return true;
		if (phoneId != null && o instanceof Phone)
			return phoneId.equals(((Phone) o).getPhoneId());

		return false;
	}

	@Override
	public int hashCode() {
		if (phoneId == null)
			return 0;
		return phoneId.hashCode();
	}
}
