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
 * Created on Jun 12, 2007
 * 
 */
package com.arahant.services.standard.billing.service;

import com.arahant.business.BGlAccount;
import com.arahant.business.BService;



/**
 * 
 *
 * Created on Jun 12, 2007
 *
 */
public class SearchForServicesItem {

	public SearchForServicesItem() {
		super();
	}
	
	/**
	 * @param products
	 */
	SearchForServicesItem(final BService products) {
		accSysId=products.getAccsysId();
		
		BGlAccount glAccount = products.getGlAccount();
		if (glAccount == null)
			defaultGLAccountFormatted="";
		else
			defaultGLAccountFormatted=glAccount.getAccountNumber() + " - " + glAccount.getAccountName();
		
		description=products.getDescription();
		serviceId=products.getProductId();
	}

	private String accSysId, defaultGLAccountFormatted, description, serviceId;

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
	 * @return Returns the defaultGLAccountFormatted.
	 */
	public String getDefaultGLAccountFormatted() {
		return defaultGLAccountFormatted;
	}

	/**
	 * @param defaultGLAccountFormatted The defaultGLAccountFormatted to set.
	 */
	public void setDefaultGLAccountFormatted(final String defaultGLAccountFormatted) {
		this.defaultGLAccountFormatted = defaultGLAccountFormatted;
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

	
