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

import com.arahant.beans.UserAttribute;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadCustomColorsReturn extends TransmitReturnBase {

	void setData(BPerson person)
	{
		approvedBenefitColor=person.getNumericPreference(UserAttribute.BENEFIT_ASSIGNMENT_APPROVED_BENEFIT_COLOR);
		approvedDeclineColor=person.getNumericPreference(UserAttribute.BENEFIT_ASSIGNMENT_APPROVED_DECLINE_COLOR);
		notYetApprovedColor=person.getNumericPreference(UserAttribute.BENEFIT_ASSIGNMENT_NOT_YET_APPROVED_COLOR);
	}
	
	private int approvedBenefitColor;
	private int approvedDeclineColor;
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

	
