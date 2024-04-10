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
package com.arahant.services.standard.hr.employeeBilling;

import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadBillingReturn extends TransmitReturnBase {
	private double balance=0;
	private String bankDraftBatchId="";
	private String bankDraftBatchDescription="";
	private String billingStatusName="";

	
	public String getBillingStatusName() {
		return billingStatusName;
	}

	public void setBillingStatusName(String billingStatusName) {
		this.billingStatusName = billingStatusName;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getBankDraftBatchDescription() {
		return bankDraftBatchDescription;
	}

	public void setBankDraftBatchDescription(String bankDraftBatchDescription) {
		this.bankDraftBatchDescription = bankDraftBatchDescription;
	}

	public String getBankDraftBatchId() {
		return bankDraftBatchId;
	}

	public void setBankDraftBatchId(String bankDraftBatchId) {
		this.bankDraftBatchId = bankDraftBatchId;
	}

	
}

	
