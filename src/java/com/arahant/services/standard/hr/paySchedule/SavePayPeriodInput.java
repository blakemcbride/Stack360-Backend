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
package com.arahant.services.standard.hr.paySchedule;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPaySchedulePeriod;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SavePayPeriodInput extends TransmitInputBase {

	void setData(BPaySchedulePeriod bc)
	{
		
		bc.setEndDate(lastDate);
		bc.setPayDate(payDate);
		bc.setBeginningOfYear(beginningOfYear);
	}
	
	@Validation (required=true)
	private String id;
	@Validation (type="date",required=true)
	private int lastDate;
	@Validation (type="date",required=true)
	private int payDate;
	@Validation (required=false)
	private boolean beginningOfYear;

	public boolean isBeginningOfYear() {
		return beginningOfYear;
	}

	public void setBeginningOfYear(boolean beginningOfYear) {
		this.beginningOfYear = beginningOfYear;
	}
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getLastDate()
	{
		return lastDate;
	}
	public void setLastDate(int lastDate)
	{
		this.lastDate=lastDate;
	}
	public int getPayDate()
	{
		return payDate;
	}
	public void setPayDate(int payDate)
	{
		this.payDate=payDate;
	}

}

	
