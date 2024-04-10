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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BReceipt;
import com.arahant.annotation.Validation;
import com.arahant.business.BInvoice;


/**
 * 
 *
 * Created on Feb 8, 2007 
 *
 */
public class SavePaymentInput extends TransmitInputBase {

	void setData(BReceipt bc)
	{
		bc.setDate(date);
		bc.setAmount(amount);
		bc.setDescription(description);
		bc.setType(type);
		bc.setPersonId(personId);
		
	}
	
	@Validation (required=true, type="date")
	private int date;
	@Validation (required=true, min=1)
	private double amount;
	@Validation (table="receipt",column="reference",required=true)
	private String description;
	@Validation (table="receipt",column="receipt_type",required=true)
	private String type;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false)
	private SavePaymentInputItem []item;
	@Validation (required=true)
	private String personId;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	

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
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public SavePaymentInputItem[] getItem() {
		if (item==null)
			item=new SavePaymentInputItem[0];
		return item;
	}

	public void setItem(SavePaymentInputItem[] item) {
		this.item = item;
	}

}

	
