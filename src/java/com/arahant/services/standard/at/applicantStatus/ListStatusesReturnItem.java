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

package com.arahant.services.standard.at.applicantStatus;

import com.arahant.business.BApplicantStatus;

public class ListStatusesReturnItem {

    private String id;
    private String name;
    private boolean considerForHire;
    private int inactiveDate;
    private boolean sendEmail;
    private String emailBody;
    private String emailAddress;
    private String emailSubject;

    public ListStatusesReturnItem() {
    }

    ListStatusesReturnItem(BApplicantStatus bc) {
        id = bc.getId();
        name = bc.getName();
        considerForHire = bc.getConsiderForHire();
        inactiveDate = bc.getInactiveDate();
        sendEmail = bc.getSendEmail() == 'Y' ? true : false;
        emailBody = bc.getEmailText();
        emailAddress = bc.getEmailSource();
        emailSubject = bc.getEmailSubject();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getConsiderForHire() {
        return considerForHire;
    }

    public void setConsiderForHire(boolean considerForHire) {
        this.considerForHire = considerForHire;
    }

    public int getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(int inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
