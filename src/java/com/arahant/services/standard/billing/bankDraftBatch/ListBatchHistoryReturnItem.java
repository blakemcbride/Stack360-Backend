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
package com.arahant.services.standard.billing.bankDraftBatch;
import com.arahant.business.BBankDraftHistory;


/**
 * 
 *
 *
 */
public class ListBatchHistoryReturnItem implements Comparable<ListBatchHistoryReturnItem> {
	
	public ListBatchHistoryReturnItem()
	{
	}

	ListBatchHistoryReturnItem (BBankDraftHistory bc)
	{
		date=bc.getDate();
		amount=bc.getAmount();
		confirmationNumber=bc.getConfirmationNumber();

	}
	
	private int date;
	private double amount;
	private String confirmationNumber;
	

	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount=amount;
	}
	public String getConfirmationNumber()
	{
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber)
	{
		this.confirmationNumber=confirmationNumber;
	}

	public int compareTo(ListBatchHistoryReturnItem o) {
		return o.date-date;
	}

}

	
