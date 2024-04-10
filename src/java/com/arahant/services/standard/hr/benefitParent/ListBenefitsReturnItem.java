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
package com.arahant.services.standard.hr.benefitParent;
import com.arahant.business.BHRBenefit;


/**
 * 
 *
 *
 */
public class ListBenefitsReturnItem {
	
	public ListBenefitsReturnItem()
	{
		
	}

	ListBenefitsReturnItem (final BHRBenefit bc)
	{
		benefitId=bc.getBenefitId();
		name=bc.getName();
		categoryName=bc.getCategoryName();
		active=bc.getActiveBool()?"Yes":"No";
		vendor=bc.getUnderwriter();
		includeInOpenEnrollment=bc.getOpenEnrollmentWizard().equals("Y")?"Yes":"No";
		includeOnboarding=bc.getOnboarding().equals("Y")?"Yes":"No";
	}
	
	private String benefitId;
	private String name;
	private String categoryName;
	private String active;
	private String vendor;
	private String includeInOpenEnrollment;
	private String includeOnboarding;


	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(final String name)
	{
		this.name=name;
	}
	public String getCategoryName()
	{
		return categoryName;
	}
	public void setCategoryName(final String categoryName)
	{
		this.categoryName=categoryName;
	}
	public String getActive()
	{
		return active;
	}
	public void setActive(final String active)
	{
		this.active=active;
	}
	public String getVendor()
	{
		return vendor;
	}
	public void setVendor(final String underwriter)
	{
		this.vendor=underwriter;
	}
	public String getIncludeInOpenEnrollment() {
		return includeInOpenEnrollment;
	}

	public void setIncludeInOpenEnrollment(String includeInOpenEnrollment) {
		this.includeInOpenEnrollment = includeInOpenEnrollment;
	}

	public String getIncludeOnboarding() {
		return includeOnboarding;
	}

	public void setIncludeOnboarding(String includeOnboarding) {
		this.includeOnboarding = includeOnboarding;
	}
}

	
