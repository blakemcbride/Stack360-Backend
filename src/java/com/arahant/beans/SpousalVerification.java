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
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = SpousalVerification.TABLE_NAME)
public class SpousalVerification extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "spousal_insurance_verif";
	public static final String PERSON = "person";
	public static final String PERSON_ID = "personId";
	public static final String YEAR = "year";
	private String spousalVerificationId;
	private Person person;
	private short year;
	private int dateReceived;
	private String personId;

	public SpousalVerification() {
	}

	@Column(name = "date_received")
	public int getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(int dateReceived) {
		this.dateReceived = dateReceived;
	}

	@Column(name = "person_id", updatable = false, insertable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if (person != null)
			this.personId = person.getPersonId();
		else
			this.personId = null;
		this.person = person;
	}

	@Id
	@Column(name = "spousal_ins_verif_id")
	public String getSpousalVerificationId() {
		return spousalVerificationId;
	}

	public void setSpousalVerificationId(String spousalVerificationId) {
		this.spousalVerificationId = spousalVerificationId;
	}

	@Column(name = "verification_year")
	public short getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "spousal_ins_verif_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return spousalVerificationId = IDGenerator.generate(this);
	}
}
