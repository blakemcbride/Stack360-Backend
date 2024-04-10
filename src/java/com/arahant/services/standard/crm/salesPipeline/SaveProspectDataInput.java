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
package com.arahant.services.standard.crm.salesPipeline;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectLog;
import com.arahant.business.BSalesActivity;
import com.arahant.business.BSalesActivityResult;

public class SaveProspectDataInput extends TransmitInputBase {

	void setData(BProspectCompany bc)
	{
		bc.setStatusId(statusId);

		if (!isEmpty(contactText))
		{
			//make log
			BProspectLog pl=new BProspectLog();
			pl.create();
			pl.setOrgGroupId(bc.getId());
			pl.setContactDate(contactDate);
			pl.setContactTime(contactTime);
			pl.setContactText(contactText);
			pl.setEmployees(employees);
			pl.setProspectEmployees(prospectEmployees);
			pl.setSalesActivity(new BSalesActivity(activityId).getBean());
			pl.setEmployee(BPerson.getCurrent().getBEmployee().getEmployee());
			pl.setSalesActivityResult(new BSalesActivityResult(resultId).getBean());
			pl.insert();
		}
	}

	@Validation (required=true)
	private String prospectId;
	@Validation (required=true)
	private String statusId;
	@Validation (table="prospect_log",column="contact_date",type="date",required=false)
	private int contactDate;
	@Validation (table="prospect_log",column="contact_txt",required=false)
	private String contactText;
	@Validation (table="prospect_log",column="contact_time",type="time",required=false)
	private int contactTime;
	@Validation (table="prospect_log",column="employees",required=false)
	private String employees;
	@Validation (table="prospect_log",column="prospect_employees",required=false)
	private String prospectEmployees;
	@Validation (table="prospect_log",column="sales_activity_id",required=false)
	private String activityId;
	@Validation (table="prospect_log",column="sales_activity__result_id",required=false)
	private String resultId;
	

	public String getProspectId()
	{
		return prospectId;
	}
	public void setProspectId(String prospectId)
	{
		this.prospectId=prospectId;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(String statusId)
	{
		this.statusId=statusId;
	}
	public int getContactDate()
	{
		return contactDate;
	}
	public void setContactDate(int contactDate)
	{
		this.contactDate=contactDate;
	}
	public String getContactText()
	{
		return contactText;
	}
	public void setContactText(String contactText)
	{
		this.contactText=contactText;
	}
	public int getContactTime()
	{
		return contactTime;
	}
	public void setContactTime(int contactTime)
	{
		this.contactTime=contactTime;
	}
	public String getEmployees()
	{
		return employees;
	}
	public void setEmployees(String employees)
	{
		this.employees=employees;
	}
	public String getProspectEmployees()
	{
		return prospectEmployees;
	}
	public void setProspectEmployees(String prospectEmployees)
	{
		this.prospectEmployees=prospectEmployees;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
}

	
