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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.misc.vendor;

import com.arahant.business.BVendorCompany;
import com.arahant.services.TransmitReturnBase;
import org.kissweb.StringUtils;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class LoadVendorCompanyReturn extends TransmitReturnBase {
	
	private String orgGroupId;
	private String name;
	private String identifier;
	private String federalEmployerId;
	private String mainContactWorkPhone;
	private String mainContactWorkFax;
	private String mainContactPersonalEmail;
	private String mainContactJobTitle;
	private String mainContactLname;
	private String mainContactFname;
	private String mainContactLogin;
	private String mainContactPassword;
	private boolean mainContactCanLogin;
	private String mainContactScreenGroupId;
	private String mainContactScreenGroupName;
	private String mainContactScreenGroupExtId;
	private String addressId;
	private String street,street2;
	private String city;
	private String state;
	private String zip;
	private String mainPhoneNumber;
	private String mainFaxNumber;
	private String expenseGLAccountId;
	private String accountNumber;
	private String mainContactSecurityGroupId;
	private String  mainContactSecurityGroupName;
	private String ediApplicationSenderId;
	private String ediApplicationReceiverId, ediInterchangeSenderId, ediInterchangeReceiverId, ediTransferSchemeId, ediTransferHost;
	private int ediTransferPort;
	private String ediTransferUsername, ediTransferPassword, ediTransferDirectory;
	private String ediTransferEncryptionKeyIdInHex;
	private String ediTransferEncryptionKeyText;
    private int ediInterfaceId;
	private String daysToSend;
	private int timeToSend;
	private String ediActivated;
	private String ediFileType;
	private String ediFileStatus;

	public String getEdiFileStatus() {
		return ediFileStatus;
	}

	public void setEdiFileStatus(String ediFileStatus) {
		this.ediFileStatus = ediFileStatus;
	}

	public String getEdiFileType() {
		return ediFileType;
	}

	public void setEdiFileType(String ediFileType) {
		this.ediFileType = ediFileType;
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



	public LoadVendorCompanyReturn()
	{
		
	}
	
	

	/**
	 * @param company
	 */
	void setData(final BVendorCompany cc) {
		setOrgGroupId(cc.getOrgGroupId());
		setFederalEmployerId(cc.getFederalEmployerId());
		setIdentifier(cc.getIdentifier());
		setName(cc.getName());
		setAddressId(cc.getAddressId());
		setCity(cc.getCity());
		setMainContactPersonalEmail(cc.getMainContactPersonalEmail());
		setMainContactFname(cc.getMainContactFname());
		setMainContactLname(cc.getMainContactLname());
		setMainContactLogin(cc.getMainContactLogin());
		setMainContactCanLogin(cc.getMainContactCanLogin());
		setMainContactPassword(cc.getMainContactPassword());
		setMainContactScreenGroupId(cc.getMainContactScreenGroupId());
		setMainContactScreenGroupName(cc.getMainContactScreenGroupName());
		setMainContactScreenGroupExtId(cc.getMainContactScreenGroupExtId());
		
		setMainContactJobTitle(cc.getMainContactJobTitle());
		setMainContactWorkFax(cc.getMainContactWorkFax());
		setMainContactWorkPhone(cc.getMainContactWorkPhone());
		setMainFaxNumber(cc.getMainFaxNumber());
		setMainPhoneNumber(cc.getMainPhoneNumber());
		setState(cc.getState());
		setStreet(cc.getStreet());
		setZip(cc.getZip());
		setExpenseGLAccountId(cc.getGLExpenseAccount());
		setAccountNumber(cc.getAccountNumber());
		setDaysToSend(cc.getDaysToSend());
		setTimeToSend(cc.getTimeToSend());
		setEdiActivated(cc.getEdiActivated());
		mainContactSecurityGroupId=cc.getMainContactSecurityGroupId();
		mainContactSecurityGroupName=cc.getMainContactSecurityGroupName();
		street2=cc.getStreet2();
		
		
		ediApplicationSenderId=cc.getEdiApplicationSenderId();
		ediApplicationReceiverId=cc.getEdiApplicationReceiverId();
		ediInterchangeSenderId=cc.getEdiInterchangeSenderId();
		ediInterchangeReceiverId=cc.getEdiInterchangeReceiverId();
		ediTransferSchemeId=cc.getEdiTransferSchemeId();
		ediTransferHost=cc.getEdiTransferHost();
		ediTransferPort=cc.getEdiTransferPort();
		ediTransferUsername=cc.getEdiTransferUsername();
		ediTransferPassword=cc.getEdiTransferPassword();
		ediTransferDirectory=cc.getEdiTransferDirectory();
		ediTransferEncryptionKeyIdInHex=cc.getEdiTransferEncrpytionKey();
		ediTransferEncryptionKeyText=cc.getEditTransferEncryptionKeyText();
        ediInterfaceId=(int)cc.getInterfaceId();
		ediFileStatus=cc.getEdiFileStatus();
		ediFileType=cc.getEdiFileType();

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
	 * @return Returns the mainContactCanLogin.
	 */
	public boolean isMainContactCanLogin() {
		return mainContactCanLogin;
	}



	/**
	 * @param mainContactCanLogin The mainContactCanLogin to set.
	 */
	public void setMainContactCanLogin(final boolean mainContactCanLogin) {
		this.mainContactCanLogin = mainContactCanLogin;
	}



	/**
	 * @return Returns the mainContactFname.
	 */
	public String getMainContactFname() {
		return mainContactFname;
	}



	/**
	 * @param mainContactFname The mainContactFname to set.
	 */
	public void setMainContactFname(final String mainContactFname) {
		this.mainContactFname = mainContactFname;
	}



	/**
	 * @return Returns the mainContactLname.
	 */
	public String getMainContactLname() {
		return mainContactLname;
	}



	/**
	 * @param mainContactLname The mainContactLname to set.
	 */
	public void setMainContactLname(final String mainContactLname) {
		this.mainContactLname = mainContactLname;
	}



	/**
	 * @return Returns the mainContactLogin.
	 */
	public String getMainContactLogin() {
		return mainContactLogin;
	}



	/**
	 * @param mainContactLogin The mainContactLogin to set.
	 */
	public void setMainContactLogin(final String mainContactLogin) {
		this.mainContactLogin = mainContactLogin;
	}



	/**
	 * @return Returns the mainContactPassword.
	 */
	public String getMainContactPassword() {
		return mainContactPassword;
	}



	/**
	 * @param mainContactPassword The mainContactPassword to set.
	 */
	public void setMainContactPassword(final String mainContactPassword) {
		this.mainContactPassword = mainContactPassword;
	}



	/**
	 * @return Returns the mainContactPersonalEmail.
	 */
	public String getMainContactPersonalEmail() {
		return mainContactPersonalEmail;
	}



	/**
	 * @param mainContactPersonalEmail The mainContactPersonalEmail to set.
	 */
	public void setMainContactPersonalEmail(final String mainContactPersonalEmail) {
		this.mainContactPersonalEmail = mainContactPersonalEmail;
	}



	

	/**
	 * @return Returns the mainContactScreenGroupId.
	 */
	public String getMainContactScreenGroupId() {
		return mainContactScreenGroupId;
	}



	/**
	 * @param mainContactScreenGroupId The mainContactScreenGroupId to set.
	 */
	public void setMainContactScreenGroupId(final String mainContactScreenGroupId) {
		this.mainContactScreenGroupId = mainContactScreenGroupId;
	}



	/**
	 * @return Returns the mainContactScreenGroupName.
	 */
	public String getMainContactScreenGroupName() {
		return mainContactScreenGroupName;
	}



	/**
	 * @param mainContactScreenGroupName The mainContactScreenGroupName to set.
	 */
	public void setMainContactScreenGroupName(final String mainContactScreenGroupName) {
		this.mainContactScreenGroupName = mainContactScreenGroupName;
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
	 * @return Returns the mainContactJobTitle.
	 */
	public String getMainContactJobTitle() {
		return mainContactJobTitle;
	}



	/**
	 * @param mainContactJobTitle The mainContactJobTitle to set.
	 */
	public void setMainContactJobTitle(final String mainContactJobTitle) {
		this.mainContactJobTitle = mainContactJobTitle;
	}



	/**
	 * @return Returns the mainContactWorkFax.
	 */
	public String getMainContactWorkFax() {
		return mainContactWorkFax;
	}



	/**
	 * @param mainContactWorkFax The mainContactWorkFax to set.
	 */
	public void setMainContactWorkFax(final String mainContactWorkFax) {
		this.mainContactWorkFax = mainContactWorkFax;
	}



	/**
	 * @return Returns the mainContactWorkPhone.
	 */
	public String getMainContactWorkPhone() {
		return mainContactWorkPhone;
	}



	/**
	 * @param mainContactWorkPhone The mainContactWorkPhone to set.
	 */
	public void setMainContactWorkPhone(final String mainContactWorkPhone) {
		this.mainContactWorkPhone = mainContactWorkPhone;
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
	 * @return Returns the mainContactSecurityGroupId.
	 */
	public String getMainContactSecurityGroupId() {
		return mainContactSecurityGroupId;
	}



	/**
	 * @param mainContactSecurityGroupId The mainContactSecurityGroupId to set.
	 */
	public void setMainContactSecurityGroupId(final String mainContactSecurityGroupId) {
		this.mainContactSecurityGroupId = mainContactSecurityGroupId;
	}



	/**
	 * @return Returns the mainContactSecurityGroupName.
	 */
	public String getMainContactSecurityGroupName() {
		return mainContactSecurityGroupName;
	}



	/**
	 * @param mainContactSecurityGroupName The mainContactSecurityGroupName to set.
	 */
	public void setMainContactSecurityGroupName(final String mainContactSecurityGroupName) {
		this.mainContactSecurityGroupName = mainContactSecurityGroupName;
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

    public int getEdiInterfaceId() {
        return ediInterfaceId;
    }

    public void setEdiInterfaceId(int ediInterfaceId) {
        this.ediInterfaceId = ediInterfaceId;
    }

	public String getDaysToSend() {
		return daysToSend;
	}

	public void setDaysToSend(String daysToSend) {
		this.daysToSend = daysToSend;
	}

	public String getEdiActivated() {
		return ediActivated;
	}

	public void setEdiActivated(String ediActivated) {
		this.ediActivated = ediActivated;
	}

	public int getTimeToSend() {
		return timeToSend;
	}

	public void setTimeToSend(int timeToSend) {
		this.timeToSend = timeToSend;
	}
	
}

	
