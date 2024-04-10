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

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = ClientCompany.TABLE_NAME)
public class ClientCompany extends CompanyBase implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String TABLE_NAME = "client";
	// Fields
	private GlAccount glAccountByDfltArAcct;
	public static final String GLACCOUNTBYDFLTARACCT = "glAccountByDfltArAcct";
	private GlAccount glAccountByDfltSalesAcct;
	public static final String GLACCOUNTBYDFLTSALESACCT = "glAccountByDfltSalesAcct";
	private int inactiveDate;
	public static final String INACTIVEDATE = "inactiveDate";
	private float billingRate;
	public static final String BILLINGRATE = "billingRate";
	private int contractDate;
	public static final String CONTRACTDATE = "contractDate";
	public static final String COMPANY = "associatedCompany";
	private CompanyDetail associatedCompany;
	public static final String CLIENT_STATUS = "clientStatus";
	private ClientStatus clientStatus;
	private String clientStatusId;
	private String statusComments;
	public static final String LAST_CONTACT_DATE = "lastContactDate";
	private int lastContactDate;
	private short paymentTerms;
	private String vendorNumber;
	private String defaultProjectCode;
	private char copyPicturesToDisk = 'N';
	private String pictureDiskPath;
	private char copyOnlyExternal = 'Y';
	private char copyInactiveProjects = 'Y';

	// Constructors
	/**
	 * default constructor
	 */
	public ClientCompany() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_status_id")
	public ClientStatus getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(ClientStatus clientStatus) {
		this.clientStatus = clientStatus;
	}

	@Column(name = "client_status_id", updatable = false, insertable = false)
	public String getClientStatusId() {
		return clientStatusId;
	}

	public void setClientStatusId(String clientStatusId) {
		this.clientStatusId = clientStatusId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dflt_ar_acct")
	public GlAccount getGlAccountByDfltArAcct() {
		return this.glAccountByDfltArAcct;
	}

	public void setGlAccountByDfltArAcct(final GlAccount glAccountByDfltArAcct) {
		this.glAccountByDfltArAcct = glAccountByDfltArAcct;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dflt_sales_acct")
	public GlAccount getGlAccountByDfltSalesAcct() {
		return this.glAccountByDfltSalesAcct;
	}

	public void setGlAccountByDfltSalesAcct(final GlAccount glAccountByDfltSalesAcct) {
		this.glAccountByDfltSalesAcct = glAccountByDfltSalesAcct;
	}

	@Column(name = "inactive_date")
	public int getInactiveDate() {
		return this.inactiveDate;
	}

	public void setInactiveDate(final int inactiveDate) {
		firePropertyChange("inactiveDate", this.inactiveDate, inactiveDate);
		this.inactiveDate = inactiveDate;
	}

	@Column(name = "billing_rate")
	public float getBillingRate() {
		return this.billingRate;
	}

	public void setBillingRate(final float billingRate) {
		firePropertyChange("billingRate", this.billingRate, billingRate);
		this.billingRate = billingRate;
	}

	@Column(name = "contract_date")
	public int getContractDate() {
		return this.contractDate;
	}

	public void setContractDate(final int contractDate) {
		firePropertyChange("contractDate", this.contractDate, contractDate);
		this.contractDate = contractDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getAssociatedCompany() {
		return associatedCompany;
	}

	public void setAssociatedCompany(CompanyDetail associatedCompany) {
		this.associatedCompany = associatedCompany;
	}

	@Column(name = "status_comments")
	public String getStatusComments() {
		return this.statusComments;
	}

	public void setStatusComments(final String statusComments) {
		firePropertyChange("statusComments", this.statusComments, statusComments);
		this.statusComments = statusComments;
	}

	@Column(name = "last_contact_date")
	public int getLastContactDate() {
		return this.lastContactDate;
	}

	public void setLastContactDate(final int lastContactDate) {
		firePropertyChange("lastContactDate", this.lastContactDate, lastContactDate);
		this.lastContactDate = lastContactDate;
	}

	@Column(name = "payment_terms")
	public short getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(short paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	@Column(name = "vendor_number")
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

    @Column(name = "default_project_code")
    public String getDefaultProjectCode() {
        return defaultProjectCode;
    }

    public void setDefaultProjectCode(String defaultProjectCode) {
        this.defaultProjectCode = defaultProjectCode;
    }

	@Column(name = "copy_pictures_to_disk")
	public char getCopyPicturesToDisk() {
		return copyPicturesToDisk;
	}

	public void setCopyPicturesToDisk(char copyPicturesToDisk) {
		this.copyPicturesToDisk = copyPicturesToDisk;
	}

	@Column(name = "picture_disk_path")
	public String getPictureDiskPath() {
		return pictureDiskPath;
	}

	public void setPictureDiskPath(String pictureDiskPath) {
		this.pictureDiskPath = pictureDiskPath;
	}

	@Column(name = "copy_only_external")
	public char getCopyOnlyExternal() {
		return copyOnlyExternal;
	}

	public void setCopyOnlyExternal(char copyOnlyExternal) {
		this.copyOnlyExternal = copyOnlyExternal;
	}

	@Column(name = "copy_inactive_projects")
	public char getCopyInactiveProjects() {
		return copyInactiveProjects;
	}

	public void setCopyInactiveProjects(char copyInactiveProjects) {
		this.copyInactiveProjects = copyInactiveProjects;
	}

	@Override
	public int hashCode() {
		if (getOrgGroupId() == null)
			return 0;
		return getOrgGroupId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ClientCompany other = (ClientCompany) obj;
		return getOrgGroupId().equals(other.getOrgGroupId());
	}

	@Override
	public String notifyId() {
		return getOrgGroupId();
	}

	@Override
	public String notifyClassName() {
		return "ClientCompany";
	}
}
