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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.project.stdProjectInstantiation;
import com.arahant.business.BStandardProject;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class StandardProjects  {

	public StandardProjects() {
		super();
	}

	private boolean accessibleToAll;
	private String projectCategoryId;
	private String projectCategoryCode;
	private String employeeId;
	private String employeeName;
	private String projectTypeId;
	private String projectTypeCode;
	private String reference;
	private String detailDesc;
	private int dateReported;
	private String dateReportedFormatted;
	private double dollarCap;
	private float billingRate;
	private String requesterName;
	private String serviceId;
	private String serviceName;
	private String description;
	private String projectId;
	private String billable;


	public StandardProjects(final BStandardProject p) {
		super();
		 accessibleToAll=p.getAllEmployees()=='Y';
		 projectCategoryId=p.getProjectCategoryId();
		 projectCategoryCode=p.getProjectCategoryCode();
		 employeeId=p.getEmployeeId();
		 employeeName=p.getEmployeeName();
		 projectTypeId=p.getProjectTypeId();
		 projectTypeCode=p.getProjectCode();
		 reference=p.getReference();
		 detailDesc=p.getDetailDesc();
		 dollarCap=p.getDollarCap();
		 billingRate=p.getBillingRate();
		 requesterName=p.getRequesterName();
		 serviceId=p.getProductId();
		 serviceName=p.getProductName();
		 description=p.getDescription();
		 projectId=p.getProjectId();
		 billable=p.getBillable()+"";
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
	 * @return Returns the employeeName.
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName The employeeName to set.
	 */
	public void setEmployeeName(final String employeeName) {
		this.employeeName = employeeName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	

	/**
	 * @return Returns the projectCategoryCode.
	 */
	public String getProjectCategoryCode() {
		return projectCategoryCode;
	}

	/**
	 * @param projectCategoryCode The projectCategoryCode to set.
	 */
	public void setProjectCategoryCode(final String projectCategoryCode) {
		this.projectCategoryCode = projectCategoryCode;
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
	 * @return Returns the projectTypeCode.
	 */
	public String getProjectTypeCode() {
		return projectTypeCode;
	}

	/**
	 * @param projectTypeCode The projectTypeCode to set.
	 */
	public void setProjectTypeCode(final String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
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

	
