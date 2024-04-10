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
package com.arahant.services.standard.hr.benefitHistory;
import com.arahant.business.BHRBenefitJoinH;


/**
 * 
 *
 *
 */
public class ListEmployeeBenefitHistoryReturnItem {
	
	public ListEmployeeBenefitHistoryReturnItem()
	{
		;
	}

	ListEmployeeBenefitHistoryReturnItem (BHRBenefitJoinH bc)
	{
		
		historyId=bc.getHistoryId();
		dateTimeFormatted=bc.getDateTimeFormatted();
		changeType=bc.getChangeTypeFormatted();
		configName=bc.getConfigName();
		benefitName=bc.getBenefitName();
		policyStartDate=bc.getPolicyStartDate();
		policyEndDate=bc.getPolicyEndDate();
		coverageStartDate=bc.getCoverageStartDate();
		coverageEndDate=bc.getCoverageEndDate();

	}
	
	private String historyId;
	private String dateTimeFormatted;
	private String changeType;
	private String configName;
	private String benefitName;
	private int policyStartDate;
	private int policyEndDate;
	private int coverageStartDate;
	private int coverageEndDate;

	public String getHistoryId()
	{
		return historyId;
	}
	public void setHistoryId(String historyId)
	{
		this.historyId=historyId;
	}
	public String getDateTimeFormatted()
	{
		return dateTimeFormatted;
	}
	public void setDateTimeFormatted(String dateTimeFormatted)
	{
		this.dateTimeFormatted=dateTimeFormatted;
	}
	public String getChangeType()
	{
		return changeType;
	}
	public void setChangeType(String changeType)
	{
		this.changeType=changeType;
	}
	public String getConfigName()
	{
		return configName;
	}
	public void setConfigName(String configName)
	{
		this.configName=configName;
	}
	public String getBenefitName()
	{
		return benefitName;
	}
	public void setBenefitName(String benefitName)
	{
		this.benefitName=benefitName;
	}
	public int getPolicyStartDate()
	{
		return policyStartDate;
	}
	public void setPolicyStartDate(int policyStartDate)
	{
		this.policyStartDate=policyStartDate;
	}
	public int getPolicyEndDate()
	{
		return policyEndDate;
	}
	public void setPolicyEndDate(int policyEndDate)
	{
		this.policyEndDate=policyEndDate;
	}
	public int getCoverageStartDate()
	{
		return coverageStartDate;
	}
	public void setCoverageStartDate(int coverageStartDate)
	{
		this.coverageStartDate=coverageStartDate;
	}
	public int getCoverageEndDate()
	{
		return coverageEndDate;
	}
	public void setCoverageEndDate(int coverageEndDate)
	{
		this.coverageEndDate=coverageEndDate;
	}

}

	
