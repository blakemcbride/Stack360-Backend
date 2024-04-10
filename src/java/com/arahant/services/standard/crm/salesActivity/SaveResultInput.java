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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BSalesActivityResult;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveResultInput extends TransmitInputBase {

	void setData(BSalesActivityResult bc)
	{
		bc.setDescription(description);
		bc.setLastActiveDate(lastActiveDate);
		bc.setFirstFollowUpDays((short)firstDays);
		bc.setSecondFollowUpDays((short)secondDays);
		bc.setThirdFollowUpDays((short)thirdDays);
		bc.setFirstFollowUpTask(firstTask);
		bc.setSecondFollowUpTask(secondTask);
		bc.setThirdFollowUpTask(thirdTask);
	}
	
	@Validation (min=0,max=60,required=true)
	private String description;
	@Validation (type="date",required=false)
	private int lastActiveDate;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (min=0,required=true)
	private int firstDays;
	@Validation (min=0,required=false)
	private int secondDays;
	@Validation (min=0,required=false)
	private int thirdDays;
	@Validation (required=false)
	private String firstTask;
	@Validation (required=false)
	private String secondTask;
	@Validation (required=false)
	private String thirdTask;
	

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

	public String getFirstTask() {
		return firstTask;
	}

	public void setFirstTask(String firstTask) {
		this.firstTask = firstTask;
	}

	public int getSecondDays() {
		return secondDays;
	}

	public void setSecondDays(int secondDays) {
		this.secondDays = secondDays;
	}

	public String getSecondTask() {
		return secondTask;
	}

	public void setSecondTask(String secondTask) {
		this.secondTask = secondTask;
	}

	public int getThirdDays() {
		return thirdDays;
	}

	public void setThirdDays(int thirdDays) {
		this.thirdDays = thirdDays;
	}

	public String getThirdTask() {
		return thirdTask;
	}

	public void setThirdTask(String thirdTask) {
		this.thirdTask = thirdTask;
	}
}

	
