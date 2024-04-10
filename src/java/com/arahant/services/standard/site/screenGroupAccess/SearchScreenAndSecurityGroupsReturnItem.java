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
package com.arahant.services.standard.site.screenGroupAccess;
import com.arahant.business.BPerson;

public class SearchScreenAndSecurityGroupsReturnItem {
	
	public SearchScreenAndSecurityGroupsReturnItem()
	{
		
	}

	SearchScreenAndSecurityGroupsReturnItem (BPerson bp)
	{
		fName=bp.getFirstName();
		lName=bp.getLastName();
		securityGroupName=bp.getSecurityGroupName();
		screenGroupName=bp.getScreenGroupName();
		signInDate=BPerson.getSignInDate(bp.getPersonId());
		personId=bp.getPersonId();
	}
	
	private String fName;
	private String lName;
	private String securityGroupName;
	private String screenGroupName;
	private String signInDate;
	private String personId;
	
	public String getFName()
	{
		return fName;
	}
	public void setFName(String fName)
	{
		this.fName=fName;
	}
	public String getLName()
	{
		return lName;
	}
	public void setLName(String lName)
	{
		this.lName=lName;
	}
	public String getSecurityGroupName()
	{
		return securityGroupName;
	}
	public void setSecurityGroupName(String securityGroupName)
	{
		this.securityGroupName=securityGroupName;
	}
	public String getScreenGroupName()
	{
		return screenGroupName;
	}
	public void setScreenGroupName(String screenGroupName)
	{
		this.screenGroupName=screenGroupName;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getSignInDate() {
		return signInDate;
	}

	public void setSignInDate(String signInDate) {
		this.signInDate = signInDate;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}

	
