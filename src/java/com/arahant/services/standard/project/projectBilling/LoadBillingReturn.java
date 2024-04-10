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

import com.arahant.beans.ProjectShift;
import com.arahant.beans.Timesheet;
import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;

public class LoadBillingReturn extends TransmitReturnBase {

	private double dollarCap;
	private float billingRate;
	private String billable;
	private String primaryParentId = "";
	private double actualBillable;
	private double actualNonBillable;
	private double estimate;
	private double estimateTimeSpan;
	private int estimateTimeSpanMeasurement;
	private int estimateMeasurement;
	private int promisedDate;
	private int estimateOnDate;
	private String approvedBy;
	private String approvalEnteredByFormatted;
	private int approvalDate;
	private int approvalTime;
	private boolean hasTimesheets;
	private String purchaseOrder;
	private String rateTypeId;

	public LoadBillingReturn() {
	}

	public String getPrimaryParentId() {
		return primaryParentId;
	}

	public void setPrimaryParentId(String primaryParentId) {
		this.primaryParentId = primaryParentId;
	}

	/**
	 * @param bp
	 */
	void setData(final BProject bp) {
		dollarCap = bp.getDollarCap();
		billingRate = bp.getBillingRate();
		billable = bp.getBillable() + "";
		defaultBillingRateFormatted = bp.getDefaultBillingRateFormatted();
		if (bp.getBillable() == 'I')
			primaryParentId = bp.getPrimaryParentId();
		approvedBy = bp.getApprovedBy();
		approvalEnteredByFormatted = bp.getApprovalEnteredBy();
		approvalDate = bp.getApprovalDate();
		approvalTime = bp.getApprovalTime();
		estimate = DateUtils.getMeasuredValue(bp.getEstimateHours());
		estimateMeasurement = DateUtils.getMeasurement(bp.getEstimateHours());
		estimateOnDate = bp.getEstimateOnDate();
		estimateTimeSpan = DateUtils.getMeasuredValue(bp.getEstimateTimeSpan());
		estimateTimeSpanMeasurement = DateUtils.getMeasurement(bp.getEstimateTimeSpan());
		promisedDate = bp.getPromisedDate();
		actualBillable = DateUtils.getMeasuredValue(bp.getBillableHours(), DateUtils.getMeasurement(bp.getEstimateHours()));
		actualNonBillable = DateUtils.getMeasuredValue(bp.getNonBillableHours(), DateUtils.getMeasurement(bp.getEstimateHours()));
		hasTimesheets = ArahantSession.getHSU().createCriteria(Timesheet.class)
				.ne(Timesheet.STATE, 'I')
				.joinTo(Timesheet.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, bp.getBean())
				.exists();
		purchaseOrder = bp.getPurchaseOrder();
		rateTypeId = bp.getRateType().getRateTypeId();
	}

	public String getDefaultBillingRateFormatted() {
		return defaultBillingRateFormatted;
	}

	public void setDefaultBillingRateFormatted(String defaultBillingRateFormatted) {
		this.defaultBillingRateFormatted = defaultBillingRateFormatted;
	}
	private String defaultBillingRateFormatted;

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

	public int getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(int approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalEnteredByFormatted() {
		return approvalEnteredByFormatted;
	}

	public void setApprovalEnteredByFormatted(String approvalEnteredByFormatted) {
		this.approvalEnteredByFormatted = approvalEnteredByFormatted;
	}

	public int getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(int approvalTime) {
		this.approvalTime = approvalTime;
	}

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

	public double getActualBillable() {
		return actualBillable;
	}

	public void setActualBillable(double actualBillable) {
		this.actualBillable = actualBillable;
	}

	public double getActualNonBillable() {
		return actualNonBillable;
	}

	public void setActualNonBillable(double actualNonBillable) {
		this.actualNonBillable = actualNonBillable;
	}

	public boolean getHasTimesheets() {
		return hasTimesheets;
	}

	public void setHasTimesheets(boolean hasTimesheets) {
		this.hasTimesheets = hasTimesheets;
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
