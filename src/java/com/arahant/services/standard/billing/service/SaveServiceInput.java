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

import com.arahant.business.BService;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveServiceInput extends TransmitInputBase {
	@Validation (required=true)
	private String serviceId;
	@Validation (table="product_service",column="accsys_id",required=true)
	private String accSysId;
	@Validation (table="product_service",column="description",required=true)
	private String description;
	@Validation (min=1,max=16,required=false)
	private String defaultGLAccountId;

	/**
	 * @param p
	 */
	void setData(final BService p) {
		p.setAccsysAccountFromGLAccountId(defaultGLAccountId);
		p.setDescription(description);
		p.setAccsysId(accSysId);
	}
	
	/**
	 * @return Returns the accSysId.
	 */
	public String getAccSysId() {
		return accSysId;
	}

	/**
	 * @param accSysId The accSysId to set.
	 */
	public void setAccSysId(final String accSysId) {
		this.accSysId = accSysId;
	}

	/**
	 * @return Returns the defaultGLAccountId.
	 */
	public String getDefaultGLAccountId() {
		return defaultGLAccountId;
	}

	/**
	 * @param defaultGLAccountId The defaultGLAccountId to set.
	 */
	public void setDefaultGLAccountId(final String defaultGLAccountId) {
		this.defaultGLAccountId = defaultGLAccountId;
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
	 * @return Returns the productServiceId.
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param productServiceId The productServiceId to set.
	 */
	public void setServiceId(final String serviceId) {
		this.serviceId = serviceId;
	}
}

	
