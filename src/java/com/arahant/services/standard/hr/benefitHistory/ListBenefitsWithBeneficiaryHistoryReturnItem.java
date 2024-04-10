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
import com.arahant.business.BHRBenefit;


/**
 * 
 *
 *
 */
public class ListBenefitsWithBeneficiaryHistoryReturnItem {
	
	public ListBenefitsWithBeneficiaryHistoryReturnItem()
	{
		;
	}

	ListBenefitsWithBeneficiaryHistoryReturnItem (BHRBenefit bc)
	{
		
		benefitId=bc.getBenefitId();
		benefitName=bc.getName();

	}
	
	private String benefitId;
	private String benefitName;

	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(String benefitId)
	{
		this.benefitId=benefitId;
	}
	public String getBenefitName()
	{
		return benefitName;
	}
	public void setBenefitName(String benefitName)
	{
		this.benefitName=benefitName;
	}

}

	
