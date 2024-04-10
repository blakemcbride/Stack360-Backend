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

import com.arahant.business.BGlAccount.GLAccountType;


/**
 * 
 *
 * Created on Jun 12, 2007
 *
 */
public class ListGLAccountTypesItem {


	public ListGLAccountTypesItem() {
		super();
	}
	
	/**
	 * @param type
	 */
	ListGLAccountTypesItem(final GLAccountType type) {
		accountType=type.type;
		accountTypeFormatted=type.typeFormatted;
	}

	private int accountType;
	private String accountTypeFormatted;
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
	
}

	
