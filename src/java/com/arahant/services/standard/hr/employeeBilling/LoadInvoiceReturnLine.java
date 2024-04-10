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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.hr.employeeBilling;

import com.arahant.business.BInvoiceLineItem;

/**
 *
 */
public class LoadInvoiceReturnLine {
	private String id;
	private double amount;
	private String description;
	private String productServiceId;
	private String productServiceName;
	private String glAccountId;
	private String glAccountName;

	public LoadInvoiceReturnLine()
	{
		
	}   
	LoadInvoiceReturnLine(BInvoiceLineItem bInvoiceLineItem) {
		setData(bInvoiceLineItem);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGlAccountId() {
		return glAccountId;
	}

	public void setGlAccountId(String glAccountId) {
		this.glAccountId = glAccountId;
	}

	public String getGlAccountName() {
		return glAccountName;
	}

	public void setGlAccountName(String glAccountName) {
		this.glAccountName = glAccountName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductServiceId() {
		return productServiceId;
	}

	public void setProductServiceId(String productServiceId) {
		this.productServiceId = productServiceId;
	}

	public String getProductServiceName() {
		return productServiceName;
	}

	public void setProductServiceName(String productServiceName) {
		this.productServiceName = productServiceName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	void setData(BInvoiceLineItem bc)
	{
		
		id=bc.getId();
		amount=bc.getTotal();
		description=bc.getDescription();
		productServiceId=bc.getProductServiceId();
		productServiceName=bc.getProductServiceName();
		glAccountId=bc.getGlAccountId();
		glAccountName=bc.getGlAccountName();

	}
	
}
