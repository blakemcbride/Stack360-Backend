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
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRPhysician;


/**
 * 
 *
 *
 */
public class LoadDetailReturnPhysician {
	
	public LoadDetailReturnPhysician(){

	}

	LoadDetailReturnPhysician (final BHRPhysician bc) {
		physicianId = bc.getPhysicianId();
		name = bc.getPhysicianName();
		code = bc.getPhysicianCode();
		address = bc.getAddress();
		changeReason = bc.getChangeReason();
		changeDate = bc.getChangeDate();
		annualVisit = bc.getAnnualVisit();
		coveredPerson = new BHRBenefitJoin(bc.getBenefitJoinId()).getCoveredPerson().getNameFML();
		personId = new BHRBenefitJoin(bc.getBenefitJoinId()).getCoveredPersonId();
	}
	
	private String physicianId;
	private String name;
	private String code;
	private String address;
	private String changeReason;
	private int changeDate;
	private boolean annualVisit;
	private String coveredPerson;
	private String personId;
	
	
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

	
