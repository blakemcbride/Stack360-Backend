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

package com.arahant.reports;

import com.arahant.beans.Timesheet;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class TimesheetAccountingReviewReport extends ReportBase {

    public TimesheetAccountingReviewReport() {
        super("rep", "Timesheet Accounting Review Report", true);
    }

    public String build(int billableItemsIndicator, String companyId, String personId, String projectId, int timesheetEndDate, int timesheetStartDate, String timesheetState, String invoiceId) throws DocumentException {
        if (timesheetEndDate == 0)
            timesheetEndDate = DateUtils.today();
        if (timesheetStartDate == 0)
            timesheetStartDate = DateUtils.add(timesheetEndDate, -7);
        try {

            PdfPTable table;

            switch (billableItemsIndicator) {
                case 0:
                    writeHeaderLine("Billable", "All");
                    break;
                case 1:
                    writeHeaderLine("Billable", "Yes");
                    break;
                case 2:
                    writeHeaderLine("Billable", "No");
                    break;

            }

            if (!isEmpty(companyId)) {
                BCompanyBase c = BCompanyBase.get(companyId);
                writeHeaderLine("Company", c.getName());
            }

            if (!isEmpty(personId)) {
                BPerson p = new BPerson(personId);
                writeHeaderLine("Employee", p.getNameLFM());
            }

            if (!isEmpty(projectId)) {
                BProject p = new BProject(projectId);
                writeHeaderLine("Project", p.getDescription());
            }

            if (timesheetStartDate != 0)
                writeHeaderLine("Start Date", DateUtils.getDateFormatted(timesheetStartDate));

            if (timesheetEndDate != 0)
                writeHeaderLine("End Date", DateUtils.getDateFormatted(timesheetEndDate));

            if (!isEmpty(timesheetState))
                writeHeaderLine("Timesheet State", timesheetState);

            if (invoiceId.equals("%"))
                invoiceId = "";
            if (!isEmpty(invoiceId))
                writeHeaderLine("Invoice Name Like", invoiceId.replace('%', ' ').trim());


            addHeaderLine();


            table = makeTable(6, 12, 9, 8, 12, 6, 39, 12);

            writeColHeader(table, "State", Element.ALIGN_LEFT);
            writeColHeader(table, "Invoice ID", Element.ALIGN_LEFT);
            writeColHeader(table, "Project", Element.ALIGN_LEFT);
            writeColHeader(table, "Billable", Element.ALIGN_LEFT);
            writeColHeader(table, "Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Hours", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            writeColHeader(table, "Name", Element.ALIGN_LEFT);


            boolean alternateRow = true;


            HibernateScrollUtil<Timesheet> tss = BTimesheet.searchForAccountingReview(
                    billableItemsIndicator, companyId, personId,
                    projectId, timesheetEndDate, timesheetStartDate,
                    timesheetState, invoiceId, 0);


            //	State, Invoice Id, Project Id, Billable,  Date, Hours, Description, Name

            float billableHours = 0, nonBillableHours = 0;
            while (tss.next()) {
                BTimesheet bc = new BTimesheet(tss.get());
                alternateRow = !alternateRow;

                write(table, (bc.getState() + ""), alternateRow);
                write(table, bc.getInvoiceIdentifier(), alternateRow);
                write(table, bc.getProjectName(), alternateRow);
                write(table, (bc.getBillable() + ""), alternateRow);
                write(table, DateUtils.getDateFormatted(bc.getWorkDate()), alternateRow);
                write(table, roundTime(bc.getTotalHours()), alternateRow);
                write(table, bc.getDescription(), alternateRow);
                write(table, bc.getEmployeeNameLFM(), alternateRow);

                if (bc.getBillable() == 'Y')
                    billableHours += bc.getTotalHours();
                else
                    nonBillableHours += bc.getTotalHours();
            }

            addTable(table);

            table = makeTable(100);

            write(table, "Total Billable Hours: " + roundTime(billableHours));
            write(table, "Total Non Billable Hours: " + roundTime(nonBillableHours));
            writeBoldLeft(table, "Total Hours: " + roundTime((billableHours + nonBillableHours)), 10F);

            addTable(table);

        } finally {
            close();

        }
        return getFilename();
    }

    private String roundTime(double time) {
        return com.arahant.utils.Formatting.formatNumberWithCommas(time, 2);
    }
}
