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
 */
package com.arahant.services.standard.hr.employmentHistory;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPreviousEmployment;

public class LoadEmploymentHistoryReturn extends TransmitReturnBase {

    private String companyName;
    private String companyPhone;
    private String supervisor;
    private String jobTitle;
    private int startingSalary;
    private int endingSalary;
    private String responsibilities;
    private String reasonForLeaving;
    private String contactSupervisor;
    private int workFromMonth;
    private int workFromYear;
    private int workToMonth;
    private int workToYear;
    private String street;
    private String city;
    private String state;

    void setData(BPreviousEmployment bc) {
        companyName = bc.getCompany();
        companyPhone = bc.getPhone();
        supervisor = bc.getSupervisor();
        jobTitle = bc.getJobTitle();
        startingSalary = bc.getStartingSalary();
        endingSalary = bc.getEndingSalary();
        responsibilities = bc.getResponsibilities();
        reasonForLeaving = bc.getReasonForLeaving();
        contactSupervisor = bc.getContactSupervisor() + "";
        workFromMonth = bc.getStartDate() % 100;
        workFromYear = bc.getStartDate() / 100;
        workToMonth = bc.getEndDate() % 100;
        workToYear = bc.getEndDate() / 100;
        street = bc.getStreet();
        city = bc.getCity();
        state = bc.getState();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getStartingSalary() {
        return startingSalary;
    }

    public void setStartingSalary(int startingSalary) {
        this.startingSalary = startingSalary;
    }

    public int getEndingSalary() {
        return endingSalary;
    }

    public void setEndingSalary(int endingSalary) {
        this.endingSalary = endingSalary;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getReasonForLeaving() {
        return reasonForLeaving;
    }

    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    public String getContactSupervisor() {
        return contactSupervisor;
    }

    public void setContactSupervisor(String contactSupervisor) {
        this.contactSupervisor = contactSupervisor;
    }

    public int getWorkFromMonth() {
        return workFromMonth;
    }

    public void setWorkFromMonth(int workFromMonth) {
        this.workFromMonth = workFromMonth;
    }

    public int getWorkFromYear() {
        return workFromYear;
    }

    public void setWorkFromYear(int workFromYear) {
        this.workFromYear = workFromYear;
    }

    public int getWorkToMonth() {
        return workToMonth;
    }

    public void setWorkToMonth(int workToMonth) {
        this.workToMonth = workToMonth;
    }

    public int getWorkToYear() {
        return workToYear;
    }

    public void setWorkToYear(int workToYear) {
        this.workToYear = workToYear;
    }

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}

	
