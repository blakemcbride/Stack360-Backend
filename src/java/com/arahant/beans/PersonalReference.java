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
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = PersonalReference.TABLE_NAME)
public class PersonalReference extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "personal_reference";
	public static final String REFERENCE_ID = "referenceId";
	public static final String PERSON = "person";
	public static final String REFERENCE_NAME = "referenceName";
	public static final String RELATIONSHIP_TYPE = "relationshipType";
	

	private String referenceId;
	private Person person;
	private String referenceName;
	private char relationshipType;
	private String relationshipOther;
	private String company;
	private String phone;
	private String address;
	private short yearsKnown;

	@Id
	@Column(name="reference_id")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Column(name="address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name="reference_name")
	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	@Column(name="relationship_other")
	public String getRelationshipOther() {
		return relationshipOther;
	}

	public void setRelationshipOther(String relationshipOther) {
		this.relationshipOther = relationshipOther;
	}

	@Column(name="relationship_type")
	public char getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(char relationshipType) {
		this.relationshipType = relationshipType;
	}

	@Column(name="years_known")
	public short getYearsKnown() {
		return yearsKnown;
	}

	public void setYearsKnown(short yearsKnown) {
		this.yearsKnown = yearsKnown;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "reference_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return referenceId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PersonalReference other = (PersonalReference) obj;
		if ((this.referenceId == null) ? (other.referenceId != null) : !this.referenceId.equals(other.referenceId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + (this.referenceId != null ? this.referenceId.hashCode() : 0);
		return hash;
	}

}
