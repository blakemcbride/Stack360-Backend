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

import com.arahant.business.BPaymentInfo;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;

public class LoadPaymentInfoReturn extends TransmitReturnBase {

	private String paymentInfoId;
	private int paymentType;
	private String accountName;
	private String accountNumber;
	private String bankName;
	private String bankRoute;
	private int expirationDate;
	private String cvcCode;
	private String street;
	private String city;
	private String state;
	private String zip;

	public void setData(BPaymentInfo bpi) {
		paymentInfoId = bpi.getPaymentInfoId();
		paymentType = bpi.getPaymentType();
		accountName = bpi.getAccountName();
		accountNumber = bpi.getAccountNumber();
		bankName = bpi.getBankDraftBankName();
		bankRoute = bpi.getBankDraftBankRoute();
		expirationDate = bpi.getCcExpire();
		cvcCode = bpi.getCcCvcCode();
		street = bpi.getBillingStreet();
		city = bpi.getBillingCity();
		state = bpi.getBillingState();
		zip = bpi.getBillingZip();
	}

	void setData(String personId) {
		BPerson bp = new BPerson(personId);
		if(bp.hasPending(personId))
			bp.loadPending(personId);
		street = bp.getStreet();
		if(isEmpty(street))
			street = bp.getStreetPending();
		city = bp.getCity();
		if(isEmpty(city))
			city = bp.getCityPending();
		state = bp.getState();
		if(isEmpty(state))
			state = bp.getStatePending();
		zip = bp.getZip();
		if(isEmpty(zip))
			zip = bp.getZipPending();
		accountName = bp.getNameFML();
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

	public String getPaymentInfoId() {
		return paymentInfoId;
	}

	public void setPaymentInfoId(String paymentInfoId) {
		this.paymentInfoId = paymentInfoId;
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
}

	
