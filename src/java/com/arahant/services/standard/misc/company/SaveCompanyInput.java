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
package com.arahant.services.standard.misc.company;

import com.arahant.annotation.Validation;
import com.arahant.business.BCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class SaveCompanyInput extends TransmitInputBase {

	@Validation(table = "company_detail", column = "accounting_basis", required = false)
	private String accountingBasis;
	@Validation(table = "org_group", column = "name", required = true)
	private String name;
	@Validation(table = "company_base", column = "federal_employer_id", required = false)
	private String federalEmployerId;
	@Validation(table = "address", column = "street", required = false)
	private String addressLine1;
	@Validation(table = "address", column = "street2", required = false)
	private String addressLine2;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(table = "address", column = "state", required = false)
	private String stateProvince;
	@Validation(table = "address", column = "zip", required = false)
	private String zipPostalCode;
	@Validation(table = "address", column = "country_code", required = false)
	private String country;
	@Validation(table = "address", column = "county", required = false)
	private String county;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainPhoneNumber;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainFaxNumber;
	@Validation(required = true)
	private String orgGroupId;
	@Validation(min = 0, required = false)
	private double billingRate;
	@Validation(table = "company_detail", column = "ar_account_id", required = false)
	private String glARAccountId;
	@Validation(table = "company_detail", column = "employee_advance_account_id", required = false)
	private String glEmployeeAdvanceAccountId;
	@Validation(table = "company_detail", column = "cash_account_id", required = false)
	private String glCashAccountId;
	@Validation(table = "company_detail", column = "eeo1_id", required = false)
	private String eeoCompanyNumber;
	@Validation(table = "org_group", column = "eeo1_unit", required = false)
	private String eeoUnitNumber;
	@Validation(table = "company_detail", column = "dun_bradstreet_num", required = false)
	private String dunBradstreet;
	@Validation(table = "company_detail", column = "naics", required = false)
	private String naics;
	@Validation(table = "company_base", column = "arahant_url")
	private String arahantUrl;
	@Validation(required = false)
	private boolean accrualsUseTimeOffRequest;
	@Validation(required = true, min = 1, max = 12)
	private short fiscalBeginningMonth;
	@Validation(required = false)
	private String logo;
	@Validation(table = "company_detail", column = "email_out_user_id", required = false)
	private String emailUser;
	@Validation(table = "company_detail", column = "email_out_user_pw", required = false)
	private String emailPw;
	@Validation(table = "company_detail", column = "email_out_type", required = false)
	private short emailType;
	@Validation(table = "company_detail", column = "email_out_host", required = false)
	private String emailHost;
	@Validation(table = "company_detail", column = "email_out_from_name", required = false)
	private String emailFromName;
	@Validation(table = "company_detail", column = "email_out_from_email", required = false)
	private String emailFromEmail;
	@Validation(table = "company_detail", column = "email_out_port", required = false)
	private int emailPort;
	@Validation(table = "company_detail", column = "email_out_encryption", required = false)
	private String emailEncryption;
	@Validation(table = "company_detail", column = "email_out_authentication", required = false)
	private String emailAuthentication;
	@Validation(table = "company_detail", column = "email_out_domain", required = false)
	private String emailDomain;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean getAccrualsUseTimeOffRequest() {
		return accrualsUseTimeOffRequest;
	}

	public void setAccrualsUseTimeOffRequest(boolean accrualsUseTimeOffRequest) {
		this.accrualsUseTimeOffRequest = accrualsUseTimeOffRequest;
	}

	public String getArahantUrl() {
		return arahantUrl;
	}

	public void setArahantUrl(String arahantUrl) {
		this.arahantUrl = arahantUrl;
	}

	public String getGlARAccountId() {
		return glARAccountId;
	}

	public void setGlARAccountId(String glARAccountId) {
		this.glARAccountId = glARAccountId;
	}

	public String getGlCashAccountId() {
		return glCashAccountId;
	}

	public void setGlCashAccountId(String glCashAccountId) {
		this.glCashAccountId = glCashAccountId;
	}

	public String getGlEmployeeAdvanceAccountId() {
		return glEmployeeAdvanceAccountId;
	}

	public void setGlEmployeeAdvanceAccountId(String glEmployeeAdvanceAccountId) {
		this.glEmployeeAdvanceAccountId = glEmployeeAdvanceAccountId;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
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
	 * @param bcc
	 * @throws ArahantException
	 */
	public void makeCompany(final BCompany bcc) throws ArahantException {
		bcc.setAccountingBasis(accountingBasis.charAt(0));
		bcc.setFederalEmployerId(federalEmployerId);
		bcc.setName(name);
		bcc.setStreet(addressLine1);
		bcc.setStreet2(addressLine2);
		bcc.setCity(city);
		bcc.setState(stateProvince);
		bcc.setZip(zipPostalCode);
		bcc.setCountry(country);
		bcc.setCounty(county);
		bcc.setMainPhoneNumber(mainPhoneNumber);
		bcc.setMainFaxNumber(mainFaxNumber);
		bcc.setBillingRate(billingRate);
		bcc.setARAccountId(glARAccountId);
		bcc.setEmployeeAdvanceAccountId(glEmployeeAdvanceAccountId);
		bcc.setCashAccountId(glCashAccountId);
		bcc.setEeo1CompanyId(eeoCompanyNumber);
		bcc.setEeo1UnitId(eeoUnitNumber);
		bcc.setDunBradstreet(dunBradstreet);
		bcc.setNaicsCode(naics);
		bcc.setArahantURL(arahantUrl);
		bcc.setAccrualsUseTimeOffRequest(accrualsUseTimeOffRequest);
		bcc.setFiscalBeginningMonth(fiscalBeginningMonth);
		if (isEmpty(logo)) {
			bcc.setLogoData(null);
			bcc.setLogoExtension(null);
			bcc.setLogoSource(null);
		}
		bcc.setEmailOutUserId(emailUser);
		bcc.setEmailOutUserPw(emailPw);
		bcc.setEmailOutType(emailType);
		bcc.setEmailOutHost(emailHost);
		bcc.setEmailOutFromName(emailFromName);
		bcc.setEmailOutFromEmail(emailFromEmail);
		bcc.setEmailOutPort(emailPort);
		bcc.setEmailOutEncryption(charValue(emailEncryption, 'N'));
		bcc.setEmailOutAuthentication(charValue(emailAuthentication, 'N'));
		bcc.setEmailOutDomain(emailDomain);
	}
	
	private char charValue(String x, char dflt) {
		return x == null || x.length() < 1 ? dflt : x.charAt(0);
	}

	/**
	 * @return Returns the accountingBasis.
	 */
	public String getAccountingBasis() {
		return accountingBasis;
	}

	/**
	 * @param accountingBasis The accountingBasis to set.
	 */
	public void setAccountingBasis(final String accountingBasis) {
		this.accountingBasis = accountingBasis;
	}

	public String getDunBradstreet() {
		return dunBradstreet;
	}

	public void setDunBradstreet(String dunBradstreet) {
		this.dunBradstreet = dunBradstreet;
	}

	public String getEeoCompanyNumber() {
		return eeoCompanyNumber;
	}

	public void setEeoCompanyNumber(String eeoCompanyNumber) {
		this.eeoCompanyNumber = eeoCompanyNumber;
	}

	public String getEeoUnitNumber() {
		return eeoUnitNumber;
	}

	public void setEeoUnitNumber(String eeoUnitNumber) {
		this.eeoUnitNumber = eeoUnitNumber;
	}

	public String getNaics() {
		return naics;
	}

	public void setNaics(String naics) {
		this.naics = naics;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}

	public short getFiscalBeginningMonth() {
		return fiscalBeginningMonth;
	}

	public void setFiscalBeginningMonth(short fiscalBeginningMonth) {
		this.fiscalBeginningMonth = fiscalBeginningMonth;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public String getEmailPw() {
		return emailPw;
	}

	public void setEmailPw(String emailPw) {
		this.emailPw = emailPw;
	}

	public short getEmailType() {
		return emailType;
	}

	public void setEmailType(short emailType) {
		this.emailType = emailType;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public String getEmailFromName() {
		return emailFromName;
	}

	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}

	public String getEmailFromEmail() {
		return emailFromEmail;
	}

	public void setEmailFromEmail(String emailFromEmail) {
		this.emailFromEmail = emailFromEmail;
	}

	public int getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(int emailPort) {
		this.emailPort = emailPort;
	}

	public String getEmailEncryption() {
		return emailEncryption;
	}

	public void setEmailEncryption(String emailEncryption) {
		this.emailEncryption = emailEncryption;
	}

	public String getEmailAuthentication() {
		return emailAuthentication;
	}

	public void setEmailAuthentication(String emailAuthentication) {
		this.emailAuthentication = emailAuthentication;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
	
}
