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

import com.arahant.beans.Address;
import com.arahant.beans.InvoiceLineItem;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Phone;
import com.arahant.business.BClientCompany;
import com.arahant.business.BCompany;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.util.Iterator;

/*
 * Author: Blake McBride
 * Date: 12/3/16
 */

/**
 * Nestor Invoice format
 */
public class InvoiceReport2 extends InvoiceReport {

    @Override
    protected void writeHeader() throws DocumentException {
        PdfPTable table;
        PdfPCell cell;

        // invoice header
//        table = new PdfPTable(1);
//        table.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.setTotalWidth(75F);
//        table.setLockedWidth(true);
//
//        cell = new PdfPCell(new Paragraph("Invoice", new Font(Font.FontFamily.COURIER, 12F, Font.NORMAL)));
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setPaddingBottom(8F);
//        cell.setPaddingTop(5F);
//        table.addCell(cell);
//
//        this.getDocument().add(table);


        // header line
//        table = new PdfPTable(1);
//        table.setWidthPercentage(100F);
//        table.setSpacingBefore(15F);

//        cell = new PdfPCell(new Paragraph("", this.baseFont));
//        cell.setBackgroundColor(new BaseColor(192, 192, 192));
//        cell.disableBorderSide(1 | 2 | 4 | 8);
//        table.addCell(cell);
//
//        this.getDocument().add(table);


        // company info
        this.writeCompanyInfo();


        // customer info
        this.writeCustomerInfo();


        // header line
        table = new PdfPTable(1);
        table.setWidthPercentage(100F);
        table.setSpacingBefore(15F);

        cell = new PdfPCell(new Paragraph("", this.baseFont));
        cell.setBackgroundColor(new BaseColor(192, 192, 192));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        table.addCell(cell);

        this.getDocument().add(table);
    }

    @Override
    protected void writeCompanyInfo() throws DocumentException {
        PdfPTable table;
        PdfPCell cell;
        final OrgGroup orgGroup = this.currentCompany;
        final Address address = orgGroup.getAddresses().iterator().next();
        final Iterator phonesItr = orgGroup.getPhones().iterator();
        Phone phone = null;
        Phone fax = null;

        // look for the work phone
        while (phonesItr.hasNext()) {
            Phone p = (Phone) phonesItr.next();

            if (phone == null  &&  p.getPhoneType() == ArahantConstants.PHONE_WORK)
                phone = p;
            if (fax == null  &&  p.getPhoneType() == ArahantConstants.PHONE_FAX)
                fax = p;
        }

        // get logo image (left side of company table)
        Image reportLogoImage = null;
        try {
            File reportLogoImageFile = BCompany.getReportLogo(null);
            if (reportLogoImageFile != null)
                reportLogoImage = Image.getInstance(reportLogoImageFile.getAbsolutePath());
        } catch (final Exception e) {
            logger.error(e);
        }

        // build company info (right side of company table)
        final PdfPTable nested = new PdfPTable(1);
        nested.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

        nested.addCell(leftParagraph(orgGroup.getName()));
        nested.addCell(leftParagraph(this.getFormattedStreet(address)));
        if (!"".equals(getFormattedStreet2(address)))
            nested.addCell(leftParagraph(this.getFormattedStreet2(address)));
        nested.addCell(leftParagraph(this.getFormattedCityStateZip(address)));
        nested.addCell(leftParagraph((phone == null || phone.getPhoneNumber().trim().length() == 0) ? /*
				 * "(no phone)"
				 */ "" : phone.getPhoneNumber()));
        if (fax != null  &&  fax.getPhoneNumber().trim().length() != 0)
            nested.addCell(leftParagraph("Fax " + fax.getPhoneNumber()));
        String url = currentCompany.getComUrl();
        if (url != null  &&  url.trim().length() != 0)
            nested.addCell(leftParagraph(url));

        nested.setHorizontalAlignment(Element.ALIGN_LEFT);
        // assemble company table
        table = new PdfPTable(2);
        table.setWidthPercentage(100F);
        //table.setSpacingBefore(15F);
        table.setWidths(new int[]{45, 55});
        table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

        if (reportLogoImage == null)
            cell = new PdfPCell(new Paragraph(" ", this.baseFont));
        else {
            reportLogoImage.scaleToFit(250, 75);
            cell = new PdfPCell(reportLogoImage);
        }
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(nested);
        table.addCell(cell);

        this.getDocument().add(table);
    }

    @Override
    protected void writeLineItems() throws DocumentException {
        if (this.lineItemsIncluded) {
            HibernateCriteriaUtil hcu;
            Iterator lineItemIterator;
            InvoiceLineItem lineItem;
            float quantity;
            double unitPrice;
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
            table = new PdfPTable(4);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(15F);
            table.setWidths(new int[]{4, 21, 26, 19});
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

            cell = new PdfPCell(new Paragraph("Amount", this.baseFont));
            cell.disableBorderSide(1 | 4 | 8);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.setHeaderRows(1);

            while (lineItemIterator.hasNext()) {
                lineItem = (InvoiceLineItem) lineItemIterator.next();
                quantity = lineItem.getAdjHours();
                unitPrice = lineItem.getAdjRate();

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

}
