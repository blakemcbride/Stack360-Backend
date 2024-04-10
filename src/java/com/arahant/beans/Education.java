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
@Table(name = Education.TABLE_NAME)
public class Education extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "education";
	public static final String EDUCATION_ID = "educationId";
	public static final String PERSON = "person";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String GRADUATE = "graduate";

	private String educationId;
	private Person person;
	private int startDate;
	private int endDate;
	private char schoolType;
	private String schoolName;
	private String schoolLocation;
	private char graduate;
	private String subject;
	private String otherType;
	private Short gpa;
	private Character current;

	@Id
	@Column(name="education_id")
	public String getEducationId() {
		return educationId;
	}

	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}

	@Column(name="end_date")
	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	@Column(name="graduate")
	public char getGraduate() {
		return graduate;
	}

	public void setGraduate(char graduate) {
		this.graduate = graduate;
	}

	@Column(name="other_type")
	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name="school_location")
	public String getSchoolLocation() {
		return schoolLocation;
	}

	public void setSchoolLocation(String schoolLocation) {
		this.schoolLocation = schoolLocation;
	}

	@Column(name="school_name")
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Column(name="school_type")
	public char getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(char schoolType) {
		this.schoolType = schoolType;
	}

	@Column(name="start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@Column(name="subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name="gpa")
	public Short getGpa() {
		return gpa;
	}

	public void setGpa(Short gpa) {
		this.gpa = gpa;
	}

	@Column(name="current")
	public Character getCurrent() {
		return current;
	}

	public void setCurrent(Character current) {
		this.current = current;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "education_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return educationId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Education other = (Education) obj;
		if ((this.educationId == null) ? (other.educationId != null) : !this.educationId.equals(other.educationId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (this.educationId != null ? this.educationId.hashCode() : 0);
		return hash;
	}

}
