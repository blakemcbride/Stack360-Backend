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
package com.arahant.services.standard.crm.clientListReport;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Mar 29, 2007
 *
 */
public class GetClientReportInput extends TransmitInputBase {

	//company_base
	@Validation (required=false)
    private boolean identifier;
    //org_group
    //name *
	@Validation (required=false)
    private boolean primaryContactName;
	@Validation (required=false)
    private boolean companyPhoneNumber;
	@Validation (required=false)
    private boolean address;
	@Validation (required=false)
    private boolean contractDate;
	@Validation (required=false)
    private boolean billingRate;
	@Validation (min=1,max=2,required=false)
    private int sortType;//Sort options:  identifier, name, or contract_date (default is name)
	@Validation (required=false)
    private boolean sortAsc;
    
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
	 * @return Returns the billingRate.
	 */
	public boolean isBillingRate() {
		return billingRate;
	}

	/**
	 * @param billingRate The billingRate to set.
	 */
	public void setBillingRate(final boolean billingRate) {
		this.billingRate = billingRate;
	}

	/**
	 * @return Returns the companyPhoneNumber.
	 */
	public boolean isCompanyPhoneNumber() {
		return companyPhoneNumber;
	}

	/**
	 * @param companyPhoneNumber The companyPhoneNumber to set.
	 */
	public void setCompanyPhoneNumber(final boolean companyPhoneNumber) {
		this.companyPhoneNumber = companyPhoneNumber;
	}

	/**
	 * @return Returns the contractDate.
	 */
	public boolean isContractDate() {
		return contractDate;
	}

	/**
	 * @param contractDate The contractDate to set.
	 */
	public void setContractDate(final boolean contractDate) {
		this.contractDate = contractDate;
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
	 * @return Returns the primaryContactName.
	 */
	public boolean isPrimaryContactName() {
		return primaryContactName;
	}

	/**
	 * @param primaryContactName The primaryContactName to set.
	 */
	public void setPrimaryContactName(final boolean primaryContactName) {
		this.primaryContactName = primaryContactName;
	}

	public GetClientReportInput() {
		super();
	}
}

	
