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
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;

public class SavePersonMiscInput extends TransmitInputBase {

	@Validation(required = true)
    private String personId;

	@Validation(table = "person", column = "smoker", required = true)
    private String tobaccoUse;
	@Validation(table = "person", column = "drivers_license_exp", type = "date", required = false)
    private int driversLicenseExpirationDate;
    @Validation(table = "person", column = "drivers_license_number", required = false)
    private String driversLicenseNumber;
    @Validation(table = "person", column = "drivers_license_state", required = false)
    private String driversLicenseState;
	@Validation(table = "person", column = "auto_insurance_carrier", required = false)
    private String automotiveInsuranceCarrier;
    @Validation(table = "person", column = "auto_insurance_start", type = "date", required = false)
    private int automotiveInsuranceStartDate;
    @Validation(table = "person", column = "auto_insurance_policy", type = "date", required = false)
    private int automotiveInsuranceExpirationDate;
    @Validation(table = "person", column = "auto_insurance_exp", required = false)
    private String automotiveInsurancePolicyNumber;
    @Validation(table = "person", column = "auto_insurance_coverage", required = false)
    private String automotiveInsuranceCoverage;
	@Validation(table = "person", column = "default_project_id", required = false)
    private String defaultProjectId;
	@Validation(required = false)
    private short height;
    @Validation(required = false)
    private short weight;
	

	void makeEmployee(final BEmployee emp) throws ArahantException
	{
		emp.setTabaccoUse(tobaccoUse);
        emp.setDriversLicenseExpirationDate(driversLicenseExpirationDate);
        emp.setDriversLicenseNumber(driversLicenseNumber);
        emp.setDriversLicenseState(driversLicenseState);
        emp.setAutomotiveInsuranceCarrier(automotiveInsuranceCarrier);
        emp.setAutomotiveInsuranceStartDate(automotiveInsuranceStartDate);
        emp.setAutomotiveInsuranceExpirationDate(automotiveInsuranceExpirationDate);
        emp.setAutomotiveInsurancePolicyNumber(automotiveInsurancePolicyNumber);
        emp.setAutomotiveInsuranceCoverage(automotiveInsuranceCoverage);
        emp.setDefaultProjectId(defaultProjectId);
		emp.setHeight(height);
        emp.setWeight(weight);
		
	}

	void makePerson(BPerson per) throws ArahantException
	{
		per.setTabaccoUse(tobaccoUse);
        per.setDriversLicenseExpirationDate(driversLicenseExpirationDate);
        per.setDriversLicenseNumber(driversLicenseNumber);
        per.setDriversLicenseState(driversLicenseState);
        per.setAutomotiveInsuranceCarrier(automotiveInsuranceCarrier);
        per.setAutomotiveInsuranceStartDate(automotiveInsuranceStartDate);
        per.setAutomotiveInsuranceExpirationDate(automotiveInsuranceExpirationDate);
        per.setAutomotiveInsurancePolicyNumber(automotiveInsurancePolicyNumber);
        per.setAutomotiveInsuranceCoverage(automotiveInsuranceCoverage);
        per.setHeight(height);
        per.setWeight(weight);
	}

	public String getAutomotiveInsuranceCarrier() {
		return automotiveInsuranceCarrier;
	}

	public void setAutomotiveInsuranceCarrier(String automotiveInsuranceCarrier) {
		this.automotiveInsuranceCarrier = automotiveInsuranceCarrier;
	}

	public String getAutomotiveInsuranceCoverage() {
		return automotiveInsuranceCoverage;
	}

	public void setAutomotiveInsuranceCoverage(String automotiveInsuranceCoverage) {
		this.automotiveInsuranceCoverage = automotiveInsuranceCoverage;
	}

	public int getAutomotiveInsuranceExpirationDate() {
		return automotiveInsuranceExpirationDate;
	}

	public void setAutomotiveInsuranceExpirationDate(int automotiveInsuranceExpirationDate) {
		this.automotiveInsuranceExpirationDate = automotiveInsuranceExpirationDate;
	}

	public String getAutomotiveInsurancePolicyNumber() {
		return automotiveInsurancePolicyNumber;
	}

	public void setAutomotiveInsurancePolicyNumber(String automotiveInsurancePolicyNumber) {
		this.automotiveInsurancePolicyNumber = automotiveInsurancePolicyNumber;
	}

	public int getAutomotiveInsuranceStartDate() {
		return automotiveInsuranceStartDate;
	}

	public void setAutomotiveInsuranceStartDate(int automotiveInsuranceStartDate) {
		this.automotiveInsuranceStartDate = automotiveInsuranceStartDate;
	}

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public int getDriversLicenseExpirationDate() {
		return driversLicenseExpirationDate;
	}

	public void setDriversLicenseExpirationDate(int driversLicenseExpirationDate) {
		this.driversLicenseExpirationDate = driversLicenseExpirationDate;
	}

	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber) {
		this.driversLicenseNumber = driversLicenseNumber;
	}

	public String getDriversLicenseState() {
		return driversLicenseState;
	}

	public void setDriversLicenseState(String driversLicenseState) {
		this.driversLicenseState = driversLicenseState;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getTobaccoUse() {
		return tobaccoUse;
	}

	public void setTobaccoUse(String tobaccoUse) {
		this.tobaccoUse = tobaccoUse;
	}

	public short getWeight() {
		return weight;
	}

	public void setWeight(short weight) {
		this.weight = weight;
	}
}

	
