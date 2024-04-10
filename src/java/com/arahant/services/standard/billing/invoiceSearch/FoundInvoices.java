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
 *
 * Created on Feb 11, 2007
 */
package com.arahant.services.standard.billing.invoiceSearch;

import com.arahant.business.BInvoice;
import com.arahant.utils.DateUtils;

public class FoundInvoices  {

	private String invoiceId;
	private String invoiceDate;
	private String clientName;
	private String invoiceTransmittedDate;
	private String invoiceDescription;
	private double invoiceAmount;
	private String accountingInvoiceId;
	private String invoiceAmountFormatted;
	
	public FoundInvoices()
	{
	}

	FoundInvoices(final BInvoice inv) {
		super();
		invoiceId=inv.getInvoiceId();
		invoiceDate=DateUtils.getDateTimeFormatted(inv.getCreateDate());
		clientName=inv.getCompanyName();
		invoiceTransmittedDate=DateUtils.getDateTimeFormatted(inv.getExportDate());
		invoiceDescription=inv.getDescription();
		accountingInvoiceId=inv.getAccountingInvoiceIdentifier();
		invoiceAmount=inv.getInvoiceAmount();
		invoiceAmountFormatted=inv.getInvoiceAmountFormatted();
	}

	/**
	 * @return Returns the clientName.
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * @param clientName The clientName to set.
	 */
	public void setClientName(final String clientName) {
		this.clientName = clientName;
	}
	/**
	 * @return Returns the invoiceAmount.
	 */
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	/**
	 * @param invoiceAmount The invoiceAmount to set.
	 */
	public void setInvoiceAmount(final double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	/**
	 * @return Returns the invoiceDate.
	 */
	public String getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * @param invoiceDate The invoiceDate to set.
	 */
	public void setInvoiceDate(final String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * @return Returns the invoiceDescription.
	 */
	public String getInvoiceDescription() {
		return invoiceDescription;
	}
	/**
	 * @param invoiceDescription The invoiceDescription to set.
	 */
	public void setInvoiceDescription(final String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
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
	 * @return Returns the invoiceTransmittedDate.
	 */
	public String getInvoiceTransmittedDate() {
		return invoiceTransmittedDate;
	}
	/**
	 * @param invoiceTransmittedDate The invoiceTransmittedDate to set.
	 */
	public void setInvoiceTransmittedDate(final String invoiceTransmittedDate) {
		this.invoiceTransmittedDate = invoiceTransmittedDate;
	}

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
	 * @return Returns the invoiceAmountFormatted.
	 */
	public String getInvoiceAmountFormatted() {
		return invoiceAmountFormatted;
	}

	/**
	 * @param invoiceAmountFormatted The invoiceAmountFormatted to set.
	 */
	public void setInvoiceAmountFormatted(final String invoiceAmountFormatted) {
		this.invoiceAmountFormatted = invoiceAmountFormatted;
	}
}

	
