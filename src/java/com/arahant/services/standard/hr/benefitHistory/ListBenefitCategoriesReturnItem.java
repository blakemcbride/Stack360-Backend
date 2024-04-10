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
import com.arahant.business.BHRBenefitCategory;


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

	ListBenefitCategoriesReturnItem (BHRBenefitCategory bc)
	{
		
		categoryId=bc.getCategoryId();
		categoryName=bc.getCategoryName();

	}
	
	private String categoryId;
	private String categoryName;

	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getCategoryName()
	{
		return categoryName;
	}
	public void setCategoryName(String categoryName)
	{
		this.categoryName=categoryName;
	}

}

	
