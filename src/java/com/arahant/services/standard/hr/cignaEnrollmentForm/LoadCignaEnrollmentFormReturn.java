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
package com.arahant.services.standard.hr.cignaEnrollmentForm;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;

public class LoadCignaEnrollmentFormReturn extends TransmitReturnBase {

    void setData(BEmployee bc)
    {
            setEmployeeLastName(bc.getLastName());
            setEmployeeFirstName(bc.getFirstName());
            setEmployeeMiddleName(bc.getMiddleName());
            setEmployeeSSN(bc.getSsn());
            setEmployeeSex(bc.getSex());
            setEmployeeAddress(bc.getStreet());
            setEmployeeCity(bc.getCity());
            setEmployeeState(bc.getState());
            setEmployeeZip(bc.getZip());
            setEmployeeEmail(bc.getPersonalEmail());
            setEmployeePhone(bc.getHomePhone());
            setEmployeeWorkPhone(bc.getWorkPhone());
            setEmployeeExternalId(bc.getExtRef());

			BHREmplDependent []deps=bc.getDependents();
			dependents=new LoadCignaEnrollmentFormReturnItem[deps.length];
			otherCoverage=new LoadCignaEnrollmentFormReturnItem2[deps.length];

			for (int loop=0;loop<dependents.length;loop++)
			{
				dependents[loop]=new LoadCignaEnrollmentFormReturnItem(deps[loop]);
				otherCoverage[loop]=new LoadCignaEnrollmentFormReturnItem2(deps[loop]);
			}
    }
	private LoadCignaEnrollmentFormReturnItem2 otherCoverage[];
	private LoadCignaEnrollmentFormReturnItem dependents[];
    private String employeeLastName;
    private String employeeFirstName;
    private String employeeMiddleName;
    private String employeeSSN;
    private String employeeSex;
    private String employeeAddress;
    private String employeeCity;
    private String employeeState;
    private String employeeZip;
    private String employeeEmail;
    private String employeePhone;
    private String employeeWorkPhone;
    private String employeeExternalId;

	public LoadCignaEnrollmentFormReturnItem[] getDependents() {
		return dependents;
	}

	public void setDependents(LoadCignaEnrollmentFormReturnItem[] dependents) {
		this.dependents = dependents;
	}

	public LoadCignaEnrollmentFormReturnItem2[] getOtherCoverage() {
		return otherCoverage;
	}

	public void setOtherCoverage(LoadCignaEnrollmentFormReturnItem2[] otherCoverage) {
		this.otherCoverage = otherCoverage;
	}



    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeCity() {
        return employeeCity;
    }

    public void setEmployeeCity(String employeeCity) {
        this.employeeCity = employeeCity;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeExternalId() {
        return employeeExternalId;
    }

    public void setEmployeeExternalId(String employeeExternalId) {
        this.employeeExternalId = employeeExternalId;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeMiddleName() {
        return employeeMiddleName;
    }

    public void setEmployeeMiddleName(String employeeMiddleName) {
        this.employeeMiddleName = employeeMiddleName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeSSN() {
        return employeeSSN;
    }

    public void setEmployeeSSN(String employeeSSN) {
        this.employeeSSN = employeeSSN;
    }

    public String getEmployeeSex() {
        return employeeSex;
    }

    public void setEmployeeSex(String employeeSex) {
        this.employeeSex = employeeSex;
    }

    public String getEmployeeState() {
        return employeeState;
    }

    public void setEmployeeState(String employeeState) {
        this.employeeState = employeeState;
    }

    public String getEmployeeWorkPhone() {
        return employeeWorkPhone;
    }

    public void setEmployeeWorkPhone(String employeeWorkPhone) {
        this.employeeWorkPhone = employeeWorkPhone;
    }

    public String getEmployeeZip() {
        return employeeZip;
    }

    public void setEmployeeZip(String employeeZip) {
        this.employeeZip = employeeZip;
    }
}

	
