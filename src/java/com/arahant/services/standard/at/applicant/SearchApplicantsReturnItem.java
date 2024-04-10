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

package com.arahant.services.standard.at.applicant;

import com.arahant.beans.HrPosition;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplication;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

public class SearchApplicantsReturnItem implements Comparable<SearchApplicantsReturnItem> {

    private String personId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String applicantSource;
    private String applicantStatus;
    private int firstAwareDate;
    private String applicantSourceId;
    private String applicantStatusId;
    private boolean employee;
    private String position;
    private String applicationStatus;
    private String applicationId;
    private String positionId;
    private float payRate;
    private String state;
    private String employeeStatus;
    private int lastEventDate;
    private String emailAddress;
    private String mobilePhone;
    private boolean sortReverse;

    public SearchApplicantsReturnItem() {
    }

    SearchApplicantsReturnItem(Connection db, BApplicant bc, BApplication ba, String empStat) throws Exception {
        if (ba != null  &&  ba.getPosition() != null) {
            applicationStatus = ba.getStatus();
            applicationId = ba.getId();
            HrPosition pos = ba.getPosition();
            if (pos != null) {
                position = pos.getName();
                positionId = ba.getPosition().getPositionId();
                Float pr = ba.getPayRate();
                payRate = pr == null ? 0.0f : pr;
            }
        }
        personId = bc.getId();
        lastName = bc.getLastName();
        firstName = bc.getFirstName();
        middleName = bc.getMiddleName();
        applicantSource = bc.getApplicantSource();
        applicantStatus = bc.getApplicantStatus();
        firstAwareDate = bc.getFirstAwareDate();
        applicantSourceId = bc.getApplicantSourceId();
        applicantStatusId = bc.getApplicantStatusId();
        employee = bc.getIsEmployee();
        state = bc.getState();
        employeeStatus = empStat;
        emailAddress = bc.getPersonalEmail();
        mobilePhone = bc.getMobilePhone();

        Record rec = db.fetchOne("select change_when from change_log where person_id = ? order by change_when desc", personId);
        if (rec != null)
            lastEventDate = rec.getDateAsInt("change_when");
        else if (ba != null && ba.getDate() > 0)
            lastEventDate = ba.getDate();
        else
            lastEventDate = bc.getFirstAwareDate();
    }

    public void setSortReverse() {
        sortReverse = true;
    }

    public boolean isEmployee() {
        return employee;
    }

    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getApplicantSource() {
        return applicantSource;
    }

    public void setApplicantSource(String applicantSource) {
        this.applicantSource = applicantSource;
    }

    public String getApplicantStatus() {
        return applicantStatus;
    }

    public void setApplicantStatus(String applicantStatus) {
        this.applicantStatus = applicantStatus;
    }

    public int getFirstAwareDate() {
        return firstAwareDate;
    }

    public void setFirstAwareDate(int firstAwareDate) {
        this.firstAwareDate = firstAwareDate;
    }

    public String getApplicantSourceId() {
        return applicantSourceId;
    }

    public void setApplicantSourceId(String applicantSourceId) {
        this.applicantSourceId = applicantSourceId;
    }

    public String getApplicantStatusId() {
        return applicantStatusId;
    }

    public void setApplicantStatusId(String applicantStatusId) {
        this.applicantStatusId = applicantStatusId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public float getPayRate() {
        return payRate;
    }

    public void setPayRate(float payRate) {
        this.payRate = payRate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public int getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(int lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public int compareTo(SearchApplicantsReturnItem other) {
        return this.sortReverse ? Integer.compare(other.getLastEventDate(), this.lastEventDate) : Integer.compare(this.lastEventDate, other.getLastEventDate());
    }
}

	
