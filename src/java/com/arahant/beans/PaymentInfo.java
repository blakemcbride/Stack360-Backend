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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.Date;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "payment_info")
public class PaymentInfo extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "payment_info";
	public static final String PAYMENT_INFO_ID = "paymentInfoId";
	private String paymentInfoId;
	public static final String PERSON = "person";
	private Person person;
	public static final String PERSON_ID = "personId";
	private String personId;
	public static final String DATE_AUTHORIZED = "dateAuthorized";
	protected Date dateAuthorized;
	public static final String ADDRESS_IP = "addressIP";
	private String addressIP;
	public static final String PAYMENT_TYPE = "paymentType";
	private int paymentType;
	public static final String ACCOUNT_NAME = "accountName";
	private String accountName;
	public static final String ACCOUNT_NUMBER = "accountNumber";
	private String accountNumber;
	public static final String BANK_DRAFT_BANK_NAME = "bankDraftBankName";
	private String bankDraftBankName;
	public static final String BANK_DRAFT_BANK_ROUTE = "bankDraftBankRoute";
	private String bankDraftBankRoute;
	public static final String CC_EXPIRE = "ccExpire";
	private int ccExpire;
	public static final String CC_CVC_CODE = "ccCvcCode";
	private String ccCvcCode;
	public static final String BILLING_STREET = "billingStreet";
	private String billingStreet;
	public static final String BILLING_CITY = "billingCity";
	private String billingCity;
	public static final String BILLING_STATE = "billingState";
	private String billingState;
	public static final String BILLING_ZIP = "billingZip";
	private String billingZip;
	public static final String BENEFIT_ID = "benefitId";
	private String benefitId;

	//Default constructor
	public PaymentInfo() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "person_id", updatable = false, insertable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Column(name = "address_ip")
	public String getAddressIP() {
		return addressIP;
	}

	public void setAddressIP(String addressIP) {
		this.addressIP = addressIP;
	}

	@Id
	@Column(name = "payment_id")
	public String getPaymentInfoId() {
		return paymentInfoId;
	}

	public void setPaymentInfoId(String paymentInfoId) {
		this.paymentInfoId = paymentInfoId;
	}

	@Column(name = "account_name")
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "account_number")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "bank_draft_bank_name")
	public String getBankDraftBankName() {
		return bankDraftBankName;
	}

	public void setBankDraftBankName(String bankDraftBankName) {
		this.bankDraftBankName = bankDraftBankName;
	}

	@Column(name = "bank_draft_bank_route")
	public String getBankDraftBankRoute() {
		return bankDraftBankRoute;
	}

	public void setBankDraftBankRoute(String bankDraftBankRoute) {
		this.bankDraftBankRoute = bankDraftBankRoute;
	}

	@Column(name = "billing_city")
	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	@Column(name = "billing_state")
	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	@Column(name = "billing_street")
	public String getBillingStreet() {
		return billingStreet;
	}

	public void setBillingStreet(String billingStreet) {
		this.billingStreet = billingStreet;
	}

	@Column(name = "billing_zip")
	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	@Column(name = "cc_cvc_code")
	public String getCcCvcCode() {
		return ccCvcCode;
	}

	public void setCcCvcCode(String ccCvcCode) {
		this.ccCvcCode = ccCvcCode;
	}

	@Column(name = "cc_expire")
	public int getCcExpire() {
		return ccExpire;
	}

	public void setCcExpire(int ccExpire) {
		this.ccExpire = ccExpire;
	}

	@Column(name = "date_authorized")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateAuthorized() {
		return dateAuthorized;
	}

	public void setDateAuthorized(Date dateAuthorized) {
		this.dateAuthorized = dateAuthorized;
	}

	@Column(name = "payment_type")
	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	@Column(name = "benefit_id")
	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "payment_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setPaymentInfoId(IDGenerator.generate(this));
		return paymentInfoId;
	}

	@Override
	public PaymentInfo clone() {
		PaymentInfo p = new PaymentInfo();
		p.generateId();
		return p;
	}

	@Override
	public boolean equals(Object o) {
		if (paymentInfoId == null && o == null)
			return true;
		if (paymentInfoId != null && o instanceof PaymentInfo)
			return paymentInfoId.equals(((PaymentInfo) o).getPaymentInfoId());

		return false;
	}

	@Override
	public int hashCode() {
		if (paymentInfoId == null)
			return 0;
		return paymentInfoId.hashCode();
	}
}
