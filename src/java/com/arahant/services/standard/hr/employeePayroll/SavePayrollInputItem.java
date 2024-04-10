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
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;

/**
 *
 */
public class SavePayrollInputItem {
	private String id;
	private double amount;
	private String amountType;
	private String routingTransitNumber;
	private String accountNumber;
	private String accountType;
	private String wageTypeId;
	
	String saveData(BEmployee bc, short seq)
	{
		
		if (routingTransitNumber!=null && routingTransitNumber.length()>0 && routingTransitNumber.length()<9)
			throw new ArahantException("Routing transit number must be 9 digits long.");
		
		if (accountNumber!=null && accountNumber.length()>0 && accountNumber.length()<6)
			throw new ArahantException("Account number must be at least 6 digits long.");
		
		if (id==null || "".equals(id.trim()))
		{
			BElectronicFundTransfer eft=new BElectronicFundTransfer();
			id=eft.create();
			eft.setAmount(amount);
			eft.setAmountType(amountType);
			eft.setRoutingTransitNumber(routingTransitNumber);
			eft.setAccountNumber(accountNumber);
			eft.setAccountType(accountType);
			eft.setPerson(bc);
			eft.setSeq(seq);
			eft.setWageTypeId(wageTypeId);
			eft.insert();
		}
		else
		{
			BElectronicFundTransfer eft=new BElectronicFundTransfer(id);
			eft.setAmount(amount);
			eft.setAmountType(amountType);
			eft.setRoutingTransitNumber(routingTransitNumber);
			eft.setAccountNumber(accountNumber);
			eft.setAccountType(accountType);
			eft.setPerson(bc);
			eft.setWageTypeId(wageTypeId);
			eft.setSeq(seq);
			eft.update();
		}	
		
		return id;
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
