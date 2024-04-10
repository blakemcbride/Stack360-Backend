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
package com.arahant.services.standard.project.employeeProjectList;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListAssignedProjectsInput extends TransmitInputBase {

@Validation (required=false)
	private String projectStatusId;;
@Validation (required=false)
	private int dateAssigned;
@Validation (required=false)
	private int dateAssignedSearchType;

	public String getProjectStatusId()
	{
		return projectStatusId;
	}
	public void setProjectStatusId(final String projectStatusId)
	{
		this.projectStatusId=projectStatusId;
	}
	public int getDateAssigned()
	{
		return dateAssigned;
	}
	public void setDateAssigned(final int dateAssigned)
	{
		this.dateAssigned=dateAssigned;
	}
	public int getDateAssignedSearchType()
	{
		return dateAssignedSearchType;
	}
	public void setDateAssignedSearchType(final int dateAssignedSearchType)
	{
		this.dateAssignedSearchType=dateAssignedSearchType;
	}


}

	
