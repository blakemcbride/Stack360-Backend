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
package com.arahant.services.standard.crm.clientSummary;

import com.arahant.annotation.Validation;
import com.arahant.business.BClientCompany;
import com.arahant.services.TransmitInputBase;

public class SaveSummaryInput extends TransmitInputBase {

	@Validation(min = 1, max = 16, required = true)
	private String id;
	@Validation(min = .01, max = 100000, table = "client_company", column = "billing_rate", required = false)
	private double billingRate;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(type = "date", required = false)
	private int contractDate;
	@Validation(table = "company_base", column = "federal_employer_id", required = false)
	private String federalEmployerId;
	@Validation(table = "gl_account", column = "gl_account_id", required = false)
	private String glSalesAccountId;
	@Validation(table = "company_base", column = "identifier", required = false)
	private String identifier;
	@Validation(type = "date", required = false)
	private int inactiveDate;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainFaxNumber;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainPhoneNumber;
	@Validation(table = "org_group", column = "name", required = true)
	private String name;
	@Validation(table = "address", column = "state", required = false)
	private String state;
	@Validation(table = "address", column = "street", required = false)
	private String street;
	@Validation(table = "address", column = "street2", required = false)
	private String street2;
	@Validation(table = "address", column = "zip", required = false)
	private String zip;
	@Validation(table = "client_status", column = "client_status_id", required = true)
	private String statusId;
	@Validation(min = 0, max = 360, required = false)
	private short paymentTerms;
	@Validation(table = "client", column = "vendor_number", required = false)
	private String vendorNumber;
	@Validation(table = "client", column = "default_project_code", required = false)
	private String projectCode;

	void setData(BClientCompany bc) {
		bc.setBillingRate((float) billingRate);
		bc.setCity(city);
		bc.setContractDate(contractDate);
		bc.setFederalEmployerId(federalEmployerId);
		bc.setGlSalesAccount(glSalesAccountId);
		bc.setIdentifier(identifier);
		bc.setInactiveDate(inactiveDate);
		bc.setMainFaxNumber(mainFaxNumber);
		bc.setMainPhoneNumber(mainPhoneNumber);
		bc.setName(name);
		bc.setState(state);
		bc.setStreet(street);
		bc.setStreet2(street2);
		bc.setZip(zip);
		bc.setClientStatusId(statusId);
		bc.setPaymentTerms(paymentTerms);
		bc.setVendorNumber(vendorNumber);
		bc.setDefaultProjectCode(projectCode);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getContractDate() {
		return contractDate;
	}

	public void setContractDate(int contractDate) {
		this.contractDate = contractDate;
	}

	public String getFederalEmployerId() {
		return federalEmployerId;
	}

	public void setFederalEmployerId(String federalEmployerId) {
		this.federalEmployerId = federalEmployerId;
	}

	public String getGlSalesAccountId() {
		return glSalesAccountId;
	}

	public void setGlSalesAccountId(String glSalesAccountId) {
		this.glSalesAccountId = glSalesAccountId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public String getMainFaxNumber() {
		return mainFaxNumber;
	}

	public void setMainFaxNumber(String mainFaxNumber) {
		this.mainFaxNumber = mainFaxNumber;
	}

	public String getMainPhoneNumber() {
		return mainPhoneNumber;
	}

	public void setMainPhoneNumber(String mainPhoneNumber) {
		this.mainPhoneNumber = mainPhoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public short getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(short paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
