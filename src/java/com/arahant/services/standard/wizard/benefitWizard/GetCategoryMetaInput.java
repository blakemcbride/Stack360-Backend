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

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class GetCategoryMetaInput extends TransmitInputBase {

	@Validation(required = true)
	private String categoryId;
	@Validation(required = false)
	private String employeeId;
	@Validation(required = false)
	private int asOfDate;
	@Validation(required = true)
	private String wizardConfigurationId;
	@Validation(required = true)
	private String bcrId;

	public String getBcrId() {
		return bcrId;
	}

	public void setBcrId(String bcrId) {
		this.bcrId = bcrId;
	}

	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(int asOfDate) {
		this.asOfDate = asOfDate;
	}
}
