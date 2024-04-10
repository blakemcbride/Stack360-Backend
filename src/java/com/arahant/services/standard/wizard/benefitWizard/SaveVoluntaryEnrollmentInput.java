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

public class SaveVoluntaryEnrollmentInput extends TransmitInputBase {

	@Validation(required = true)
	private String benefitId;
	@Validation(required = true)
	private String changeReasonId;
	@Validation(type = "date", required = true)
	private int asOfDate;
	@Validation(required = false)
	private String employeeId;
	@Validation(required = false)
	private String configId;
	@Validation(required = false)
	private String[] enrolleeIds;
	@Validation(required = false)
	private double annualCoverage;
	@Validation(required = false)
	private String explanation;

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getChangeReasonId() {
		return changeReasonId;
	}

	public void setChangeReasonId(String changeReasonId) {
		this.changeReasonId = changeReasonId;
	}

	public int getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(int asOfDate) {
		this.asOfDate = asOfDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String[] getEnrolleeIds() {
		if (enrolleeIds == null)
			enrolleeIds = new String[0];
		return enrolleeIds;
	}

	public void setEnrolleeIds(String[] enrolleeIds) {
		this.enrolleeIds = enrolleeIds;
	}

	public double getAnnualCoverage() {
		return annualCoverage;
	}

	public void setAnnualCoverage(double annualCoverage) {
		this.annualCoverage = annualCoverage;
	}
}
