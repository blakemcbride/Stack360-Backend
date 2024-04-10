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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPreviousEmployment;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;

public class NewEmploymentHistoryInput extends TransmitInputBase {

    @Validation(table = "previous_employment", column = "person_id", required = true)
    private String personId;
    @Validation(table = "previous_employment", column = "company", required = true)
    private String companyName;
    @Validation(table = "previous_employment", column = "phone", required = false)
    private String companyPhone;
    @Validation(table = "previous_employment", column = "supervisor", required = false)
    private String supervisor;
    @Validation(table = "previous_employment", column = "job_title", required = false)
    private String jobTitle;
    @Validation(table = "previous_employment", column = "starting_salary", required = false)
    private int startingSalary;
    @Validation(table = "previous_employment", column = "ending_salary", required = false)
    private int endingSalary;
    @Validation(table = "previous_employment", column = "responsibilities", required = false)
    private String responsibilities;
    @Validation(table = "previous_employment", column = "reason_for_leaving", required = false)
    private String reasonForLeaving;
    @Validation(required = false)
    private String contactSupervisor;
    @Validation(table = "previous_employment", column = "address", required = true)
    private String companyAddress;
    @Validation(required = true)
    private int workFromMonth;
    @Validation(required = true)
    private int workFromYear;
    @Validation(required = true)
    private int workToMonth;
    @Validation(required = true)
    private int workToYear;
    private String street;
    private String city;
    private String state;


    void setData(BPreviousEmployment bc) {
        bc.setPerson(new BPerson(personId).getPerson());
        bc.setCompany(companyName);
        bc.setPhone(companyPhone);
        bc.setSupervisor(supervisor);
        bc.setJobTitle(jobTitle);
        bc.setStartingSalary(startingSalary);
        bc.setEndingSalary(endingSalary);
        bc.setResponsibilities(responsibilities);
        bc.setReasonForLeaving(reasonForLeaving);
        bc.setContactSupervisor(contactSupervisor.charAt(0));
        bc.setStartDate((workFromYear * 100) + workFromMonth);
        bc.setEndDate((workToYear * 100) + workToMonth);
        bc.setStreet(street);
        bc.setCity(city);
        bc.setState(state);
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
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

	
