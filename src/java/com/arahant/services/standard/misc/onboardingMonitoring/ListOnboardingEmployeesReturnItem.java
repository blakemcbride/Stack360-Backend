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
package com.arahant.services.standard.misc.onboardingMonitoring;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.business.BPerson;
import java.util.Date;

public class ListOnboardingEmployeesReturnItem {
	
	public ListOnboardingEmployeesReturnItem()
	{
		
	}

	ListOnboardingEmployeesReturnItem (BHREmplStatusHistory bc)
	{
		personName=bc.getEmployee().getNameLFM();
		loginDate=BPerson.getSignIn(bc.getEmployee().getPersonId());
		lastOnline=BPerson.getSignInDateAndTime(bc.getEmployee().getPersonId()).equals("")?"Never":BPerson.getSignInDateAndTime(bc.getEmployee().getPersonId());
		personId=bc.getEmployee().getPersonId();
	}
	
	private String personName;
	private String lastOnline;
	private String personId;
	private Date loginDate;  //Only used for sorting Frontend does not need
	
	public String getPersonName()
	{
		return personName;
	}
	public void setPersonName(String personName)
	{
		this.personName=personName;
	}
	public String getLastOnline()
	{
		return lastOnline;
	}
	public void setLastOnline(String lastOnline)
	{
		this.lastOnline=lastOnline;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}

	//Only used for sorting Frontend does not need
	public Date LoginDate() {
		return loginDate;
	}

	//Only used for sorting Frontend does not need
	public void LoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
}

	
