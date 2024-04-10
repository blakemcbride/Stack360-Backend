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
@Table(name = "student_verification")
public class StudentVerification extends ArahantBean implements Serializable {

	public static final String PERSON = "person";
	public static final String YEAR = "schoolYear";
	public static final String TERM = "calendarPeriod";
	public static final String PERSON_ID = "personId";
	public static final short PERIOD_FALL = 1;
	public static final short PERIOD_WINTER = 2;
	public static final short PERIOD_SPRING = 3;
	public static final short PERIOD_SUMMER = 4;
	private String studentVerificationId;
	private Person person;
	private short schoolYear;
	private short calendarPeriod;
	private String personId;

	public StudentVerification() {
	}

	@Column(name = "calendar_period")
	public short getCalendarPeriod() {
		return calendarPeriod;
	}

	public void setCalendarPeriod(short calendarPeriod) {
		this.calendarPeriod = calendarPeriod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "school_year")
	public short getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(short schoolYear) {
		this.schoolYear = schoolYear;
	}

	@Id
	@Column(name = "student_verification_id")
	public String getStudentVerificationId() {
		return studentVerificationId;
	}

	public void setStudentVerificationId(String studentVerificationId) {
		this.studentVerificationId = studentVerificationId;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public String tableName() {
		return "student_verification";
	}

	@Override
	public String keyColumn() {
		return "student_verification_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return studentVerificationId = IDGenerator.generate(this);
	}
}
