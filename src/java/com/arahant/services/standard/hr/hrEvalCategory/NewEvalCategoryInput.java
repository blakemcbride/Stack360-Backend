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
import com.arahant.annotation.Validation;
import com.arahant.business.BHREvalCategory;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class NewEvalCategoryInput extends TransmitInputBase {

	@Validation (table="hr_eval_category",column="name",required=true)
	private String name;
	@Validation (min=.01,max=10,table="hr_eval_category",column="weight",required=false)
	private short weight;
	@Validation (min=1,table="hr_eval_category",column="description",required=false)
	private String description;
	@Validation (required=false, type="date")
	private int firstActiveDate;
	@Validation (required=false, type="date")
	private int lastActiveDate;

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
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
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

	/**
	 * @param x
	 */
	void setData(final BHREvalCategory x) {
		x.setName(name);
		x.setWeight(weight);
		x.setDescription(description);
		x.setLastActiveDate(lastActiveDate);
		x.setFirstActiveDate(firstActiveDate);
	}
}

	
