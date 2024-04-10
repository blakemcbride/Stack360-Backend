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
package com.arahant.services.standard.project.projectRoute;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRoute;


/**
 * 
 *
 *
 */
public class ListCompanyAndOrgGroupsForRouteReturnItem {
	
	public ListCompanyAndOrgGroupsForRouteReturnItem()
	{
		;
	}

	ListCompanyAndOrgGroupsForRouteReturnItem (BOrgGroup bc)
	{
		
		nameFormatted=bc.getNameFormatted();
		orgGroupId=bc.getOrgGroupId();
		companyName=bc.getCompanyName();
		orgGroupName=bc.getName();
		companyId=bc.getCompanyId();
		
		if (companyId.equals(orgGroupId))
			orgGroupId="";
		
		if (orgGroupName.equals(companyName))
			orgGroupName="";

	}
	
	private String nameFormatted;
	private String orgGroupId;
	private String companyName;
	private String orgGroupName;
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	
	
	

	public String getNameFormatted()
	{
		return nameFormatted;
	}
	public void setNameFormatted(String nameFormatted)
	{
		this.nameFormatted=nameFormatted;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName=companyName;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}

}

	
