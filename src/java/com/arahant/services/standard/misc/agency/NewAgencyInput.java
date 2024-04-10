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
package com.arahant.services.standard.misc.agency;

import com.arahant.annotation.Validation;

import com.arahant.business.BAgency;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class NewAgencyInput extends TransmitInputBase {

    @Validation(required = true)
    private String name;
    @Validation(table = "company_base", column = "identifier", required = false)
    private String identifier;
    @Validation(table = "company_base", column = "federal_employer_id", required = false)
    private String federalEmployerId;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String primaryAgentWorkPhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String primaryAgentWorkFax;
    @Validation(table = "person", column = "personal_email", required = false)
    private String primaryAgentPersonalEmail;
    @Validation(table = "person", column = "job_title", required = false)
    private String primaryAgentJobTitle;
    @Validation(table = "person", column = "lname", required = false)
    private String primaryAgentLname;
    @Validation(table = "person", column = "fname", required = false)
    private String primaryAgentFname;
    @Validation(table = "prophet_login", column = "user_login", required = false)
    private String primaryAgentLogin;
    @Validation(table = "prophet_login", column = "user_password", required = false)
    private String primaryAgentPassword;
    @Validation(required = false)
    private boolean primaryAgentCanLogin;
    @Validation(required = false)
    private String primaryAgentScreenGroupId;
    @Validation(table = "address", column = "street", required = false)
    private String street;
    @Validation(table = "address", column = "street2", required = false)
    private String street2;
    @Validation(table = "address", column = "city", required = false)
    private String city;
    @Validation(table = "address", column = "state", required = false)
    private String state;
    @Validation(table = "address", column = "zip", required = false)
    private String zip;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String mainPhoneNumber;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String mainFaxNumber;
    @Validation(required = false)
    private String expenseGLAccountId;
    @Validation(table = "vendor", column = "account_number", required = false)
    private String accountNumber;
    @Validation(required = false)
    private String primaryAgentSecurityGroupId;
    @Validation(required = false)
    private String ediApplicationSenderId;
    @Validation(required = false)
    private String ediApplicationReceiverId;
    @Validation(required = false)
    private String ediInterchangeSenderId;
    @Validation(required = false)
    private String ediInterchangeReceiverId;
    @Validation(required = false)
    private String ediTransferSchemeId;
    @Validation(required = false)
    private String ediTransferHost;
    @Validation(min = 0, required = false)
    private int ediTransferPort;
    @Validation(required = false)
    private String ediTransferUsername;
    @Validation(required = false)
    private String ediTransferPassword;
    @Validation(required = false)
    private String ediTransferDirectory;
    @Validation(required = false)
    private String ediTransferEncryptionKeyIdInHex;
    @Validation(required = false)
    private String ediTransferEncryptionKeyText;

    public String getPrimaryAgentSecurityGroupId() {
        return primaryAgentSecurityGroupId;
    }

    public void setPrimaryAgentSecurityGroupId(String primaryAgentSecurityGroupId) {
        this.primaryAgentSecurityGroupId = primaryAgentSecurityGroupId;
    }

    /**
     * @param v
     * @throws ArahantException
     */
    void makeAgency(final BAgency a) throws ArahantException {
        a.setName(name);
        a.setIdentifier(identifier);
        a.setFederalEmployerId(federalEmployerId);
        a.setMainContactWorkPhone(primaryAgentWorkPhone);
        a.setMainContactWorkFax(primaryAgentWorkFax);
        a.setMainContactPersonalEmail(primaryAgentPersonalEmail);
        a.setMainContactJobTitle(primaryAgentJobTitle);
        a.setMainContactFname(primaryAgentFname);
        a.setMainContactLname(primaryAgentLname);
        a.setMainContactLogin(primaryAgentLogin);
        a.setMainContactPassword(primaryAgentPassword,primaryAgentCanLogin);
        a.setMainContactScreenGroupId(primaryAgentScreenGroupId);
        a.setState(state);
        a.setStreet(street);
        a.setCity(city);
        a.setZip(zip);
        a.setMainPhoneNumber(mainPhoneNumber);
        a.setMainFaxNumber(mainFaxNumber);
        a.setMainContactSecurityGroupId(primaryAgentSecurityGroupId);
        a.setStreet2(street2);

        a.setEdiApplicationSenderId(ediApplicationSenderId);
        a.setEdiApplicationReceiverId(ediApplicationReceiverId);
        a.setEdiInterchangeSenderId(ediInterchangeSenderId);
        a.setEdiInterchangeReceiverId(ediInterchangeReceiverId);
        a.setEdiTransferURL(ediTransferSchemeId, ediTransferHost, ediTransferPort, ediTransferUsername);
        a.setEdiTransferPassword(ediTransferPassword);
        a.setEdiTransferDirectory(ediTransferDirectory);
        a.setEdiTransferEncryptionKey(ediTransferEncryptionKeyIdInHex);
        a.setEdiTransferEncryptionKeyText(ediTransferEncryptionKeyText);

    }

    /**
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city to set.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return Returns the federalEmployerId.
     */
    public String getFederalEmployerId() {
        return federalEmployerId;
    }

    /**
     * @param federalEmployerId The federalEmployerId to set.
     */
    public void setFederalEmployerId(final String federalEmployerId) {
        this.federalEmployerId = federalEmployerId;
    }

    /**
     * @return Returns the identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public boolean isPrimaryAgentCanLogin() {
        return primaryAgentCanLogin;
    }

    public void setPrimaryAgentCanLogin(boolean primaryAgentCanLogin) {
        this.primaryAgentCanLogin = primaryAgentCanLogin;
    }

    public String getPrimaryAgentFname() {
        return primaryAgentFname;
    }

    public void setPrimaryAgentFname(String primaryAgentFname) {
        this.primaryAgentFname = primaryAgentFname;
    }

    public String getPrimaryAgentJobTitle() {
        return primaryAgentJobTitle;
    }

    public void setPrimaryAgentJobTitle(String primaryAgentJobTitle) {
        this.primaryAgentJobTitle = primaryAgentJobTitle;
    }

    public String getPrimaryAgentLname() {
        return primaryAgentLname;
    }

    public void setPrimaryAgentLname(String primaryAgentLname) {
        this.primaryAgentLname = primaryAgentLname;
    }

    public String getPrimaryAgentLogin() {
        return primaryAgentLogin;
    }

    public void setPrimaryAgentLogin(String primaryAgentLogin) {
        this.primaryAgentLogin = primaryAgentLogin;
    }

    public String getPrimaryAgentPassword() {
        return primaryAgentPassword;
    }

    public void setPrimaryAgentPassword(String primaryAgentPassword) {
        this.primaryAgentPassword = primaryAgentPassword;
    }

    public String getPrimaryAgentPersonalEmail() {
        return primaryAgentPersonalEmail;
    }

    public void setPrimaryAgentPersonalEmail(String primaryAgentPersonalEmail) {
        this.primaryAgentPersonalEmail = primaryAgentPersonalEmail;
    }

    public String getPrimaryAgentScreenGroupId() {
        return primaryAgentScreenGroupId;
    }

    public void setPrimaryAgentScreenGroupId(String primaryAgentScreenGroupId) {
        this.primaryAgentScreenGroupId = primaryAgentScreenGroupId;
    }

    public String getPrimaryAgentWorkFax() {
        return primaryAgentWorkFax;
    }

    public void setPrimaryAgentWorkFax(String primaryAgentWorkFax) {
        this.primaryAgentWorkFax = primaryAgentWorkFax;
    }

    public String getPrimaryAgentWorkPhone() {
        return primaryAgentWorkPhone;
    }

    public void setPrimaryAgentWorkPhone(String primaryAgentWorkPhone) {
        this.primaryAgentWorkPhone = primaryAgentWorkPhone;
    }


    /**
     * @param mainFaxNumber The mainFaxNumber to set.
     */
    public void setMainFaxNumber(final String mainFaxNumber) {
        this.mainFaxNumber = mainFaxNumber;
    }

    public String getMainFaxNumber() {
        return mainFaxNumber;
    }

    /**
     * @return Returns the mainPhoneNumber.
     */
    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    /**
     * @param mainPhoneNumber The mainPhoneNumber to set.
     */
    public void setMainPhoneNumber(final String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state to set.
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return Returns the street.
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street The street to set.
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * @return Returns the zip.
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip The zip to set.
     */
    public void setZip(final String zip) {
        this.zip = zip;
    }

    /**
     * @return Returns the accountNumber.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber The accountNumber to set.
     */
    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return Returns the expenseGLAccountId.
     */
    public String getExpenseGLAccountId() {
        return expenseGLAccountId;
    }

    /**
     * @param expenseGLAccountId The expenseGLAccountId to set.
     */
    public void setExpenseGLAccountId(final String expenseGLAccountId) {
        this.expenseGLAccountId = expenseGLAccountId;
    }

    /**
     * @return Returns the street2.
     */
    public String getStreet2() {
        return street2;
    }

    /**
     * @param street2 The street2 to set.
     */
    public void setStreet2(final String street2) {
        this.street2 = street2;
    }

    public String getEdiApplicationReceiverId() {
        return ediApplicationReceiverId;
    }

    public void setEdiApplicationReceiverId(String ediApplicationReceiverId) {
        this.ediApplicationReceiverId = ediApplicationReceiverId;
    }

    public String getEdiApplicationSenderId() {
        return ediApplicationSenderId;
    }

    public void setEdiApplicationSenderId(String ediApplicationSenderId) {
        this.ediApplicationSenderId = ediApplicationSenderId;
    }

    public String getEdiInterchangeReceiverId() {
        return ediInterchangeReceiverId;
    }

    public void setEdiInterchangeReceiverId(String ediInterchangeReceiverId) {
        this.ediInterchangeReceiverId = ediInterchangeReceiverId;
    }

    public String getEdiInterchangeSenderId() {
        return ediInterchangeSenderId;
    }

    public void setEdiInterchangeSenderId(String ediInterchangeSenderId) {
        this.ediInterchangeSenderId = ediInterchangeSenderId;
    }

    public String getEdiTransferDirectory() {
        return ediTransferDirectory;
    }

    public void setEdiTransferDirectory(String ediTransferDirectory) {
        this.ediTransferDirectory = ediTransferDirectory;
    }

    public String getEdiTransferEncryptionKeyIdInHex() {
        return ediTransferEncryptionKeyIdInHex;
    }

    public void setEdiTransferEncryptionKeyIdInHex(String ediTransferEncryptionKeyIdInHex) {
        this.ediTransferEncryptionKeyIdInHex = ediTransferEncryptionKeyIdInHex;
    }

    public String getEdiTransferEncryptionKeyText() {
        return ediTransferEncryptionKeyText;
    }

    public void setEdiTransferEncryptionKeyText(String ediTransferEncryptionKeyText) {
        this.ediTransferEncryptionKeyText = ediTransferEncryptionKeyText;
    }

    public String getEdiTransferHost() {
        return ediTransferHost;
    }

    public void setEdiTransferHost(String ediTransferHost) {
        this.ediTransferHost = ediTransferHost;
    }

    public String getEdiTransferPassword() {
        return ediTransferPassword;
    }

    public void setEdiTransferPassword(String ediTransferPassword) {
        this.ediTransferPassword = ediTransferPassword;
    }

    public int getEdiTransferPort() {
        return ediTransferPort;
    }

    public void setEdiTransferPort(int ediTransferPort) {
        this.ediTransferPort = ediTransferPort;
    }

    public String getEdiTransferSchemeId() {
        return ediTransferSchemeId;
    }

    public void setEdiTransferSchemeId(String ediTransferSchemeId) {
        this.ediTransferSchemeId = ediTransferSchemeId;
    }

    public String getEdiTransferUsername() {
        return ediTransferUsername;
    }

    public void setEdiTransferUsername(String ediTransferUsername) {
        this.ediTransferUsername = ediTransferUsername;
    }
}

	
