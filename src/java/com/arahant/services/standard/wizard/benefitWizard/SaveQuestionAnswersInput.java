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

/**
 *
 *
 *
 */
public class SaveQuestionAnswersInput extends TransmitInputBase {

	@Validation(required = true)
	private String smoker;
	@Validation(required = false)
	private String smokerSpouse;
	@Validation(required = false)
	private String unableToPerform;
	@Validation(required = true)
	private String activelyAtWork;
	@Validation(required = false)
	private String hasAids;
	@Validation(required = false)
	private String hasAidsSpouse;
	@Validation(required = false)
	private String hasCancer;
	@Validation(required = false)
	private String hasCancerSpouse;
	@Validation(required = false)
	private String hasHeartCondition;
	@Validation(required = false)
	private String hasHeartConditionSpouse;
	@Validation(required = true)
	private String employeeId;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getHasAids() {
		return hasAids;
	}

	public void setHasAids(String hasAids) {
		this.hasAids = hasAids;
	}

	public String getHasAidsSpouse() {
		return hasAidsSpouse;
	}

	public void setHasAidsSpouse(String hasAidsSpouse) {
		this.hasAidsSpouse = hasAidsSpouse;
	}

	public String getHasCancer() {
		return hasCancer;
	}

	public void setHasCancer(String hasCancer) {
		this.hasCancer = hasCancer;
	}

	public String getHasCancerSpouse() {
		return hasCancerSpouse;
	}

	public void setHasCancerSpouse(String hasCancerSpouse) {
		this.hasCancerSpouse = hasCancerSpouse;
	}

	public String getHasHeartCondition() {
		return hasHeartCondition;
	}

	public void setHasHeartCondition(String hasHeartCondition) {
		this.hasHeartCondition = hasHeartCondition;
	}

	public String getHasHeartConditionSpouse() {
		return hasHeartConditionSpouse;
	}

	public void setHasHeartConditionSpouse(String hasHeartConditionSpouse) {
		this.hasHeartConditionSpouse = hasHeartConditionSpouse;
	}

	public String getActivelyAtWork() {
		return activelyAtWork;
	}

	public void setActivelyAtWork(String activelyAtWork) {
		this.activelyAtWork = activelyAtWork;
	}

	public String getSmoker() {
		return smoker;
	}

	public void setSmoker(String smoker) {
		this.smoker = smoker;
	}

	public String getSmokerSpouse() {
		return smokerSpouse;
	}

	public void setSmokerSpouse(String smokerSpouse) {
		this.smokerSpouse = smokerSpouse;
	}

	public String getUnableToPerform() {
		return unableToPerform;
	}

	public void setUnableToPerform(String unableToPerform) {
		this.unableToPerform = unableToPerform;
	}
}
