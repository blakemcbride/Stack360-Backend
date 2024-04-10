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
package com.arahant.services.standard.project.projectBilling;

import com.arahant.annotation.Validation;
import com.arahant.beans.ProjectShift;
import com.arahant.beans.Timesheet;
import com.arahant.business.BProject;
import com.arahant.business.BRateType;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class SaveBillingInput extends TransmitInputBase {

	@Validation(table = "project", column = "billable", required = true)
	private String billable;
	@Validation(required = true)
	private String projectId;
	@Validation(min = .01, max = 10000000, table = "project", column = "dollar_cap", required = false)
	private double dollarCap;
	@Validation(min = .01, max = 800, table = "project", column = "billing_rate", required = false)
	private float billingRate;
	@Validation(required = false)
	private String serviceId;
	@Validation(required = false)
	private String primaryParentId;
	@Validation(table = "project", column = "approving_person_txt", required = false)
	private String approvedBy;
	@Validation(type = "date", required = false)
	private int promisedDate;
	@Validation(type = "date", required = false)
	private int estimateOnDate;
	@Validation(min = 1, max = 4, required = false)
	private int estimateMeasurement;
	@Validation(required = false)
	private double estimate;
	@Validation(required = false)
	private double estimateTimeSpan;
	@Validation(min = 1, max = 4, required = false)
	private int estimateTimeSpanMeasurement;
	@Validation(required = false)
	private boolean changeTimesheets;
	@Validation(required = false)
	private String purchaseOrder;
	@Validation(required = true)
	private String rateTypeId;

	/**
	 * @param bp
	 * @throws ArahantDeleteException
	 */
	void makeProject(final BProject bp) throws ArahantDeleteException {

		bp.setDollarCap(dollarCap);
		bp.setBillingRate(billingRate);

		bp.setProjectId(projectId);

		bp.setBillable(billable.charAt(0));
		bp.setPrimaryParentId(primaryParentId);
		bp.setProductServiceId(serviceId);

		if (billable.charAt(0) == 'I' && (primaryParentId == null || primaryParentId.equals("")))
			throw new ArahantWarning("Can't be internal billing without parent.");

		bp.setApprovedBy(approvedBy);
		bp.setEstimateHours(DateUtils.getHours(estimate, estimateMeasurement));
		bp.setPromisedDate(promisedDate);
		bp.setEstimateOnDate(estimateOnDate);
		bp.setEstimateTimeSpan(DateUtils.getHours(estimateTimeSpan, estimateTimeSpanMeasurement));
		//	bp.setApprovalDate(DateUtils.getDate(approvalDate,approvalTime));
		//	bp.setApprovalDate(new Date());
		bp.setPurchaseOrder(purchaseOrder);
		bp.setRateType(new BRateType(rateTypeId));

		if (changeTimesheets) {
			List<Timesheet> timesheets = ArahantSession.getHSU().createCriteria(Timesheet.class)
					.ne(Timesheet.STATE, 'I')
					.joinTo(Timesheet.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, bp.getBean())
					.list();

			for (Timesheet ts : timesheets) {
				BTimesheet bt = new BTimesheet(ts);
				bt.setBillable(billable.charAt(0));
				bt.update();
			}
		}
	}

	public SaveBillingInput() {
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getPrimaryParentId() {
		return primaryParentId;
	}

	public void setPrimaryParentId(String primaryParentId) {
		this.primaryParentId = primaryParentId;
	}
	/*
	 public int getApprovalDate() {
	 return approvalDate;
	 }
	 */

	/*
	 public void setApprovalDate(int approvalDate) {
	 this.approvalDate = approvalDate;
	 }
	 */

	/*
	 public int getApprovalTime() {
	 return approvalTime;
	 }
	 */

	/*
	 public void setApprovalTime(int approvalTime) {
	 this.approvalTime = approvalTime;
	 }
	 */
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public double getEstimate() {
		return estimate;
	}

	public void setEstimate(double estimate) {
		this.estimate = estimate;
	}

	public int getEstimateMeasurement() {
		return estimateMeasurement;
	}

	public void setEstimateMeasurement(int estimateMeasurement) {
		this.estimateMeasurement = estimateMeasurement;
	}

	public int getEstimateOnDate() {
		return estimateOnDate;
	}

	public void setEstimateOnDate(int estimateOnDate) {
		this.estimateOnDate = estimateOnDate;
	}

	public double getEstimateTimeSpan() {
		return estimateTimeSpan;
	}

	public void setEstimateTimeSpan(double estimateTimeSpan) {
		this.estimateTimeSpan = estimateTimeSpan;
	}

	public int getEstimateTimeSpanMeasurement() {
		return estimateTimeSpanMeasurement;
	}

	public void setEstimateTimeSpanMeasurement(int estimateTimeSpanMeasurement) {
		this.estimateTimeSpanMeasurement = estimateTimeSpanMeasurement;
	}

	public int getPromisedDate() {
		return promisedDate;
	}

	public void setPromisedDate(int promisedDate) {
		this.promisedDate = promisedDate;
	}

	public boolean getChangeTimesheets() {
		return changeTimesheets;
	}

	public void setChangeTimesheets(boolean changeTimesheets) {
		this.changeTimesheets = changeTimesheets;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }
}
