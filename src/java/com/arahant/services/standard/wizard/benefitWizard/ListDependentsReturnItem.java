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


package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.HrEmplDependentWizard;
import com.arahant.business.BHREmplDependent;
import com.arahant.utils.DateUtils;


public class ListDependentsReturnItem {

	private boolean isEmployee;
	private String personId;
	private String lastName;
	private String firstName;
	private String middleName;
	private String relationship;
	private String sex;
	private String ssn;
	private String dob;
	private String flags;
	private String id;
	private int inactiveDate;

	public ListDependentsReturnItem() {
	}

	ListDependentsReturnItem(final HrEmplDependentWizard ew) {
		if (ew.getRecordType() == 'C') {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			personId = bc.getPersonId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();
			dob = DateUtils.getDateFormatted(bc.getDob());
			flags = "";
			if (bc.getStudent())
				flags += "S";
			if (bc.getHandicap())
				flags += "D";

			isEmployee = bc.getIsEmployee();
			id = bc.getDependentId();
			inactiveDate = bc.getInactiveDate();
		} else {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			personId = bc.getPersonId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();
			dob = DateUtils.getDateFormatted(bc.getDob());
			flags = "";
			if (bc.getStudent())
				flags += "S";
			if (bc.getHandicap())
				flags += "D";

			isEmployee = bc.getIsEmployee();
			id = bc.getDependentId();
			inactiveDate = ew.getDateInactive();
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(final String dob) {
		this.dob = dob;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(final String flags) {
		this.flags = flags;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	/**
	 * @return Returns the isEmployee.
	 */
	public boolean isEmployee() {
		return isEmployee;
	}

	/**
	 * @param isEmployee The isEmployee to set.
	 */
	public void setEmployee(final boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
}
