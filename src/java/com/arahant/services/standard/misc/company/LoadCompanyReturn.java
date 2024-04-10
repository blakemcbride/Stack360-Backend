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

import com.arahant.business.BCompany;
import com.arahant.services.TransmitReturnBase;

public final class LoadCompanyReturn extends TransmitReturnBase {

	private String accountingBasis;
	private String name;
	private String federalEmployerId;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String stateProvince;
	private String zipPostalCode;
	private String country;
	private String county;
	private String mainPhoneNumber;
	private String mainFaxNumber;
	private double billingRate;
	private String eeoCompanyNumber;
	private String eeoUnitNumber;
	private String dunBradstreet;
	private String naics;
	private String arahantURL;
	private boolean accrualsUseTimeOffRequest;
	private String logoSource;
	private short fiscalBeginningMonth;
	private String emailUser;
	private String emailPw;
	private short emailType;
	private String emailHost;
	private String emailFromName;
	private String emailFromEmail;
	private int emailPort;
	private String emailEncryption;
	private String emailAuthentication;
	private String emailDomain;

	public String getLogoSource() {
		return logoSource;
	}

	public void setLogoSource(String logoSource) {
		this.logoSource = logoSource;
	}

	public boolean getAccrualsUseTimeOffRequest() {
		return accrualsUseTimeOffRequest;
	}

	public void setAccrualsUseTimeOffRequest(boolean accrualsUseTimeOffRequest) {
		this.accrualsUseTimeOffRequest = accrualsUseTimeOffRequest;
	}

	public String getArahantURL() {
		return arahantURL;
	}

	public void setArahantURL(String arahantURL) {
		this.arahantURL = arahantURL;
	}

	public String getDefaultBillingRateFormatted() {
		return defaultBillingRateFormatted;
	}

	public void setDefaultBillingRateFormatted(String defaultBillingRateFormatted) {
		this.defaultBillingRateFormatted = defaultBillingRateFormatted;
	}
	private String defaultBillingRateFormatted;

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}

	public LoadCompanyReturn() {
	}

	/**
	 * @param company
	 */
	public LoadCompanyReturn(final BCompany company) {
		setAccountingBasis(company.getAccountingBasis() + "");
		setFederalEmployerId(company.getFederalEmployerId());
		setName(company.getName());
		addressLine1 = company.getStreet();
		addressLine2 = company.getStreet2();
		city = company.getCity();
		stateProvince = company.getState();
		zipPostalCode = company.getZip();
		country = company.getCountry();
		county = company.getCounty();
		setMainFaxNumber(company.getMainFaxNumber());
		setMainPhoneNumber(company.getMainPhoneNumber());
		billingRate = company.getBillingRate();
		defaultBillingRateFormatted = BCompany.getDefaultBillingRateFormatted();
		county = company.getCounty();
		eeoCompanyNumber = company.getEeo1CompanyId();
		eeoUnitNumber = company.getEeo1UnitId();
		dunBradstreet = company.getDunBradstreet();
		naics = company.getNaicsCode();
		arahantURL = company.getArahantURL();
		accrualsUseTimeOffRequest = company.getAccrualsUseTimeOffRequest();
		logoSource = company.getBean().getLogoSource();
		fiscalBeginningMonth = company.getFiscalBeginningMonth();
		emailUser = company.getEmailOutUserId();
		emailPw = company.getEmailOutUserPw();
		emailType = company.getEmailOutType();
		emailHost = company.getEmailOutHost();
		emailFromName = company.getEmailOutFromName();
		emailFromEmail = company.getEmailOutFromEmail();
		emailPort = company.getEmailOutPort();
		emailEncryption = company.getEmailOutEncryption() + "";
		emailAuthentication = company.getEmailOutAuthentication() + "";
		emailDomain = company.getEmailOutDomain();
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
