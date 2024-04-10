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
package com.arahant.services.standard.site.changePassword;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class ChangePasswordInput extends TransmitInputBase {
	@Validation(required=true)
	private String oldPassword;
	@Validation(required=true)
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return Returns the newPassword.
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword The newPassword to set.
	 */
	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}
}

	
