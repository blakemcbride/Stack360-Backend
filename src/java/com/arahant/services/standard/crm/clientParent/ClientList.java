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
package com.arahant.services.standard.crm.clientParent;
import com.arahant.business.BClientCompany;
import com.arahant.business.BCompanyBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class ClientList {

	
	private String clientId;
	private String clientName;
	private String clientIdentifier;
	private String clientPhone;
	private String clientContactLastName;
	private String clientContactFirstName;
	private String clientContactPhone;
	private String clientStatus;
	

	public ClientList() {
		
	}

	
	/**
	 * @param company
	 */
	ClientList(final BCompanyBase company) {
		super();
		this.setClientContactFirstName(company.getMainContactFname());
		this.setClientContactLastName(company.getMainContactLname());
		this.setClientContactPhone(company.getMainContactWorkPhone());
		this.setClientId(company.getOrgGroupId());
		this.setClientIdentifier(company.getIdentifier());
		this.setClientName(company.getName());
		this.setClientPhone(company.getMainPhoneNumber());
		
		BClientCompany clientCompany = new BClientCompany(company.getId());
		this.setClientStatus(clientCompany.getInactiveDate()==0?"Active":"Inactive");
	}


	/**
	 * @return Returns the clientStatus.
	 */
	public String getClientStatus() {
		return clientStatus;
	}


	/**
	 * @param clientStatus The clientStatus to set.
	 */
	public void setClientStatus(final String clientStatus) {
		this.clientStatus = clientStatus;
	}
	
	/**
	 * @return Returns the clientContactFirstName.
	 */
	public String getClientContactFirstName() {
		return clientContactFirstName;
	}


	/**
	 * @param clientContactFirstName The clientContactFirstName to set.
	 */
	public void setClientContactFirstName(final String clientContactFirstName) {
		this.clientContactFirstName = clientContactFirstName;
	}


	/**
	 * @return Returns the clientContactLastName.
	 */
	public String getClientContactLastName() {
		return clientContactLastName;
	}


	/**
	 * @param clientContactLastName The clientContactLastName to set.
	 */
	public void setClientContactLastName(final String clientContactLastName) {
		this.clientContactLastName = clientContactLastName;
	}


	/**
	 * @return Returns the clientContactPhone.
	 */
	public String getClientContactPhone() {
		return clientContactPhone;
	}


	/**
	 * @param clientContactPhone The clientContactPhone to set.
	 */
	public void setClientContactPhone(final String clientContactPhone) {
		this.clientContactPhone = clientContactPhone;
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
	 * @return Returns the clientIdentifier.
	 */
	public String getClientIdentifier() {
		return clientIdentifier;
	}


	/**
	 * @param clientIdentifier The clientIdentifier to set.
	 */
	public void setClientIdentifier(final String clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
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
	 * @return Returns the clientPhone.
	 */
	public String getClientPhone() {
		return clientPhone;
	}


	/**
	 * @param clientPhone The clientPhone to set.
	 */
	public void setClientPhone(final String clientPhone) {
		this.clientPhone = clientPhone;
	}

	
}

	
