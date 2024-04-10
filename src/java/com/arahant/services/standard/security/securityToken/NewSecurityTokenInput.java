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
 * Created on Jun 6, 2007
 * 
 */
package com.arahant.services.standard.security.securityToken;
import com.arahant.annotation.Validation;
import com.arahant.business.BRight;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Jun 6, 2007
 *
 */
public class NewSecurityTokenInput extends TransmitInputBase {

	@Validation (required=false)
	private String name;
	@Validation (required=false)
	private String description;

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param b
	 */
	void setData(final BRight b) {
		b.setDescription(description);
		b.setIdentifier(name);
	}
	
	
}

	
