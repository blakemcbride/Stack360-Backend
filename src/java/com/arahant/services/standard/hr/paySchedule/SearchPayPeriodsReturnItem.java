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
import com.arahant.business.BPaySchedulePeriod;


/**
 * 
 *
 *
 */
public class SearchPayPeriodsReturnItem {
	
	public SearchPayPeriodsReturnItem()
	{
		
	}

	SearchPayPeriodsReturnItem (BPaySchedulePeriod bc)
	{
		
		firstDate=bc.getStartDate();
		lastDate=bc.getEndDate();
		payDate=bc.getPayDate();
		id=bc.getId();
		beginningOfYear=bc.getBeginningOfYear();
	}
	
	private int firstDate;
	private int lastDate;
	private int payDate;
	private String id;
	private boolean beginningOfYear;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isBeginningOfYear() {
		return beginningOfYear;
	}

	public void setBeginningOfYear(boolean beginningOfYear) {
		this.beginningOfYear = beginningOfYear;
	}
	
	

	public int getFirstDate()
	{
		return firstDate;
	}
	public void setFirstDate(int firstDate)
	{
		this.firstDate=firstDate;
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

	
