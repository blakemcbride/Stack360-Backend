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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.peachtreeApp;
import com.arahant.annotation.Validation;
import com.arahant.business.BGlAccount;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class SaveGLAccountInput extends TransmitInputBase {

	@Validation (required=false)
	private String glAccountId;
	@Validation (required=false)
	private String accountNumber;
	@Validation (required=false)
	private String accountName;
	@Validation (required=false)
	private int accountType;
	@Validation (required=false)
	private boolean defaultFlag;
	
	
	/**
	 * @param gl
	 */
	void setData(final BGlAccount gl) {
		gl.setAccountName(accountName);
		gl.setAccountNumber(accountNumber);
		gl.setAccountType(accountType);
		gl.setDefaultFlag(defaultFlag);
	
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
	 * @return Returns the defaultFlag.
	 */
	public boolean getDefaultFlag() {
		return defaultFlag;
	}

	/**
	 * @param defaultFlag The defaultFlag to set.
	 */
	public void setDefaultFlag(final boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
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

	public SaveGLAccountInput() {
		super();
	}

	

	
}

	
