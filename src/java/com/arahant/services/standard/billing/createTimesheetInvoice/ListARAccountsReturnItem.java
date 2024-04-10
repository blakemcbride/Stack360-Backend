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

import com.arahant.business.BGlAccount;

public class ListARAccountsReturnItem {

	private String glAccountId;
	private String accountNumber;
	private String accountName;
	private int accountType;
	private short defaultFlag;

	public ListARAccountsReturnItem() {
	}

	/**
	 * @param account
	 */
	ListARAccountsReturnItem(final BGlAccount gla) {
		glAccountId = gla.getGlAccountId();
		accountNumber = gla.getAccountNumber();
		accountName = gla.getAccountName();
		accountType = gla.getAccountType();
		defaultFlag = gla.getDefaultFlag();
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#getAccountName()
	 */
	public String getAccountName() {
		return accountName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#setAccountName(java.lang.String)
	 */
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#getAccountNumber()
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#setAccountNumber(java.lang.String)
	 */
	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#getAccountType()
	 */
	public int getAccountType() {
		return accountType;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#setAccountType(int)
	 */
	public void setAccountType(final int accountType) {
		this.accountType = accountType;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#getGlAccountId()
	 */
	public String getGlAccountId() {
		return glAccountId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#setGlAccountId(java.lang.String)
	 */
	public void setGlAccountId(final String glAccountId) {
		this.glAccountId = glAccountId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#getDefaultFlag()
	 */
	public short getDefaultFlag() {
		return defaultFlag;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IGLAccountTransmit#setDefaultFlag(short)
	 */
	public void setDefaultFlag(final short defValue) {
		this.defaultFlag = defValue;
	}
}
