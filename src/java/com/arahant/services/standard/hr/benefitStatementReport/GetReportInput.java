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
package com.arahant.services.standard.hr.benefitStatementReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

@Validation (required=false)
	private String statusIdArray[];
@Validation (required=false)
	private boolean includeCredentials;
@Validation (required=false)
	private boolean includeEmployees;
@Validation (required=false)
	private boolean includeDependents;
@Validation (required=false)
	private boolean dependentMustHaveActiveBenefit;
	/** 
	 * @return Returns the includeCredentials.
	 */
	public boolean isIncludeCredentials() {
		return includeCredentials;
	}
	/**
	 * @param includeCredentials The includeCredentials to set.
	 */
	public void setIncludeCredentials(boolean includeCredentials) {
		this.includeCredentials = includeCredentials;
	}
	public String []getStatusIdArray()
	{
            if (statusIdArray==null)
                return new String[0];
		return statusIdArray;
	}
	public void setStatusIdArray(final String statusIdArray[])
	{
		this.statusIdArray=statusIdArray;
	}
	public boolean isDependentMustHaveActiveBenefit() {
		return dependentMustHaveActiveBenefit;
	}
	public void setDependentMustHaveActiveBenefit(
			boolean dependentMustHaveActiveBenefit) {
		this.dependentMustHaveActiveBenefit = dependentMustHaveActiveBenefit;
	}
	public boolean isIncludeDependents() {
		return includeDependents;
	}
	public void setIncludeDependents(boolean includeDependents) {
		this.includeDependents = includeDependents;
	}
	public boolean isIncludeEmployees() {
		return includeEmployees;
	}
	public void setIncludeEmployees(boolean includeEmployees) {
		this.includeEmployees = includeEmployees;
	}


}

	
