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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.project.standardProject;
import com.arahant.business.BStandardProject;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class LoadStandardProjectReturn extends TransmitReturnBase  {

	public LoadStandardProjectReturn() {
		super();
	}

	private boolean accessibleToAll;

	private String reference;
	private String detailDesc;
	private int dateReported;
	private String dateReportedFormatted;
	private double dollarCap;
	private float billingRate;
	private String requesterName;

	private String description;
	private String projectId;
	private String billable;
	private boolean allCompanies;

	/**
	 * @param project
	 */
	void setData(final BStandardProject p) {
		 accessibleToAll=p.getAllEmployees()=='Y';
		 reference=p.getReference();
		 detailDesc=p.getDetailDesc();
		 dollarCap=p.getDollarCap();
		 billingRate=p.getBillingRate();
		 requesterName=p.getRequesterName();
		 description=p.getDescription();
		 projectId=p.getProjectId();
		 billable=p.getBillable()+"";
		 allCompanies=p.getCompany()==null;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}


	/**
	 * @return Returns the accessibleToAll.
	 */
	public boolean getAccessibleToAll() {
		return accessibleToAll;
	}

	/**
	 * @param accessibleToAll The accessibleToAll to set.
	 */
	public void setAccessibleToAll(final boolean accessibleToAll) {
		this.accessibleToAll = accessibleToAll;
	}

	/**
	 * @return Returns the billable.
	 */
	public String getBillable() {
		return billable;
	}

	/**
	 * @param billable The billable to set.
	 */
	public void setBillable(final String billable) {
		this.billable = billable;
	}

	/**
	 * @return Returns the billingRate.
	 */
	public float getBillingRate() {
		return billingRate;
	}

	/**
	 * @param billingRate The billingRate to set.
	 */
	public void setBillingRate(final float billingRate) {
		this.billingRate = billingRate;
	}

	/**
	 * @return Returns the dateReported.
	 */
	public int getDateReported() {
		return dateReported;
	}

	/**
	 * @param dateReported The dateReported to set.
	 */
	public void setDateReported(final int dateReported) {
		this.dateReported = dateReported;
	}

	/**
	 * @return Returns the dateReportedFormatted.
	 */
	public String getDateReportedFormatted() {
		return dateReportedFormatted;
	}

	/**
	 * @param dateReportedFormatted The dateReportedFormatted to set.
	 */
	public void setDateReportedFormatted(final String dateReportedFormatted) {
		this.dateReportedFormatted = dateReportedFormatted;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the detailDesc.
	 */
	public String getDetailDesc() {
		return detailDesc;
	}

	/**
	 * @param detailDesc The detailDesc to set.
	 */
	public void setDetailDesc(final String detailDesc) {
		this.detailDesc = detailDesc;
	}

	/**
	 * @return Returns the dollarCap.
	 */
	public double getDollarCap() {
		return dollarCap;
	}

	/**
	 * @param dollarCap The dollarCap to set.
	 */
	public void setDollarCap(final double dollarCap) {
		this.dollarCap = dollarCap;
	}



	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	

	/**
	 * @return Returns the reference.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference The reference to set.
	 */
	public void setReference(final String reference) {
		this.reference = reference;
	}

	/**
	 * @return Returns the requesterName.
	 */
	public String getRequesterName() {
		return requesterName;
	}

	/**
	 * @param requesterName The requesterName to set.
	 */
	public void setRequesterName(final String requesterName) {
		this.requesterName = requesterName;
	}
	
}

	
