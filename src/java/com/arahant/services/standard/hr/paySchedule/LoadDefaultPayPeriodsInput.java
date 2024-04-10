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
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class LoadDefaultPayPeriodsInput extends TransmitInputBase {

	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (type="date",required=true)
	private int fromDate;
	@Validation (type="date",required=true)
	private int toDate;
	@Validation (required=true, min=1, max=31)
	private int periodStart;
	@Validation (required=false, min=1, max=31)
	private int periodStart2;
	@Validation (min=1, max=4)
	private int periodType;
	@Validation (required=false)
	private boolean setFirstPayPeriodAsBeginning;


	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public boolean getSetFirstPayPeriodAsBeginning() {
		return setFirstPayPeriodAsBeginning;
	}

	public void setSetFirstPayPeriodAsBeginning(boolean setFirstPayPeriodAsBeginning) {
		this.setFirstPayPeriodAsBeginning = setFirstPayPeriodAsBeginning;
	}
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getToDate()
	{
		return toDate;
	}
	public void setToDate(int toDate)
	{
		this.toDate=toDate;
	}
	public int getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(int fromDate)
	{
		this.fromDate=fromDate;
	}
	public int getPeriodStart()
	{
		return periodStart;
	}
	public void setPeriodStart(int periodStart)
	{
		this.periodStart=periodStart;
	}
	public int getPeriodStart2()
	{
		return periodStart2;
	}
	public void setPeriodStart2(int periodStart2)
	{
		this.periodStart2=periodStart2;
	}


}

	
