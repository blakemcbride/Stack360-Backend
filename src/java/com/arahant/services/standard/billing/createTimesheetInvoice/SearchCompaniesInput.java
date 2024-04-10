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


package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on May 6, 2007
 *
 */
public class SearchCompaniesInput extends TransmitInputBase {

    @Validation (required=false)
	private String name;
    @Validation(required=false)
	private int nameSearchType;
    @Validation(required=false)
	private String identifier;
    @Validation(required=false)
	private int identifierSearchType;
    @Validation(required=false)
	private int billableItemsIndicator; // 1 = has ready billable items, 2 = has no billable items, 0 = either
    @Validation(type="date", required=false)
    private int billableFromDate;  // flag clients with billable time on or after this date with 2 astrix
	
	
	public SearchCompaniesInput() {
	}


	/**
	 * @return Returns the billableItemsIndicator.
	 */
	public int getBillableItemsIndicator() {
		return billableItemsIndicator;
	}


	/**
	 * @param billableItemsIndicator The billableItemsIndicator to set.
	 */
	public void setBillableItemsIndicator(final int billableItemsIndicator) {
		this.billableItemsIndicator = billableItemsIndicator;
	}


	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return modifyForSearch(identifier, identifierSearchType);
	}


	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}


	/**
	 * @return Returns the identifierSearchType.
	 */
	public int getIdentifierSearchType() {
		return identifierSearchType;
	}


	/**
	 * @param identifierSearchType The identifierSearchType to set.
	 */
	public void setIdentifierSearchType(final int identifierSearchType) {
		this.identifierSearchType = identifierSearchType;
	}


	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}


	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
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

    public int getBillableFromDate() {
        return billableFromDate;
    }

    public void setBillableFromDate(int billableFromDate) {
        this.billableFromDate = billableFromDate;
    }

}

	
