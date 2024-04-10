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
 *  Created on Feb 8, 2007
 */

package com.arahant.services.standard.at.applicantPosition;

import com.arahant.business.BHRPosition;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BApplicantPosition;
import com.arahant.annotation.Validation;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

public class SavePositionInput extends TransmitInputBase {
    @Validation(min = 1, max = 40, required = true)
    private String jobTitle;
    private String positionId;
    @Validation(min = 1, max = 16, required = true)
    private String orgGroupId;
    @Validation(min = 1, max = 1, required = true)
    private String statusType;
    @Validation(type = "date", required = true)
    private int jobStartDate;
    @Validation(type = "date", required = true)
    private int acceptApplicationDate;
    @Validation(min = 1, max = 16, required = true)
    private String id;
    @Validation(required = false)
    private PositionInformationItem[] information;
    @Validation(required = true)
    private boolean informationChangedFlag;
    @Validation(required = false)
    private String[] deleteIds;
    @Validation(required = true)
    private boolean reorderedFlag;

    void setData(BApplicantPosition bc) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        bc.setJobTitleName(jobTitle);
        bc.setOrgGroupId(orgGroupId);
        bc.setStatusType(statusType);
        bc.setJobStartDate(jobStartDate);
        bc.setAcceptApplicationDate(acceptApplicationDate);
        bc.setPosition(new BHRPosition(positionId).getBean());
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PositionInformationItem[] getInformation() {
        if (information == null)
            return new PositionInformationItem[0];
        return information;
    }

    public void setInformation(PositionInformationItem[] information) {
        this.information = information;
    }

    public boolean isInformationChangedFlag() {
        return informationChangedFlag;
    }

    public void setInformationChangedFlag(boolean informationChangedFlag) {
        this.informationChangedFlag = informationChangedFlag;
    }

    public String[] getDeleteIds() {
        if (deleteIds == null)
            deleteIds = new String[0];
        return deleteIds;
    }

    public void setDeleteIds(String[] deleteIds) {
        this.deleteIds = deleteIds;
    }

    public boolean isReorderedFlag() {
        return reorderedFlag;
    }

    public void setReorderedFlag(boolean reorderedFlag) {
        this.reorderedFlag = reorderedFlag;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}

	
