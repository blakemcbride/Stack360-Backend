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

package com.arahant.reports;

import com.arahant.beans.InvoiceLineItem;
import com.arahant.beans.Timesheet;
import com.arahant.business.BClientCompany;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Formatting;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/*
 * User: Blake McBride
 * Date: 12/3/16
 */

/**
 * Invoice format for Way To Go
 */
public class InvoiceReport3 extends InvoiceReport {

    protected String [][] headerRight(BClientCompany clientCompany) {
        int i = 0;
        Date invdate = currentInvoice.getCreateDate();
        String [][] s = new String[2][headerRows];

        s[0][i] = "Invoice Date:";
        s[1][i++] = dateFormatter.format(invdate);

        s[0][i] = "Invoice Num:";
        s[1][i++] = currentInvoice.getAccountingInvoiceIdentifier();

        String cid = clientCompany.getIdentifier();
        if (cid != null  &&  cid.length() > 0) {
            s[0][i] = "Customer ID:";
            s[1][i++] = cid;
        }

        String vin = clientCompany.getVendorNumber();
        if (vin != null  && vin.length() > 0) {
            s[0][i] = "VIN:";
            s[1][i++] = vin;
        }

        String po = currentInvoice.getPurchaseOrder();
        if (po != null  &&  po.length() > 0) {
            s[0][i] = "Purchase Order:";
            s[1][i++] = po;
        }

        short terms = currentInvoice.getPaymentTerms();
        if (terms < 1) {
            s[0][i] = "Terms:  Payable";
            s[1][i++] = "upon receipt";
        } else {
            s[0][i] = "Terms:";
            s[1][i++] = "Net " + terms;

            s[0][i] = "Due Date:";
            int dueDate = DateUtils.addDays(DateUtils.getDate(invdate), terms);
            s[1][i++] = DateUtils.getDateFormatted(dueDate);
        }

        s[0][i] = "Invoice Amt:";
        s[1][i++] = MoneyUtils.formatMoney(currentInvoiceTotal);

        while (i < headerRows) {
            s[0][i] = "";
            s[1][i++] = "";
        }
        return s;
    }

    @Override
    protected void writeLineItems() throws DocumentException {
        if (this.lineItemsIncluded) {
            HibernateCriteriaUtil hcu;
            Iterator lineItemIterator;
            InvoiceLineItem lineItem;
            float quantity;
            double unitPrice;
            double lineAmount;
            char type;
            boolean alternateRow = true;
            final BaseColor alternateRowColor = new BaseColor(192, 192, 192);
            PdfPTable table;
            PdfPCell cell;
            String item;
            String lastItem = "";
            String description;
            String lastDescription = "";
            int lineItemNumber = 0;

            // load the line items
            hcu = hsu.createCriteria(InvoiceLineItem.class);
            hcu.eq(InvoiceLineItem.INVOICE, this.currentInvoice);
            hcu.orderBy(InvoiceLineItem.INVOICELINEITEMID);
            lineItemIterator = hcu.list().iterator();

            // line items
            table = new PdfPTable(6);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);
            table.setWidths(new int[]{4, 21, 26, 11, 19, 19});
            table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

            cell = new PdfPCell(new Paragraph(" ", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Item", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Description", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantity", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Unit Price", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Extension", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.setHeaderRows(1);

            while (lineItemIterator.hasNext()) {
                lineItem = (InvoiceLineItem) lineItemIterator.next();
                quantity = lineItem.getAdjHours();
                unitPrice = lineItem.getAdjRate();
                lineAmount = lineItem.getAmount();
                type = lineItem.getBillingType();

                if (type == 'P') {
                    quantity = 1;
                    unitPrice = lineAmount;
                }

                // toggle the alternate row
                alternateRow = !alternateRow;

                lineItemNumber++;
                cell = new PdfPCell(new Paragraph(lineItemNumber + "", this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                item = lineItem.getProductService().getDescription();
                if (lastItem.equals(item))
                    item = "same";
                else
                    lastItem = item;
                cell = new PdfPCell(new Paragraph(item, this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                table.addCell(cell);

                description = lineItem.getDescription();
                if (lastDescription.equals(description))
                    description = "same";
                else
                    lastDescription = description;
                cell = new PdfPCell(new Paragraph(description, this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(Formatting.formatNumber(quantity, 2), this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(unitPrice), this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                if (unitPrice >= 0)
                    cell.setPaddingRight(8);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(quantity * unitPrice), this.baseFont));
                if (alternateRow)
                    cell.setBackgroundColor(alternateRowColor);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                if ((quantity * unitPrice) >= 0)
                    cell.setPaddingRight(8);
                table.addCell(cell);
            }

            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));

            table.addCell(new Paragraph("", this.baseFont));
            table.addCell(new Paragraph("", this.baseFont));
            table.addCell(new Paragraph("", this.baseFont));
            table.addCell(new Paragraph("", this.baseFont));

            cell = new PdfPCell(new Paragraph("Total:", this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(this.currentInvoiceTotal), this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            if (this.currentInvoiceTotal >= 0)
                cell.setPaddingRight(8);
            table.addCell(cell);

            this.getDocument().add(table);
        }
    }

    @Override
    protected void writeDetail() throws DocumentException {
        if (this.detailIncluded) {
            PdfPTable table;
            PdfPCell cell;
            final String customerName = this.currentInvoice.getCompanyBase().getName();
            final String customerId = this.currentInvoice.getCompanyBase().getIdentifier();
            HibernateCriteriaUtil hcu;
            Iterator lineItemIterator;
            InvoiceLineItem lineItem;
            Iterator timesheetIterator;
            Timesheet timesheet;
            boolean alternateRow = true;
            final BaseColor alternateRowColor = new BaseColor(192, 192, 192);
            double totalHours = 0.0;
            String project;
            String lastProject = "";
            String description;
            String lastDescription = "";
            int lineItemNumber = 0;

            // start a new page and new page number
            this.getDocument().setPageSize(PageSize.LETTER.rotate());
            this.getDocument().newPage();
            this.getDocument().setPageCount(1);

            lastPage = false;

            // company header
            table = new PdfPTable(1);
            table.setWidthPercentage(100F);

            cell = new PdfPCell(new Paragraph(this.currentCompany.getName(), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Invoice Detail Work Listing", this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            this.getDocument().add(table);


            // header line
            table = new PdfPTable(1);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);

            cell = new PdfPCell(new Paragraph("", this.baseFont));
            cell.setBackgroundColor(new BaseColor(192, 192, 192));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);

            this.getDocument().add(table);


            // invoice info
            table = new PdfPTable(2);
            table.setWidths(new int[]{15, 85});
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);
            table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

            table.addCell(new Paragraph("INVOICE NUM:", this.baseFont));
            table.addCell(new Paragraph(this.currentInvoice.getAccountingInvoiceIdentifier(), this.baseFont));
            table.addCell(new Paragraph("INVOICE DATE:", this.baseFont));
            table.addCell(new Paragraph(dateFormatter.format(this.currentInvoice.getCreateDate()), this.baseFont));
            table.addCell(new Paragraph("CUSTOMER:", this.baseFont));
            table.addCell(new Paragraph(customerName + (isEmpty(customerId) ? "" : (" (" + customerId + ")")), this.baseFont));

            this.getDocument().add(table);


            // header line
            table = new PdfPTable(1);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);

            cell = new PdfPCell(new Paragraph("", this.baseFont));
            cell.setBackgroundColor(new BaseColor(192, 192, 192));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);

            this.getDocument().add(table);


            // detail
            table = new PdfPTable(5);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);
            table.setWidths(new int[]{10, 8, 4, 25, 25});
            table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

            cell = new PdfPCell(new Paragraph("Work Date", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Hours", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("", this.baseFont));  // blank column
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Worker", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            table.addCell(cell);

            table.setHeaderRows(1);

            // load the line items
            hcu = hsu.createCriteria(InvoiceLineItem.class);
            hcu.eq(InvoiceLineItem.INVOICE, this.currentInvoice);
            hcu.orderBy(InvoiceLineItem.INVOICELINEITEMID);
            lineItemIterator = hcu.list().iterator();

            while (lineItemIterator.hasNext()) {
                lineItem = (InvoiceLineItem) lineItemIterator.next();
                lineItemNumber++;
                final char type = lineItem.getBillingType();

                cell = new PdfPCell(new Paragraph(" ", this.baseFont));
                cell.setColspan(6);
                cell.disableBorderSide(1 | 2 | 4 | 8);
                table.addCell(cell);

                SimpleDateFormat workDateFormat = new SimpleDateFormat("MM/dd/yy");

                ArrayList<Timesheet> timeList = new ArrayList<Timesheet>(lineItem.getTimesheets().size());
                timeList.addAll(lineItem.getTimesheets());

                java.util.Collections.sort(timeList);

                timesheetIterator = timeList.iterator();

                if (type != 'P' && timesheetIterator.hasNext())
                    cell = new PdfPCell(new Paragraph("Line Item " + lineItemNumber + " (" + lineItem.getDescription() + ")", this.baseFont));
                else
                    cell = new PdfPCell(new Paragraph("Line Item " + lineItemNumber + " - No Detail", this.baseFont));
                cell.setColspan(6);
                cell.disableBorderSide(1 | 4 | 8);
                table.addCell(cell);

                while (type != 'P' && timesheetIterator.hasNext()) {
                    timesheet = (Timesheet) timesheetIterator.next();

                    // toggle the alternate row
                    alternateRow = !alternateRow;

                    cell = new PdfPCell(new Paragraph(workDateFormat.format(DateUtils.getDate(timesheet.getWorkDate())), this.baseFont));
                    if (alternateRow)
                        cell.setBackgroundColor(alternateRowColor);
                    cell.disableBorderSide(1 | 2 | 4 | 8);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(timesheet.getTotalHours() + "", this.baseFont));
                    if (alternateRow)
                        cell.setBackgroundColor(alternateRowColor);
                    cell.disableBorderSide(1 | 2 | 4 | 8);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("", this.baseFont));  // blank column
                    if (alternateRow)
                        cell.setBackgroundColor(alternateRowColor);
                    cell.disableBorderSide(1 | 2 | 4 | 8);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(timesheet.getPerson().getNameLFM(), this.baseFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    if (alternateRow)
                        cell.setBackgroundColor(alternateRowColor);
                    cell.disableBorderSide(1 | 2 | 4 | 8);
                    table.addCell(cell);

                    project = timesheet.getProjectShift().getProject().getDescription();
                    if (lastProject.equals(project))
                        project = "same";
                    else
                        lastProject = project;
                    cell = new PdfPCell(new Paragraph("", this.baseFont));
                    if (alternateRow)
                        cell.setBackgroundColor(alternateRowColor);
                    cell.disableBorderSide(1 | 2 | 4 | 8);
                    table.addCell(cell);

                    description = timesheet.getDescription();
                    description = description == null ? "" : description;
                    if (lastDescription.equals(description)  &&  description.length() > 0)
                        description = "same";
                    else
                        lastDescription = description;

                    totalHours += timesheet.getTotalHours();
                }
            }

            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));

            cell = new PdfPCell(new Paragraph("TOTAL:", this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(Formatting.formatNumber(totalHours, 2), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));
            table.addCell(new Paragraph(" ", this.baseFont));

            this.getDocument().add(table);
        }
    }

}
