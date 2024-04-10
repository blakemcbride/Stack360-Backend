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
import com.arahant.business.BInvoice;
import com.arahant.annotation.Validation;
import com.arahant.business.BReceipt;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewInvoiceInput extends TransmitInputBase {

	void setData(BInvoice bc)
	{
		
		bc.setPersonId(personId);
		bc.setDescription(description);
		bc.setDate(date);
		bc.setGlAccountId(glAccountId);
		
		//make sure not null
		getLines();
		getPayments();
		getAdjustments();
		
		for (int loop=0;loop<lines.length;loop++)
			bc.createInvoiceLineItem(lines[loop].getAmount(),lines[loop].getDescription(), lines[loop].getProductServiceId(),null);
		
		// create payments and adjustments
		for (int loop=0;loop<payments.length;loop++)
		{  
			BReceipt br;
			if (isEmpty(payments[loop].getId()))
			{
				//create a payment
				br=new BReceipt();
				br.create();
				br.setAmount(payments[loop].getAmount());
				br.setDate(payments[loop].getDate());
				br.setDescription(payments[loop].getDescription());
				br.setType(payments[loop].getType());
				br.setPersonId(personId);
				br.insert();
			}
			else
				br=new BReceipt(payments[loop].getId());
			
			bc.applyPayment(br, payments[loop].getAppliedToThisInvoice());
		}
		
		for (int loop=0;loop<adjustments.length;loop++)
		{
			BReceipt br;
			if (isEmpty(adjustments[loop].getId()))
			{
				//create a payment
				br=new BReceipt();
				br.create();
				br.setAmount(adjustments[loop].getAmount());
				br.setDate(adjustments[loop].getDate());
				br.setDescription(adjustments[loop].getDescription());
				br.setPersonId(personId);
				br.setType("A");
				br.insert();
			}
			else
				br=new BReceipt(adjustments[loop].getId());
			
			bc.applyPayment(br, adjustments[loop].getAmount());
		}
 
		
		
	}
	
	@Validation (table="invoice",column="person_id",required=true)
	private String personId;
	@Validation (table="invoice",column="description",required=true)
	private String description;
	@Validation (required=true, type="date")
	private int date;
	@Validation (required=true)
	private String glAccountId;
	@Validation (required=false)
	private NewInvoiceInputLine []lines;
	@Validation (required=false)
	private NewInvoiceInputPayment []payments;
	@Validation (required=false)
	private NewInvoiceInputAdjustment []adjustments;
	
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public String getGlAccountId()
	{
		return glAccountId;
	}
	public void setGlAccountId(String glAccountId)
	{
		this.glAccountId=glAccountId;
	}

	public NewInvoiceInputAdjustment[] getAdjustments() {
		if (adjustments==null)
			adjustments=new NewInvoiceInputAdjustment[0];
		return adjustments;
	}

	public void setAdjustments(NewInvoiceInputAdjustment[] adjustments) {
		this.adjustments = adjustments;
	}

	public NewInvoiceInputLine[] getLines() {
		if (lines==null)
			lines=new NewInvoiceInputLine[0];
		return lines;
	}

	public void setLines(NewInvoiceInputLine[] lines) {
		this.lines = lines;
	}

	public NewInvoiceInputPayment[] getPayments() {
		if (payments==null)
			payments=new NewInvoiceInputPayment[0];
		return payments;
	}

	public void setPayments(NewInvoiceInputPayment[] payments) {
		this.payments = payments;
	}

}

	
