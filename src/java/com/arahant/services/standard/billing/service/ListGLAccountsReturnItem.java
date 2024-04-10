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
 * Created on Jun 12, 2007
 * 
 */
package com.arahant.services.standard.billing.service;
import com.arahant.business.BGlAccount;


/**
 * 
 *
 * Created on Jun 12, 2007
 *
 */
public class ListGLAccountsReturnItem {

	private String accountNumber, accountName, accountId, accountTypeFormatted ;

	/**
	 * @return Returns the accountId.
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId The accountId to set.
	 */
	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return Returns the accountName.
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName The accountName to set.
	 */
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return Returns the accountNumber.
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber The accountNumber to set.
	 */
	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return Returns the accountTypeFormatted.
	 */
	public String getAccountTypeFormatted() {
		return accountTypeFormatted;
	}

	/**
	 * @param accountTypeFormatted The accountTypeFormatted to set.
	 */
	public void setAccountTypeFormatted(final String accountTypeFormatted) {
		this.accountTypeFormatted = accountTypeFormatted;
	}

	public ListGLAccountsReturnItem() {
		super();
	}

	/**
	 * @param account
	 */
	ListGLAccountsReturnItem(final BGlAccount a) {
		accountId=a.getGlAccountId();
		accountName=a.getAccountName();
		accountNumber=a.getAccountNumber();
		accountTypeFormatted=a.getAccountTypeFormatted();
	}
}

	
