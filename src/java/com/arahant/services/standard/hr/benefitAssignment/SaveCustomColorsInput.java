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
package com.arahant.services.standard.hr.benefitAssignment;
import com.arahant.annotation.Validation;

import com.arahant.beans.UserAttribute;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Jan  21, 2008
 *
 */
public class SaveCustomColorsInput extends TransmitInputBase {

	void setData(BPerson person)
	{
		person.savePreference(UserAttribute.BENEFIT_ASSIGNMENT_APPROVED_BENEFIT_COLOR, approvedBenefitColor);
		person.savePreference(UserAttribute.BENEFIT_ASSIGNMENT_APPROVED_DECLINE_COLOR, approvedDeclineColor);
		person.savePreference(UserAttribute.BENEFIT_ASSIGNMENT_NOT_YET_APPROVED_COLOR, notYetApprovedColor);
	}
	
	@Validation (required=false)
	private int approvedBenefitColor;
	@Validation (required=false)
	private int approvedDeclineColor;
	@Validation (required=false)
	private int notYetApprovedColor;

	public int getApprovedBenefitColor() {
		return approvedBenefitColor;
	}

	public void setApprovedBenefitColor(int approvedBenefitColor) {
		this.approvedBenefitColor = approvedBenefitColor;
	}

	public int getApprovedDeclineColor() {
		return approvedDeclineColor;
	}

	public void setApprovedDeclineColor(int approvedDeclineColor) {
		this.approvedDeclineColor = approvedDeclineColor;
	}

	public int getNotYetApprovedColor() {
		return notYetApprovedColor;
	}

	public void setNotYetApprovedColor(int notYetApprovedColor) {
		this.notYetApprovedColor = notYetApprovedColor;
	}
	
}

	
