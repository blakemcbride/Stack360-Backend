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
package com.arahant.services.standard.hr.education;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BEducation;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;

public class NewEducationInput extends TransmitInputBase {

	void setData(BEducation bc)
	{
		bc.setPerson(new BPerson(personId).getPerson());
		bc.setSchoolName(schoolName);

		if (schoolType.length() > 1)
		{
			bc.setSchoolType('O');
			bc.setOtherType(schoolType);
		}
		else
			bc.setSchoolType(schoolType.charAt(0));
			
		bc.setSchoolLocation(schoolLocation);
		bc.setStartDate((enrollmentFromYear * 100) + enrollmentFromMonth);
		bc.setEndDate((enrollmentToYear * 100) + enrollmentToMonth);
		bc.setGraduate(graduate?'Y':'N');
		bc.setSubject(subject);
	}
	
	@Validation (table="education", column="person_id", required=true)
	private String personId;
	@Validation (table="education", column="school_name", required=true)
	private String schoolName;
	@Validation (table="education", column="school_type", required=true)
	private String schoolType;
	@Validation (table="education", column="school_location", required=true)
	private String schoolLocation;
	@Validation (required=true)
	private int enrollmentFromMonth;
	@Validation (required=true)
	private int enrollmentFromYear;
	@Validation (required=true)
	private int enrollmentToMonth;
	@Validation (required=true)
	private int enrollmentToYear;
	@Validation (required=false)
	private boolean graduate;
	@Validation (table="education", column="subject", required=false)
	private String subject;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getSchoolName()
	{
		return schoolName;
	}
	public void setSchoolName(String schoolName)
	{
		this.schoolName=schoolName;
	}
	public String getSchoolType()
	{
		return schoolType;
	}
	public void setSchoolType(String schoolType)
	{
		this.schoolType=schoolType;
	}
	public String getSchoolLocation()
	{
		return schoolLocation;
	}
	public void setSchoolLocation(String schoolLocation)
	{
		this.schoolLocation=schoolLocation;
	}
	public int getEnrollmentFromMonth()
	{
		return enrollmentFromMonth;
	}
	public void setEnrollmentFromMonth(int enrollmentFromMonth)
	{
		this.enrollmentFromMonth=enrollmentFromMonth;
	}
	public int getEnrollmentFromYear()
	{
		return enrollmentFromYear;
	}
	public void setEnrollmentFromYear(int enrollmentFromYear)
	{
		this.enrollmentFromYear=enrollmentFromYear;
	}
	public int getEnrollmentToMonth()
	{
		return enrollmentToMonth;
	}
	public void setEnrollmentToMonth(int enrollmentToMonth)
	{
		this.enrollmentToMonth=enrollmentToMonth;
	}
	public int getEnrollmentToYear()
	{
		return enrollmentToYear;
	}
	public void setEnrollmentToYear(int enrollmentToYear)
	{
		this.enrollmentToYear=enrollmentToYear;
	}
	public boolean getGraduate()
	{
		return graduate;
	}
	public void setGraduate(boolean graduate)
	{
		this.graduate=graduate;
	}
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject=subject;
	}
}

	
