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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.peachtreeApp;
import com.arahant.business.BInvoice;
import com.arahant.business.BInvoiceLineItem;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class LoadInvoiceReturn  extends TransmitReturnBase{

	public LoadInvoiceReturn() {
		super();
	}

	/**
	 * @param inv
	 */
	void setData(final BInvoice i) {
		invoiceId=i.getInvoiceId();
		description=i.getDescription();
//		createDate=DateUtils.getDate(i.getCreateDate());
		arAccount=i.getArAccount();
		amount=i.getInvoiceAmount();
		customerAcctId=i.getCustomerAcctId();
//		totalBillableHours=i.getTotalBillableHours();
//		customerProphetId=i.getCustomerProphetId();
		accountingInvoiceId=i.getAccountingInvoiceIdentifier();
	}

	/**
	 * @param lineItems
	 */
	void setData(final BInvoiceLineItem[] lineItems) {
		invoiceLineItemTransmit=new InvoiceLineItemTransmit[lineItems.length];
		for (int loop=0;loop<lineItems.length;loop++)
			invoiceLineItemTransmit[loop]=new InvoiceLineItemTransmit(lineItems[loop]);
		
	}
	
	private InvoiceLineItemTransmit invoiceLineItemTransmit[];
	private String invoiceId;
	private String description;
//	private int createDate;
//	private int exportDate;
	private String arAccount;
	private double amount;
	private String customerAcctId;
//	private double totalBillableHours;
//	private String customerProphetId;
	private String accountingInvoiceId;

	/**
	 * @return Returns the accountingInvoiceId.
	 */
	public String getAccountingInvoiceId() {
		return accountingInvoiceId;
	}

	/**
	 * @param accountingInvoiceId The accountingInvoiceId to set.
	 */
	public void setAccountingInvoiceId(final String accountingInvoiceId) {
		this.accountingInvoiceId = accountingInvoiceId;
	}

	/**
	 * @return Returns the amount.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(final double amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the arAccount.
	 */
	public String getArAccount() {
		return arAccount;
	}

	/**
	 * @param arAccount The arAccount to set.
	 */
	public void setArAccount(final String arAccount) {
		this.arAccount = arAccount;
	}

	/**
	 * @return Returns the createDate.
	 *
	public int getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate The createDate to set.
	 *
	public void setCreateDate(int createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return Returns the customerAcctId.
	 */
	public String getCustomerAcctId() {
		return customerAcctId;
	}

	/**
	 * @param customerAcctId The customerAcctId to set.
	 */
	public void setCustomerAcctId(final String customerAcctId) {
		this.customerAcctId = customerAcctId;
	}

	/**
	 * @return Returns the customerProphetId.
	 *
	public String getCustomerProphetId() {
		return customerProphetId;
	}

	/**
	 * @param customerProphetId The customerProphetId to set.
	 *
	public void setCustomerProphetId(String customerProphetId) {
		this.customerProphetId = customerProphetId;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the exportDate.
	 *
	public int getExportDate() {
		return exportDate;
	}

	/**
	 * @param exportDate The exportDate to set.
	 *
	public void setExportDate(int exportDate) {
		this.exportDate = exportDate;
	}

	/**
	 * @return Returns the invoiceId.
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId The invoiceId to set.
	 */
	public void setInvoiceId(final String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return Returns the invoiceLineItemTransmit.
	 */
	public InvoiceLineItemTransmit[] getInvoiceLineItemTransmit() {
		return invoiceLineItemTransmit;
	}

	/**
	 * @param invoiceLineItemTransmit The invoiceLineItemTransmit to set.
	 */
	public void setInvoiceLineItemTransmit(
			final InvoiceLineItemTransmit[] invoiceLineItemTransmit) {
		this.invoiceLineItemTransmit = invoiceLineItemTransmit;
	}

	/**
	 * @return Returns the totalBillableHours.
	 *
	public double getTotalBillableHours() {
		return totalBillableHours;
	}

	/**
	 * @param totalBillableHours The totalBillableHours to set.
	 *
	public void setTotalBillableHours(double totalBillableHours) {
		this.totalBillableHours = totalBillableHours;
	}
	
	*/
}

	
