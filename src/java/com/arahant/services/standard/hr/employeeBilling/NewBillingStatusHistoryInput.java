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
package com.arahant.services.standard.hr.employeeBilling;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BBillingStatusHistory;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewBillingStatusHistoryInput extends TransmitInputBase {

	void setData(BBillingStatusHistory bc)
	{
		
		bc.setPersonId(personId);
		bc.setStartDate(startDate);
		bc.setFinalDate(finalDate);
		bc.setBillingStatusId(billingStatusId);

	}
	
	@Validation (required=true)
	private String personId;
	@Validation (type="date",required=true)
	private int startDate;
	@Validation (type="date",required=false)
	private int finalDate;
	@Validation (required=true)
	private String billingStatusId;

	public String getBillingStatusId() {
		return billingStatusId;
	}

	public void setBillingStatusId(String billingStatusId) {
		this.billingStatusId = billingStatusId;
	}
	
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}

}

	
