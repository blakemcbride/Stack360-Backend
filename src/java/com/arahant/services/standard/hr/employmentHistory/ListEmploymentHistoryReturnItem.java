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
package com.arahant.services.standard.hr.employmentHistory;
import com.arahant.business.BPreviousEmployment;

public class ListEmploymentHistoryReturnItem {
	
	public ListEmploymentHistoryReturnItem()
	{
		
	}

	ListEmploymentHistoryReturnItem (BPreviousEmployment bc)
	{
		workPeriod=getDateFormatted(bc.getStartDate()) + " - " + getDateFormatted(bc.getEndDate());
		companyName=bc.getCompany();
		employmentId=bc.getEmploymentId();
		jobTitle=bc.getJobTitle();
	}
	
	private String workPeriod;
	private String companyName;
	private String employmentId;
	private String jobTitle;
	

	public String getWorkPeriod()
	{
		return workPeriod;
	}
	public void setWorkPeriod(String workPeriod)
	{
		this.workPeriod=workPeriod;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName=companyName;
	}
	public String getEmploymentId()
	{
		return employmentId;
	}
	public void setEmploymentId(String employmentId)
	{
		this.employmentId=employmentId;
	}
	public String getJobTitle()
	{
		return jobTitle;
	}
	public void setJobTitle(String jobTitle)
	{
		this.jobTitle=jobTitle;
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

	
