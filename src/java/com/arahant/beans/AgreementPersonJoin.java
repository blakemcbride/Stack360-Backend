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
import java.util.Date;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "agreement_person_join")
public class AgreementPersonJoin extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "agreement_person_join";
	public static final String PERSON = "person";
	public static final String FORM = "form";
	private String agreementPersonJoinId;
	private AgreementForm form;
	private Person person;
	private Date agreementTime;

	public AgreementPersonJoin() { }
	
	@Column(name = "agreement_person_join_id")
	@Id
	public String getAgreementPersonJoinId() {
		return agreementPersonJoinId;
	}

	public void setAgreementPersonJoinId(String agreementPersonJoinId) {
		this.agreementPersonJoinId = agreementPersonJoinId;
	}

	@Column(name = "agreement_time")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getAgreementTime() {
		return agreementTime;
	}

	public void setAgreementTime(Date agreementTime) {
		this.agreementTime = agreementTime;
	}

	@ManyToOne
	@JoinColumn(name = "agreement_form_id")
	public AgreementForm getForm() {
		return form;
	}

	public void setForm(AgreementForm form) {
		this.form = form;
	}

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "agreement_person_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return agreementPersonJoinId = IDGenerator.generate(this);
	}
}
