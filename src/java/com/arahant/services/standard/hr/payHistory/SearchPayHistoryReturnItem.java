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
package com.arahant.services.standard.hr.payHistory;
import com.arahant.business.BWagePaid;


/**
 * 
 *
 *
 */
public class SearchPayHistoryReturnItem {
	
	public SearchPayHistoryReturnItem()
	{
		
	}

	SearchPayHistoryReturnItem (BWagePaid bc)
	{
		
		id=bc.getId();
		payDate=bc.getPayDate();
		paymentMethod=bc.getPaymentMethod();
		checkNumber=bc.getCheckNumber();
		totalAmount=bc.getTotalAmount();

	}
	
	private String id;
	private int payDate;
	private int paymentMethod;
	private int checkNumber;
	private double totalAmount;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getPayDate()
	{
		return payDate;
	}
	public void setPayDate(int payDate)
	{
		this.payDate=payDate;
	}
	public int getPaymentMethod()
	{
		return paymentMethod;
	}
	public void setPaymentMethod(int paymentMethod)
	{
		this.paymentMethod=paymentMethod;
	}
	public int getCheckNumber()
	{
		return checkNumber;
	}
	public void setCheckNumber(int checkNumber)
	{
		this.checkNumber=checkNumber;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

}

	
