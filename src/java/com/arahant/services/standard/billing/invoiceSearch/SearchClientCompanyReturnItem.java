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
package com.arahant.services.standard.billing.invoiceSearch;
import com.arahant.business.BCompanyBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class SearchClientCompanyReturnItem {

	
	private String clientId;
	private String clientName;
	
	

	public SearchClientCompanyReturnItem() {
		
	}

	
	/**
	 * @param company
	 */
	SearchClientCompanyReturnItem(final BCompanyBase company) {
		super();

		this.setClientId(company.getOrgGroupId());

		this.setClientName(company.getName());

	}


	


	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}


	/**
	 * @param clientId The clientId to set.
	 */
	public void setClientId(final String clientId) {
		this.clientId = clientId;
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


	
}

	
