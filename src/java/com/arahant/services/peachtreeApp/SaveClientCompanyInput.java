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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.peachtreeApp;
import com.arahant.annotation.Validation;

import com.arahant.business.BCompanyBase;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class SaveClientCompanyInput extends TransmitInputBase {
	@Validation (required=false)
	private String name;
	@Validation (required=false)
	private String identifier;
	@Validation (required=false)
	private String street;
	@Validation (required=false)
	private String city;
	@Validation (required=false)
	private String state;
	@Validation (required=false)
	private String zip;
	@Validation (required=false)
	private String orgGroupId;


	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}


	/**
	 * @param city The city to set.
	 */
	public void setCity(final String city) {
		this.city = city;
	}


	
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}


	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}


	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}


	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}


	/**
	 * @param state The state to set.
	 */
	public void setState(final String state) {
		this.state = state;
	}


	/**
	 * @return Returns the street.
	 */
	public String getStreet() {
		return street;
	}


	/**
	 * @param street The street to set.
	 */
	public void setStreet(final String street) {
		this.street = street;
	}


	/**
	 * @return Returns the zip.
	 */
	public String getZip() {
		return zip;
	}


	/**
	 * @param zip The zip to set.
	 */
	public void setZip(final String zip) {
		this.zip = zip;
	}


	/**
	 * @param bcc
	 * @throws ArahantException 
	 */
	void makeClientCompany(final BCompanyBase bcc) throws ArahantException {

		bcc.setIdentifier(identifier);

		bcc.setName(name);
		
		bcc.setStreet(street);
		bcc.setCity(city);
		bcc.setState(state);
		bcc.setZip(zip);

	}


	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}


	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	
}

	
