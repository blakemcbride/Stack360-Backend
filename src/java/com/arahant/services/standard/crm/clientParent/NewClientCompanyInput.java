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
package com.arahant.services.standard.crm.clientParent;
import com.arahant.annotation.Validation;

import com.arahant.beans.ClientStatus;
import com.arahant.business.BClientCompany;
import com.arahant.business.BClientStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class NewClientCompanyInput extends TransmitInputBase  {
	
	@Validation (type="date",required=false)
	private int contractDate;
	@Validation (type="date",required=false)
	private int inactiveDate;
	@Validation (min=.01,max=100000,table="client_company",column="billing_rate",required=false)
	private double billingRate;
	@Validation (table="gl_account",column="gl_account_id",required=false)
	private String glSalesAccount;
	@Validation (required=false)
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
	@Validation (table="person",column="jobTitle",required=false)
	private String mainContactJobTitle;
	@Validation (table="person",column="lname",required=false)
	private String mainContactLname;
	@Validation (table="person",column="fname",required=false)
	private String mainContactFname;
	@Validation (table="prophet_login",column="user_login",required=false)
	private String mainContactLogin;
	@Validation (table="prophet_login",column="user_password",required=false)
	private String mainContactPassword;
	@Validation (required=true)
	private boolean mainContactCanLogin;
	@Validation (table="prophet_login",column="screen_group_id",required=false)
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
	@Validation (table="prophet_login",column="security_group_id",required=false)
	private String mainContactSecurityGroupId;
	@Validation (table="client_status",column="client_status_id",required=true)
	private String statusId;
	@Validation (required=false)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
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
	 * @return Returns the billingRate.
	 */
	public double getBillingRate() {
		return billingRate;
	}
	/**
	 * @param billingRate The billingRate to set.
	 */
	public void setBillingRate(final double billingRate) {
		this.billingRate = billingRate;
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
	 * @return Returns the contractDate.
	 */
	public int getContractDate() {
		return contractDate;
	}
	/**
	 * @param contractDate The contractDate to set.
	 */
	public void setContractDate(final int contractDate) {
		this.contractDate = contractDate;
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
	 * @return Returns the glSalesAccount.
	 */
	public String getGlSalesAccount() {
		return glSalesAccount;
	}
	/**
	 * @param glSalesAccount The glSalesAccount to set.
	 */
	public void setGlSalesAccount(final String glSalesAccount) {
		this.glSalesAccount = glSalesAccount;
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
	 * @return Returns the inactiveDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}
	/**
	 * @param inactiveDate The inactiveDate to set.
	 */
	public void setInactiveDate(final int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	/**
	 * @return Returns the mainContactCanLogin.
	 */
	public boolean getMainContactCanLogin() {
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

	public String getMainContactJobTitle() {
		return mainContactJobTitle;
	}

	public void setMainContactJobTitle(String mainContactJobTitle) {
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
	 * @param bcc
	 * @throws ArahantException 
	 */
	void makeClientCompany(final BClientCompany bcc) throws ArahantException {
		bcc.setClientStatus(BClientStatus.findOrMake("New").getBean());
		bcc.setBillingRate((float)billingRate);
		bcc.setContractDate(contractDate);
		bcc.setFederalEmployerId(federalEmployerId);
		bcc.setIdentifier(identifier);
		bcc.setInactiveDate(inactiveDate);
		bcc.setGlSalesAccount(glSalesAccount);
		bcc.setName(name);
		bcc.setMainContactWorkPhone(mainContactWorkPhone);
		bcc.setMainContactWorkFax(mainContactWorkFax);
		bcc.setMainContactPersonalEmail(mainContactPersonalEmail);
		bcc.setMainContactJobTitle(mainContactJobTitle);
		bcc.setMainContactLname(mainContactLname);
		bcc.setMainContactFname(mainContactFname);
		bcc.setMainContactLogin(mainContactLogin);
		bcc.setMainContactPassword(mainContactPassword, mainContactCanLogin);
		bcc.setMainContactScreenGroupId(mainContactScreenGroupId);
		bcc.setStreet(street);
		bcc.setCity(city);
		bcc.setState(state);
		bcc.setZip(zip);
		bcc.setMainPhoneNumber(mainPhoneNumber);
		bcc.setMainFaxNumber(mainFaxNumber);
		bcc.setMainContactSecurityGroupId(mainContactSecurityGroupId);
		bcc.setStreet2(street2);
/*		bcc.setEdiApplicationSenderId(ediApplicationSenderId);
		bcc.setEdiApplicationReceiverId(ediApplicationReceiverId);
		bcc.setEdiInterchangeSenderId(ediInterchangeSenderId);
		bcc.setEdiInterchangeReceiverId(ediInterchangeReceiverId);
		bcc.setEdiTransferURL(ediTransferSchemeId,ediTransferHost,ediTransferPort,ediTransferUsername);
		bcc.setEdiTransferPassword(ediTransferPassword);
		bcc.setEdiTransferDirectory(ediTransferDirectory);
		bcc.setEdiTransferEncryptionKey(ediTransferEncryptionKeyIdInHex);
		bcc.setEdiTransferEncryptionKeyText(ediTransferEncryptionKeyText);
 * */
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
}

	
