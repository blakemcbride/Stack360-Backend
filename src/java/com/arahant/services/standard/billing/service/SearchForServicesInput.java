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
package com.arahant.services.standard.billing.service;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchForServicesInput extends TransmitInputBase {

	@Validation (table="product_service",column="accsys_id",required=false)
	private String accSysId;
	@Validation (table="product_service",column="description",required=false)
	private String description;
	@Validation (required=false)
	private int accSysIdSearchType;
	@Validation (required=false)
	private int descriptionSearchType;
	
	
	/**
	 * @return Returns the accSysId.
	 */
	public String getAccSysId() {
		return modifyForSearch(accSysId,accSysIdSearchType);
	}
	/**
	 * @param accSysId The accSysId to set.
	 */
	public void setAccSysId(final String accSysId) {
		this.accSysId = accSysId;
	}
	/**
	 * @return Returns the accSysIdSearchType.
	 */
	public int getAccSysIdSearchType() {
		return accSysIdSearchType;
	}
	/**
	 * @param accSysIdSearchType The accSysIdSearchType to set.
	 */
	public void setAccSysIdSearchType(final int accSysIdSearchType) {
		this.accSysIdSearchType = accSysIdSearchType;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return modifyForSearch(description,descriptionSearchType);
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 * @return Returns the descriptionSearchType.
	 */
	public int getDescriptionSearchType() {
		return descriptionSearchType;
	}
	/**
	 * @param descriptionSearchType The descriptionSearchType to set.
	 */
	public void setDescriptionSearchType(final int descriptionSearchType) {
		this.descriptionSearchType = descriptionSearchType;
	}
}

	
