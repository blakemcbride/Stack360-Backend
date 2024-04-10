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
package com.arahant.services.standard.at.applicant;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**  
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (min=1,max=3,required=true)
	private int searchType;
	@Validation (min=0,max=16,required=false)
	private String positionId;
	@Validation (min=0,max=16,required=false)
	private String jobTypeId;
	@Validation (min=0,max=16,table="person",column="lname",required=false)
	private String lastName;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	@Validation (table="person",column="fname",required=false)
	private String firstName;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (min=0,max=16,required=false)
	private String applicationStatus;
	@Validation (min=0,max=16,required=false)
	private String applicationSource;
	@Validation (min=0,max=16,required=false)
	private String applicantStatus;
	@Validation (min=0,max=16,required=false)
	private String applicantSource;
	@Validation (required=false)
	private GetReportInputAnswer []answers;
	

	public int getSearchType()
	{
		return searchType;
	}
	public void setSearchType(int searchType)
	{
		this.searchType=searchType;
	}
	public String getPositionId()
	{
		return positionId;
	}
	public void setPositionId(String positionId)
	{
		this.positionId=positionId;
	}
	public String getJobTypeId()
	{
		return jobTypeId;
	}
	public void setJobTypeId(String jobTypeId)
	{
		this.jobTypeId=jobTypeId;
	}
	public String getLastName()
	{
		return modifyForSearch(lastName,lastNameSearchType);
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public int getLastNameSearchType()
	{
		return lastNameSearchType;
	}
	public void setLastNameSearchType(int lastNameSearchType)
	{
		this.lastNameSearchType=lastNameSearchType;
	}
	public String getFirstName()
	{
		return modifyForSearch(firstName,firstNameSearchType);
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public int getFirstNameSearchType()
	{
		return firstNameSearchType;
	}
	public void setFirstNameSearchType(int firstNameSearchType)
	{
		this.firstNameSearchType=firstNameSearchType;
	}
	public String getApplicationStatus()
	{
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus)
	{
		this.applicationStatus=applicationStatus;
	}
	public String getApplicationSource()
	{
		return applicationSource;
	}
	public void setApplicationSource(String applicationSource)
	{
		this.applicationSource=applicationSource;
	}
	public String getApplicantStatus()
	{
		return applicantStatus;
	}
	public void setApplicantStatus(String applicantStatus)
	{
		this.applicantStatus=applicantStatus;
	}
	public String getApplicantSource()
	{
		return applicantSource;
	}
	public void setApplicantSource(String applicantSource)
	{
		this.applicantSource=applicantSource;
	}



}

	
