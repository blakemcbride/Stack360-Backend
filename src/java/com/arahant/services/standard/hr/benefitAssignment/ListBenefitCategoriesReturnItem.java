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
package com.arahant.services.standard.hr.benefitAssignment;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class ListBenefitCategoriesReturnItem {
	
	public ListBenefitCategoriesReturnItem()
	{
		;
	}

	ListBenefitCategoriesReturnItem (final BHRBenefitCategory bc, final BPerson bemp)
	{	
		categoryId=bc.getCategoryId();
		categoryName=bc.getCategoryName();
		allowsMultipleBenefits=bc.getAllowsMultipleBenefits();
		categoryRequiresDecline=bc.getRequiresDecline();
		
		final HrBenefitJoin bj=bemp.getApprovedBenefitJoinOf(bc);
		if (bj!=null && !allowsMultipleBenefits)
		{
			activeAssignedBenfitName=bj.getHrBenefitConfig().getHrBenefit().getName();
			activeAssignedConfigName=bj.getHrBenefitConfig().getName();
			activeUsingCOBRA=bj.getUsingCOBRA()=='Y';
		}
	}
	
	private String categoryId;
	private String categoryName;
	private boolean categoryRequiresDecline; 
	private boolean allowsMultipleBenefits;
	private String activeAssignedBenfitName, activeAssignedConfigName;
	private boolean activeUsingCOBRA;

	/**
	 * @return Returns the activeUsingCOBRA.
	 */
	public boolean getActiveUsingCOBRA() {
		return activeUsingCOBRA;
	}

	/**
	 * @param activeUsingCOBRA The activeUsingCOBRA to set.
	 */
	public void setActiveUsingCOBRA(final boolean activeUsingCOBRA) {
		this.activeUsingCOBRA = activeUsingCOBRA;
	}
	
	/**
	 * @return Returns the activeAssignedBenfitName.
	 */
	public String getActiveAssignedBenfitName() {
		return activeAssignedBenfitName;
	}

	/**
	 * @param activeAssignedBenfitName The activeAssignedBenfitName to set.
	 */
	public void setActiveAssignedBenfitName(final String activeAssignedBenfitName) {
		this.activeAssignedBenfitName = activeAssignedBenfitName;
	}

	/**
	 * @return Returns the activeAssignedConfigName.
	 */
	public String getActiveAssignedConfigName() {
		return activeAssignedConfigName;
	}

	/**
	 * @param activeAssignedConfigName The activeAssignedConfigName to set.
	 */
	public void setActiveAssignedConfigName(final String activeAssignedConfigName) {
		this.activeAssignedConfigName = activeAssignedConfigName;
	}

	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(final String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getCategoryName()
	{
		return categoryName;
	}
	public void setCategoryName(final String categoryName)
	{
		this.categoryName=categoryName;
	}
	public boolean getAllowsMultipleBenefits()
	{
		return allowsMultipleBenefits;
	}
	public void setAllowsMultipleBenefits(final boolean allowsMultipleBenefits)
	{
		this.allowsMultipleBenefits=allowsMultipleBenefits;
	}

	/**
	 * @return Returns the categoryRequiresDecline.
	 */
	public boolean isCategoryRequiresDecline() {
		return categoryRequiresDecline;
	}

	/**
	 * @param categoryRequiresDecline The categoryRequiresDecline to set.
	 */
	public void setCategoryRequiresDecline(boolean categoryRequiresDecline) {
		this.categoryRequiresDecline = categoryRequiresDecline;
	}


}

	
