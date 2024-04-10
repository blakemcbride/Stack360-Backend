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
import com.arahant.business.BInvoiceLineItem;
import com.arahant.business.BService;
import com.arahant.business.BReceipt;
import com.arahant.exceptions.ArahantException;
import java.util.Set;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveInvoiceInput extends TransmitInputBase {

	void setData(BInvoice bc) throws ArahantException
	{
		bc.setPersonId(personId);
		bc.setDescription(description);
		bc.setDate(date);
		bc.setGlAccountId(glAccountId);

		//make sure not null
		getLines();
		getPayments();
		getAdjustments();
		
		//get a list of all line item id's currently associated
		Set<String> lineItemIds=bc.getLineItemIds();
		
		for (int loop=0;loop<lines.length;loop++)
		{
			if (isEmpty(lines[loop].getId()))
				bc.createInvoiceLineItem(lines[loop].getAmount(),lines[loop].getDescription(), lines[loop].getProductServiceId(),null);
			else
			{
				BInvoiceLineItem bli=new BInvoiceLineItem(lines[loop].getId());
				bli.setAmount(lines[loop].getAmount());
				bli.setDescription(lines[loop].getDescription());
				if (isEmpty(lines[loop].getProductServiceId()))
					throw new ArahantException("Could not find Product!");
				BService prod = new BService(lines[loop].getProductServiceId());
				bli.setProductId(prod.getProductId());
				if (prod.getGlAccount() == null)
					throw new ArahantException("Product/Service does not have default GL Expense Account!");
				bli.setGLAccountId(prod.getGlAccount().getGlAccountId());
		
				lineItemIds.remove(lines[loop].getId());
			}
		}
		//delete those line items
		BInvoiceLineItem.delete(lineItemIds);
		
		bc.deletePaymentJoins();  //I'll just make them again below
		
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
		
		
		//wipe adjustments and make new ones
		bc.deleteAdjustments();
		for (int loop=0;loop<adjustments.length;loop++)
		{
			BReceipt br;
		//	if (isEmpty(adjustments[loop].getId()))
			{
				//create a payment
				br=new BReceipt();
				br.create();
				br.setAmount(adjustments[loop].getAmount());
				br.setDate(adjustments[loop].getDate());
				br.setDescription(adjustments[loop].getDescription());
				br.setType("A");
				br.setPersonId(personId);
				br.insert();
			}
		//	else
		//		br=new BReceipt(adjustments[loop].getId());
			
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
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false)
	private SaveInvoiceInputLine []lines;
	@Validation (required=false)
	private SaveInvoiceInputPayment []payments;
	@Validation (required=false)
	private SaveInvoiceInputAdjustment []adjustments;

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
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	
		public SaveInvoiceInputAdjustment[] getAdjustments() {
		if (adjustments==null)
			adjustments=new SaveInvoiceInputAdjustment[0];
		return adjustments;
	}

	public void setAdjustments(SaveInvoiceInputAdjustment[] adjustments) {
		this.adjustments = adjustments;
	}

	public SaveInvoiceInputLine[] getLines() {
		if (lines==null)
			lines=new SaveInvoiceInputLine[0];
		return lines;
	}

	public void setLines(SaveInvoiceInputLine[] lines) {
		this.lines = lines;
	}

	public SaveInvoiceInputPayment[] getPayments() {
		if (payments==null)
			payments=new SaveInvoiceInputPayment[0];
		return payments;
	}

	public void setPayments(SaveInvoiceInputPayment[] payments) {
		this.payments = payments;
	}


}

	
