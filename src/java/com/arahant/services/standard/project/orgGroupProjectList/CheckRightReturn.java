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
package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CheckRightReturn extends TransmitReturnBase {
    private int statusAccessLevel;
	private int clientPriorityAccessLevel;
	private int companyAccessLevel;

	public int getCompanyAccessLevel() {
		return companyAccessLevel;
	}

	public void setCompanyAccessLevel(int companyAccessLevel) {
		this.companyAccessLevel = companyAccessLevel;
	}
	
	
	

	public int getClientPriorityAccessLevel() {
		return clientPriorityAccessLevel;
	}

	public void setClientPriorityAccessLevel(int clientPriorityAccessLevel) {
		this.clientPriorityAccessLevel = clientPriorityAccessLevel;
	}

	public int getStatusAccessLevel() {
		return statusAccessLevel;
	}

	public void setStatusAccessLevel(int statusAccessLevel) {
		this.statusAccessLevel = statusAccessLevel;
	}

		/**
	 * @param string
	 */
	public CheckRightReturn(final String msg) {
		super(msg);
	}

	public CheckRightReturn()
	{
		super();
	}
	
}

	
