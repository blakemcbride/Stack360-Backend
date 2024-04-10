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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.business.BHREmplDependent;
import com.arahant.services.TransmitInputBase;

public class SaveDependentInput extends TransmitInputBase {

	@Validation(required = true)
	private String firstName;
	@Validation(required = true)
	private String lastName;
	@Validation(required = false)
	private String middleName;
	@Validation(required = false)
	private String sex;
	@Validation(type = "date", required = false)
	private int dob;
	@Validation(required = false)
	private boolean handicap;
	@Validation(required = true)
	private String relationshipType;
	@Validation(max = 11, min = 0, required = false)
	private String ssn;
	@Validation(required = false)
	private boolean student;
	@Validation(min = 1, max = 16, required = true)
	private String id;
	@Validation(required = false)
	private String other;
	@Validation(type = "date", required = false)
	private int inactiveDate;

	void setData(BHREmplDependent bc) {
		bc.setFirstName(firstName);
		bc.setLastName(lastName);
		bc.setMiddleName(middleName);
		bc.setSex(sex);
		bc.setDob(dob);
		bc.setHandicap(handicap);
		bc.setSsn(ssn);
		bc.setStudent(student);
		bc.setRelationshipType(relationshipType);
		if (relationshipType.equals("O"))
			bc.setRelationship(other);

		bc.setDateInactive(inactiveDate);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	public boolean getHandicap() {
		return handicap;
	}

	public void setHandicap(boolean handicap) {
		this.handicap = handicap;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public boolean getStudent() {
		return student;
	}

	public void setStudent(boolean student) {
		this.student = student;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
}
