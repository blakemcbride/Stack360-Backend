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
import com.arahant.business.BInvoice;
import com.arahant.business.BInvoiceLineItem;
import com.arahant.business.BReceipt;


/**
 * 
 *
 *
 */
public class LoadInvoiceReturn extends TransmitReturnBase {

	private LoadInvoiceReturnLine [] lines;
	private LoadInvoiceReturnPayment [] payments;
	private LoadInvoiceReturnAdjustment []adjustments;
	
		
	public LoadInvoiceReturnLine[] getLines() {
		return lines;
	}

	public void setLines(LoadInvoiceReturnLine[] lines) {
		this.lines = lines;
	}

	public LoadInvoiceReturnPayment[] getPayments() {
		return payments;
	}

	public void setPayments(LoadInvoiceReturnPayment[] payments) {
		this.payments = payments;
	}

	public LoadInvoiceReturnAdjustment[] getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(LoadInvoiceReturnAdjustment[] adjustments) {
		this.adjustments = adjustments;
	}
	
	

	void setData(BInvoice bInvoice) {
		BInvoiceLineItem [] lin=bInvoice.getLineItems();
		lines=new LoadInvoiceReturnLine[lin.length];
		for (int loop=0;loop<lin.length;loop++)
			lines[loop]=new LoadInvoiceReturnLine(lin[loop]);
		
		BReceipt [] recs=bInvoice.getReceipts();
		payments=new LoadInvoiceReturnPayment[recs.length];
		for (int loop=0;loop<recs.length;loop++)
			payments[loop]=new LoadInvoiceReturnPayment(recs[loop], bInvoice);
		
		BReceipt []adjs=bInvoice.getAdjustments();
		adjustments=new LoadInvoiceReturnAdjustment[adjs.length];
		for (int loop=0;loop<adjs.length;loop++)
			adjustments[loop]=new LoadInvoiceReturnAdjustment(adjs[loop]);
	}


	

}

	
