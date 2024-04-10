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

package com.arahant.services.standard.billing.invoiceSearch;


import com.arahant.business.interfaces.IInvoiceSearchCriteria;
import com.arahant.services.TransmitInputBase;

/**
 * Author: Blake McBride
 * Date: 12/7/21
 */
public class ReportInput extends TransmitInputBase implements IInvoiceSearchCriteria {

    private String invoiceId;
    private int invoiceStartDate;
    private int invoiceEndDate;
    private String clientId;
    private int invoiceStatus;
    private int amountSearchType;
    private double amount;
    private int invoiceIdSearchType;

    public int getInvoiceIdSearchType() {
        return invoiceIdSearchType;
    }

    public void setInvoiceIdSearchType(final int invoiceIdSearchType) {
        this.invoiceIdSearchType = invoiceIdSearchType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public int getAmountSearchType() {
        return amountSearchType;
    }

    public void setAmountSearchType(final int amountSearchType) {
        this.amountSearchType = amountSearchType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public int getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(final int invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }

    public String getInvoiceId() {
        return modifyForSearch(invoiceId, invoiceIdSearchType);
    }

    public void setInvoiceId(final String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(final int invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public int getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(final int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

}
