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
 * Created on Dec 4, 2007
 * 
 */
package com.arahant.services.standard.project.projectBilling;

import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Dec 4, 2007
 *
 */
public class CheckRightReturn extends TransmitReturnBase {
	private int AccessLevel;
	private int billableAccessLevel;
	private int estimateAccessLevel;
	private int estimateApprovalAccessLevel;
	private int defaultServiceAccessLevel;
		
	
	
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

	public int getBillableAccessLevel() {
		return billableAccessLevel;
	}

	public void setBillableAccessLevel(int billableAccessLevel) {
		this.billableAccessLevel = billableAccessLevel;
	}

	public int getEstimateAccessLevel() {
		return estimateAccessLevel;
	}

	public void setEstimateAccessLevel(int estimateAccessLevel) {
		this.estimateAccessLevel = estimateAccessLevel;
	}

	public int getEstimateApprovalAccessLevel() {
		return estimateApprovalAccessLevel;
	}

	public void setEstimateApprovalAccessLevel(int estimateApprovalAccessLevel) {
		this.estimateApprovalAccessLevel = estimateApprovalAccessLevel;
	}

	public int getDefaultServiceAccessLevel() {
		return defaultServiceAccessLevel;
	}

	public void setDefaultServiceAccessLevel(int defaultServiceAccessLevel) {
		this.defaultServiceAccessLevel = defaultServiceAccessLevel;
	}
	
	
}

	
