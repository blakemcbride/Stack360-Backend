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
package com.arahant.services.standard.misc.onboardingTasks;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BOnboardingConfig;
import com.arahant.annotation.Validation;
import com.arahant.business.BOnboardingTask;
import com.arahant.business.BScreen;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveTaskInput extends TransmitInputBase {

	@Validation(required=true)
	String taskId;
	
	@Validation(required=true)
	String taskName;

	@Validation(required=true)
	String description;

	@Validation(required=true,min=1,max=99)
	int completeByDays;

	@Validation(required=true)
	String screenId;

	void setData(BOnboardingTask bc)
	{
		bc.setCompletedByDays(completeByDays);
		bc.setDescription(description);
		bc.setScreen(new BScreen(screenId).getBean());
		bc.setOnboardingTaskName(taskName);
	}

	public int getCompleteByDays() {
		return completeByDays;
	}

	public void setCompleteByDays(int completeByDays) {
		this.completeByDays = completeByDays;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}

	
