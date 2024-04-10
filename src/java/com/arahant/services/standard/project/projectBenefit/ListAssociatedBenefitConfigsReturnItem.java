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
package com.arahant.services.standard.project.projectBenefit;
import com.arahant.business.BHRBenefitConfig;


/**
 * 
 *
 *
 */
public class ListAssociatedBenefitConfigsReturnItem {
	
	public ListAssociatedBenefitConfigsReturnItem()
	{
		;
	}

	ListAssociatedBenefitConfigsReturnItem (BHRBenefitConfig bc)
	{
		
		categoryName=bc.getCategoryName();
		benefitName=bc.getBenefitName();
		configName=bc.getConfigName();
		configId=bc.getConfigId();

	}
	
	private String categoryName;
	private String benefitName;
	private String configName;
	private String configId;

	public String getCategoryName()
	{
		return categoryName;
	}
	public void setCategoryName(String categoryName)
	{
		this.categoryName=categoryName;
	}
	public String getBenefitName()
	{
		return benefitName;
	}
	public void setBenefitName(String benefitName)
	{
		this.benefitName=benefitName;
	}
	public String getConfigName()
	{
		return configName;
	}
	public void setConfigName(String configName)
	{
		this.configName=configName;
	}
	public String getConfigId()
	{
		return configId;
	}
	public void setConfigId(String configId)
	{
		this.configId=configId;
	}

}

	
