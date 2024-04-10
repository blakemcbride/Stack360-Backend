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
package com.arahant.services.standard.billing.glAccount;
import com.arahant.business.BGlAccount;



/**
 * 
 *
 * Created on Jun 12, 2007
 *
 */
public class ListGLAccountsItem {

	/**
	 * @return Returns the defaultForType.
	 */
	public boolean getDefaultForType() {
		return defaultForType;
	}

	public ListGLAccountsItem() {
		super();
	}
	
	/**
	 * @param account
	 */
	ListGLAccountsItem(final BGlAccount a) {
		accountNumber=a.getAccountNumber();
		accountName=a.getAccountName();
		accountId=a.getGlAccountId();
		accountType=a.getAccountType();
		accountTypeFormatted=a.getAccountTypeFormatted();
		defaultForTypeFormatted=(a.getDefaultFlag()==1)?"Yes":"No";
		defaultForType=a.getDefaultFlag()==1;
		allCompanies = a.getCompany() == null;
	}

	private String accountNumber, accountName, accountId, defaultForTypeFormatted ;
	private int accountType;
	private String accountTypeFormatted ;
	private boolean defaultForType, allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}


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
	 * @return Returns the accountType.
	 */
	public int getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType The accountType to set.
	 */
	public void setAccountType(final int accountType) {
		this.accountType = accountType;
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

	/**
	 * @return Returns the defaultForTypeFormatted.
	 */
	public String getDefaultForTypeFormatted() {
		return defaultForTypeFormatted;
	}

	/**
	 * @param defaultForTypeFormatted The defaultForTypeFormatted to set.
	 */
	public void setDefaultForTypeFormatted(final String defaultForTypeFormatted) {
		this.defaultForTypeFormatted = defaultForTypeFormatted;
	}

	/**
	 * @param defaultForType The defaultForType to set.
	 */
	public void setDefaultForType(final boolean defaultForType) {
		this.defaultForType = defaultForType;
	}
	
	
}

	
