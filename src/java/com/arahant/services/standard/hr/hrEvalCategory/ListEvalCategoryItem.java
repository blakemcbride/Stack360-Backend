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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvalCategory;
import com.arahant.business.BHREvalCategory;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEvalCategoryItem {

	private String evalCategoryId;
	private String name;
	private short weight;
	private String descriptionPreview;
	private int lastActiveDate;
	private int firstActiveDate;
	
	/**
	 * @return Returns the descriptionPreview.
	 */
	public String getDescriptionPreview() {
		return descriptionPreview;
	}
	/**
	 * @param descriptionPreview The descriptionPreview to set.
	 */
	public void setDescriptionPreview(final String descriptionPreview) {
		this.descriptionPreview = descriptionPreview;
	}
	/**
	 * @return Returns the weight.
	 */
	public short getWeight() {
		return weight;
	}
	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(final short weight) {
		this.weight = weight;
	}
	public ListEvalCategoryItem()
	{
		
	}
	/**
	 * @return Returns the accrualAccountId.
	 */
	public String getEvalCategoryId() {
		return evalCategoryId;
	}

	/**
	 * @param accrualAccountId The accrualAccountId to set.
	 */
	public void setEvalCategoryId(final String evalCategoryId) {
		this.evalCategoryId = evalCategoryId;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}



	/**
	 * @param account
	 */
	ListEvalCategoryItem(final BHREvalCategory account) {
		evalCategoryId=account.getEvalCategoryId();
		name=account.getName();
		weight=account.getWeight();
		descriptionPreview=account.getDescription();
		if (descriptionPreview!=null && descriptionPreview.length()>100)
			descriptionPreview=descriptionPreview.substring(0,100);
		lastActiveDate=account.getLastActiveDate();
		firstActiveDate=account.getFirstActiveDate();
	}
}

	
