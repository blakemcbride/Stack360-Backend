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
 *
 * Created on Feb 8, 2007
 */

package com.arahant.services.standard.at.applicantPosition;

import com.arahant.business.BHRPosition;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BApplicantPosition;


public class NewPositionInput extends TransmitInputBase {

    private String jobTitle;
    private String positionId;
    private String orgGroupId;
    private String statusType;
    private int jobStartDate;
    private int acceptApplicationDate;
    private PositionInformationItem[] information;

    void setData(BApplicantPosition bc) {
        bc.setJobTitleName(jobTitle);
        bc.setPosition(new BHRPosition(positionId).getBean());
        bc.setOrgGroupId(orgGroupId);
        bc.setStatusType(statusType);
        bc.setJobStartDate(jobStartDate);
        bc.setAcceptApplicationDate(acceptApplicationDate);
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(String orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
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

    public PositionInformationItem[] getInformation() {
        return information;
    }

    public void setInformation(PositionInformationItem[] information) {
        this.information = information;
    }
}

	
