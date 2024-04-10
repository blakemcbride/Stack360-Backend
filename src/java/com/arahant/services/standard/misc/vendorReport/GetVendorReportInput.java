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
 * Created on Mar 29, 2007
 * 
 */
package com.arahant.services.standard.misc.vendorReport;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Mar 29, 2007
 *
 */
public class GetVendorReportInput extends TransmitInputBase {

	public GetVendorReportInput() {
		super();
	}
	/*
	 * Fields on Vendor List (* means always appears) If fields selected they should be in the order shown below
----------------------------------------------
company_base
    identifier
org_group
    name *
    (primary contact name)
    (company phone number)
address
    street
    city, state, zip        (use two lines and one selection box for all
three fields)
vendor_company
    account number


Sort options:  identifier, name  (default name)
	 */
	
@Validation (required=false)
	private boolean identifier;
@Validation (required=false)
	private boolean primaryContact;
@Validation (required=false)
	private boolean companyPhone;
@Validation (required=false)
	private boolean address;
@Validation (required=false)
	private boolean accountNumber;
@Validation (required=false)
	private int sortType;
@Validation (required=false)
    private boolean sortAsc;
	/**
	 * @return Returns the accountNumber.
	 */
	public boolean isAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber The accountNumber to set.
	 */
	public void setAccountNumber(final boolean accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return Returns the address.
	 */
	public boolean isAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(final boolean address) {
		this.address = address;
	}
	/**
	 * @return Returns the companyPhone.
	 */
	public boolean isCompanyPhone() {
		return companyPhone;
	}
	/**
	 * @param companyPhone The companyPhone to set.
	 */
	public void setCompanyPhone(final boolean companyPhone) {
		this.companyPhone = companyPhone;
	}
	/**
	 * @return Returns the identifier.
	 */
	public boolean isIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(final boolean identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return Returns the sortAsc.
	 */
	public boolean isSortAsc() {
		return sortAsc;
	}
	/**
	 * @param sortAsc The sortAsc to set.
	 */
	public void setSortAsc(final boolean sortAsc) {
		this.sortAsc = sortAsc;
	}
	/**
	 * @return Returns the sortType.
	 */
	public int getSortType() {
		return sortType;
	}
	/**
	 * @param sortType The sortType to set.
	 */
	public void setSortType(final int sortType) {
		this.sortType = sortType;
	}
	/**
	 * @return Returns the primaryContact.
	 */
	public boolean isPrimaryContact() {
		return primaryContact;
	}
	/**
	 * @param primaryContact The primaryContact to set.
	 */
	public void setPrimaryContact(final boolean primaryContact) {
		this.primaryContact = primaryContact;
	}
}

	
