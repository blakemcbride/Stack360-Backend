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

/**
 *
 * Arahant
 */
// exclude mainOps
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.Date;
//import javax.persistence.*;

//@Entity
//@Table(name=SystemAccessRecord.TABLE_NAME)
public class SystemAccessRecord extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "system_access_records";
	public static final String PERSON_ID = "personId";
	public static final String ENTRY_DATE = "entryDate";
	public static final String WEBMETHOD_NAME = "webmethodName";
	public static final String CLASS_NAME = "className";
	private String accessRecordId;
	private Person person;
	private String personId;
	private Date entryDate;
	private String webmethodName;
	private String className;

	public SystemAccessRecord() {
	}

	//@Override
	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	//@Id
	//@Column (name="access_record_id")
	public String getAccessRecordId() {
		return accessRecordId;
	}

	public void setAccessRecordId(String accessRecordId) {
		this.accessRecordId = accessRecordId;
	}

	//@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn (name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	//@Column (name="person_id",insertable=false,updatable=false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	//@Column(name="entry_date")
	//@Temporal(TemporalType.TIMESTAMP)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(final Date entryDate) {
		this.entryDate = entryDate;
	}

	//@Column (name="webmethod_name")
	public String getWebmethodName() {
		return webmethodName;
	}

	public void setWebmethodName(String webmethodName) {
		this.webmethodName = webmethodName;
	}

	//@Column (name="class_name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	//@Override
	@Override
	public String keyColumn() {
		return "access_record_id";
	}

	//@Override
	@Override
	public String generateId() throws ArahantException {
		setAccessRecordId(IDGenerator.generate(this));
		return getAccessRecordId();
	}
}
