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
import com.arahant.business.BPerson;
import com.arahant.services.TransmitInputBase;

public class SaveFlexConfigsAndCostsInput extends TransmitInputBase {

	@Validation(required = false)
	private String employeeId;
	@Validation(required = false)
	private FlexElection[] flexElections;
	@Validation(required = true)
	private String changeReasonId;
	@Validation(type = "date", required = false)
	private int asOfDate;
	@Validation(required = false)
	private String explanation;

	void setData(BPerson bc) {
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public FlexElection[] getFlexElections() {
		if (flexElections == null)
			flexElections = new FlexElection[0];
		return flexElections;
	}

	public void setFlexElections(FlexElection[] flexElections) {
		this.flexElections = flexElections;
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
}
