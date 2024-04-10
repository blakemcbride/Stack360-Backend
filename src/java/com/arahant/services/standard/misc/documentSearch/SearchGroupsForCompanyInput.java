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
package com.arahant.services.standard.misc.documentSearch;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchGroupsForCompanyInput extends TransmitInputBase {

	@Validation (required=false)
	private String orgGroupName;
	@Validation (min=2,max=5,required=false)
	private int orgGroupNameSearchType;
	@Validation (required=true)
	private String companyId;
	

	public String getOrgGroupName()
	{
		return modifyForSearch(orgGroupName,orgGroupNameSearchType);
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public int getOrgGroupNameSearchType()
	{
		return orgGroupNameSearchType;
	}
	public void setOrgGroupNameSearchType(int orgGroupNameSearchType)
	{
		this.orgGroupNameSearchType=orgGroupNameSearchType;
	}
	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
	}


}

	
