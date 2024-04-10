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

import com.arahant.annotation.Validation;
import com.arahant.business.BBenefitRider;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewRiderInput extends TransmitInputBase {

	@Validation(required=true)
	private String baseBenefitId;
	@Validation(required=true)
	private String riderBenefitId;
	@Validation(required=false)
	private Boolean hidden;
	@Validation(required=false)
	private Boolean required;

	public String getBaseBenefitId() {
		return baseBenefitId;
	}

	public void setBaseBenefitId(String baseBenefitId) {
		this.baseBenefitId = baseBenefitId;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRiderBenefitId() {
		return riderBenefitId;
	}

	public void setRiderBenefitId(String riderBenefitId) {
		this.riderBenefitId = riderBenefitId;
	}

	public void setData(BBenefitRider bbr)
	{
		bbr.setRequired(required);
		bbr.setHidden(hidden);
		bbr.setRiderBenefitId(riderBenefitId);
		bbr.setBaseBenefitId(baseBenefitId);
	}


}

	
