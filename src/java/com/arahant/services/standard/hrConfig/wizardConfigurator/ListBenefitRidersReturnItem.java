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
import com.arahant.business.BBenefitRider;


/**
 * 
 *
 *
 */
public class ListBenefitRidersReturnItem {
	
	public ListBenefitRidersReturnItem()
	{
	}

	ListBenefitRidersReturnItem (final BBenefitRider bc)
	{
		benefitRiderId = bc.getBenefitRiderId();
		baseBenefitId = bc.getBaseBenefitId();
		riderBenefitId = bc.getRiderBenefitId();
		riderName = bc.getRiderBenefit().getName();
		hidden = bc.getHiddenBoolean();
		required = bc.getRequiredBoolean();

	}

	private String benefitRiderId;
	private String baseBenefitId;
	private String riderBenefitId;
	private String riderName;
	private Boolean hidden;
	private Boolean required;

	public String getBaseBenefitId() {
		return baseBenefitId;
	}

	public void setBaseBenefitId(String baseBenefitId) {
		this.baseBenefitId = baseBenefitId;
	}

	public String getBenefitRiderId() {
		return benefitRiderId;
	}

	public void setBenefitRiderId(String benefitRiderId) {
		this.benefitRiderId = benefitRiderId;
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

	public String getRiderName() {
		return riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = riderName;
	}


}

	
