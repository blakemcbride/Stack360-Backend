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
package com.arahant.services.standard.inventory.quote;

import com.arahant.business.BGlAccount;
import com.arahant.services.TransmitReturnBase;




/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class ListGLSalesAccountsReturn extends TransmitReturnBase {

	GlAccountTransmit []glAccountTransmit;
	String defaultAccountId;

	/**
	 * @return Returns the glAccountTransmit.
	 */
	public GlAccountTransmit[] getGlAccountTransmit() {
		return glAccountTransmit;
	}

	/**
	 * @param glAccountTransmit The glAccountTransmit to set.
	 */
	public void setGlAccountTransmit(final GlAccountTransmit[] glAccountTransmit) {
		this.glAccountTransmit = glAccountTransmit;
	}

	/**
	 * @return Returns the defaultAccountId.
	 */
	public String getDefaultAccountId() {
		return defaultAccountId;
	}

	/**
	 * @param defaultAccountId The defaultAccountId to set.
	 */
	public void setDefaultAccountId(final String defaultAccountId) {
		this.defaultAccountId = defaultAccountId;
	}

	void setGLAccounts(final BGlAccount [] accts)
	{

		glAccountTransmit=new GlAccountTransmit[accts.length];

	
		for (int loop=0;loop<glAccountTransmit.length;loop++)
		{
			glAccountTransmit[loop]=new GlAccountTransmit(accts[loop]);
			if (glAccountTransmit[loop].getDefaultFlag()==1)
				setDefaultAccountId(glAccountTransmit[loop].getGlAccountId());
		}

	}
}

	
