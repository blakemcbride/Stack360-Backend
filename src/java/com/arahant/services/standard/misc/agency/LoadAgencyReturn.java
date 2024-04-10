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

import com.arahant.business.BAgency;
import com.arahant.services.TransmitReturnBase;

/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class LoadAgencyReturn extends TransmitReturnBase {

    private String orgGroupId;
    private String name;
    private String identifier;
    private String federalEmployerId;
    private String primaryAgentWorkPhone;
    private String primaryAgentWorkFax;
    private String primaryAgentPersonalEmail;
    private String primaryAgentJobTitle;
    private String primaryAgentLname;
    private String primaryAgentFname;
    private String primaryAgentLogin;
    private boolean primaryAgentCanLogin;
    private String primaryAgentPassword;
    private String primaryAgentScreenGroupId;
    private String primaryAgentScreenGroupName;
    private String mainContactScreenGroupExtId;
    private String addressId;
    private String street, street2;
    private String city;
    private String state;
    private String zip;
    private String mainPhoneNumber;
    private String mainFaxNumber;
    private String primaryAgentSecurityGroupId;
    private String primaryAgentSecurityGroupName;
    private String ediApplicationSenderId;
    private String ediApplicationReceiverId, ediInterchangeSenderId, ediInterchangeReceiverId, ediTransferSchemeId, ediTransferHost;
    private int ediTransferPort;
    private String ediTransferUsername, ediTransferPassword, ediTransferDirectory;
    private String ediTransferEncryptionKeyIdInHex;
    private String ediTransferEncryptionKeyText;

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

    public String getPrimaryAgentScreenGroupName() {
        return primaryAgentScreenGroupName;
    }

    public void setPrimaryAgentScreenGroupName(String primaryAgentScreenGroupName) {
        this.primaryAgentScreenGroupName = primaryAgentScreenGroupName;
    }

    public String getPrimaryAgentSecurityGroupId() {
        return primaryAgentSecurityGroupId;
    }

    public void setPrimaryAgentSecurityGroupId(String primaryAgentSecurityGroupId) {
        this.primaryAgentSecurityGroupId = primaryAgentSecurityGroupId;
    }

    public String getPrimaryAgentSecurityGroupName() {
        return primaryAgentSecurityGroupName;
    }

    public void setPrimaryAgentSecurityGroupName(String primaryAgentSecurityGroupName) {
        this.primaryAgentSecurityGroupName = primaryAgentSecurityGroupName;
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

    public LoadAgencyReturn() {
    }

    /**
     * @param company
     */
    void setData(final BAgency cc) {
        setOrgGroupId(cc.getOrgGroupId());
        setFederalEmployerId(cc.getFederalEmployerId());
        setIdentifier(cc.getIdentifier());
        setName(cc.getName());
        setAddressId(cc.getAddressId());
        setCity(cc.getCity());
        setPrimaryAgentPersonalEmail(cc.getMainContactPersonalEmail());
        setPrimaryAgentFname(cc.getMainContactFname());
        setPrimaryAgentLname(cc.getMainContactLname());
        setPrimaryAgentLogin(cc.getMainContactLogin());
        setPrimaryAgentCanLogin(cc.getMainContactCanLogin());
        setPrimaryAgentPassword(cc.getMainContactPassword());
        setPrimaryAgentScreenGroupId(cc.getMainContactScreenGroupId());
        setPrimaryAgentScreenGroupName(cc.getMainContactScreenGroupName());
        setMainContactScreenGroupExtId(cc.getMainContactScreenGroupExtId());

        setPrimaryAgentJobTitle(cc.getMainContactJobTitle());
        setPrimaryAgentWorkFax(cc.getMainContactWorkFax());
        setPrimaryAgentWorkPhone(cc.getMainContactWorkPhone());
        setMainFaxNumber(cc.getMainFaxNumber());
        setMainPhoneNumber(cc.getMainPhoneNumber());
        setState(cc.getState());
        setStreet(cc.getStreet());
        setZip(cc.getZip());
        primaryAgentSecurityGroupId = cc.getMainContactSecurityGroupId();
        primaryAgentSecurityGroupName = cc.getMainContactSecurityGroupName();
        street2 = cc.getStreet2();


        ediApplicationSenderId = cc.getEdiApplicationSenderId();
        ediApplicationReceiverId = cc.getEdiApplicationReceiverId();
        ediInterchangeSenderId = cc.getEdiInterchangeSenderId();
        ediInterchangeReceiverId = cc.getEdiInterchangeReceiverId();
        ediTransferSchemeId = cc.getEdiTransferSchemeId();
        ediTransferHost = cc.getEdiTransferHost();
        ediTransferPort = cc.getEdiTransferPort();
        ediTransferUsername = cc.getEdiTransferUsername();
        ediTransferPassword = cc.getEdiTransferPassword();
        ediTransferDirectory = cc.getEdiTransferDirectory();
        ediTransferEncryptionKeyIdInHex = cc.getEdiTransferEncrpytionKey();
        ediTransferEncryptionKeyText = cc.getEditTransferEncryptionKeyText();

    }

    /**
     * @return Returns the addressId.
     */
    public String getAddressId() {
        return addressId;
    }

    /**
     * @param addressId The addressId to set.
     */
    public void setAddressId(final String addressId) {
        this.addressId = addressId;
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


    /**
     * @return Returns the mainContactScreenGroupExtId.
     */
    public String getMainContactScreenGroupExtId() {
        return mainContactScreenGroupExtId;
    }

    /**
     * @param mainContactScreenGroupExtId The mainContactScreenGroupExtId to set.
     */
    public void setMainContactScreenGroupExtId(final String mainContactScreenGroupExtId) {
        this.mainContactScreenGroupExtId = mainContactScreenGroupExtId;
    }

    /**
     * @return Returns the mainFaxNumber.
     */
    public String getMainFaxNumber() {
        return mainFaxNumber;
    }

    /**
     * @param mainFaxNumber The mainFaxNumber to set.
     */
    public void setMainFaxNumber(final String mainFaxNumber) {
        this.mainFaxNumber = mainFaxNumber;
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

	
