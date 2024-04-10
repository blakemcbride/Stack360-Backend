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

import com.arahant.annotation.Validation;

import com.arahant.business.BAgent;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SaveAgencyAgentInput extends TransmitInputBase {

    @Validation(table = "phone", column = "phone_number", required = false)
    private String workPhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String workFax;
    @Validation(required = false)
    private boolean primaryIndicator;
    @Validation(table = "person", column = "personal_email", required = false)
    private String personalEmail;
    @Validation(table = "person", column = "job_title", required = false)
    private String jobTitle;
    @Validation(required = true)
    private String personId;
    @Validation(table = "person", column = "lname", required = false)
    private String lname;
    @Validation(table = "person", column = "fname", required = false)
    private String fname;
    @Validation(table = "prophet_login", column = "user_login", required = false)
    private String login;
    @Validation(table = "prophet_login", column = "user_password", required = false)
    private String contactPassword;
    @Validation(required = false)
    private boolean canLogin;
    @Validation(required = false)
    private String screenGroupId;
    @Validation(required = true)
    private String orgGroupId;
    @Validation(required = false)
    private String securityGroupId;
    @Validation(required = false)
    private String[] companyIds;
    @Validation(required = false)
    private String cellPhone;
    @Validation(required = false)
    private String agentExternalRef;
	@Validation(required = false)
    private String noCompanyScreenGroupId;

    /**
     * @return Returns the securityGroupId.
     */
    public String getSecurityGroupId() {
        return securityGroupId;
    }

    /**
     * @param securityGroupId The securityGroupId to set.
     */
    public void setSecurityGroupId(final String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    /**
     * @throws ArahantException
     *
     */
    void makeAgent(final BAgent a) throws ArahantException {
        a.setWorkPhone(workPhone);
        a.setWorkFax(workFax);

        a.setPersonalEmail(personalEmail);
        a.setJobTitle(jobTitle);
        a.setPersonId(personId);
        a.setLastName(lname);
        a.setFirstName(fname);
        a.setUserLogin(login);
        a.setUserPassword(contactPassword, canLogin);
        a.setScreenGroupId(screenGroupId);
        a.setSecurityGroupId(securityGroupId);
        a.setCompanyIds(getCompanyIds());
        a.setMobilePhone(cellPhone);
        a.setExtRef(agentExternalRef);
		a.setNoCompanyScreenGroupId(noCompanyScreenGroupId);
    }

    /**
     * @return Returns the canLogin.
     */
    public boolean isCanLogin() {
        return canLogin;
    }

    /**
     * @param canLogin The canLogin to set.
     */
    public void setCanLogin(final boolean canLogin) {
        this.canLogin = canLogin;
    }

    /**
     * @return Returns the contactPassword.
     */
    public String getContactPassword() {
        return contactPassword;
    }

    /**
     * @param contactPassword The contactPassword to set.
     */
    public void setContactPassword(final String contactPassword) {
        this.contactPassword = contactPassword;
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
     * @return Returns the login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login The login to set.
     */
    public void setLogin(final String login) {
        this.login = login;
    }

    /**
     * @return Returns the orgGroupId.
     */
    public String getOrgGroupId() {
        return orgGroupId;
    }

    /**
     * @param orgGroupId The orgGroupId to set.
     */
    public void setOrgGroupId(final String orgGroupId) {
        this.orgGroupId = orgGroupId;
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

    /**
     * @return Returns the primaryIndicator.
     */
    public boolean isPrimaryIndicator() {
        return primaryIndicator;
    }

    /**
     * @param primaryIndicator The primaryIndicator to set.
     */
    public void setPrimaryIndicator(final boolean primaryIndicator) {
        this.primaryIndicator = primaryIndicator;
    }

    /**
     * @return Returns the screenGroupId.
     */
    public String getScreenGroupId() {
        return screenGroupId;
    }

    /**
     * @param screenGroupId The screenGroupId to set.
     */
    public void setScreenGroupId(final String screenGroupId) {
        this.screenGroupId = screenGroupId;
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

    public String[] getCompanyIds() {
        if (companyIds==null)
            companyIds=new String[0];
        return companyIds;
    }

    public void setCompanyIds(String[] companyIds) {
        this.companyIds = companyIds;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAgentExternalRef() {
        return agentExternalRef;
    }

    public void setAgentExternalRef(String agentExternalRef) {
        this.agentExternalRef = agentExternalRef;
    }

	public String getNoCompanyScreenGroupId() {
		return noCompanyScreenGroupId;
	}

	public void setNoCompanyScreenGroupId(String noCompanyScreenGroupId) {
		this.noCompanyScreenGroupId = noCompanyScreenGroupId;
	}
    
}

	
