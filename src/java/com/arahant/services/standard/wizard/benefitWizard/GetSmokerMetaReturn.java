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
 *
 */
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.services.TransmitReturnBase;

/**
 *
 *
 *
 */
public class GetSmokerMetaReturn extends TransmitReturnBase {

	private boolean hasSpouse;
	private boolean employeeNonSmoker;
	private boolean spouseNonSmoker;
	private boolean employeeProgram;
	private boolean spouseProgram;

	public boolean getHasSpouse() {
		return hasSpouse;
	}

	public void setHasSpouse(boolean hasSpouse) {
		this.hasSpouse = hasSpouse;
	}

	/**
	 * @return the employeeNonSmoker
	 */
	public boolean getEmployeeNonSmoker() {
		return employeeNonSmoker;
	}

	/**
	 * @param employeeNonSmoker the employeeNonSmoker to set
	 */
	public void setEmployeeNonSmoker(boolean employeeNonSmoker) {
		this.employeeNonSmoker = employeeNonSmoker;
	}

	/**
	 * @return the spouseNonSmoker
	 */
	public boolean getSpouseNonSmoker() {
		return spouseNonSmoker;
	}

	/**
	 * @param spouseNonSmoker the spouseNonSmoker to set
	 */
	public void setSpouseNonSmoker(boolean spouseNonSmoker) {
		this.spouseNonSmoker = spouseNonSmoker;
	}

	public boolean getEmployeeProgram() {
		return employeeProgram;
	}

	public void setEmployeeProgram(boolean employeeProgram) {
		this.employeeProgram = employeeProgram;
	}

	public boolean getSpouseProgram() {
		return spouseProgram;
	}

	public void setSpouseProgram(boolean spouseProgram) {
		this.spouseProgram = spouseProgram;
	}
}
