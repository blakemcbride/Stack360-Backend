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

import com.arahant.utils.StandardProperty;
import com.arahant.business.BClientCompany;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class LoadSummaryReturn extends TransmitReturnBase {

	private double billingRate;
	private String city;
	private String defaultBillingRateFormatted;
	private String federalEmployerId;
	private String glSalesAccountId;
	private String identifier;
	private String mainFaxNumber;
	private String mainPhoneNumber;
	private String name;
	private String state;
	private String street;
	private String street2;
	private String zip;
	private String statusId;
	private int contractDate;
	private int inactiveDate;
	private short paymentTerms;
	private String vendorNumber;
	private String projectCode;
	private boolean copyPicturesToDisk;
	private String pictureDiskPath;
	private boolean copyOnlyExternal;
	private boolean copyInactiveProjects;

	void setData(BClientCompany bc) {
		billingRate = bc.getBillingRate();
		city = bc.getCity();
		defaultBillingRateFormatted = BClientCompany.getDefaultBillingRateFormatted();
		federalEmployerId = bc.getFederalEmployerId();
		glSalesAccountId = bc.getGlSalesAccount();
		identifier = bc.getIdentifier();
		mainFaxNumber = bc.getMainFaxNumber();
		mainPhoneNumber = bc.getMainPhoneNumber();
		name = bc.getName();
		state = bc.getState();
		street = bc.getStreet();
		street2 = bc.getStreet2();
		zip = bc.getZip();
		contractDate = bc.getContractDate();
		inactiveDate = bc.getInactiveDate();
		statusId = bc.getClientStatusId();
		paymentTerms = bc.getPaymentTerms();
		vendorNumber = bc.getVendorNumber();
		projectCode = bc.getDefaultProjectCode();
		copyPicturesToDisk = bc.getCopyPicturesToDisk() == 'Y';
		pictureDiskPath = bc.getPictureDiskPath();
		if (pictureDiskPath != null  &&  !pictureDiskPath.isEmpty()) {
			String pc = BProperty.get(StandardProperty.WebImagePathConversion);
			if (pc != null && !pc.isEmpty()) {
				String[] parts = pc.split("~");
				if (parts.length == 2)
					pictureDiskPath = pictureDiskPath.replace(parts[0], parts[1]);
			}
		}
		copyOnlyExternal = bc.getCopyOnlyExternal() == 'Y' && bc.getCopyPicturesToDisk() == 'Y';
		copyInactiveProjects = bc.getCopyInactiveProjects() == 'Y' &&  bc.getCopyPicturesToDisk() == 'Y';
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

	public String getDefaultBillingRateFormatted() {
		return defaultBillingRateFormatted;
	}

	public void setDefaultBillingRateFormatted(String defaultBillingRateFormatted) {
		this.defaultBillingRateFormatted = defaultBillingRateFormatted;
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

	public int getContractDate() {
		return contractDate;
	}

	public void setContractDate(int contractDate) {
		this.contractDate = contractDate;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
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

	public boolean isCopyPicturesToDisk() {
		return copyPicturesToDisk;
	}

	public void setCopyPicturesToDisk(boolean copyPicturesToDisk) {
		this.copyPicturesToDisk = copyPicturesToDisk;
	}

	public String getPictureDiskPath() {
		return pictureDiskPath;
	}

	public void setPictureDiskPath(String pictureDiskPath) {
		this.pictureDiskPath = pictureDiskPath;
	}

	public boolean isCopyOnlyExternal() {
		return copyOnlyExternal;
	}

	public void setCopyOnlyExternal(boolean copyOnlyExternal) {
		this.copyOnlyExternal = copyOnlyExternal;
	}

	public boolean isCopyInactiveProjects() {
		return copyInactiveProjects;
	}

	public void setCopyInactiveProjects(boolean copyInactiveProjects) {
		this.copyInactiveProjects = copyInactiveProjects;
	}
}
