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
 * Created on Mar 15, 2007
 * 
 */
package com.arahant.services.standard.hr.hrBenefitPackage;
import com.arahant.business.BHRBenefit;


/**
 * 
 *
 * Created on Mar 15, 2007
 *
 */
public class ListAssignedBenefitsItem {

	public ListAssignedBenefitsItem() {
		super();
	}
	
	/**
	 * @param benefit
	 */
	ListAssignedBenefitsItem(final BHRBenefit benefit) {
		super();
		name=benefit.getName();
		id=benefit.getBenefitId();
	}

	private String name;
	private String id;
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
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
}

	
