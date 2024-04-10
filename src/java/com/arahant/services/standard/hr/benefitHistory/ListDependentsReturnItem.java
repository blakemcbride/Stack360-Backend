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
package com.arahant.services.standard.hr.benefitHistory;
import com.arahant.business.BHREmplDependent;


/**
 * 
 *
 *
 */
public class ListDependentsReturnItem {
	
	public ListDependentsReturnItem()
	{
		;
	}

	ListDependentsReturnItem (BHREmplDependent bc)
	{
		
		dependentId=bc.getDependentId();
		dependentName=bc.getDependentNameLFM();
		ssn=bc.getSsn();

	}
	
	private String dependentId;
	private String dependentName;
	private String ssn;

	public String getDependentId()
	{
		return dependentId;
	}
	public void setDependentId(String dependentId)
	{
		this.dependentId=dependentId;
	}
	public String getDependentName()
	{
		return dependentName;
	}
	public void setDependentName(String dependentName)
	{
		this.dependentName=dependentName;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(String ssn)
	{
		this.ssn=ssn;
	}

}

	
