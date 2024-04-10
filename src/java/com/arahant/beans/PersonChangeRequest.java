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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table(name = PersonChangeRequest.TABLE_NAME)
public class PersonChangeRequest extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "person_change_request";
	public static final String PERSON = "person";
	public static final String REQUEST_TYPE = "requestType";
	public static final String REQUEST_DATE = "requestDate";
	public static final short TYPE_CIGNA = 1;
	public static final short TYPE_METLIFE = 2;
	public static final short TYPE_MUTUAL_OF_OMAHA = 3;
	public static final short TYPE_VSP = 4;
	public static final short TYPE_GENERICWIZARD = 5;
	public static final short TYPE_PERSONAL_DATA_CHANGE = 6;
	public static final short TYPE_DEPENDENT_DATA_CHANGE = 7;
	public static final short TYPE_DEPENDENT_DATA_ADD = 8;
	private static final long serialVersionUID = 1L;
	private String requestId;
	private Person person;
	private short requestType;
	private Date requestDate;
	private String requestData;

	public PersonChangeRequest() {
	}

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "request_data")
	public String getRequestData() {

		if (requestData == null)
			return "";

		requestData = requestData.replaceAll("&lt;", "<");
		requestData = requestData.replaceAll("&gt;", ">");
		requestData = requestData.replaceAll("&", "&amp;");

		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	@Column(name = "request_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Id
	@Column(name = "request_id")
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Column(name = "request_type")
	public short getRequestType() {
		return requestType;
	}

	public void setRequestType(short requestType) {
		this.requestType = requestType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PersonChangeRequest other = (PersonChangeRequest) obj;
		if (this.requestId == null  &&  other.requestId == null)
			return true;
		if (this.requestId == null  &&  other.requestId != null  ||
				this.requestId != null  &&  other.requestId == null)
			return false;
		return this.requestId.equals(other.requestId);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 13 * hash + (this.requestId != null ? this.requestId.hashCode() : 0);
		return hash;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "request_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return requestId = IDGenerator.generate(this);
	}
}
