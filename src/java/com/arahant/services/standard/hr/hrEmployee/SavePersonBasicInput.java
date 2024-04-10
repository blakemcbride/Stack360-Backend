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
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class SavePersonBasicInput extends TransmitInputBase {

	@Validation(required = true)
	private String personId;
	@Validation(table = "person", column = "lname", required = true)
	private String lname;
	@Validation(table = "person", column = "fname", required = true)
	private String fname;
	@Validation(table = "person", column = "mname", required = false)
	private String middleName;
	@Validation(table = "person", column = "nickname", required = false)
	private String nickName;
	@Validation(table = "employee", column = "sex", required = false)
	private String sex;
	@Validation(table = "employee", column = "employment_type", required = false)
	private String workerType;
	@Validation(table = "employee", column = "ssn", required = false)
	private String ssn;
	@Validation(type = "date", required = false)
	private int dob;
	@Validation(table = "employee", column = "ext_ref", required = false)
	private String extRef;
	@Validation(table = "person", column = "personal_email", required = false)
	private String personalEmail;

	void makeEmployee(final BEmployee emp) throws ArahantException {
		emp.setPersonalEmail(personalEmail);
		emp.setLastName(lname);
		emp.setFirstName(fname);
		emp.setSsn(ssn != null && !ssn.isEmpty() ? ssn : null);
		emp.setSex(sex);
		emp.setDob(dob);
		emp.setExtRef(extRef);
		emp.setMiddleName(middleName);
		emp.setNickName(nickName);
		emp.setEmploymentType(workerType.charAt(0));
	}

	void makePerson(BPerson per) throws ArahantException {
		per.setPersonalEmail(personalEmail);
		per.setLastName(lname);
		per.setFirstName(fname);
		per.setSsn(ssn);
		per.setSex(sex);
		per.setDob(dob);
		per.setMiddleName(middleName);
		per.setNickName(nickName);
	}

	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	public String getExtRef() {
		return extRef;
	}

	public void setExtRef(String extRef) {
		this.extRef = extRef;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getWorkerType() {
		return workerType;
	}

	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}
	
}
