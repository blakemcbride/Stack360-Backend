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
package com.arahant.services.standard.site.loginListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (type="date",required=false)
	private int loginAttemptDateFrom;
	@Validation (type="date",required=false)
	private int loginAttemptDateTo;
	@Validation (required=false)
	private boolean includeLoginAttemptStatus;
	@Validation (required=false)
	private boolean includeLoginName;
	@Validation (required=false)
	private boolean includeName;
	@Validation (required=false)
	private boolean showFailedLoginAttempts;
	@Validation (required=false)
	private boolean showSuccessfulLoginAttempts;
	@Validation (required=false)
	private int sortType;
	@Validation (required=false)
	private boolean sortAsc;

	public int getLoginAttemptDateFrom()
	{
		return loginAttemptDateFrom;
	}
	public void setLoginAttemptDateFrom(int loginAttemptDateFrom)
	{
		this.loginAttemptDateFrom=loginAttemptDateFrom;
	}
	public int getLoginAttemptDateTo()
	{
		return loginAttemptDateTo;
	}
	public void setLoginAttemptDateTo(int loginAttemptDateTo)
	{
		this.loginAttemptDateTo=loginAttemptDateTo;
	}
	public boolean getIncludeLoginAttemptStatus()
	{
		return includeLoginAttemptStatus;
	}
	public void setIncludeLoginAttemptStatus(boolean includeLoginAttemptStatus)
	{
		this.includeLoginAttemptStatus=includeLoginAttemptStatus;
	}
	public boolean getIncludeLoginName()
	{
		return includeLoginName;
	}
	public void setIncludeLoginName(boolean includeLoginName)
	{
		this.includeLoginName=includeLoginName;
	}
	public boolean getIncludeName()
	{
		return includeName;
	}
	public void setIncludeName(boolean includeName)
	{
		this.includeName=includeName;
	}
	public boolean getShowFailedLoginAttempts()
	{
		return showFailedLoginAttempts;
	}
	public void setShowFailedLoginAttempts(boolean showFailedLoginAttempts)
	{
		this.showFailedLoginAttempts=showFailedLoginAttempts;
	}
	public boolean getShowSuccessfulLoginAttempts()
	{
		return showSuccessfulLoginAttempts;
	}
	public void setShowSuccessfulLoginAttempts(boolean showSuccessfulLoginAttempts)
	{
		this.showSuccessfulLoginAttempts=showSuccessfulLoginAttempts;
	}
	public int getSortType()
	{
		return sortType;
	}
	public void setSortType(int sortType)
	{
		this.sortType=sortType;
	}
	public boolean getSortAsc()
	{
		return sortAsc;
	}
	public void setSortAsc(boolean sortAsc)
	{
		this.sortAsc=sortAsc;
	}


}

	
