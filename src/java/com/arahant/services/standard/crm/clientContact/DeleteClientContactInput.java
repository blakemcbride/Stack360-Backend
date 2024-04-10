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


package com.arahant.services.standard.crm.clientContact;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class DeleteClientContactInput extends TransmitInputBase {

	@Validation(required = false)
	private String ids[];
	@Validation(required = true)
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return Returns the ids.
	 */
	public String[] getIds() {
		if (ids == null)
			return new String[0];
		return ids;
	}

	/**
	 * @param ids The ids to set.
	 */
	public void setIds(final String[] ids) {
		this.ids = ids;
	}
}
