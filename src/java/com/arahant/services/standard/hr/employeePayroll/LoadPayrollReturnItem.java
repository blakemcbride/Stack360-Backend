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

package com.arahant.services.standard.hr.employeePayroll;

import com.arahant.business.BElectronicFundTransfer;

/**
 *
 */
public class LoadPayrollReturnItem {

	private String id;
	private double amount;
	private String amountType;
	private String routingTransitNumber;
	private String accountNumber;
	private String accountType;
	private String wageTypeId;
	
	public LoadPayrollReturnItem()
	{
		
	}
	
	public LoadPayrollReturnItem(BElectronicFundTransfer bc)
	{
		id=bc.getId();
		amount=bc.getAmount();
		amountType=bc.getAmountType();
		routingTransitNumber=bc.getRoutingTransitNumber();
		accountNumber=bc.getAccountNumber();
		accountType=bc.getAccountType();
		wageTypeId=bc.getWageTypeId();

	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoutingTransitNumber() {
		return routingTransitNumber;
	}

	public void setRoutingTransitNumber(String routingTransitNumber) {
		this.routingTransitNumber = routingTransitNumber;
	}
	
	
	
}
