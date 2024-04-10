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

import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRPhysician;

public class ListPhysiciansReturnItem {

	private String physicianId;
	private String name;
	private String code;
	private String address;
	private String changeReason;
	private int changeDate;
	private boolean annualVisit;
	private String coveredPerson;
	private String personId;
	private String benefitName;
	private String benefitJoinId;

	public ListPhysiciansReturnItem() {
	}

	ListPhysiciansReturnItem(BHRPhysician bp) {

		physicianId = bp.getPhysicianId();
		name = bp.getPhysicianName();
		code = bp.getPhysicianCode();
		address = bp.getAddress();
		changeReason = bp.getChangeReason();
		changeDate = bp.getChangeDate();
		annualVisit = bp.getAnnualVisit();
		coveredPerson = new BHRBenefitJoin(bp.getBenefitJoinId()).getCoveredPerson().getNameFML();
		personId = new BHRBenefitJoin(bp.getBenefitJoinId()).getCoveredPersonId();
		benefitName = new BHRBenefitJoin(bp.getBenefitJoinId()).getBenefitName();
	}

	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isAnnualVisit() {
		return annualVisit;
	}

	public void setAnnualVisit(boolean annualVisit) {
		this.annualVisit = annualVisit;
	}

	public int getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCoveredPerson() {
		return coveredPerson;
	}

	public void setCoveredPerson(String coveredPerson) {
		this.coveredPerson = coveredPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(String physicianId) {
		this.physicianId = physicianId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}
