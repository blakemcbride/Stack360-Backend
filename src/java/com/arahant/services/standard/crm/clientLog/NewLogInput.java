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
package com.arahant.services.standard.crm.clientLog;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProspectLog;
import com.arahant.business.BSalesActivity;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewLogInput extends TransmitInputBase {

	void setData(BProspectLog bc)
	{
		bc.setOrgGroupId(id);
		bc.setContactDate(contactDate);
		bc.setContactTime(contactTime);
		bc.setContactText(contactText);
		bc.setEmployees(employees);
		bc.setProspectEmployees(prospectEmployees);
	}
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (table="prospect_log",column="contact_date",type="date",required=true)
	private int contactDate;
	@Validation (table="prospect_log",column="contact_time",type="time",required=true)
	private int contactTime;
	@Validation (table="prospect_log",column="contact_txt",required=true)
	private String contactText;
	@Validation (table="prospect_log",column="employees",required=false)
	private String employees;
	@Validation (table="prospect_log",column="prospect_employees",required=false)
	private String prospectEmployees;


	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getContactDate()
	{
		return contactDate;
	}
	public void setContactDate(int contactDate)
	{
		this.contactDate=contactDate;
	}
	public int getContactTime()
	{
		return contactTime;
	}
	public void setContactTime(int contactTime)
	{
		this.contactTime=contactTime;
	}
	public String getContactText()
	{
		return contactText;
	}
	public void setContactText(String contactText)
	{
		this.contactText=contactText;
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
}

	
