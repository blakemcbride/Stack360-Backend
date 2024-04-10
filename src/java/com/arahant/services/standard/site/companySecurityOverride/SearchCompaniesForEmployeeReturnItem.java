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
 *
 */
package com.arahant.services.standard.site.companySecurityOverride;
import com.arahant.business.BCompany;
import com.arahant.business.BProphetLoginOverride;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BSecurityGroup;

public class SearchCompaniesForEmployeeReturnItem {
	
	public SearchCompaniesForEmployeeReturnItem()
	{
		
	}
 
	SearchCompaniesForEmployeeReturnItem (BProphetLoginOverride bpl)
	{
		companyName=new BCompany(bpl.getCompanyId()).getName();
		companyId=bpl.getCompanyId();

		securityGroupName=new BSecurityGroup(bpl.getSecurityGroupId()).getName();
		securityGroupId=bpl.getSecurityGroupId();

		screenGroupName=new BScreenGroup(bpl.getScreenGroupId()).getName();
		screenGroupId=bpl.getScreenGroupId();

		loginExceptionId=bpl.getLoginExceptionId();
	}
	
	private String companyName;
	private String companyId;
	private String securityGroupName;
	private String securityGroupId;
	private String screenGroupName;
	private String screenGroupId;
	private String loginExceptionId;
	

	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName=companyName;
	}
	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
	}
	public String getSecurityGroupName()
	{
		return securityGroupName;
	}
	public void setSecurityGroupName(String securityGroupName)
	{
		this.securityGroupName=securityGroupName;
	}
	public String getSecurityGroupId()
	{
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId)
	{
		this.securityGroupId=securityGroupId;
	}
	public String getScreenGroupName()
	{
		return screenGroupName;
	}
	public void setScreenGroupName(String screenGroupName)
	{
		this.screenGroupName=screenGroupName;
	}
	public String getScreenGroupId()
	{
		return screenGroupId;
	}
	public void setScreenGroupId(String screenGroupId)
	{
		this.screenGroupId=screenGroupId;
	}

	public String getLoginExceptionId() {
		return loginExceptionId;
	}

	public void setLoginExceptionId(String loginExceptionId) {
		this.loginExceptionId = loginExceptionId;
	}
}

	
