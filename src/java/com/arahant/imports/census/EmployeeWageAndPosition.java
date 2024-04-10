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


package com.arahant.imports.census;

public class EmployeeWageAndPosition {

	private String wageTypeId;
	private double wageAmount;
	private int wageStartDate;
	private String positionId;

	/**
	 * Creates an employee wage & position representation
	 *
	 * @param wageTypeId required wage type id that must exist already
	 * @param wageAmount requied wage amout
	 * @param wageStartDate required wage start date
	 * @param positionId required position id that must exist already
	 */
	public EmployeeWageAndPosition(String wageTypeId, double wageAmount, int wageStartDate, String positionId) {
		this.wageTypeId = wageTypeId;
		this.wageAmount = wageAmount;
		this.wageStartDate = wageStartDate;
		this.positionId = positionId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public double getWageAmount() {
		return wageAmount;
	}

	public void setWageAmount(double wageAmount) {
		this.wageAmount = wageAmount;
	}

	public int getWageStartDate() {
		return wageStartDate;
	}

	public void setWageStartDate(int wageStartDate) {
		this.wageStartDate = wageStartDate;
	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}
}
