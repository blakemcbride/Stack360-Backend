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
package com.arahant.services.standard.hrConfig.benefitSetup;
import com.arahant.annotation.Validation;

import com.arahant.business.BVendorCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class NewVendorCompanyInput extends TransmitInputBase {

	@Validation (required=true)
	private String name;
	@Validation (table="company_base",column="identifier",required=false)
	private String identifier;
	@Validation (table="company_base",column="federal_employer_id",required=false)
	private String federalEmployerId;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactWorkPhone;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactWorkFax;
	@Validation (table="person",column="personal_email",required=false)
	private String mainContactPersonalEmail;
	@Validation (table="person",column="job_title",required=false)
	private String mainContactJobTitle;
	@Validation (table="person",column="lname",required=false)
	private String mainContactLname;
	@Validation (table="person",column="fname",required=false)
	private String mainContactFname;
	@Validation (table="prophet_login",column="user_login",required=false)
	private String mainContactLogin;
	@Validation (table="prophet_login",column="user_password",required=false)
	private String mainContactPassword;
	@Validation (required=false)
	private boolean mainContactCanLogin;
	@Validation (required=false)
	private String mainContactScreenGroupId;
	@Validation (table="address",column="street",required=false)
	private String street;
	@Validation (table="address",column="street2",required=false)
	private String street2;
	@Validation (table="address",column="city",required=false)
	private String city;
	@Validation (table="address",column="state",required=false)
	private String state;
	@Validation (table="address",column="zip",required=false)
	private String zip;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainPhoneNumber;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainFaxNumber;
	@Validation (required=false)
	private String expenseGLAccountId;
	@Validation (table="vendor",column="account_number",required=false)
	private String accountNumber;
	@Validation (required=false)
	private String mainContactSecurityGroupId;
	@Validation (required=false)
	private String ediApplicationSenderId;
	@Validation (required=false)
	private String ediApplicationReceiverId;
	@Validation (required=false)
	private String ediInterchangeSenderId;
	@Validation (required=false)
	private String ediInterchangeReceiverId;
	@Validation (required=false)
	private String ediTransferSchemeId;
	@Validation (required=false)
	private String ediTransferHost;
	@Validation (min=0,required=false)
	private int ediTransferPort;
	@Validation (required=false)
	private String ediTransferUsername;
	@Validation (required=false)
	private String ediTransferPassword;
	@Validation (required=false)
	private String ediTransferDirectory;
	@Validation (required=false)
	private String ediTransferEncryptionKeyIdInHex;
	@Validation (required=false)
	private String ediTransferEncryptionKeyText;
	

	
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
	 * @param v
	 * @throws ArahantException 
	 */
	void makeVendor(final BVendorCompany v) throws ArahantException {
		v.setName(name);
		v.setIdentifier(identifier);
		v.setFederalEmployerId(federalEmployerId);
		v.setMainContactWorkPhone(mainContactWorkPhone);
		v.setMainContactWorkFax(mainContactWorkFax);
		v.setMainContactPersonalEmail(mainContactPersonalEmail);
		v.setMainContactJobTitle(mainContactJobTitle);
		v.setMainContactFname(mainContactFname);
		v.setMainContactLname(mainContactLname);
		v.setMainContactLogin(mainContactLogin);
		v.setMainContactPassword(mainContactPassword, mainContactCanLogin);
		v.setMainContactScreenGroupId(mainContactScreenGroupId);
		v.setState(state);
		v.setStreet(street);
		v.setCity(city);
		v.setZip(zip);
		v.setMainPhoneNumber(mainPhoneNumber);
		v.setMainFaxNumber(mainFaxNumber);
		v.setAccountNumber(accountNumber);
		v.setExpenseGLAccount(expenseGLAccountId);
		v.setMainContactSecurityGroupId(mainContactSecurityGroupId);
		v.setStreet2(street2);
		
		v.setEdiApplicationSenderId(ediApplicationSenderId);
		v.setEdiApplicationReceiverId(ediApplicationReceiverId);
		v.setEdiInterchangeSenderId(ediInterchangeSenderId);
		v.setEdiInterchangeReceiverId(ediInterchangeReceiverId);
		v.setEdiTransferURL(ediTransferSchemeId,ediTransferHost,ediTransferPort,ediTransferUsername);
		v.setEdiTransferPassword(ediTransferPassword);
		v.setEdiTransferDirectory(ediTransferDirectory);
		v.setEdiTransferEncryptionKey(ediTransferEncryptionKeyIdInHex);
		v.setEdiTransferEncryptionKeyText(ediTransferEncryptionKeyText);
		
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

	
