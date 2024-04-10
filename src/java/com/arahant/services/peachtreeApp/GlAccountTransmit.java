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
 * Created on Oct 5, 2006
 * 
 */
package com.arahant.services.peachtreeApp;

import com.arahant.business.BGlAccount;



/**
 * 
 *
 * Created on Oct 5, 2006
 *
 */
public class GlAccountTransmit  {
	private String glAccountId;

	private String accountNumber;

	private String accountName;

	private int accountType;
	
	private short defaultFlag;

	
	public GlAccountTransmit()
	{
		;
	}

	/**
	 * @param account
	 */
	GlAccountTransmit(final BGlAccount gla) {
		super();
		if (gla==null)
			return;
		glAccountId=gla.getGlAccountId();
		accountNumber=gla.getAccountNumber();
		accountName=gla.getAccountName();
		accountType=gla.getAccountType();
		defaultFlag=gla.getDefaultFlag();
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
	 * @return Returns the glAccountId.
	 */
	public String getGlAccountId() {
		return glAccountId;
	}

	/**
	 * @param glAccountId The glAccountId to set.
	 */
	public void setGlAccountId(final String glAccountId) {
		this.glAccountId = glAccountId;
	}

	/**
	 * @return Returns the defaultFlag.
	 */
	public short getDefaultFlag() {
		return defaultFlag;
	}

	/**
	 * @param defaultFlag The defaultFlag to set.
	 */
	public void setDefaultFlag(final short defValue) {
		this.defaultFlag = defValue;
	}
}

	
