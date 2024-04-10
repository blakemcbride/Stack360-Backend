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
package com.arahant.services.standard.hr.benefitsUsingCOBRAReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Jan 11, 2008
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int policyStartDateFrom;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int policyStartDateTo;
	@Validation (required=false)
	private boolean includeCoverageDetail;
	@Validation (required=false)
	private boolean limitToNoPolicyEndDate;

	public int getPolicyStartDateFrom()
	{
		return policyStartDateFrom;
	}
	public void setPolicyStartDateFrom(int policyStartDateFrom)
	{
		this.policyStartDateFrom=policyStartDateFrom;
	}
	public int getPolicyStartDateTo()
	{
		return policyStartDateTo;
	}
	public void setPolicyStartDateTo(int policyStartDateTo)
	{
		this.policyStartDateTo=policyStartDateTo;
	}
	public boolean getIncludeCoverageDetail()
	{
		return includeCoverageDetail;
	}
	public void setIncludeCoverageDetail(boolean includeCoverageDetail)
	{
		this.includeCoverageDetail=includeCoverageDetail;
	}
	public boolean getLimitToNoPolicyEndDate()
	{
		return limitToNoPolicyEndDate;
	}
	public void setLimitToNoPolicyEndDate(boolean limitToNoPolicyEndDate)
	{
		this.limitToNoPolicyEndDate=limitToNoPolicyEndDate;
	}
}

	
