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
 * Created on Nov 2, 2006
 * 
 */
package com.arahant.services.standard.project.projectTimesheetReport;

import com.arahant.beans.Timesheet;
import com.arahant.business.BTimesheet;



/**
 * 
 *
 * Created on Nov 2, 2006
 *
 */
public class GetProjectReportDataReturnItem {
	private String projectName;
	private double billableHours;
	private double nonBillableHours;
	private double billableAmount;
	private double nonBillableAmount;
	private double unknownAmount;
	private double unknownHours;
	private double estimatedHours;
	private String summary;
	private double invoicedHours;
	private double remainingEstimatedHours;
	private String projectId;
	
	private double estimateAmount;
	private double invoicedAmount;
	private double remainingEstimatedAmount;

	float billingRate;

	

	public double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public double getInvoicedAmount() {
		return invoicedAmount;
	}

	public void setInvoicedAmount(double invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
	}

	public double getRemainingEstimatedAmount() {
		return remainingEstimatedAmount;
	}

	public void setRemainingEstimatedAmount(double remainingEstimatedAmount) {
		if (remainingEstimatedAmount<0)
			remainingEstimatedAmount=0;
		this.remainingEstimatedAmount = remainingEstimatedAmount;
	}

	
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	

	public double getInvoicedHours() {
		return invoicedHours;
	}

	public void setInvoicedHours(double invoicedHours) {
		this.invoicedHours = invoicedHours;
	}

	public double getRemainingEstimatedHours() {
		return remainingEstimatedHours;
	}

	public void setRemainingEstimatedHours(double remainingEstimatedHours) {
		if (remainingEstimatedHours<0)
			remainingEstimatedHours=0;
		this.remainingEstimatedHours = remainingEstimatedHours;
	}
			
	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getBillableHours()
	 */
	public double getBillableHours() {
		return billableHours;
	}

	public double getEstimatedHours() {
		return estimatedHours;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setBillableHours(double)
	 */
	public void setBillableHours(final double billableHours) {
		billableAmount=billableHours*billingRate;
		this.billableHours = billableHours;
		
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getNonBillableHours()
	 */
	public double getNonBillableHours() {
		return nonBillableHours;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setNonBillableHours(double)
	 */
	public void setNonBillableHours(final double nonBillableHours) {
		nonBillableAmount=nonBillableHours*billingRate;
		this.nonBillableHours = nonBillableHours;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getProjectName()
	 */
	public String getProjectName() {
		return projectName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setProjectName(java.lang.String)
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getBillableAmount()
	 */
	public double getBillableAmount() {
		return billableAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setBillableAmount(double)
	 */
	public void setBillableAmount(final double billableAmount) {
		this.billableAmount = billableAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getNonBillableAmount()
	 */
	public double getNonBillableAmount() {
		return nonBillableAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setNonBillableAmount(double)
	 */
	public void setNonBillableAmount(final double nonBillableAmount) {
		this.nonBillableAmount = nonBillableAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getUnknownAmount()
	 */
	public double getUnknownAmount() {
		return unknownAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setUnknownAmount(double)
	 */
	public void setUnknownAmount(final double unknownAmount) {
		this.unknownAmount = unknownAmount;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getSummary()
	 */
	public String getSummary() {
		return summary;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setSummary(java.lang.String)
	 */
	public void setSummary(final String summary) {
		this.summary = summary;
	}
	
	void setHours(final BTimesheet ts) {
		
		setHours(ts.getTotalHours(), ts.getBillable(), ts.getBillingRate());
	}
	
	/**
	 * @param ts
	 */
	void setHours(final Timesheet ts) {
		
		setHours(new BTimesheet(ts));
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#getUnknownHours()
	 */
	public double getUnknownHours() {
		return unknownHours;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReportDetail#setUnknownHours(double)
	 */
	public void setUnknownHours(final double unknownHours) {
		this.unknownHours = unknownHours;
	}
	/**
	 * @param totalHours
	 * @param billable
	 */
	public void setHours(final double totalHours, final char billable, final double billingRate) {
		
		if (billable=='Y')
		{
			setBillableHours(getBillableHours()+totalHours);
			setBillableAmount(getBillableAmount()+totalHours*billingRate);
		}
		if (billable=='N')
		{
			setNonBillableHours(getNonBillableHours()+totalHours);
			setNonBillableAmount(getNonBillableAmount()+totalHours*billingRate);
		}
		if (billable=='U')
		{
			setUnknownHours(getUnknownHours()+totalHours);
			setUnknownAmount(getUnknownHours()+totalHours*billingRate);
		}
		
	}
	 
}

	
