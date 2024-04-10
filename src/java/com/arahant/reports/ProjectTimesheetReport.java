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
 * Created on Jan 18, 2007
 * 
 */
package com.arahant.reports;

import com.arahant.beans.*;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BRight;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * 
 *
 * Created on Jan 18, 2007
 *
 */
public class ProjectTimesheetReport extends ReportBase {

	public ProjectTimesheetReport() {
		super("prtimrpt", "Project Billing Report", true);
	}

	private class ProjectReportDetail implements Comparable<ProjectReportDetail> {

		private String projectName;
		private String summary;
		private double estimatedHours;
		private double invoicedHours;
		private double hours;
		private double billableHours;
		private double billableAmount;
		private double nonBillableHours;
		private double nonBillableAmount;
		private double unknownHours;
		private double unknownAmount;
		private double totalUnknownHours;
		private double remainingEstimatedHours;
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
			this.remainingEstimatedAmount = remainingEstimatedAmount;
		}

		public double getRemainingEstimatedHours() {
			return remainingEstimatedHours;
		}

		public void setRemainingEstimatedHours(double remainingEstimatedHours) {
			this.remainingEstimatedHours = remainingEstimatedHours;
		}

		public double getTotalUnknownHours() {
			return totalUnknownHours;
		}

		public void setTotalUnknownHours(double totalUnknownHours) {
			this.totalUnknownHours = totalUnknownHours;
		}

		public double getNonBillableAmount() {
			return nonBillableAmount;
		}

		public void setNonBillableAmount(double nonBillableAmount) {
			this.nonBillableAmount = nonBillableAmount;
		}

		public double getNonBillableHours() {
			return nonBillableHours;
		}

		public void setNonBillableHours(double nonBillableHours) {
			nonBillableAmount=nonBillableHours*billingRate;
			this.nonBillableHours = nonBillableHours;
		}

		public double getUnknownAmount() {
			return unknownAmount;
		}

		public void setUnknownAmount(double unknownAmount) {
			this.unknownAmount = unknownAmount;
		}

		public double getUnknownHours() {
			return unknownHours;
		}

		public void setUnknownHours(double unknownHours) {
			this.unknownHours = unknownHours;
		}

		public double getBillableAmount() {
			return billableAmount;
		}

		public void setBillableAmount(double billableAmount) {
			this.billableAmount = billableAmount;
		}

		public double getBillableHours() {
			return billableHours;
		}

		public void setBillableHours(double billableHours) {
			billableAmount=billableHours*billingRate;
			this.billableHours = billableHours;
		}

		public double getEstimatedHours() {
			return estimatedHours;
		}

		public void setEstimatedHours(double estimatedHours) {
			this.estimatedHours = estimatedHours;
		}

		public double getHours() {
			return hours;
		}

		public void setHours(double hours) {
			this.hours = hours;
		}

		public double getInvoicedHours() {
			return invoicedHours;
		}

		public void setInvoicedHours(double invoicedHours) {
			this.invoicedHours = invoicedHours;
		}

		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		void setHours(final BTimesheet ts) {
			if (ts.getBillable() == 'Y') {
				setBillableHours(getBillableHours() + ts.getTotalHours());
				setBillableAmount(getBillableAmount() + ts.getTotalHours() * ts.getBillingRate());
			}
			if (ts.getBillable() == 'N') {
				setNonBillableHours(getNonBillableHours() + ts.getTotalHours());
				setNonBillableAmount(getNonBillableAmount() + ts.getTotalHours() * ts.getBillingRate());
			}
			if (ts.getBillable() == 'U') {
				setTotalUnknownHours(getTotalUnknownHours() + ts.getTotalHours());
				setUnknownHours(getUnknownHours() + ts.getTotalHours());
				setUnknownAmount(getTotalUnknownHours() + ts.getTotalHours() * ts.getBillingRate());
			}
		}

		public void setHours(final double totalHours, final char billable, final double billingRate) {
			if (billable == 'Y') {
				setBillableHours(getBillableHours() + totalHours);
				setBillableAmount(getBillableAmount() + totalHours * billingRate);
			}
			if (billable == 'N') {
				setNonBillableHours(getNonBillableHours() + totalHours);
				setNonBillableAmount(getNonBillableAmount() + totalHours * billingRate);
			}
			if (billable == 'U') {
				setTotalUnknownHours(getTotalUnknownHours() + totalHours);
				setUnknownHours(getUnknownHours() + totalHours);
				setUnknownAmount(getTotalUnknownHours() + totalHours * billingRate);
			}
		}

		/**
		 * @param ts
		 */
		void setHours(final Timesheet ts) {
			if (ts.getBillable() == 'Y') {
				setBillableHours(getBillableHours() + ts.getTotalHours());
				setBillableAmount(getBillableAmount() + ts.getTotalHours() * ts.getProjectShift().getProject().getBillingRate());
			}
			if (ts.getBillable() == 'N') {
				setNonBillableHours(getNonBillableHours() + ts.getTotalHours());
				setNonBillableAmount(getNonBillableAmount() + ts.getTotalHours() * ts.getProjectShift().getProject().getBillingRate());
			}
			if (ts.getBillable() == 'U') {
				setTotalUnknownHours(getTotalUnknownHours() + ts.getTotalHours());
				setUnknownHours(getUnknownHours() + ts.getTotalHours());
				setUnknownAmount(getTotalUnknownHours() + ts.getTotalHours() * ts.getProjectShift().getProject().getBillingRate());
			}
		}

		public int compareTo(ProjectReportDetail o) {
			return projectName.compareTo(o.projectName);
		}
	}

	private class ProjectReportData {

		private double billableHours;
		private double billableAmount;
		private double nonBillableHours;
		private double nonBillableAmount;
		private double unknownHours;
		private double unknownAmount;
		private double totalBusinessHours;
		private ProjectReportDetail[] projectDetail;

		public ProjectReportDetail[] getProjectDetail() {
			return projectDetail;
		}

		public void setProjectDetail(ProjectReportDetail[] projectDetail) {
			this.projectDetail = projectDetail;
		}

		public double getTotalBusinessHours() {
			return totalBusinessHours;
		}

		public void setTotalBusinessHours(double totalBusinessHours) {
			this.totalBusinessHours = totalBusinessHours;
		}

		public double getBillableAmount() {
			return billableAmount;
		}

		public void setBillableAmount(double billableAmount) {
			this.billableAmount = billableAmount;
		}

		public double getBillableHours() {
			return billableHours;
		}

		public void setBillableHours(double billableHours) {
			this.billableHours = billableHours;
		}

		public double getNonBillableAmount() {
			return nonBillableAmount;
		}

		public void setNonBillableAmount(double nonBillableAmount) {
			this.nonBillableAmount = nonBillableAmount;
		}

		public double getNonBillableHours() {
			return nonBillableHours;
		}

		public void setNonBillableHours(double nonBillableHours) {
			this.nonBillableHours = nonBillableHours;
		}

		public double getUnknownAmount() {
			return unknownAmount;
		}

		public void setUnknownAmount(double unknownAmount) {
			this.unknownAmount = unknownAmount;
		}

		public double getUnknownHours() {
			return unknownHours;
		}

		public void setUnknownHours(double unknownHours) {
			this.unknownHours = unknownHours;
		}
	}

	private double dollars=0;
	private double dollarsWithinEstimate=0;
	
	public String build(String clientCompanyId, String employeeId, String orgGroupId, String projectId, int startDate, int endDate,
			boolean isApproved, boolean isNotApproved, boolean isInvoiced, boolean showInDollars) {

		try {

			HibernateScrollUtil sumAndProjects;
			if ("SKIP".equals(employeeId))		
				sumAndProjects=BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(),
					clientCompanyId, projectId, 
					startDate, endDate, isApproved, isNotApproved, isInvoiced, -1);
			else
				sumAndProjects=BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(),
					clientCompanyId, projectId, orgGroupId, employeeId,
					startDate, endDate, isApproved, isNotApproved, isInvoiced, -1);

			final ProjectReportData data = new ProjectReportData();

			final HashMap<String, ProjectReportDetail> projectsMap = new HashMap<String, ProjectReportDetail>();


			while (sumAndProjects.next()) {
				final Object[] eles = sumAndProjects.getObjects();
				final Double hours = (Double) eles[0];
				final Project p = (Project) eles[1];
				final Character c = (Character) eles[2];

				ProjectReportDetail prd = projectsMap.get(p.getProjectId());
				BProject bp=new BProject(p);
				if (prd == null) {
					prd = new ProjectReportDetail();
					projectsMap.put(p.getProjectId(), prd);
					prd.setProjectName(p.getProjectName().trim());
					prd.setSummary(p.getDescription());
					prd.setEstimatedHours(p.getEstimateHours());
					prd.setInvoicedHours(bp.getInvoicedHours());
					prd.billingRate=bp.getCalculatedBillingRate(null);
				}

				prd.setHours(hours.doubleValue(), c.charValue(), p.getBillingRate());

			}
			sumAndProjects.close();
			final LinkedList<ProjectReportDetail> projectsList = new LinkedList<ProjectReportDetail>();
			projectsList.addAll(projectsMap.values());
			
			Collections.sort(projectsList);

			final ProjectReportDetail prda[] = new ProjectReportDetail[projectsList.size()];

			final Iterator<ProjectReportDetail> prdItr = projectsList.iterator();

			for (int loop = 0; loop < prda.length; loop++) {
				prda[loop] = prdItr.next();
				prda[loop].setBillableHours(roundToHundredths(prda[loop].getBillableHours()));
				prda[loop].setNonBillableHours(roundToHundredths(prda[loop].getNonBillableHours()));
				prda[loop].setTotalUnknownHours(roundToHundredths(prda[loop].getTotalUnknownHours()));
				if (prda[loop].getEstimatedHours()>0)
					prda[loop].setRemainingEstimatedHours(prda[loop].getEstimatedHours() - prda[loop].getInvoicedHours());


				data.setBillableAmount(data.getBillableAmount() + prda[loop].billableAmount);
				data.setBillableHours(data.getBillableHours() + prda[loop].billableHours);
				data.setNonBillableAmount(data.getNonBillableAmount() + prda[loop].getNonBillableAmount());
				data.setNonBillableHours(data.getNonBillableHours() + prda[loop].getNonBillableHours());
				data.setUnknownAmount(data.getUnknownAmount() + prda[loop].getUnknownAmount());
				data.setUnknownHours(data.getUnknownHours() + prda[loop].getUnknownHours());
				
				dollars+=prda[loop].getBillableHours()*prda[loop].billingRate;
			
			
				if (prda[loop].getRemainingEstimatedHours()<prda[loop].getBillableHours())
					dollarsWithinEstimate+=prda[loop].getRemainingEstimatedHours()*prda[loop].billingRate;
				else
					dollarsWithinEstimate+=prda[loop].getBillableHours()*prda[loop].billingRate;
				}


			data.setTotalBusinessHours(getBusinessHoursBetween(startDate, endDate, hsu));

			//make the hours neater

			data.setBillableHours(roundToHundredths(data.getBillableHours()));
			data.setNonBillableHours(roundToHundredths(data.getNonBillableHours()));
			data.setUnknownHours(roundToHundredths(data.getUnknownHours()));

			data.setBillableAmount(roundToHundredths(data.getBillableAmount()));
			data.setNonBillableAmount(roundToHundredths(data.getNonBillableAmount()));
			data.setUnknownAmount(roundToHundredths(data.getUnknownAmount()));

			data.setProjectDetail(prda);
			// write out the parts of our report
			
			//write header
			if (!isEmpty(orgGroupId))
				writeHeaderLine("Organizational Group", this.getOrganizationalGroupDisplay(orgGroupId));
			if (!isEmpty(employeeId))
				writeHeaderLine("Employee", getEmployeeDisplay(employeeId));
			if (startDate != 0 || endDate != 0)
				writeHeaderLine("Date Range", (startDate==0?"(no limit)":DateUtils.getDateFormatted(startDate)) + " - " + (endDate==0?"(no limit)":DateUtils.getDateFormatted(endDate)));
			if (!isEmpty(clientCompanyId))
				writeHeaderLine("Requesting Company", getCompanyDisplay(clientCompanyId));
			if (!isEmpty(projectId))
				writeHeaderLine("Project ID", getProjectDisplay(projectId));
			if (isNotApproved || isApproved || isInvoiced)
				writeHeaderLine("Timesheet Entry Status", getTimesheetEntryStatusDisplay(isNotApproved,isApproved,isInvoiced));
			addHeaderLine();
		
			this.writeProjectDetails(data.getProjectDetail());
			this.writeFooter(data);
		} catch (DocumentException ex) {
			throw new ArahantException(ex);
		} finally {
			close();
		}
		return getFilename();
	}

	protected void writeHeader(final ProjectReportData projectReportDataInput) throws DocumentException {

		
	}

	private String getOrganizationalGroupDisplay(String orgGroupId) {
		String display = "(all)";

		if (!this.isEmpty(orgGroupId)) {
			final OrgGroup orgGroup = this.hsu.get(OrgGroup.class, orgGroupId);

			if (orgGroup != null) {
				display = orgGroup.getName();
			}
		}

		return display;
	}

	private String getEmployeeDisplay(String employeeId) {
		String display = "(all)";

		if (!this.isEmpty(employeeId)) {
			final Person employee = this.hsu.get(Person.class, employeeId);

			if (employee != null) {
				display = employee.getNameLFM();
			}
		}

		return display;
	}



	private String getCompanyDisplay(final String clientCompanyId) {
		String display = "(all)";

		if (!this.isEmpty(clientCompanyId)) {
			final CompanyBase company = this.hsu.get(CompanyBase.class, clientCompanyId);

			if (company != null) {
				display = company.getName();
			}
		}

		return display;
	}

	private String getProjectDisplay(final String projectId) {
		String display = "(all)";

		if (!this.isEmpty(projectId)) {
			final Project project = this.hsu.get(Project.class, projectId);

			if (project != null) {
				display = project.getProjectName();
			}
		}

		return display;
	}

	private String getTimesheetEntryStatusDisplay(boolean isNotApproved, boolean isApproved, boolean isInvoiced) {
		String display = "";

		if (isNotApproved) {
			display = "Not Yet Approved";
		}
		if (isApproved) {
			if (display.length() != 0) {
				display += ", ";
			}
			display += "Approved";
		}
		if (isInvoiced) {
			if (display.length() != 0) {
				display += ", ";
			}
			display += "Invoiced";
		}

		return display;
	}

	protected void writeProjectDetails(final ProjectReportDetail[] projectReportDetails) throws DocumentException {
		PdfPTable table = makeTable(new int[]{6, 32, 10, 10, 10, 10, 12, 10});

		ProjectReportDetail projectReportDetail;
		boolean alternateRow = true;


		// line items
		writeColHeader(table, "ID", Element.ALIGN_RIGHT);
		writeColHeader(table, "Project Summary", Element.ALIGN_LEFT);
		writeColHeader(table, "Estimated", Element.ALIGN_RIGHT);
		writeColHeader(table, "Invoiced", Element.ALIGN_RIGHT);
		writeColHeader(table, "Remaining", Element.ALIGN_RIGHT);
		writeColHeader(table, "Billable", Element.ALIGN_RIGHT);
		writeColHeader(table, "Non-Billable", Element.ALIGN_RIGHT);
		writeColHeader(table, "Unknown", Element.ALIGN_RIGHT);

		table.setHeaderRows(1);

		for (final ProjectReportDetail element : projectReportDetails) {
			projectReportDetail = element;

			// toggle the alternate row
			alternateRow = !alternateRow;

			writeRight(table, projectReportDetail.getProjectName(), alternateRow, 1, Element.ALIGN_TOP);
			writeLeft(table, projectReportDetail.getSummary(), alternateRow, 1, Element.ALIGN_TOP);
			writeRight(table, projectReportDetail.getEstimatedHours() + "", alternateRow, 1, Element.ALIGN_TOP);
			writeRight(table, projectReportDetail.getInvoicedHours() + "", alternateRow, 1, Element.ALIGN_TOP);
			writeRight(table, projectReportDetail.getRemainingEstimatedHours() + "", alternateRow, 1, Element.ALIGN_TOP);
			if (BRight.checkRight("AccessBillableLevel")>1)
			{
				writeRight(table, MoneyUtils.formatMoney(projectReportDetail.getBillableAmount()), alternateRow, 1, Element.ALIGN_TOP);
				writeRight(table, MoneyUtils.formatMoney(projectReportDetail.getNonBillableAmount()), alternateRow, 1, Element.ALIGN_TOP);
			}
			else
			{
				writeRight(table, projectReportDetail.getBillableHours() + "", alternateRow, 1, Element.ALIGN_TOP);
				writeRight(table, projectReportDetail.getNonBillableHours() + "", alternateRow, 1, Element.ALIGN_TOP);
			}
			writeRight(table, projectReportDetail.getTotalUnknownHours() + "", alternateRow, 1, Element.ALIGN_TOP);
		}

		addTable(table);
	}

	protected void writeFooter(final ProjectReportData projectReportTransmit) throws DocumentException {
		PdfPTable table = makeTable(new int[]{20, 10, 70});


		// footer detail

		writeColHeader(table, "Summary", Element.ALIGN_LEFT);
		writeColHeader(table, " ", Element.ALIGN_LEFT);
		writeColHeader(table, " ", Element.ALIGN_LEFT);

		write(table, "Billable Hours:");
		writeRight(table, projectReportTransmit.getBillableHours() + "", false);
		write(table, " ");

		write(table, "Non-Billable Hours:");
		writeRight(table, projectReportTransmit.getNonBillableHours() + "", false);
		write(table, " ");

		write(table, "Unknown Hours:");
		writeRight(table, projectReportTransmit.getUnknownHours() + "", false);
		write(table, " ");

		write(table, "Business Hours in Date Range:");
		writeRight(table, projectReportTransmit.getTotalBusinessHours() + "", false);
		write(table, " ");

		if (BRight.checkRight("AccessBillableLevel")>1)
		{
			write(table, "Total Dollars:");
			writeRight(table, MoneyUtils.formatMoney(dollars), false);
			write(table, " ");

			write(table, "Dollars in Estimate:");
			writeRight(table, MoneyUtils.formatMoney(dollarsWithinEstimate), false);
			write(table, " ");
		}
		addTable(table);
	}

	public static int getBusinessHoursBetween(final int start_date, final int end_date, final HibernateSessionUtil hsu) {
		//now from the start date to the end date, how many business hours were there?

		int hours = 0;

		if (start_date != 0) {
			final Calendar loopDate = DateUtils.getCalendar(start_date);
			Calendar endDate;

			if (end_date != 0) {
				endDate = DateUtils.getCalendar(end_date);
				endDate.add(Calendar.DAY_OF_YEAR, 1);//to keep from stopping too soon
			} else {
				endDate = DateUtils.getNow();
			}
			while (loopDate.before(endDate)) {
				if (isWorkDay(DateUtils.getDate(loopDate), hsu)) {
					hours += 8;
				}
				loopDate.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return hours;
	}

	public static boolean isWorkDay(final int day, final HibernateSessionUtil hsu) {
		final Calendar d = DateUtils.getCalendar(day);
		if (d.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || d.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return false;
		}
		if (hsu.createCriteria(Holiday.class).eq(Holiday.HDATE, day).exists()) {
			return false;
		}
		return true;
	}
}

	
