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
package com.arahant.services.standard.hr.dynamicReportParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class SearchDynamicReportsInput extends TransmitInputBase {

	@Validation (required = false)
	private int reportType;
	@Validation (required = false)
	private String reportName;
	@Validation (min = 2,max = 5,required = false)
	private int reportNameSearchType;
	

	public int getReportType()
	{
		return reportType;
	}
	public void setReportType(int reportType)
	{
		this.reportType = reportType;
	}
	public String getReportName()
	{
		return modifyForSearch(reportName,reportNameSearchType);
	}
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
	public int getReportNameSearchType()
	{
		return reportNameSearchType;
	}
	public void setReportNameSearchType(int reportNameSearchType)
	{
		this.reportNameSearchType = reportNameSearchType;
	}


}

	
