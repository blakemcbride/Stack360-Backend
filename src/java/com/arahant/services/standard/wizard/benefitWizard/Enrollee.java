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


package com.arahant.services.standard.wizard.benefitWizard;

/**
 *
 * Arahant
 */
public class Enrollee {

	private String relationshipId;
	private String name;
	private double coverageAmount;

	public double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelationshipId() {
		return relationshipId;
	}

	/*
	 * The following method is a MAJOR problem! Sometimes it is passed
	 * personID's and other times it is passed relationshipID's. It may? work
	 * because the sender and receiver may agree at runtime what is being sent.
	 * I don't know.
	 *
	 * This class is used by web services too so it affects the names of web
	 * service data. Those data items used to be called perdonId. I (Blake)
	 * changed it to relationshipId. This affects both sides. What is really
	 * being passed is unclear to me.
	 */
	public void setRelationshipId(String personId) {
		this.relationshipId = personId;
	}
}
