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

package com.arahant.services.standard.at.applicantPosition;

import com.arahant.business.BApplicantPosition;

public class ListPositionsReturnItem {
    private String id;
    private String jobTitle;
    private String statusType;
    private String orgGroupId;
    private String orgGroupName;
    private int jobStartDate;
    private int acceptApplicationDate;
    private String positionId;

    public ListPositionsReturnItem() {
        ;
    }

    ListPositionsReturnItem(BApplicantPosition bc) {
        id = bc.getId();
        jobTitle = bc.getJobTitle();
        statusType = bc.getStatusType();
        orgGroupId = bc.getOrgGroupId();
        orgGroupName = bc.getOrgGroupName();
        jobStartDate = bc.getJobStartDate();
        acceptApplicationDate = bc.getAcceptApplicationDate();
        positionId = bc.getPosition().getPositionId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(String orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public int getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(int jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public int getAcceptApplicationDate() {
        return acceptApplicationDate;
    }

    public void setAcceptApplicationDate(int acceptApplicationDate) {
        this.acceptApplicationDate = acceptApplicationDate;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}

	
