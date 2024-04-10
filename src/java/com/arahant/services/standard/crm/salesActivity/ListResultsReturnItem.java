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
package com.arahant.services.standard.crm.salesActivity;
import com.arahant.business.BSalesActivityResult;


/**
 * 
 *
 *
 */
public class ListResultsReturnItem {
	
	public ListResultsReturnItem()
	{
		
	}

	ListResultsReturnItem (BSalesActivityResult bc)
	{
		description=bc.getDescription();
		lastActiveDate=bc.getLastActiveDate();
		id=bc.getSalesActivityResultId();
		firstDays=bc.getFirstFollowUpDays();
		secondDays=bc.getSecondFollowUpDays();
		thirdDays=bc.getThirdFollowUpDays();
	}
	
	private String description;
	private int lastActiveDate;
	private String id;
	private int firstDays;
	private int secondDays;
	private int thirdDays;

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public int getFirstDays() {
		return firstDays;
	}

	public void setFirstDays(int firstDays) {
		this.firstDays = firstDays;
	}

	public int getSecondDays() {
		return secondDays;
	}

	public void setSecondDays(int secondDays) {
		this.secondDays = secondDays;
	}

	public int getThirdDays() {
		return thirdDays;
	}

	public void setThirdDays(int thirdDays) {
		this.thirdDays = thirdDays;
	}
}

	
