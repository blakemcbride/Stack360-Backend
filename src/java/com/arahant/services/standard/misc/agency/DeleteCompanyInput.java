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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agency;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class DeleteCompanyInput extends TransmitInputBase {
	@Validation (required=false)
	private String clientCompanyId[];

	/**
	 * @return Returns the clientCompanyId.
	 */
	public String[] getVendorCompanyId() {
            if (clientCompanyId==null)
                return new String[0];
		return clientCompanyId;
	}

	/**
	 * @param clientCompanyId The clientCompanyId to set.
	 */
	public void setVendorCompanyId(final String[] clientCompanyId) {
		this.clientCompanyId = clientCompanyId;
	}

	
}

	
