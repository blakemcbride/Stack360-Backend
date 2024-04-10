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

import com.arahant.business.BBenefitDependency;
import com.arahant.business.BBenefitRestriction;


/**
 * 
 *
 *
 */
public class ListBenefitDependenciesReturnItem {
	
	public ListBenefitDependenciesReturnItem()
	{
		;
	}

	ListBenefitDependenciesReturnItem (final BBenefitDependency bd)
	{
		requiredBenefit = bd.getRequiredBenefit().getName();
		requiredBenefitId = bd.getRequiredBenefitId();
		dependentBenefit = bd.getDependentBenefit().getName();
		dependentBenefitId = bd.getDependentBenefitId();
		benefitDependencyId = bd.getBenefitDependencyId();
		required = bd.getRequiredBoolean();
		hidden = bd.getHiddenBoolean();
	}

	private String requiredBenefit;
	private String requiredBenefitId;
	private String dependentBenefit;
	private String dependentBenefitId;
	private String benefitDependencyId;
	private boolean required;
	private boolean hidden;

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean r) {
		this.required = r;
	}

	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	public String getBenefitDependencyId() {
		return benefitDependencyId;
	}

	public void setBenefitDependencyId(String benefitDependencyId) {
		this.benefitDependencyId = benefitDependencyId;
	}

	public String getDependentBenefit() {
		return dependentBenefit;
	}

	public void setDependentBenefit(String dependentBenefit) {
		this.dependentBenefit = dependentBenefit;
	}

	public String getDependentBenefitId() {
		return dependentBenefitId;
	}

	public void setDependentBenefitId(String dependentBenefitId) {
		this.dependentBenefitId = dependentBenefitId;
	}

	public String getRequiredBenefit() {
		return requiredBenefit;
	}

	public void setRequiredBenefit(String requiredBenefit) {
		this.requiredBenefit = requiredBenefit;
	}

	public String getRequiredBenefitId() {
		return requiredBenefitId;
	}

	public void setRequiredBenefitId(String requiredBenefitId) {
		this.requiredBenefitId = requiredBenefitId;
	}



}

	
