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

package com.arahant.services.standard.hr.education;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEducation;

public class LoadEducationReturn extends TransmitReturnBase {

	private String schoolName;
	private String schoolType;
	private String schoolLocation;
	private int enrollmentFromMonth;
	private int enrollmentFromYear;
	private int enrollmentToMonth;
	private int enrollmentToYear;
	private boolean graduate;
	private String subject;
	private Short gpa;
	private Character current;
	private String otherType;

	void setData(BEducation bc) {
		schoolName = bc.getSchoolName();
		schoolType = bc.getSchoolType() + "";
		otherType = bc.getOtherType();
		schoolLocation = bc.getSchoolLocation();
		enrollmentFromMonth = bc.getStartDate() % 100;
		enrollmentFromYear = bc.getStartDate() / 100;
		enrollmentToMonth = bc.getEndDate() % 100;
		enrollmentToYear = bc.getEndDate() / 100;
		graduate = bc.getGraduate() == 'Y';
		subject = bc.getSubject();
		gpa = bc.getGpa();
		current = bc.getCurrent();
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getSchoolLocation() {
		return schoolLocation;
	}

	public void setSchoolLocation(String schoolLocation) {
		this.schoolLocation = schoolLocation;
	}

	public int getEnrollmentFromMonth() {
		return enrollmentFromMonth;
	}

	public void setEnrollmentFromMonth(int enrollmentFromMonth) {
		this.enrollmentFromMonth = enrollmentFromMonth;
	}

	public int getEnrollmentFromYear() {
		return enrollmentFromYear;
	}

	public void setEnrollmentFromYear(int enrollmentFromYear) {
		this.enrollmentFromYear = enrollmentFromYear;
	}

	public int getEnrollmentToMonth() {
		return enrollmentToMonth;
	}

	public void setEnrollmentToMonth(int enrollmentToMonth) {
		this.enrollmentToMonth = enrollmentToMonth;
	}

	public int getEnrollmentToYear() {
		return enrollmentToYear;
	}

	public void setEnrollmentToYear(int enrollmentToYear) {
		this.enrollmentToYear = enrollmentToYear;
	}

	public boolean getGraduate() {
		return graduate;
	}

	public void setGraduate(boolean graduate) {
		this.graduate = graduate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Short getGpa() {
		return gpa;
	}

	public void setGpa(Short gpa) {
		this.gpa = gpa;
	}

	public Character getCurrent() {
		return current;
	}

	public void setCurrent(Character current) {
		this.current = current;
	}

	public String getOtherType() {
        return otherType;
    }

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}
}

	
