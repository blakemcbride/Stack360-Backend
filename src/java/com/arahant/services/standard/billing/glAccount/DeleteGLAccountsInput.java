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

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class DeleteGLAccountsInput extends TransmitInputBase {

@Validation (required=false)
	private String accountIds[];

	/**
	 * @return Returns the accountIds.
	 */
	public String[] getAccountIds() {
            if (accountIds==null)
                return new String[0];
		return accountIds;
	}

	/**
	 * @param accountIds The accountIds to set.
	 */
	public void setAccountIds(final String[] accountIds) {
		this.accountIds = accountIds;
	}
}

	
