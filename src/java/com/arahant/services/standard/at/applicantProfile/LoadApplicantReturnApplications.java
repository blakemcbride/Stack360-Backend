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

package com.arahant.services.standard.at.applicantProfile;

import com.arahant.beans.ApplicantPosition;
import com.arahant.business.BApplicantContact;
import com.arahant.business.BApplication;

import java.util.Date;

public class LoadApplicantReturnApplications {
    private String id;
    private int date;
    private String statusId;
    private String status;
    private LoadApplicantReturnContact[] contacts;
    private String orgGroupName;
    private Float payRate;
    private Date offerFirstGenerated;
    private Date offerLastGenerated;
    private Date offerFirstEmailed;
    private Date offerLastEmailed;
    private Date offerElecSignedDate;
    private String offerElecSignedIp;

    private String applicantPositionId;   //  applicant_application.applicant_position_id
    private String jobTitle;              //  applicant_position.job_title
    private String positionId;            // applicant_position.hr_position
    private String applicationPositionId; // applicant_application.hr_position

    public LoadApplicantReturnApplications() {
    }

    LoadApplicantReturnApplications(BApplication bc) {
        id = bc.getId();
        date = bc.getDate();
        statusId = bc.getStatusId();
        status = bc.getStatus();
        payRate = bc.getPayRate();
        positionId = bc.getPosition().getPositionId();
        offerFirstGenerated = bc.getOfferFirstGenerated();
        offerLastGenerated = bc.getOfferLastGenerated();
        offerFirstEmailed = bc.getOfferFirstEmailed();
        offerLastEmailed = bc.getOfferLastEmailed();
        offerElecSignedDate = bc.getOfferElectronicallySignedDate();
        offerElecSignedIp = bc.getOfferElectronicallySignedIp();
        ApplicantPosition apos = bc.getApplicantPosition();
        if (apos != null) {
            applicantPositionId = apos.getApplicantPositionId();
            jobTitle = apos.getJobTitle();
            orgGroupName = apos.getOrgGroup().getName();
        } else {
            applicantPositionId = null;
            jobTitle = null;
            orgGroupName = null;
        }
        applicationPositionId = bc.getPosition().getPositionId();

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

    public Float getPayRate() {
        return payRate;
    }

    public void setPayRate(Float payRate) {
        this.payRate = payRate;
    }

    public Date getOfferFirstGenerated() {
        return offerFirstGenerated;
    }

    public void setOfferFirstGenerated(Date offerFirstGenerated) {
        this.offerFirstGenerated = offerFirstGenerated;
    }

    public Date getOfferLastGenerated() {
        return offerLastGenerated;
    }

    public void setOfferLastGenerated(Date offerLastGenerated) {
        this.offerLastGenerated = offerLastGenerated;
    }

    public Date getOfferFirstEmailed() {
        return offerFirstEmailed;
    }

    public void setOfferFirstEmailed(Date offerFirstEmailed) {
        this.offerFirstEmailed = offerFirstEmailed;
    }

    public Date getOfferLastEmailed() {
        return offerLastEmailed;
    }

    public void setOfferLastEmailed(Date offerLastEmailed) {
        this.offerLastEmailed = offerLastEmailed;
    }

    public Date getOfferElecSignedDate() {
        return offerElecSignedDate;
    }

    public void setOfferElecSignedDate(Date offerElecSignedDate) {
        this.offerElecSignedDate = offerElecSignedDate;
    }

    public String getOfferElecSignedIp() {
        return offerElecSignedIp;
    }

    public void setOfferElecSignedIp(String offerElecSignedIp) {
        this.offerElecSignedIp = offerElecSignedIp;
    }

    public String getApplicantPositionId() {
        return applicantPositionId;
    }

    public void setApplicantPositionId(String applicantPositionId) {
        this.applicantPositionId = applicantPositionId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getApplicationPositionId() {
        return applicationPositionId;
    }

    public void setApplicationPositionId(String applicationPositionId) {
        this.applicationPositionId = applicationPositionId;
    }
}
