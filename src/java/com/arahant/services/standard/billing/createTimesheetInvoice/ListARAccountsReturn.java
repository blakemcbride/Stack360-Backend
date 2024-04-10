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
import com.arahant.services.TransmitReturnBase;

public class ListARAccountsReturn extends TransmitReturnBase {

	private ListARAccountsReturnItem[] glAccountTransmit;
	private String defaultAccountId;
	private Boolean companyUsesAR;
	private short paymentTerms;

	public ListARAccountsReturnItem[] getGlAccountTransmit() {
		return glAccountTransmit;
	}

	public void setGlAccountTransmit(final ListARAccountsReturnItem[] glAccountTransmit) {
		this.glAccountTransmit = glAccountTransmit;
	}

	public String getDefaultAccountId() {
		return defaultAccountId;
	}

	public void setDefaultAccountId(final String defaultAccountId) {
		this.defaultAccountId = defaultAccountId;
	}

	public Boolean getCompanyUsesAR() {
		return companyUsesAR;
	}

	public void setCompanyUsesAR(final Boolean companyUsesAR) {
		this.companyUsesAR = companyUsesAR;
	}

	public short getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(short paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	/**
	 * @param accounts
	 */
	void setGLAccounts(final BGlAccount[] accts) {
		glAccountTransmit = new ListARAccountsReturnItem[accts.length];

		for (int loop = 0; loop < glAccountTransmit.length; loop++) {
			glAccountTransmit[loop] = new ListARAccountsReturnItem(accts[loop]);
			if (glAccountTransmit[loop].getDefaultFlag() == 1)
				setDefaultAccountId(glAccountTransmit[loop].getGlAccountId());
		}
	}
}
