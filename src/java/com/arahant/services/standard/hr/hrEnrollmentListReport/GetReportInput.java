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
package com.arahant.services.standard.hr.hrEnrollmentListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (table="hr_benefit_category",column="benefit_cat_id",required=true)
	private String benefitCategoryId;
	@Validation (table="hr_benefit",column="benefit_id",required=false)
	private String benefitId;
	@Validation (table="hr_employee_status",column="status_id",required=false)
	private String statusId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (type="date",required=true)
	private int enrolledAsOf;

	public String getBenefitCategoryId()
	{
		return benefitCategoryId;
	}
	public void setBenefitCategoryId(final String benefitCategoryId)
	{
		this.benefitCategoryId=benefitCategoryId;
	}
	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(final String statusId)
	{
		this.statusId=statusId;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(final String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public int getEnrolledAsOf()
	{
		return enrolledAsOf;
	}
	public void setEnrolledAsOf(final int enrolledAsOf)
	{
		this.enrolledAsOf=enrolledAsOf;
	}
}

	
