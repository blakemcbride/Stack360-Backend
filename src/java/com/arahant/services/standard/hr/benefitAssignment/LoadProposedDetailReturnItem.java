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



package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.business.BHRBenefitJoin;
import com.arahant.exceptions.ArahantException;

public class LoadProposedDetailReturnItem implements Comparable<LoadProposedDetailReturnItem> {

	private double amountCovered;
	/**
	 * Coverage end date
	 */
	private int endDate;
	private String firstName;
	private String lastName;
	private String middleName;
	private String personId;
	private String relationship;
	private String ssn;
	/**
	 * Coverage start date
	 */
	private int startDate;
	private int originalCoverageDate;
	private boolean isActive;
	private String comments = "";
	private String otherInsurance;
	private boolean otherInsurancePrimary;

	public LoadProposedDetailReturnItem() {
	}

	public LoadProposedDetailReturnItem(final BHRBenefitJoin bc) throws ArahantException {
		amountCovered = bc.getAmountCovered();
		endDate = bc.getCoverageEndDate();
		try {
			firstName = bc.getCoveredFirstName();
			lastName = bc.getCoveredLastName();
			middleName = bc.getCoveredMiddleName();
			personId = bc.getCoveredPersonId();
			relationship = bc.getRelationshipText();
			if (bc.isPolicyBenefitJoin())
				isActive = true;
			else
				isActive = bc.getRelationship().isCurrentlyActive();
		} catch (Exception e) {
			firstName = "Pending";
			lastName = "Pending";
			middleName = "Pending";
			personId = bc.getCoveredPersonId();
			relationship = "Pending";
			isActive = false;
		}

		ssn = bc.getCoveredSsn();
		startDate = bc.getCoverageStartDate();
		originalCoverageDate = bc.getOriginalCoverageDate();

		otherInsurance = bc.getOtherInsurance();
		otherInsurancePrimary = bc.getOtherInsurancePrimary();
	}

	public String getOtherInsurance() {
		return otherInsurance;
	}

	public void setOtherInsurance(String otherInsurance) {
		this.otherInsurance = otherInsurance;
	}

	public boolean getOtherInsurancePrimary() {
		return otherInsurancePrimary;
	}

	public void setOtherInsurancePrimary(boolean otherInsurancePrimary) {
		this.otherInsurancePrimary = otherInsurancePrimary;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getOriginalCoverageDate() {
		return originalCoverageDate;
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		this.originalCoverageDate = originalCoverageDate;
	}

	public double getAmountCovered() {
		return amountCovered;
	}

	public void setAmountCovered(final double amountCovered) {
		this.amountCovered = amountCovered;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int compareTo(LoadProposedDetailReturnItem o) {
		try {
			if ("Employee".equals(relationship))
				return -1;

			if ("Employee".equals(o.relationship))
				return 1;

			//first, check for spouse
			if ("Spouse".equals(relationship))
				return -1;

			if ("Spouse".equals(o.relationship))
				return 1;

			if ("Child".equals(relationship) && "Unknown".equals(o.relationship))
				return -1;

			if ("Unknown".equals(relationship) && "Child".equals(o.relationship))
				return 1;

			return startDate - o.startDate;
		} catch (Exception e) {
			return 0;
		}
	}
}
