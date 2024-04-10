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
package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;

public class LoadEmployeeStatusReturn extends TransmitReturnBase {

	void setData(BEmployee be)
	{
		currentStatus=be.getStatus();
		name=be.getNameFML();
		changeDate=DateUtils.now();

		BHRBenefitJoin joins[]=be.getApprovedBenefitJoinsNonDeclines(changeDate);
		benefit = new Benefit[joins.length];
		int loop = 0;
		for (BHRBenefitJoin hrb :joins)
		{
			benefit[loop]= new Benefit(hrb);
			loop++;
		}

	}
	
	private String currentStatus;
	private String name;
	private int changeDate;
	private Benefit[] benefit;
	

	public String getCurrentStatus()
	{
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus)
	{
		this.currentStatus=currentStatus;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}

	public Benefit[] getBenefit() {
		return benefit;
	}

	public void setBenefit(Benefit[] benefit) {
		this.benefit = benefit;
	}

	public int getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}

}

	
