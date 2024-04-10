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
 *
 */
package com.arahant.reports;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectShift;
import com.arahant.beans.Timesheet;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BillingDetailReport extends ReportBase {

	Font underline = new Font(FontFamily.COURIER, 10F, Font.UNDERLINE);
	Font bold = new Font(FontFamily.COURIER, 10F, Font.BOLD);
	Font projectFont = new Font(FontFamily.COURIER, 12F, Font.UNDERLINE);
	Font companyFont = new Font(FontFamily.COURIER, 12F, Font.BOLD);

	int[] columnWidths = new int[]{12, 2, 10, 11, 2, 18, 45};

	public BillingDetailReport() throws ArahantException {
		super("BillDet", "Billing Detail Report", true);
	}

	public String build(final boolean nonApproved, final boolean approved, final boolean invoiced, final int startDate, final int endDate, final String clientId, final String employeeId, final String projectId, final boolean billable, final boolean nonbillable) throws DocumentException {

		try {

			writeHeaderInfo(nonApproved, approved, invoiced, startDate, endDate, clientId, employeeId, projectId, billable, nonbillable);

			PdfPTable table;

			addHeaderLine();

			double grandTotalHours = 0;

			double grandTotalDollars = 0;

			boolean firstTime = true;  //Is this the first time through the report?

			boolean alternateRow = true;

			HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME);

			if (!isEmpty(clientId))
				hcu.eq(OrgGroup.ORGGROUPID, clientId);
			
			HibernateScrollUtil<OrgGroup> scr = hcu.scroll();

			//For Each Orggroup
			while (scr.next()) {

				BOrgGroup bo = new BOrgGroup(scr.get());

				HibernateCriteriaUtil<Project> proj = ArahantSession.getHSU().createCriteria(Project.class).eq(Project.REQUESTING_ORG_GROUP, bo.getOrgGroup()).orderBy(Project.PROJECTNAME);

				if (!isEmpty(projectId))
					proj.eq(Project.PROJECTID, projectId);

				List<Character> billingState = new ArrayList<Character>();

				if (billable)
					billingState.add('Y');

				if (nonbillable)
					billingState.add('N');

				if (billingState.size() > 0)
					proj.in(Project.BILLABLE, billingState);

				HibernateScrollUtil<Project> scp = proj.scroll();

				boolean first = true;

				double clientTotalDollars = 0;
				double clientTotalHours = 0;

				//For Each Project
				while (scp.next()) {
					
					BProject bp = new BProject(scp.get());

					HibernateCriteriaUtil<Timesheet> t = ArahantSession.getHSU().createCriteria(Timesheet.class)
							.joinTo(Timesheet.PROJECTSHIFT)
							.eq(ProjectShift.PROJECT, bp.getBean())
							.orderBy(Timesheet.BEGINNING_ENTRY_DATE);

					List<Character> state = new ArrayList<Character>();

					if (nonApproved)
						state.add('N');

					if (approved)
						state.add('A');

					if (invoiced)
						state.add('I');

					if (state.size() > 0)
						t.in(Timesheet.STATE, state);

					if (!isEmpty(employeeId))
						t.eq(Timesheet.PERSON, new BEmployee(employeeId).getPerson());

					if (startDate != 0 && endDate != 0)
						t.dateBetween(Timesheet.BEGINNING_ENTRY_DATE, DateUtils.getDate(startDate), DateUtils.getDate(endDate));

					if (startDate == 0 && endDate != 0)
						t.dateBefore(Timesheet.BEGINNING_ENTRY_DATE, DateUtils.getDate(endDate));

					if (startDate != 0 && endDate == 0)
						t.dateAfter(Timesheet.BEGINNING_ENTRY_DATE, DateUtils.getDate(startDate));

					HibernateScrollUtil<Timesheet> sc = t.scroll();

					boolean firstProj = true;

					table = makeTable(columnWidths);

					double totalHours = 0;
					double totalDollars = 0;

					//For Each Timesheet
					while (sc.next()) 
					{
						//Print out the Orggroup
						if (first)
						{
							if (!firstTime)
								getDocument().add(Chunk.NEXTPAGE);
							else
								firstTime = false;

							String extId = "";
							if (!isEmpty(bo.getExternalId()))
								extId = "(" + bo.getExternalId() + ")";

							String company = bo.getName() + extId;

							if (!bo.getCompanyId().equals(bo.getOrgGroupId()))
								company += " - " + new BOrgGroup(bo.getCompanyId()).getName();
							
							int centerWidth = company.length();
							int sideWidth = (100 - centerWidth) / 2;

							PdfPTable clientTtable = makeTable(new int[]{sideWidth, centerWidth, sideWidth});
							write(clientTtable, "");
							writeCenteredWithBorder(clientTtable, company);
							write(clientTtable, "");
							addTable(clientTtable);
							first = false;
						}

						if (firstProj)
						{
							Paragraph paragraph = new Paragraph(Chunk.NEWLINE);
							paragraph.add(new Chunk(bp.getProjectName().trim() + " - " + bp.getDescription(), underline));
							paragraph.setAlignment(Element.ALIGN_CENTER);
							paragraph.add(Chunk.NEWLINE);
							getDocument().add(paragraph);

							writeColHeader(table, "Date", Element.ALIGN_LEFT);
							writeColHeader(table, "B", Element.ALIGN_LEFT);
							writeColHeader(table, "Hours", Element.ALIGN_RIGHT);
							writeColHeader(table, "Dollars", Element.ALIGN_RIGHT);
							writeColHeader(table, "");
							writeColHeader(table, "Person", Element.ALIGN_LEFT);
							writeColHeader(table, "Description", Element.ALIGN_LEFT);

							firstProj = false;
						}

						BTimesheet bt = new BTimesheet(sc.get());
						
						// toggle the alternate row
						if (bt.getBillable() == 'Y')
							alternateRow = false;
						else
							alternateRow = true;

						write(table, DateUtils.getDateFormatted(sc.get().getBeginningEntryDate()), alternateRow);
						write(table, bt.getBillable() + "", alternateRow);

						double hours = bt.getTotalHours();
						totalHours += hours;
						clientTotalHours += hours;
						grandTotalHours += hours;
						write(table, hours, alternateRow);

						float billingRate = bp.getBillingRate();
						double dollars = billingRate * hours;
						totalDollars += dollars;
						clientTotalDollars += dollars;
						grandTotalDollars += dollars;
						write(table, MoneyUtils.formatMoney(dollars), alternateRow);

						write(table,"", alternateRow);
						write(table, bt.getEmployeeNameLFM(), alternateRow);
						write(table, bp.getDescription(), alternateRow);
					}

					if (!firstProj)
					{
						writeBlankLine(table);

						PdfPCell cell = new PdfPCell(new Paragraph("Project Total:", bold));
						cell.setColspan(2);
						cell.disableBorderSide(1 | 2 | 4 | 8);
						table.addCell(cell);

						writeRight(table, formatTwoDecimals(totalHours), bold);
						writeRight(table, MoneyUtils.formatMoney(totalDollars), bold);
						write(table, "");
						write(table, "");
						write(table, "");

						addTable(table);
					}

					sc.close();
					
				}

				if (!first)
				{
					table = makeTable(new int[]{12, 2, 10, 11, 20, 45});

					writeBlankLine(table);

					write(table, "Client Total:", bold);
					write(table, "");
					writeRight(table, formatTwoDecimals(clientTotalHours), bold);
					writeRight(table, MoneyUtils.formatMoney(clientTotalDollars), bold);
					write(table, "");
					write(table, "");

					addTable(table);
				}

				scp.close();

			}

			table = makeTable(new int[]{12, 2, 10, 11, 20, 45});

			writeBlankLine(table);

			write(table, "Grand Total:", bold);
			write(table, "");
			writeRight(table, formatTwoDecimals(grandTotalHours), bold);
			writeRight(table, MoneyUtils.formatMoney(grandTotalDollars), bold);
			write(table, "");
			write(table, "");

			addTable(table);

			scr.close();

		} finally {
			close();

		}
		
		return getFilename();
	}

	private void writeBlankLine(final PdfPTable table)
	{
		for (int i = 0; i < table.getNumberOfColumns(); i++)
		{
			writeColHeader(table, "", Element.ALIGN_LEFT);
		}
	}

	private String formatTwoDecimals(double d)
	{
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
	}

	private void writeHeaderInfo(final boolean nonApproved, final boolean approved, final boolean invoiced, final int startDate, final int endDate, final String clientId, final String employeeId, final String projectId, final boolean billable, final boolean nonbillable) throws ArahantException, DocumentException
	{
		String timesheetStatus = "";

		if (nonApproved)
			timesheetStatus = "Non-Approved";

		if (approved)
		{
			if (timesheetStatus.equals(""))
				timesheetStatus = "Approved";
			else
				timesheetStatus += ", Approved";
		}

		if (invoiced)
		{
			if (timesheetStatus.equals(""))
				timesheetStatus = "Invoiced";
			else
				timesheetStatus += ", Invoiced";
		}

		if (nonApproved || approved || invoiced)
			writeHeaderLine("Timesheet Status", timesheetStatus);

		if (startDate != 0 && endDate != 0)
			writeHeaderLine("Date Range", DateUtils.getDateFormatted(startDate) + " - " + DateUtils.getDateFormatted(endDate));

		if (startDate == 0 && endDate != 0)
			writeHeaderLine("To Date", DateUtils.getDateFormatted(endDate));

		if (startDate != 0 && endDate == 0)
			writeHeaderLine("From Date", DateUtils.getDateFormatted(startDate));

		if (!isEmpty(clientId))
			writeHeaderLine("Client", new BOrgGroup(clientId).getName());

		if (!isEmpty(employeeId))
			writeHeaderLine("Employee", new BEmployee(employeeId).getNameLFM());

		if (!isEmpty(projectId))
			writeHeaderLine("Project", new BProject(projectId).getDescription());

		String bill = "";

		if (billable)
			bill = "Billable";

		if (nonbillable)
		{
			if (bill.equals(""))
				bill = "Non-Billable";
			else
				bill += ", Non-Billable";
		}
		
		if (billable || nonbillable)
			writeHeaderLine("Billing State", bill);
	}

	public static void main(String args[]) {
		try {
			//public String build(final boolean nonApproved, final boolean approved, final boolean invoiced, final int startDate, final int endDate, final String clientId, final String employeeId, final String projectId, final boolean billable, final boolean nonbillable) throws DocumentException {
			new BillingDetailReport().build(true, true, true, 20090101, 20090223, "", "", "", true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
