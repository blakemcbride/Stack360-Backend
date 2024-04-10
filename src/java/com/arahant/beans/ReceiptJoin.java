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

package com.arahant.beans;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 *
 */
@Entity
@Table(name="receipt_join")
public class ReceiptJoin extends ArahantBean {
	public static final String RECEIPT="receipt";
	public static final String INVOICE="invoice";
	public static final String AMOUNT="amount";

	private String receiptJoinId;
	private double amount;
	private Invoice invoice;
	private Receipt receipt;

	@Column (name="amount")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="invoice_id")
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="receipt_id")
	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	@Id
	@Column (name="receipt_join_id")
	public String getReceiptJoinId() {
		return receiptJoinId;
	}

	public void setReceiptJoinId(String receiptJoinId) {
		this.receiptJoinId = receiptJoinId;
	}
	
	
	
	
	@Override
	public String tableName() {
		return "receipt_join";
	}

	@Override
	public String keyColumn() {
		return "receipt_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return receiptJoinId=IDGenerator.generate(this);
	}

}
