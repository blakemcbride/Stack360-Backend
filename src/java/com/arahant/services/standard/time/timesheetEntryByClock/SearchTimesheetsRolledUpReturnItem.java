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
package com.arahant.services.standard.time.timesheetEntryByClock;
import com.arahant.business.BTimesheet;


/**
 * 
 *
 *
 */
public class SearchTimesheetsRolledUpReturnItem implements Comparable<SearchTimesheetsRolledUpReturnItem> {
	
	public SearchTimesheetsRolledUpReturnItem()
	{

	}

	SearchTimesheetsRolledUpReturnItem (BTimesheet bc, int d)
	{
		
		date=d;
		elapsedTimeFormatted=bc.getElapsedTimeFormatted(d);
		elapsedTime=bc.getElapsedTime(d);
		

	}
	
	private int date;
	private int elapsedTime;
	private String elapsedTimeFormatted;
	

	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public String getElapsedTimeFormatted()
	{
		return elapsedTimeFormatted;
	}
	public void setElapsedTimeFormatted(String elapsedTimeFormatted)
	{
		this.elapsedTimeFormatted=elapsedTimeFormatted;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public int compareTo(SearchTimesheetsRolledUpReturnItem o) {
		return date-o.date;
	}

}

	
