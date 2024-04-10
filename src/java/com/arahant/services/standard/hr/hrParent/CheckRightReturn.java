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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.hr.hrParent;

import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CheckRightReturn extends TransmitReturnBase {
	private int AccessLevel;
	private String checkMaxEmployees;
	private boolean canEditHicNumber;

	public boolean getCanEditHicNumber() {
		return canEditHicNumber;
	}

	public void setCanEditHicNumber(boolean canEditHicNumber) {
		this.canEditHicNumber = canEditHicNumber;
	}

	public CheckRightReturn(final String msg) {
		super(msg);
	}

	public CheckRightReturn()
	{
		super();
	}
	/**
	 * @return Returns the accessLevel.
	 */
	public int getAccessLevel() {
		return AccessLevel;
	}

	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final int accessLevel) {
		AccessLevel = accessLevel;
	}

	public String getCheckMaxEmployees() {
		return this.checkMaxEmployees;
	}

	public void setCheckMaxEmployees(String checkMaxEmployees) {
		this.checkMaxEmployees = checkMaxEmployees;
	}
}

	
