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
package com.arahant.services.standard.crm.clientParent;
import com.arahant.annotation.Validation;

import com.arahant.business.interfaces.ISearchClientCompanies;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class SearchClientCompanyInput extends TransmitInputBase implements ISearchClientCompanies {

	@Validation (table="org_group",column="name",required=false)
	private String name;
	@Validation (table="company_base",column="identifier",required=false)
	private String id;

	@Validation (table="person",column="fname",required=false)
	private String mainContactFirstName;
	@Validation (table="person",column="lname",required=false)
	private String mainContactLastName;
	

	@Validation (min=2,max=5,required=false)
	private int idSearchType;
	@Validation (min=2,max=5,required=false)
	private int mainContactFirstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int mainContactLastNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	
	// 0 = any, 1 = active, 2 = inactive
	@Validation (min=0,max=2,required=false)
	private int status;
	
	@Validation (required=false)
	private String sortOn; 
	@Validation (required=false)
	private boolean sortAsc;

	public boolean getSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public String getSortOn() {
		return sortOn;
	}

	public void setSortOn(String sortOn) {
		this.sortOn = sortOn;
	}
	
	/**
	 * @return Returns the idSearchType.
	 */
	public int getIdSearchType() {
		return idSearchType;
	}

	/**
	 * @param idSearchType The idSearchType to set.
	 */
	public void setIdSearchType(final int idSearchType) {
		this.idSearchType = idSearchType;
	}

	/**
	 * @return Returns the mainContactFirstNameSearchType.
	 */
	public int getMainContactFirstNameSearchType() {
		return mainContactFirstNameSearchType;
	}

	/**
	 * @param mainContactFirstNameSearchType The mainContactFirstNameSearchType to set.
	 */
	public void setMainContactFirstNameSearchType(final int mainContactFirstNameSearchType) {
		this.mainContactFirstNameSearchType = mainContactFirstNameSearchType;
	}

	/**
	 * @return Returns the mainContactLastNameSearchType.
	 */
	public int getMainContactLastNameSearchType() {
		return mainContactLastNameSearchType;
	}

	/**
	 * @param mainContactLastNameSearchType The mainContactLastNameSearchType to set.
	 */
	public void setMainContactLastNameSearchType(final int mainContactLastNameSearchType) {
		this.mainContactLastNameSearchType = mainContactLastNameSearchType;
	}

	/**
	 * @return Returns the nameSearchType.
	 */
	public int getNameSearchType() {
		return nameSearchType;
	}

	/**
	 * @param nameSearchType The nameSearchType to set.
	 */
	public void setNameSearchType(final int nameSearchType) {
		this.nameSearchType = nameSearchType;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#getMainContactFirstName()
	 */
	public String getMainContactFirstName() {
		return modifyForSearch(mainContactFirstName, mainContactFirstNameSearchType);
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#setMainContactFirstName(java.lang.String)
	 */
	public void setMainContactFirstName(final String mainContactFirstName) {
		this.mainContactFirstName = mainContactFirstName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#getMainContactLastName()
	 */
	public String getMainContactLastName() {
		return modifyForSearch(mainContactLastName,mainContactLastNameSearchType);
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#setMainContactLastName(java.lang.String)
	 */
	public void setMainContactLastName(final String mainContactLastName) {
		this.mainContactLastName = mainContactLastName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#getId()
	 */
	public String getId() {
		return modifyForSearch(id, idSearchType);
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#setId(java.lang.String)
	 */
	public void setId(final String vendorId) {
		this.id = vendorId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#getName()
	 */
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#setName(java.lang.String)
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#getStatus()
	 */
	public int getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchClientCompanies#setStatus(int)
	 */
	public void setStatus(final int status) {
		this.status = status;
	}
	
}

	
