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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.billing.invoiceSearch;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetInvoiceReportInput extends TransmitInputBase  {

	
	@Validation (required=false)
	private boolean descriptionIncluded; // 0=No 1=Yes
	@Validation (required=false)
	private boolean lineItemsIncluded; // 0=No 1=Yes
	@Validation (required=false)
	private boolean detailIncluded; // 0=No 1=Yes
	@Validation (required=false)
	private String invoiceIds[];
	

	public GetInvoiceReportInput() {
		super();
	}

	/**
	 * @return
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#getDescriptionIncluded()
	 */
	public boolean getDescriptionIncluded() {
		return descriptionIncluded;
	}

	/**
	 * @return
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#getDetailIncluded()
	 */
	public boolean getDetailIncluded() {
		return detailIncluded;
	}


	/**
	 * @return
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#getLineItemsIncluded()
	 */
	public boolean getLineItemsIncluded() {
		return lineItemsIncluded;
	}

	/**
	 * @param descriptionIncluded
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#setDescriptionIncluded(int)
	 */
	public void setDescriptionIncluded(final boolean descriptionIncluded) {
		this.descriptionIncluded=descriptionIncluded;
	}

	/**
	 * @param detailIncluded
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#setDetailIncluded(int)
	 */
	public void setDetailIncluded(final boolean detailIncluded) {
		this.detailIncluded=detailIncluded;
	}


	/**
	 * @param lineItemsIncluded
	 * @see com.arahant.operations.transmit.InvoiceReportCriteria#setLineItemsIncluded(int)
	 */
	public void setLineItemsIncluded(final boolean lineItemsIncluded) {
		this.lineItemsIncluded=lineItemsIncluded;
	}

	/**
	 * @return Returns the invoiceIds.
	 */
	public String[] getInvoiceIds() {
            if (invoiceIds==null)
                return new String[0];
		return invoiceIds;
	}

	


	/**
	 * @param invoiceIds
	 * @see com.arahant.operations.transmit.IInvoiceReportCriteria#setInvoiceIds(java.lang.String)
	 */
	public void setInvoiceIds(final String []invoiceIds) {
		this.invoiceIds=invoiceIds;
	}
}

	
