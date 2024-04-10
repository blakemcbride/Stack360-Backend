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
package com.arahant.services.standard.misc.employeeHomePage;
import com.arahant.business.BHRBenefitJoin;

public class ListBenefitCostsForEmployeeReturnItem {
	
	public ListBenefitCostsForEmployeeReturnItem()
	{
		
	}

	ListBenefitCostsForEmployeeReturnItem (BHRBenefitJoin bc)
	{
		benefitConfigName=bc.getBenefitConfig().getBenefitName();
		benefitConfigId=bc.getBenefitConfig().getBenefitConfigId();
		monthlyCost=bc.getCalculatedCostMonthly();
		benefitName = bc.getBenefitName();
		policyStart = bc.getPolicyStartDate();
	}
	
	private int policyStart;
	private String benefitName;
	private String benefitConfigName;
	private String benefitConfigId;
	private String monthlyCost;
	

	public String getBenefitConfigName()
	{
		return benefitConfigName;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public int getPolicyStart() {
		return policyStart;
	}

	public void setPolicyStart(int policyStart) {
		this.policyStart = policyStart;
	}
	public void setBenefitConfigName(String benefitConfigName)
	{
		this.benefitConfigName=benefitConfigName;
	}
	public String getBenefitConfigId()
	{
		return benefitConfigId;
	}
	public void setBenefitConfigId(String benefitConfigId)
	{
		this.benefitConfigId=benefitConfigId;
	}
	public String getMonthlyCost()
	{
		return monthlyCost;
	}
	public void setMonthlyCost(String monthlyCost)
	{
		this.monthlyCost=monthlyCost;
	}

}

	
