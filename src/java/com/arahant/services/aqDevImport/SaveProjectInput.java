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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.aqDevImport;
import com.arahant.annotation.Validation;
import com.arahant.business.BProject;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProjectInput extends TransmitInputBase{


	@Validation (required=false)
	private boolean accessibleToAll;
	@Validation (required=false)
	private String projectStatusId;
	@Validation (required=false)
	private String projectCategoryId;
	@Validation (required=false)
	private String projectTypeId;
	@Validation (required=false)
	private String reference;
	@Validation (required=false)
	private String detailDesc;
	@Validation (required=false)
	private int dateReported;
	@Validation (required=false)
	private double dollarCap;
	@Validation (required=false)
	private float billingRate;
	@Validation (required=false)
	private String requesterName;
	@Validation (required=false)
	private String requestingCompanyId;
	@Validation (required=false)
	private String productId;
	@Validation (required=false)
	private String employeeId;
	@Validation (required=false)
	private String description;
	@Validation (required=false)
	private String projectId;
	@Validation (required=false)
	private String billable;

	
	/**
	 * @param bp
	 */
	void makeProject(final BProject bp) {
		bp.setAllEmployees(accessibleToAll?'Y':'N');
		bp.setProjectStatusId(projectStatusId);
		bp.setProjectCategoryId(projectCategoryId);
		bp.setEmployeeId(employeeId);
		bp.setProjectTypeId(projectTypeId);
		bp.setReference(reference);
		bp.setDetailDesc(detailDesc);
		bp.setDollarCap(dollarCap);
		bp.setBillingRate(billingRate);
		bp.setRequesterName(requesterName);
		bp.setRequestingOrgGroupId(requestingCompanyId);
		bp.setProjectId(projectId);
		bp.setDescription(description);
		bp.setBillable(billable.charAt(0));
	}
	
	
	public SaveProjectInput() {
		super();
	}


	/**
	 * @return Returns the accessibleToAll.
	 */
	public boolean isAccessibleToAll() {
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
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}


	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(final String productId) {
		this.productId = productId;
	}


	/**
	 * @return Returns the projectCategoryId.
	 */
	public String getProjectCategoryId() {
		return projectCategoryId;
	}


	/**
	 * @param projectCategoryId The projectCategoryId to set.
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
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
	 * @return Returns the projectSponsorId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}


	/**
	 * @param projectSponsorId The projectSponsorId to set.
	 */
	public void setEmployeeId(final String projectSponsorId) {
		this.employeeId = projectSponsorId;
	}


	/**
	 * @return Returns the projectStatusId.
	 */
	public String getProjectStatusId() {
		return projectStatusId;
	}


	/**
	 * @param projectStatusId The projectStatusId to set.
	 */
	public void setProjectStatusId(final String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}


	/**
	 * @return Returns the projectTypeId.
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}


	/**
	 * @param projectTypeId The projectTypeId to set.
	 */
	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
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


	/**
	 * @return Returns the requestingCompanyId.
	 */
	public String getRequestingCompanyId() {
		return requestingCompanyId;
	}


	/**
	 * @param requestingCompanyId The requestingCompanyId to set.
	 */
	public void setRequestingCompanyId(final String requestingCompanyId) {
		this.requestingCompanyId = requestingCompanyId;
	}
	
}

	
