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
 * Created on Feb 21, 2007
 * 
 */
package com.arahant.services.standard.project.standardProject;
import com.arahant.annotation.Validation;
import com.arahant.business.BStandardProject;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 21, 2007
 *
 */
public class NewStandardProjectInput extends TransmitInputBase {

	public NewStandardProjectInput() {
		super();
	}
	
	@Validation (table="standard_project",column="billable",required=true)
	private String billable;
	@Validation (required=false)
	private boolean accessibleToAll;
	@Validation (required=true)
	private String projectCategoryId;
	@Validation (required=false)
	private String employeeId;
	@Validation (required=true)
	private String projectTypeId;
	@Validation (table="standard_project",column="reference",required=false)
	private String reference;
	@Validation (table="standard_project",column="detail_desc",required=false)
	private String detailDesc;
	@Validation (min=.01,required=false)
	private double dollarCap;
	@Validation (min=.01,required=false)
	private float billingRate;
	@Validation (required=false)
	private String serviceId;
	@Validation (table="standard_project",column="description",required=true)
	private String description;
	@Validation (table="standard_project",column="requester_name",required=false)
	private String requesterName;
	@Validation (required=false)
	private boolean allCompanies;
	
	/**
	 * @param sp
	 */
	void makeStandardProject(final BStandardProject p) {
		p.setAllEmployees(isAccessibleToAll()?'Y':'N');
		
		p.setProjectTypeId(getProjectTypeId());
		
		p.setProjectCategoryId(getProjectCategoryId());
		
		p.setBillable(getBillable().charAt(0));
		
		p.setEmployeeId(getEmployeeId());

		p.setProductServiceId(getServiceId());
		
		p.setBillingRate(getBillingRate());
		p.setDollarCap(getDollarCap());
		p.setReference(getReference());
		p.setDetailDesc(getDetailDesc());
		p.setReference(getReference());
		p.setRequesterName(getRequesterName());
		p.setDescription(getDescription());
		if(allCompanies)
			p.setCompany(null);
		else
			p.setCompany(ArahantSession.getHSU().getCurrentCompany());
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
	 * @return Returns the employeeId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return Returns the productId.
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param productId The productId to set.
	 */
	public void setServiceId(final String serviceId) {
		this.serviceId = serviceId;
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
}

	
