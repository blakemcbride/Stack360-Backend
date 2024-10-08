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
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class ListUnexportedInvoicesReturn extends TransmitReturnBase {


	private Invoices []invoices;
	
	public ListUnexportedInvoicesReturn() {
		super();
	}

	/**
	 * @return Returns the invoices.
	 */
	public Invoices[] getInvoices() {
		return invoices;
	}

	/**
	 * @param invoices The invoices to set.
	 */
	public void setInvoices(final Invoices[] invoices) {
		this.invoices = invoices;
	}

	/**
	 * @param b
	 */
	void setInvoices(final BInvoice[] b) {
		invoices=new Invoices[b.length];
		for (int loop=0;loop<b.length;loop++)
			invoices[loop]=new Invoices(b[loop]);
	}
}

	
