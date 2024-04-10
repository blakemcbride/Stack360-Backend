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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.peachtreeApp;
import com.arahant.business.BService;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class Products extends TransmitReturnBase {

	private String productId;
	private String accsysId;
	private int productType;
	private String accsysAccount;
	private String description;

	public Products() {

	}

	
	/**
	 * @param products
	 */
	Products(final BService products) {
		super();
		productId=products.getProductId();
		accsysId=products.getAccsysId();
		productType=products.getProductType();
		accsysAccount=products.getAccsysAccount();
		description=products.getDescription();
	}


	/**
	 * @return Returns the accsysAccount.
	 */
	public String getAccsysAccount() {
		return accsysAccount;
	}


	/**
	 * @param accsysAccount The accsysAccount to set.
	 */
	public void setAccsysAccount(final String accsysAccount) {
		this.accsysAccount = accsysAccount;
	}


	/**
	 * @return Returns the accsysId.
	 */
	public String getAccsysId() {
		return accsysId;
	}


	/**
	 * @param accsysId The accsysId to set.
	 */
	public void setAccsysId(final String accsysId) {
		this.accsysId = accsysId;
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
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}


	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(final String productId) {
		this.productId = productId;
	}


	/**
	 * @return Returns the productType.
	 */
	public int getProductType() {
		return productType;
	}


	/**
	 * @param productType The productType to set.
	 */
	public void setProductType(final int productType) {
		this.productType = productType;
	}

	
}

	
