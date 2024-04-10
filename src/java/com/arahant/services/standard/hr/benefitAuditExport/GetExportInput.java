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
package com.arahant.services.standard.hr.benefitAuditExport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class GetExportInput extends TransmitInputBase {

	@Validation (required = false)
	private String[] categoryIds;
	@Validation (required = false)
	private String[] benefitIds;
	@Validation (required = false)
	private String[] configIds;
	@Validation (required = true)
	private String employeeStatus;
	@Validation (required = true, type = "date")
	private int asOfDate;
	

	public String[] getCategoryIds()
	{
		if (categoryIds == null)
			categoryIds = new String[0];
		return categoryIds;
	}
	public void setCategoryIds(String [] categoryIds)
	{
		this.categoryIds = categoryIds;
	}
	public String[] getBenefitIds()
	{
		if (benefitIds == null)
			benefitIds = new String[0];
		return benefitIds;
	}
	public void setBenefitIds(String [] benefitIds)
	{
		this.benefitIds = benefitIds;
	}
	public String[] getConfigIds()
	{
		if (configIds == null)
			configIds = new String[0];
		return configIds;
	}
	public void setConfigIds(String [] configIds)
	{
		this.configIds = configIds;
	}
	public String getEmployeeStatus()
	{
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus)
	{
		this.employeeStatus = employeeStatus;
	}
	public int getAsOfDate()
	{
		return asOfDate;
	}
	public void setAsOfDate(int asOfDate)
	{
		this.asOfDate = asOfDate;
	}


}

	
