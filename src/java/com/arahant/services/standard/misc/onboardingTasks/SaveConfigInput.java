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
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveConfigInput extends TransmitInputBase {

	void setData(BOnboardingConfig bc)
	{
		bc.setOnboardingConfigName(configName);
		if(!allCompanies)
		{
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
		}
		else
		{
			bc.setCompany(null);
		}

	}
	
	@Validation (min=1,max=16,required=true)
	private String configId;
	@Validation(required=true)
	String configName;
	@Validation(required=false)
	boolean allCompanies;

	public String getConfigId()
	{
		return configId;
	}
	public void setConfigId(String id)
	{
		this.configId=id;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}



}

	
