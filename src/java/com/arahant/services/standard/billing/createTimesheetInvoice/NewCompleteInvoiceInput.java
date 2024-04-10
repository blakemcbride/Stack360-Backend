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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.annotation.Validation;
import com.arahant.business.BInvoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class NewCompleteInvoiceInput extends TransmitInputBase {

	@Validation(required = false)
	private NewInvoiceLineItemTransmit[] invoiceLineItemTransmit;
	@Validation(table = "invoice", column = "description", required = false)
	private String description;
	@Validation(required = false)
	private String arAccount;
	@Validation(required = true)
	private String customerProphetId;
	@Validation(required = false)
	private String purchaseOrder;
	@Validation(required = false)
	private short paymentTerms;
	@Validation(type="date", required = true)
	private int invoiceDate;

	/**
	 * @param bi
	 * @throws ArahantException
	 */
	void makeInvoice(final BInvoice bi) throws ArahantException {
		bi.setDescription(description);
		bi.setArAccount(arAccount);
		bi.setCustomerId(customerProphetId);
		bi.setPurchaseOrder(purchaseOrder);
		bi.setPaymentTerms(paymentTerms);
		bi.setDate(invoiceDate);
		bi.insert();

		for (final NewInvoiceLineItemTransmit ili : getInvoiceLineItemTransmit())
			bi.createInvoiceLineItem(ili.getAdjHours(), ili.getAdjRate(),
					ili.getDescription(), ili.getServiceId(),
					ili.getTimesheetIds(), ili.getType().charAt(0), ili.getLineAmount(), ili.getProjectId());
	}

	public String getArAccount() {
		return arAccount;
	}

	public void setArAccount(final String arAccount) {
		this.arAccount = arAccount;
	}

	public String getCustomerProphetId() {
		return customerProphetId;
	}

	public void setCustomerProphetId(final String customerProphetId) {
		this.customerProphetId = customerProphetId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public short getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(short paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public int getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(int invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	/**
	 * @return Returns the invoiceLineItemTransmit.
	 */
	public NewInvoiceLineItemTransmit[] getInvoiceLineItemTransmit() {
		if (invoiceLineItemTransmit == null)
			return new NewInvoiceLineItemTransmit[0];
		return invoiceLineItemTransmit;
	}

	/**
	 * @param invoiceLineItemTransmit The invoiceLineItemTransmit to set.
	 */
	public void setInvoiceLineItemTransmit(
			final NewInvoiceLineItemTransmit[] invoiceLineItemTransmit) {
		this.invoiceLineItemTransmit = invoiceLineItemTransmit;
	}
}
