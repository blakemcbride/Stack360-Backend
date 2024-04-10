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
import com.arahant.business.BHRBenefit;
import com.arahant.services.TransmitInputBase;

public class SaveConfigAndEnrolleesHumanaInput extends TransmitInputBase {

	@Validation(required = false)
	private String benefitId;
	@Validation(required = false)
	private String configId;
	@Validation(required = false)
	private String[] enrolleeIds;
	@Validation(required = false)
	private String employeeId;
	@Validation(type = "date", required = false)
	private int qualifyingEventDate;
	@Validation(required = true)
	private String changeReasonId;
	@Validation(required = false)
	private double coverageAmount;
	@Validation(required = false)
	private boolean doEnrollment;
	@Validation(required = true)
	private String categoryId;
	@Validation(required = true)
	private String explanation;
	@Validation(required = false)
	private boolean autoApproveDeclines;

	void setData(BHRBenefit bc) {
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public boolean getDoEnrollment() {
		return doEnrollment;
	}

	public void setDoEnrollment(boolean doEnrollment) {
		this.doEnrollment = doEnrollment;
	}

	public double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
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

	public int getQualifyingEventDate() {
		return qualifyingEventDate;
	}

	public void setQualifyingEventDate(int qualifyingEventDate) {
		this.qualifyingEventDate = qualifyingEventDate;
	}

	public String getChangeReasonId() {
		return changeReasonId;
	}

	public void setChangeReasonId(String changeReasonId) {
		this.changeReasonId = changeReasonId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public boolean getAutoApproveDeclines() {
		return autoApproveDeclines;
	}

	public void setAutoApproveDeclines(boolean autoApproveDeclines) {
		this.autoApproveDeclines = autoApproveDeclines;
	}
}
