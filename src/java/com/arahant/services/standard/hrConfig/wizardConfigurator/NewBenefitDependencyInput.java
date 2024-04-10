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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class NewBenefitDependencyInput extends TransmitInputBase {

	@Validation(required=true)
	private String requiredBenefitId;
	@Validation(required=true)
	private String dependentBenefitId;
	@Validation(required=false)
	private boolean required;
	@Validation(required=false)
	private boolean hidden;

	public String getDependentBenefitId() {
		return dependentBenefitId;
	}

	public void setDependentBenefitId(String dependentBenefitId) {
		this.dependentBenefitId = dependentBenefitId;
	}

	public String getRequiredBenefitId() {
		return requiredBenefitId;
	}

	public void setRequiredBenefitId(String requiredBenefitId) {
		this.requiredBenefitId = requiredBenefitId;
	}

	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}


}

	
