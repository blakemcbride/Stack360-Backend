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

package com.arahant.services.standard.at.applicantProfile;

/**
 * Author: Blake McBride
 * Date: 2/4/23
 */
public class SearchJobsReturnItem {

    private String applicantPositionId;
    private String jobTitle;
    private String orgGroupName;
    private String positionId;

    public SearchJobsReturnItem(String applicantPositionId, String jobTitle, String orgGroupName, String positionId) {
        this.applicantPositionId = applicantPositionId;
        this.jobTitle = jobTitle;
        this.orgGroupName = orgGroupName;
        this.positionId = positionId;
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

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
