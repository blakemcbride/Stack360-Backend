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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.business.BBenefitRestriction;


/**
 * 
 *
 *
 */
public class ListBenefitRestrictionsReturnItem {
	
	public ListBenefitRestrictionsReturnItem()
	{
		;
	}

	ListBenefitRestrictionsReturnItem (final BBenefitRestriction br)
	{
		categoryId = br.getBenefitCategoryId();
		category = br.getBenefitCategory().getDescription();
		bcrId = br.getBenefitChangeReasonId();
		bcr = br.getBenefitChangeReason().getDescription();
		benefitRestrictionId = br.getBenefitRestrictionId();
	}

	private String category;
	private String categoryId;
	private String bcr;
	private String bcrId;
	private String benefitRestrictionId;

	public String getBenefitRestrictionId() {
		return benefitRestrictionId;
	}

	public void setBenefitRestrictionId(String benefitRestrictionId) {
		this.benefitRestrictionId = benefitRestrictionId;
	}

	public String getBcr() {
		return bcr;
	}

	public void setBcr(String bcr) {
		this.bcr = bcr;
	}

	public String getBcrId() {
		return bcrId;
	}

	public void setBcrId(String bcrId) {
		this.bcrId = bcrId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


}

	
