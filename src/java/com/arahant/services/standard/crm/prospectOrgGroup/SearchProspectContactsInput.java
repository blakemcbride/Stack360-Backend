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
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.crm.prospectOrgGroup;
import com.arahant.annotation.Validation;

import com.arahant.business.interfaces.IContactSearchCriteria;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class SearchProspectContactsInput extends TransmitInputBase implements IContactSearchCriteria{
	@Validation (required=false)
	private int associatedIndicator;
	@Validation (table="person",column="fname",required=false)
	private String firstName;
	@Validation (table="person",column="lname",required=false)
	private String lastName;
	@Validation (min=1,max=16,required=false)
	private String orgGroupId;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;

	
	/**
	 * @return Returns the firstNameSearchType.
	 */
	public int getFirstNameSearchType() {
		return firstNameSearchType;
	}
	/**
	 * @param firstNameSearchType The firstNameSearchType to set.
	 */
	public void setFirstNameSearchType(final int firstNameSearchType) {
		this.firstNameSearchType = firstNameSearchType;
	}
	/**
	 * @return Returns the lastNameSearchType.
	 */
	public int getLastNameSearchType() {
		return lastNameSearchType;
	}
	/**
	 * @param lastNameSearchType The lastNameSearchType to set.
	 */
	public void setLastNameSearchType(final int lastNameSearchType) {
		this.lastNameSearchType = lastNameSearchType;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#getAssociatedIndicator()
	 */
	public int getAssociatedIndicator() {
		return associatedIndicator;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#setAssociatedIndicator(int)
	 */
	public void setAssociatedIndicator(final int associated_indicator) {
		this.associatedIndicator = associated_indicator;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#getFirstName()
	 */
	public String getFirstName() {
		return modifyForSearch(firstName, firstNameSearchType);
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#setFirstName(java.lang.String)
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#getLastName()
	 */
	public String getLastName() {
		return modifyForSearch(lastName, lastNameSearchType);
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#setLastName(java.lang.String)
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#getOrgGroupId()
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.clientOrgGroup.IContactSearchCriteria#setOrgGroupId(java.lang.String)
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	
}

	
