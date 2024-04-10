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
 * Created on Mar 19, 2007
 * 
 */
package com.arahant.services.standard.hr.hrWageHistory;
import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Mar 19, 2007
 *
 */
public class GetLastWageReturn extends TransmitReturnBase {

	private String lastPosition;
	private double lastWageAmount;
	private String lastWageTypeId;
	/**
	 * @return Returns the lastPosition.
	 */
	public String getLastPosition() {
		return lastPosition;
	}
	/**
	 * @param lastPosition The lastPosition to set.
	 */
	public void setLastPosition(final String lastPosition) {
		this.lastPosition = lastPosition;
	}
	/**
	 * @return Returns the lastWageAmount.
	 */
	public double getLastWageAmount() {
		return lastWageAmount;
	}
	/**
	 * @param lastWageAmount The lastWageAmount to set.
	 */
	public void setLastWageAmount(final double lastWageAmount) {
		this.lastWageAmount = lastWageAmount;
	}

	public String getLastWageTypeId() {
		return lastWageTypeId;
	}

	public void setLastWageTypeId(String lastWageTypeId) {
		this.lastWageTypeId = lastWageTypeId;
	}


	/**
	 * @param employee
	 */
	void setData(final BEmployee employee) {
		lastPosition=employee.getPositionId();
		lastWageAmount=employee.getCurrentSalary();
		lastWageTypeId=employee.getWageTypeId();
	}
}

	
