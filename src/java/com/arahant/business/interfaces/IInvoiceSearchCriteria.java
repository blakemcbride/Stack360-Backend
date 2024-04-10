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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.business.interfaces;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public interface IInvoiceSearchCriteria {

	/**
	 * @return Returns the amount.
	 */
	public abstract double getAmount();

	/**
	 * @param amount The amount to set.
	 */
	public abstract void setAmount(double amount);

	/**
	 * @return Returns the amountSearchType.
	 */
	public abstract int getAmountSearchType();

	/**
	 * @param amountSearchType The amountSearchType to set.
	 */
	public abstract void setAmountSearchType(int amountSearchType);

	/**
	 * @return Returns the clientId.
	 */
	public abstract String getClientId();

	/**
	 * @param clientId The clientId to set.
	 */
	public abstract void setClientId(String clientId);

	/**
	 * @return Returns the invoiceEndDate.
	 */
	public abstract int getInvoiceEndDate();

	/**
	 * @param invoiceEndDate The invoiceEndDate to set.
	 */
	public abstract void setInvoiceEndDate(int invoiceEndDate);

	/**
	 * @return Returns the invoiceId.
	 */
	public abstract String getInvoiceId();

	/**
	 * @param invoiceId The invoiceId to set.
	 */
	public abstract void setInvoiceId(String invoiceId);

	/**
	 * @return Returns the invoiceStartDate.
	 */
	public abstract int getInvoiceStartDate();

	/**
	 * @param invoiceStartDate The invoiceStartDate to set.
	 */
	public abstract void setInvoiceStartDate(int invoiceStartDate);

	/**
	 * @return Returns the invoiceStatus.
	 */
	public abstract int getInvoiceStatus();

	/**
	 * @param invoiceStatus The invoiceStatus to set.
	 */
	public abstract void setInvoiceStatus(int invoiceStatus);

}
