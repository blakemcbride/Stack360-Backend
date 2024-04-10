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

import com.arahant.business.BApplicantContact;
import com.arahant.business.BApplication;

public class LoadApplicantReturnApplications {
    private String id;
    private String positionId;
    private int date;
    private String statusId;
    private String status;
    private LoadApplicantReturnContact[] contacts;
    private String orgGroupName;
    private float payRate;

    public LoadApplicantReturnApplications() {
    }

    LoadApplicantReturnApplications(BApplication bc) {
        id = bc.getId();
        date = bc.getDate();
        statusId = bc.getStatusId();
        status = bc.getStatus();
		positionId = bc.getPosition().getPositionId();
        payRate = bc.getPayRate();

        BApplicantContact[] conts = bc.getApplicationContacts();
        contacts = new LoadApplicantReturnContact[conts.length];
        for (int loop = 0; loop < conts.length; loop++)
            contacts[loop] = new LoadApplicantReturnContact(conts[loop]);
    }

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public LoadApplicantReturnContact[] getContacts() {
        return contacts;
    }

    public void setContacts(LoadApplicantReturnContact[] contacts) {
        this.contacts = contacts;
    }

    public float getPayRate() {
        return payRate;
    }

    public void setPayRate(float payRate) {
        this.payRate = payRate;
    }
}
