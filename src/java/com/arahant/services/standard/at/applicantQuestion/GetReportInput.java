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
package com.arahant.services.standard.at.applicantQuestion;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	
	@Validation (table="job_type",column="job_type_id",required=false)
	private String jobTypeId;
	@Validation (min=0,max=2,table="job_type",column="job_type_id",required=false)
	private int activeType;
	

	public String getJobTypeId()
	{
		return jobTypeId;
	}
	public void setJobTypeId(String jobTypeId)
	{
		this.jobTypeId=jobTypeId;
	}
	public int getActiveType()
	{
		return activeType;
	}
	public void setActiveType(int activeType)
	{
		this.activeType=activeType;
	}



}

	
