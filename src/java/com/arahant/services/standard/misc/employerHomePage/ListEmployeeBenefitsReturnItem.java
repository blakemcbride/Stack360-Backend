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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.utils.DateUtils;

public class ListEmployeeBenefitsReturnItem {
	
	public ListEmployeeBenefitsReturnItem()
	{
		
	}

	ListEmployeeBenefitsReturnItem (BHRBenefitJoin bc)
	{
		name=bc.getBenefitConfigName();
		categoryName = bc.getBenefitCategoryName();
		benefitName = bc.getBenefitName();
		id=bc.getBenefitJoinId();
		startDate=bc.getCoverageStartDate();
		endDate=bc.getCoverageEndDate();
		if (bc.getBenefitConfig() != null && endDate == 0)
		{
			if (bc.getBenefitConfig().getBenefit() != null)
			{
				HrBenefit benefit = bc.getBenefitConfig().getBenefit();
				if(benefit.getCoverageEndType() == 1)
				{
					proposedEndDate = DateUtils.endOfMonth(DateUtils.now());
				}
				else if(benefit.getCoverageEndType() == 2)
				{
					proposedEndDate = DateUtils.now();
				}
				else if(benefit.getCoverageEndType() == 3)
				{
					proposedEndDate = DateUtils.addDays(DateUtils.now(), benefit.getCoverageEndPeriod());
				}
			}
		}
	}
	
	private String name;
	private String categoryName;
	private String benefitName;
	private String id;
	private int startDate;
	private int endDate;
	private int proposedEndDate;

	public int getProposedEndDate() {
		return proposedEndDate;
	}

	public void setProposedEndDate(int proposedEndDate) {
		this.proposedEndDate = proposedEndDate;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

}

	
