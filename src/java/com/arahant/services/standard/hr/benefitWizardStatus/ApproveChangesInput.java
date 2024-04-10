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



package com.arahant.services.standard.hr.benefitWizardStatus;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 *
 */
public class ApproveChangesInput extends TransmitInputBase {

	@Validation(required=true)
	private String[] ids;

	@Validation(required=false)
	private boolean approveDemographics;
	
	@Validation(required=false)
	private boolean approveDependents;
	
	@Validation(required=false)
	private boolean approveBenefits;

	public boolean getApproveBenefits() {
		return approveBenefits;
	}

	public void setApproveBenefits(boolean approveBenefits) {
		this.approveBenefits = approveBenefits;
	}

	public boolean getApproveDemographics() {
		return approveDemographics;
	}

	public void setApproveDemographics(boolean approveDemographics) {
		this.approveDemographics = approveDemographics;
	}

	public boolean getApproveDependents() {
		return approveDependents;
	}

	public void setApproveDependents(boolean approveDependents) {
		this.approveDependents = approveDependents;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}

	
