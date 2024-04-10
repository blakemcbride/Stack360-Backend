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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.hr.employeeBilling;
import com.arahant.business.BService;


/**
 *  
 *
 * Created on Feb 12, 2007
 *
 */
public class SearchProductsAndServicesReturnItem  {


	public SearchProductsAndServicesReturnItem() {
		super();
	}

	private String productId;
	private String accsysId;
	private int productType;
	private String description;
	private Boolean hasGLExpenseAccount;
	private String glExpenseAccountNumber;
	private String glExpenseAccountName;

	
	/**
	 * @param products
	 */
	SearchProductsAndServicesReturnItem(final BService products) {
		super();
		productId=products.getProductId();
		accsysId=products.getAccsysId();
		productType=products.getProductType();
		description=products.getDescription();
		hasGLExpenseAccount = products.getGlAccount() != null;
		if (hasGLExpenseAccount) {
			glExpenseAccountNumber=products.getGlAccount().getAccountNumber();
			glExpenseAccountName=products.getGlAccount().getAccountName();
		}
	}

	public String getGlExpenseAccountName() {
		return glExpenseAccountName;
	}

	public void setGlExpenseAccountName(String glExpenseAccountName) {
		this.glExpenseAccountName = glExpenseAccountName;
	}

	public String getGlExpenseAccountNumber() {
		return glExpenseAccountNumber;
	}

	public void setGlExpenseAccountNumber(String glExpenseAccountNumber) {
		this.glExpenseAccountNumber = glExpenseAccountNumber;
	}

	public Boolean getHasGLExpenseAccount() {
		return hasGLExpenseAccount;
	}

	public void setHasGLExpenseAccount(Boolean hasGLExpenseAccount) {
		this.hasGLExpenseAccount = hasGLExpenseAccount;
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

	
