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
import com.arahant.business.BOnboardingConfig;


/**
 * 
 *
 *
 */
public class ListOnboardingConfigsReturnItem {
	
	public ListOnboardingConfigsReturnItem()
	{
		;
	}

	ListOnboardingConfigsReturnItem (BOnboardingConfig bc)
	{
		
		configId=bc.getOnboardingConfigId();
		configName=bc.getOnboardingConfigName();
		allCompanies=(bc.getCompany()==null);

	}
	
	private String configId;
	private String configName;
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public String getConfigId()
	{
		return configId;
	}
	public void setConfigId(String configId)
	{
		this.configId=configId;
	}
	public String getConfigName()
	{
		return configName;
	}
	public void setConfigName(String configName)
	{
		this.configName=configName;
	}

}

	
