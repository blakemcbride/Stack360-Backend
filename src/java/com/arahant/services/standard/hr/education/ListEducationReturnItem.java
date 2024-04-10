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
import com.arahant.business.BEducation;

public class ListEducationReturnItem {
	
	public ListEducationReturnItem()
	{
		
	}

	ListEducationReturnItem (BEducation bc)
	{
		enrollmentPeriod=getDateFormatted(bc.getStartDate()) + " - " + getDateFormatted(bc.getEndDate());
		schoolType=bc.getSchoolType()!='O'?bc.getSchoolType() + "":bc.getOtherType();
		schoolName=bc.getSchoolName();
		educationId=bc.getEducationId();
		graduate=bc.getGraduate() + "";
	}
	
	private String enrollmentPeriod;
	private String schoolType;
	private String schoolName;
	private String educationId;
	private String graduate;
	

	public String getEnrollmentPeriod()
	{
		return enrollmentPeriod;
	}
	public void setEnrollmentPeriod(String enrollmentPeriod)
	{
		this.enrollmentPeriod=enrollmentPeriod;
	}
	public String getSchoolType()
	{
		return schoolType;
	}
	public void setSchoolType(String schoolType)
	{
		this.schoolType=schoolType;
	}
	public String getSchoolName()
	{
		return schoolName;
	}
	public void setSchoolName(String schoolName)
	{
		this.schoolName=schoolName;
	}
	public String getEducationId()
	{
		return educationId;
	}
	public void setEducationId(String educationId)
	{
		this.educationId=educationId;
	}
	public String getGraduate()
	{
		return graduate;
	}
	public void setGraduate(String graduate)
	{
		this.graduate=graduate;
	}

	public String getDateFormatted(int date)
	{
		String dateFormatted = "";

		int year = date / 100;
		int month = date % 100;

		switch (month)
		{
			case 1: dateFormatted += "January";
					break;
			case 2: dateFormatted += "February";
					break;
			case 3: dateFormatted += "March";
					break;
			case 4: dateFormatted += "April";
					break;
			case 5: dateFormatted += "May";
					break;
			case 6: dateFormatted += "June";
					break;
			case 7: dateFormatted += "July";
					break;
			case 8: dateFormatted += "August";
					break;
			case 9: dateFormatted += "September";
					break;
			case 10: dateFormatted += "October";
					break;
			case 11: dateFormatted += "November";
					break;
			case 12: dateFormatted += "December";
					break;
		}

		return dateFormatted + " " + year;
	}

}

	
