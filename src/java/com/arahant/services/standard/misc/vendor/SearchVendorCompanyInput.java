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
package com.arahant.services.standard.misc.vendor;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class SearchVendorCompanyInput extends TransmitInputBase {
	@Validation (table="org_group",column="name",required=false)
	private String name;
	@Validation (table="person",column="fname",required=false)
	private String mainContactFirstName;
	@Validation (table="person",column="lname",required=false)
	private String mainContactLastName;
	@Validation (table="company_base",column="identifier",required=false)
	private String identifier;
	@Validation (required=false)
	private String expenseGLAccountId;
	@Validation (table="vendor",column="account_number",required=false)
	private String accountNumber;
	
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (min=2,max=5,required=false)
	private int mainContactFirstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int mainContactLastNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int identifierSearchType;
	@Validation (min=2,max=5,required=false)
	private int accountNumberSearchType;
	
	
	/**
	 * @return Returns the accountNumberSearchType.
	 */
	public int getAccountNumberSearchType() {
		return accountNumberSearchType;
	}
	/**
	 * @param accountNumberSearchType The accountNumberSearchType to set.
	 */
	public void setAccountNumberSearchType(final int accountNumberSearchType) {
		this.accountNumberSearchType = accountNumberSearchType;
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
	/**
	 * @return Returns the accountNumber.
	 */
	public String getAccountNumber() {
		return modifyForSearch(accountNumber, accountNumberSearchType);
	}
	/**
	 * @param accountNumber The accountNumber to set.
	 */
	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return Returns the expenseGLAccountId.
	 */
	public String getExpenseGLAccountId() {
		return expenseGLAccountId;
	}
	/**
	 * @param expenseGLAccountId The expenseGLAccountId to set.
	 */
	public void setExpenseGLAccountId(final String expenseGLAccountId) {
		this.expenseGLAccountId = expenseGLAccountId;
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
	 * @return Returns the mainContactFirstName.
	 */
	public String getMainContactFirstName() {
		return modifyForSearch(mainContactFirstName, mainContactFirstNameSearchType);
	}
	/**
	 * @param mainContactFirstName The mainContactFirstName to set.
	 */
	public void setMainContactFirstName(final String mainContactFirstName) {
		this.mainContactFirstName = mainContactFirstName;
	}
	/**
	 * @return Returns the mainContactLastName.
	 */
	public String getMainContactLastName() {
		return modifyForSearch(mainContactLastName, mainContactLastNameSearchType);
	}
	/**
	 * @param mainContactLastName The mainContactLastName to set.
	 */
	public void setMainContactLastName(final String mainContactLastName) {
		this.mainContactLastName = mainContactLastName;
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
	
	
}

	
