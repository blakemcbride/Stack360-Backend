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
import com.arahant.business.BOnboardingTask;


/**
 * 
 *
 *
 */
public class OnboardingScreen {
	
	public OnboardingScreen()
	{
		
	}

	OnboardingScreen (BOnboardingTask bc)
	{		
		screenName="";//bc.getOnboardingTaskName();
		screenId="";//bc.getScreen().getScreenId();
	}
	
	private String screenName;
	private String screenId;

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getScreenId()
	{
		return screenId;
	}
	public void setScreenId(String screenId)
	{
		this.screenId=screenId;
	}

}

	
