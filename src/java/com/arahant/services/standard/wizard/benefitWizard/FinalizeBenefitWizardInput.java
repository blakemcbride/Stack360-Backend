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
 *
 */
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.business.BPaymentInfo;
import com.arahant.services.TransmitInputBase;
import java.util.Date;

public class FinalizeBenefitWizardInput extends TransmitInputBase {

	@Validation(table = "employee", column = "person_id", required = false)
	private String employeeId;
	@Validation(required = true)
	private String wizardConfigurationId;
	@Validation(required = true)
	private String modal;
	@Validation(required = false, table = "payment_info", column = "payment_info_id")
	private String paymentInfoId;
	@Validation(required = true, table = "payment_info", column = "payment_type")
	private int paymentType;
	@Validation(required = true, table = "payment_info", column = "account_name")
	private String accountName;
	@Validation(required = true, table = "payment_info", column = "account_number")
	private String accountNumber;
	@Validation(required = false, table = "payment_info", column = "bank_draft_bank_name")
	private String bankName;
	@Validation(required = false, table = "payment_info", column = "bank_draft_bank_route")
	private String bankRoute;
	@Validation(required = false, type = "date", table = "payment_info", column = "cc_expire")
	private int expirationDate;
	@Validation(required = false, table = "payment_info", column = "cc_cvc_code")
	private String cvcCode;
	@Validation(required = false, table = "payment_info", column = "billing_street")
	private String street;
	@Validation(required = false, table = "payment_info", column = "billing_city")
	private String city;
	@Validation(required = false, table = "payment_info", column = "billing_state")
	private String state;
	@Validation(required = false, table = "payment_info", column = "billing_zip")
	private String zip;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public void newPaymentInfoData(BPaymentInfo bpi, Date now, String ipAddress) {
		bpi.setPersonId(employeeId);
		bpi.setPaymentType(paymentType);
		bpi.setAccountName(accountName);
		bpi.setAccountNumber(accountNumber);
		bpi.setBankDraftBankName(bankName);
		bpi.setBankDraftBankRoute(bankRoute);
		bpi.setCcExpire(expirationDate);
		bpi.setCcCvcCode(cvcCode);
		bpi.setBillingStreet(street);
		bpi.setBillingCity(city);
		bpi.setBillingState(state);
		bpi.setBillingZip(zip);
		bpi.setDateAuthorized(now);
		bpi.setAddressIP(ipAddress);
		bpi.insert();
	}

	public void savePaymentInfoData(BPaymentInfo bpi, Date now, String ipAddress) {
		bpi.setPersonId(employeeId);
		bpi.setPaymentType(paymentType);
		bpi.setAccountName(accountName);
		bpi.setAccountNumber(accountNumber);
		bpi.setBankDraftBankName(bankName);
		bpi.setBankDraftBankRoute(bankRoute);
		bpi.setCcExpire(expirationDate);
		bpi.setCcCvcCode(cvcCode);
		bpi.setBillingStreet(street);
		bpi.setBillingCity(city);
		bpi.setBillingState(state);
		bpi.setBillingZip(zip);
		bpi.setDateAuthorized(now);
		bpi.setAddressIP(ipAddress);
		bpi.update();
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankRoute() {
		return bankRoute;
	}

	public void setBankRoute(String bankRoute) {
		this.bankRoute = bankRoute;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCvcCode() {
		return cvcCode;
	}

	public void setCvcCode(String cvcCode) {
		this.cvcCode = cvcCode;
	}

	public int getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(int expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPaymentInfoId() {
		return paymentInfoId;
	}

	public void setPaymentInfoId(String paymentInfoId) {
		this.paymentInfoId = paymentInfoId;
	}
}
