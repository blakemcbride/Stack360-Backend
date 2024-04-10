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

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;

public class LoadPersonBasicReturn extends TransmitReturnBase {

	private String middleName;
	private String lname;
	private String fname;
	private String nickName;
	private String sex;
	private String ssn;
	private String extRef;
	private int dob;
	private String personalEmail;
	private String workerType;

	public LoadPersonBasicReturn() {
	}

	public void setData(BPerson person) {
		lname = person.getLastName();
		fname = person.getFirstName();
		personalEmail = person.getPersonalEmail();
		ssn = person.getSsn();
		sex = person.getSex();
		dob = person.getDob();
		middleName = person.getMiddleName();
		nickName = person.getNickName();

		if (person.isEmployee()) {
			BEmployee emp = new BEmployee(person);
			workerType = emp.getEmploymentType() + "";
			extRef = emp.getExtRef();
		} else {
			extRef = "(n/a)";
			workerType = " ";
		}

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
