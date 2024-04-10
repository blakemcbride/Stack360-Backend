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

import com.arahant.business.BApplicantPosition;

public class SearchPositionsReturnItem {

    private String id;
    private String position;
    private String statusType;
    private String orgGroupName;
    private int acceptApplicationDate;
    private int jobStartDate;

    public SearchPositionsReturnItem() {
    }

    SearchPositionsReturnItem(BApplicantPosition bc) {
        id = bc.getId();
        position = bc.getJobTitle();
        statusType = bc.getStatusType();
        orgGroupName = bc.getOrgGroupName();
        acceptApplicationDate = bc.getAcceptApplicationDate();
        jobStartDate = bc.getJobStartDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public int getAcceptApplicationDate() {
        return acceptApplicationDate;
    }

    public void setAcceptApplicationDate(int acceptApplicationDate) {
        this.acceptApplicationDate = acceptApplicationDate;
    }

    public int getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(int jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

}

	
