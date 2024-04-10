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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectLog;


/**
 * 
 *
 *
 */
public class LoadLogReturn extends TransmitReturnBase {

	void setData(BProspectLog bc)
	{
		contactText=bc.getContactText();
		contactText=bc.getContactText();
		employees=bc.getEmployees();
		prospectEmployees=bc.getProspectEmployees();

	}
	
	private String contactText;
	private String employees;
	private String prospectEmployees;


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

	
