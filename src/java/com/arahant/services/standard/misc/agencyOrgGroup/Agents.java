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
 * Created on Oct 9, 2009
 *
 */
package com.arahant.services.standard.misc.agencyOrgGroup;

import com.arahant.business.BAgent;

/**
 *
 *
 * Created on Oct 9, 2009
 *
 */
public class Agents {

    public Agents() {
        super();
    }

    /**
     * @param contact
     */
    public Agents(final BAgent contact, String orgGroupId) {
        super();
        workPhone = contact.getWorkPhone();
        workFax = contact.getWorkFax();
        personalEmail = contact.getPersonalEmail();
        jobTitle = contact.getJobTitle();
        personId = contact.getPersonId();
        lname = contact.getLastName();
        fname = contact.getFirstName();
        primary = contact.isPrimary(orgGroupId) ? "Yes" : "No";
        companyName = contact.getCompanyName();
        companyId = contact.getId();
        companyPhone = contact.getWorkPhone();
    }
    private String workPhone;
    private String workFax;
    private String personalEmail;
    private String jobTitle;
    private String personId;
    private String lname;
    private String fname;
    private String primary;
    private String companyName;
    private String companyId;
    private String companyPhone;

    /**
     * @return Returns the primary.
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * @param primary The primary to set.
     */
    public void setPrimary(final String primary) {
        this.primary = primary;
    }

    /**
     * @return Returns the fname.
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname The fname to set.
     */
    public void setFname(final String fname) {
        this.fname = fname;
    }

    /**
     * @return Returns the lname.
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname The lname to set.
     */
    public void setLname(final String lname) {
        this.lname = lname;
    }

    /**
     * @return Returns the personalEmail.
     */
    public String getPersonalEmail() {
        return personalEmail;
    }

    /**
     * @param personalEmail The personalEmail to set.
     */
    public void setPersonalEmail(final String personalEmail) {
        this.personalEmail = personalEmail;
    }

    /**
     * @return Returns the personId.
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId The personId to set.
     */
    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @return Returns the workFax.
     */
    public String getWorkFax() {
        return workFax;
    }

    /**
     * @param workFax The workFax to set.
     */
    public void setWorkFax(final String workFax) {
        this.workFax = workFax;
    }

    /**
     * @return Returns the workPhone.
     */
    public String getWorkPhone() {
        return workPhone;
    }

    /**
     * @param workPhone The workPhone to set.
     */
    public void setWorkPhone(final String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
}

