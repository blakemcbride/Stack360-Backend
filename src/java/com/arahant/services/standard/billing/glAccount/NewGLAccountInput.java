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
 * 
 */
package com.arahant.services.standard.billing.glAccount;
import com.arahant.annotation.Validation;

import com.arahant.business.BGlAccount;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewGLAccountInput extends TransmitInputBase {

	@Validation (table="gl_account",column="account_number",required=true)
	private String accountNumber;
	@Validation (table="gl_account",column="account_name",required=true)
	private String accountName;
	@Validation (min=0,max=30,required=false)
	private int accountType;
	@Validation (required=false)
	private boolean defaultForType;
	@Validation (required=false)
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
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
	 * @return Returns the defaultForType.
	 */
	public boolean isDefaultForType() {
		return defaultForType;
	}
	/**
	 * @param defaultForType The defaultForType to set.
	 */
	public void setDefaultForType(final boolean defaultForType) {
		this.defaultForType = defaultForType;
	}
	/**
	 * @param b
	 */
	void setData(final BGlAccount b) {
		b.setAccountName(accountName);
		b.setAccountNumber(accountNumber);
		b.setAccountType(accountType);
		b.setDefaultFlag(defaultForType);
		if(allCompanies)
			b.setCompany(null);
		else
			b.setCompany(ArahantSession.getHSU().getCurrentCompany());

	}
}

	
