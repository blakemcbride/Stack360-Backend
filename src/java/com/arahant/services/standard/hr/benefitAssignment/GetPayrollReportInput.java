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
package com.arahant.services.standard.hr.benefitAssignment;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**  
 * 
 *
 * Created on Feb 6, 2008
 *
 */
public class GetPayrollReportInput extends TransmitInputBase {
	@Validation (required=false)
	private String personId;
	@Validation (min=19000101,max=30000101,type="date",required=true)
	private int reportDate;
	@Validation (required=false)
	private boolean showAsDependent;

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public int getReportDate()
	{
		return reportDate;
	}
	public void setReportDate(int reportDate)
	{
		this.reportDate=reportDate;
	}
	public boolean getShowAsDependent()
	{
		return showAsDependent;
	}
	public void setShowAsDependent(boolean showAsDependent)
	{
		this.showAsDependent=showAsDependent;
	}


}

	
