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
package com.arahant.services.standard.billing.employeeInvoice;
import com.arahant.business.BInvoice;


/**
 * 
 *
 *
 */
public class SearchInvoicesForPaymentReturnItem {
	
	public SearchInvoicesForPaymentReturnItem()
	{
		
	}

	SearchInvoicesForPaymentReturnItem (BInvoice bc)
	{
		
		id=bc.getId();
		date=bc.getDate();
		accountingInvoiceId=bc.getAccountingInvoiceId();
		total=bc.getAmount();
		balance=bc.getBalance();
		description=bc.getDescription();

	}
	
	private String id;
	private int date;
	private String accountingInvoiceId;
	private double total;
	private double balance;
	private String description;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public String getAccountingInvoiceId()
	{
		return accountingInvoiceId;
	}
	public void setAccountingInvoiceId(String accountingInvoiceId)
	{
		this.accountingInvoiceId=accountingInvoiceId;
	}
	public double getTotal()
	{
		return total;
	}
	public void setTotal(double total)
	{
		this.total=total;
	}
	public double getBalance()
	{
		return balance;
	}
	public void setBalance(double balance)
	{
		this.balance=balance;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}

}

	
