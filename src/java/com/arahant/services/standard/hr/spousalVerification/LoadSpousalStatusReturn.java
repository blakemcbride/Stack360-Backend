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
package com.arahant.services.standard.hr.spousalVerification;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPerson;
import com.arahant.business.BSpousalVerification;


/**
 * 
 *
 *
 */
public class LoadSpousalStatusReturn extends TransmitReturnBase {

	void setData(BPerson bc)
	{
		
		currentSpouseOfFirstName=bc.getCurrentSpouseOfFirstName();
		currentSpouseOfLastName=bc.getCurrentSpouseOfLastName();
		currentSpouseOfSSN=bc.getCurrentSpouseOfSSN();
		employee=bc.isEmployee();

		BSpousalVerification[] v=bc.getSpouseVerifications();

		item=new LoadSpousalStatusReturnItem[v.length];
		for (int loop=0;loop<v.length;loop++)
			item[loop]=new LoadSpousalStatusReturnItem(v[loop]);

	}
	
	private String currentSpouseOfFirstName;
	private String currentSpouseOfLastName;
	private String currentSpouseOfSSN;
	private boolean employee;
	private LoadSpousalStatusReturnItem []item;

	public LoadSpousalStatusReturnItem[] getItem() {
		if (item==null)
			item=new LoadSpousalStatusReturnItem[0];
		return item;
	}

	public void setItem(LoadSpousalStatusReturnItem[] item) {
		this.item = item;
	}


	

	public String getCurrentSpouseOfFirstName()
	{
		return currentSpouseOfFirstName;
	}
	public void setCurrentSpouseOfFirstName(String currentSpouseOfFirstName)
	{
		this.currentSpouseOfFirstName=currentSpouseOfFirstName;
	}
	public String getCurrentSpouseOfLastName()
	{
		return currentSpouseOfLastName;
	}
	public void setCurrentSpouseOfLastName(String currentSpouseOfLastName)
	{
		this.currentSpouseOfLastName=currentSpouseOfLastName;
	}
	public String getCurrentSpouseOfSSN()
	{
		return currentSpouseOfSSN;
	}
	public void setCurrentSpouseOfSSN(String currentSpouseOfSSN)
	{
		this.currentSpouseOfSSN=currentSpouseOfSSN;
	}
	public boolean getEmployee()
	{
		return employee;
	}
	public void setEmployee(boolean employee)
	{
		this.employee=employee;
	}

}

	
