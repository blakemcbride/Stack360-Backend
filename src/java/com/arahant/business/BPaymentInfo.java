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



package com.arahant.business;

import com.arahant.beans.PaymentInfo;
import com.arahant.beans.Person;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import java.util.Date;

/**
 *
 */
public class BPaymentInfo extends SimpleBusinessObjectBase<PaymentInfo> implements IDBFunctions {/**
     */
    public BPaymentInfo() {
    }

	/**
     * @param eSignatureId
     * @throws ArahantException
     */
    public BPaymentInfo(final String paymentInfoId) throws ArahantException {
        internalLoad(paymentInfoId);
    }

	/**
	 * @param ESignature
	 */
	public BPaymentInfo(final PaymentInfo p) {
		bean = p;
	}

	public String getAddressIP() {
		return bean.getAddressIP();
	}

	public void setAddressIP(String addressIP) {
		bean.setAddressIP(addressIP);
	}

	public String getPaymentInfoId() {
		return bean.getPaymentInfoId();
	}

	public void setPaymentInfoId(String paymentInfoId) {
		bean.setPaymentInfoId(paymentInfoId);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
		bean.setPersonId(personId);
	}

	public String getAccountName() {
		return bean.getAccountName();
	}

	public void setAccountName(String accountName) {
		bean.setAccountName(accountName);
	}

	public String getAccountNumber() {
		return bean.getAccountNumber();
	}

	public void setAccountNumber(String accountNumber) {
		bean.setAccountNumber(accountNumber);
	}

	public String getBankDraftBankName() {
		return bean.getBankDraftBankName();
	}

	public void setBankDraftBankName(String bankDraftBankName) {
		bean.setBankDraftBankName(bankDraftBankName);
	}

	public String getBankDraftBankRoute() {
		return bean.getBankDraftBankRoute();
	}

	public void setBankDraftBankRoute(String bankDraftBankRoute) {
		bean.setBankDraftBankRoute(bankDraftBankRoute);
	}

	public String getBillingCity() {
		return bean.getBillingCity();
	}

	public void setBillingCity(String billingCity) {
		bean.setBillingCity(billingCity);
	}

	public String getBillingState() {
		return bean.getBillingState();
	}

	public void setBillingState(String billingState) {
		bean.setBillingState(billingState);
	}

	public String getBillingStreet() {
		return bean.getBillingStreet();
	}

	public void setBillingStreet(String billingStreet) {
		bean.setBillingStreet(billingStreet);
	}

	public String getBillingZip() {
		return bean.getBillingZip();
	}

	public void setBillingZip(String billingZip) {
		bean.setBillingZip(billingZip);
	}

	public String getCcCvcCode() {
		return bean.getCcCvcCode();
	}

	public void setCcCvcCode(String ccCvcCode) {
		bean.setCcCvcCode(ccCvcCode);
	}

	public int getCcExpire() {
		return bean.getCcExpire();
	}

	public void setCcExpire(int ccExpire) {
		bean.setCcExpire(ccExpire);
	}

	public Date getDateAuthorized() {
		return bean.getDateAuthorized();
	}

	public void setDateAuthorized(Date dateAuthorized) {
		bean.setDateAuthorized(dateAuthorized);
	}

	public int getPaymentType() {
		return bean.getPaymentType();
	}

	public void setPaymentType(int paymentType) {
		bean.setPaymentType(paymentType);
	}
	
	public String getBenefitId() {
		return bean.getBenefitId();
	}
	
	public void setBenefitId(String id) {
		bean.setBenefitId(id);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		}
		catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for(String id : ids)
			new BReportTitle(id).delete();
	}

	@Override
	public String create() throws ArahantException {
        bean = new PaymentInfo();
        bean.generateId();
        return getPaymentInfoId();
	}
	
	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PaymentInfo.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}
}
