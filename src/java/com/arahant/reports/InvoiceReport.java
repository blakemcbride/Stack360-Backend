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

import com.arahant.beans.*;
import com.arahant.business.BClientCompany;
import com.arahant.business.BCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

// TODO: set detail to show summary for manual line item
public class InvoiceReport extends ReportBase {

	private boolean descriptionIncluded;
	protected boolean lineItemsIncluded;
	protected boolean detailIncluded;
	protected CompanyDetail currentCompany;
	protected Invoice currentInvoice;
	protected double currentInvoiceTotal;
	final protected SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	final protected static int headerRows = 8;

	public InvoiceReport() throws ArahantException {
		super("InvRpt", null);
	}

	/**
	 * @return Returns the descriptionIncluded.
	 */
	public boolean isDescriptionIncluded() {
		return descriptionIncluded;
	}

	public void setDescriptionIncluded(final boolean showDescription) {
		this.descriptionIncluded = showDescription;
	}

	/**
	 * @return Returns the detailIncluded.
	 */
	public boolean isDetailIncluded() {
		return detailIncluded;
	}

	public void setDetailIncluded(final boolean showDetail) {
		this.detailIncluded = showDetail;
	}

	/**
	 * @return Returns the lineItemsIncluded.
	 */
	public boolean isLineItemsIncluded() {
		return lineItemsIncluded;
	}

	public void setLineItemsIncluded(final boolean showLineItems) {
		this.lineItemsIncluded = showLineItems;
	}

	public String getReport(final HibernateSessionUtil hsu, final ArrayList<Invoice> invoiceList, final CompanyDetail company) throws ArahantException {
		this.hsu = hsu;

		try {
			final Iterator<Invoice> invoiceItr = invoiceList.iterator();
			boolean firstInvoice = true;

			this.currentCompany = company;

			// spin through all invoices passed in
			while (invoiceItr.hasNext()) {
				this.currentInvoice = invoiceItr.next();

				this.calculateInvoiceTotal();

				// determine if we need to start a new page for a new invoice
				if (firstInvoice) // no, just set flag
					firstInvoice = false;
				else {
					// yes, reset for new invoice and start a new page
					this.getDocument().setPageSize(PageSize.LETTER);
					this.getDocument().newPage();
					this.getDocument().setPageCount(1);
				}

				// write out the parts of our report
				this.writeHeader();
				this.writeDescription();
				writeLineItems();
				this.writePaymentRequest();
				this.writeDetail();
			}
		} catch (final Exception e) {
			throw new ArahantException(e);
		} finally {
			this.close();

			this.hsu = null;
			this.currentCompany = null;
			this.currentInvoice = null;
		}

		return getFilename();
	}

	protected void calculateInvoiceTotal() {
		final Iterator lineItemIterator = this.currentInvoice.getInvoiceLineItems().iterator();

		this.currentInvoiceTotal = 0;

		while (lineItemIterator.hasNext()) {
			final InvoiceLineItem lineItem = (InvoiceLineItem) lineItemIterator.next();

			this.currentInvoiceTotal += lineItem.getAdjRate() * lineItem.getAdjHours() + lineItem.getAmount();
		}
	}

	protected void writeHeader() throws DocumentException {
		PdfPTable table;
		PdfPCell cell;

		// invoice header
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setTotalWidth(75F);
		table.setLockedWidth(true);

		cell = new PdfPCell(new Paragraph("Invoice", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8F);
		cell.setPaddingTop(5F);
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

	protected void writeCompanyInfo() throws DocumentException {
		PdfPTable table;
		PdfPCell cell;
		final OrgGroup orgGroup = this.currentCompany;
		final Address address = orgGroup.getAddresses().iterator().next();
		final Iterator<Phone> phonesItr = orgGroup.getPhones().iterator();
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

		nested.addCell(centeredParagraph(orgGroup.getName()));
		nested.addCell(centeredParagraph(this.getFormattedStreet(address)));
		if (!"".equals(getFormattedStreet2(address)))
			nested.addCell(centeredParagraph(this.getFormattedStreet2(address)));
		nested.addCell(centeredParagraph(this.getFormattedCityStateZip(address)));
		nested.addCell(centeredParagraph((phone == null || phone.getPhoneNumber().trim().length() == 0) ? /*
				 * "(no phone)"
				 */ "" : phone.getPhoneNumber()));
		if (fax != null  &&  fax.getPhoneNumber().trim().length() != 0)
			nested.addCell(centeredParagraph("Fax " + fax.getPhoneNumber()));
		String url = currentCompany.getComUrl();
		if (url != null  &&  url.trim().length() != 0)
			nested.addCell(centeredParagraph(url));

		nested.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// assemble company table
		table = new PdfPTable(2);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(new int[]{60, 40});
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

		if (reportLogoImage == null)
			cell = new PdfPCell(new Paragraph(" ", this.baseFont));
		else {
			reportLogoImage.scaleToFit(300, 75);
			cell = new PdfPCell(reportLogoImage);
		}
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		table.addCell(nested);

		this.getDocument().add(table);
	}

	protected PdfPCell centeredParagraph(String text) {
		PdfPCell c = new PdfPCell(new Paragraph(text, this.baseFont));
		c.disableBorderSide(1 | 2 | 4 | 8);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		return c;
	}

	protected PdfPCell leftParagraph(String text) {
		PdfPCell c = new PdfPCell(new Paragraph(text, this.baseFont));
		c.disableBorderSide(1 | 2 | 4 | 8);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		return c;
	}

	protected String [] headerLeft(OrgGroup orgGroup) {
		String s[] = new String[headerRows];
		int i = 0;
		Address address = orgGroup.getAddresses().iterator().next();
		String tmp;
		
		s[i++] = "Bill To:";
		s[i++] = "\t" + orgGroup.getName();
		tmp = address.getStreet().trim();
		if (tmp.length() != 0)
			s[i++] = "\t" + tmp;
		tmp = address.getStreet2().trim();
		if (tmp.length() != 0)
			s[i++] = "\t" + tmp;
		tmp = getFormattedCityStateZip(address);
		if (tmp.length() != 0)
			s[i++] = "\t" + tmp;
		while (i < headerRows)
			s[i++] = "";
		return s;
	}
	
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

	protected void writeCustomerInfo() throws DocumentException {
		PdfPTable table;
		final BClientCompany clientCompany = new BClientCompany(this.currentInvoice.getClientCompany());
		final OrgGroup orgGroup = clientCompany.getBean();
		final Address address = orgGroup.getAddresses().iterator().next();

		table = new PdfPTable(3);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(new int[]{60, 20, 27});
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);


		table.addCell(new Paragraph(" ", this.baseFont));
		table.addCell(new Paragraph(" ", this.baseFont));
		table.addCell(new Paragraph(" ", this.baseFont));
		
		String [] left = headerLeft(orgGroup);
		String [][] right = headerRight(clientCompany);
		
		for (int row=0 ; row < headerRows ; row++) {
			table.addCell(new Paragraph(left[row], this.baseFont));
			table.addCell(new Paragraph(right[0][row], this.baseFont));
			table.addCell(new Paragraph(right[1][row], this.baseFont));
		}

		table.addCell(new Paragraph(" ", this.baseFont));
		table.addCell(new Paragraph(" ", this.baseFont));
		//if we have bill to name then display it
		if (!isEmpty(clientCompany.getBillToName()))
			table.addCell(new Paragraph("\t  re: " + clientCompany.getBillToName(), this.baseFont));

		table.addCell(new Paragraph(" ", this.baseFont));
		table.addCell(new Paragraph(" ", this.baseFont));

		this.getDocument().add(table);
	}

	protected void writeDescription() throws DocumentException {
		if (this.descriptionIncluded) {
			String description = this.currentInvoice.getDescription().trim();

			if (description.length() == 0)
				description = "";

			final Paragraph p = new Paragraph(description, this.baseFont);

			p.setSpacingBefore(15F);

			this.getDocument().add(p);
		}
	}

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
			table = new PdfPTable(6);
			table.setWidthPercentage(100F);
			table.setSpacingBefore(15F);
			table.setWidths(new int[]{4, 11, 21, 26, 19, 19});
			table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

			cell = new PdfPCell(new Paragraph(" ", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("Quantity", this.baseFont));
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

				// toggle the alternate row
				alternateRow = !alternateRow;

				lineItemNumber++;
				cell = new PdfPCell(new Paragraph(lineItemNumber + "", this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(Formatting.formatNumber(quantity, 2), this.baseFont));
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

	protected void writePaymentRequest() throws DocumentException {
		lastPage = true;
	}

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
			String employeeInitials;
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
			table = new PdfPTable(6);
			table.setWidthPercentage(100F);
			table.setSpacingBefore(15F);
			table.setWidths(new int[]{10, 10, 6, 25, 32, 17});
			table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

			cell = new PdfPCell(new Paragraph("Work Date", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("Hours", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("Per", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("Project", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("Description", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("External Reference", this.baseFont));
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

				cell = new PdfPCell(new Paragraph(" ", this.baseFont));
				cell.setColspan(6);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);

				SimpleDateFormat workDateFormat = new SimpleDateFormat("MM/dd/yy");

				ArrayList<Timesheet> timeList = new ArrayList<Timesheet>(lineItem.getTimesheets().size());
				timeList.addAll(lineItem.getTimesheets());

				java.util.Collections.sort(timeList);

				timesheetIterator = timeList.iterator();

				lineItemNumber++;
				if (timesheetIterator.hasNext())
					cell = new PdfPCell(new Paragraph("Line Item " + lineItemNumber, this.baseFont));
				else
					cell = new PdfPCell(new Paragraph("Line Item " + lineItemNumber + " - No Detail", this.baseFont));
				cell.setColspan(6);
				cell.disableBorderSide(1 | 4 | 8);
				table.addCell(cell);

				while (timesheetIterator.hasNext()) {
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

					employeeInitials = timesheet.getPerson().getFname().substring(0, 1);
					employeeInitials += timesheet.getPerson().getLname().substring(0, 1);
					cell = new PdfPCell(new Paragraph(employeeInitials, this.baseFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					if (alternateRow)
						cell.setBackgroundColor(alternateRowColor);
					cell.disableBorderSide(1 | 2 | 4 | 8);
					table.addCell(cell);

					project = timesheet.getProjectShift().getProject().getDescription();
					if (lastProject.equals(project))
						project = "same";
					else
						lastProject = project;
					cell = new PdfPCell(new Paragraph(project, this.baseFont));
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

					cell = new PdfPCell(new Paragraph(description, this.baseFont));
					if (alternateRow)
						cell.setBackgroundColor(alternateRowColor);
					cell.disableBorderSide(1 | 2 | 4 | 8);
					table.addCell(cell);

					cell = new PdfPCell(new Paragraph(timesheet.getProjectShift().getProject().getReference(), this.baseFont));
					if (alternateRow)
						cell.setBackgroundColor(alternateRowColor);
					cell.disableBorderSide(1 | 2 | 4 | 8);
					table.addCell(cell);

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


	protected String getFormattedStreet(final Address address) {
		String street = address.getStreet().trim();

		if (street.length() == 0) //street = "(no street)";
			street = "";
		return street;
	}

	protected String getFormattedStreet2(final Address address) {

		try {
			String street = address.getStreet2().trim();

			if (street.length() == 0) //street = "(no street)";
				street = "";

			return street;
		} catch (Exception e) {
			return "";
		}
	}

	protected String getFormattedCityStateZip(final Address address) {
		String city = address.getCity().trim();
		String state = address.getState().trim();
		String zip = address.getZip().trim();
		String cityStateZip;

		if (city.length() == 0) //city = "(no city)";
			city = "";
		if (state.length() == 0) //state = "(no state)";
			state = "";
		if (zip.length() == 0) //zip = "(no zip)";
			zip = "";

		cityStateZip = city;
		if (city.length() > 0 && (state.length() > 0 || zip.length() > 0))
			cityStateZip += ", ";
		if (state.length() > 0) {
			cityStateZip += state;

			if (zip.length() > 0)
				cityStateZip += " ";
		}
		cityStateZip += zip;

		return cityStateZip;
	}

	@Override
	public void onEndPage(final PdfWriter writer, final Document document) {
		final int pageNum = writer.getPageNumber();

		if (pageNum > 1)
			try {
				final Rectangle page = document.getPageSize();
				final Font defaultFont = new Font(FontFamily.COURIER, 10F, Font.ITALIC);
				final String invoiceId = this.currentInvoice.getAccountingInvoiceIdentifier();
				final String customerName = this.currentInvoice.getCompanyBase().getName();
				PdfPTable footer;

				if (pageNum != 1) {
					PdfPTable header;

					header = new PdfPTable(2);
					header.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
					header.setWidths(new int[]{20, 80});
					header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					header.addCell(new Paragraph("(cont'd)", defaultFont));
					header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					header.addCell(new Paragraph("Invoice " + invoiceId + " Customer " + customerName, defaultFont));
					header.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
					header.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - document.topMargin() + header.getTotalHeight(), writer.getDirectContent());
				}


				footer = new PdfPTable(1);
				footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
				footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				if (lastPage)
					try {
						PdfPTable tbl = new PdfPTable(2);
						tbl.setWidths(new int[]{50, 50});
						
						PdfPCell c;

//						c = new PdfPCell(new Paragraph("Terms: Payable upon receipt.", this.baseFont));
//						c.setHorizontalAlignment(Element.ALIGN_LEFT);
//						tbl.addCell(c);
						
						c = new PdfPCell(new Paragraph("", this.baseFont));
						c.setHorizontalAlignment(Element.ALIGN_LEFT);
						tbl.addCell(c);

						c = new PdfPCell(new Paragraph("Thank You.", this.baseFont));
						c.setHorizontalAlignment(Element.ALIGN_LEFT);
						tbl.addCell(c);

						tbl.addCell(new Paragraph("Thank You.", this.baseFont));

						footer.addCell(tbl);
					} catch (DocumentException e) {
					}

				footer.addCell(new Paragraph("Page " + pageNum, defaultFont));
				footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}
		else if (lastPage) {
			PdfPTable footer;
			final Rectangle page = document.getPageSize();
			footer = new PdfPTable(1);
			footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			if (lastPage)
				//	 PdfPTable table;
				// payment info
				//   table = new PdfPTable(2);
				//     table.setWidthPercentage(100F);
				//    table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
				//    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				//      table.setSpacingBefore(15F);
				try {
					PdfPTable tbl = new PdfPTable(2);
					tbl.setWidths(new int[]{50, 50});

					PdfPCell c;
					
//					c = new PdfPCell(new Paragraph("Terms: Payable upon receipt.", this.baseFont));
//					c.setHorizontalAlignment(Element.ALIGN_LEFT);
//					c.disableBorderSide(1 | 2 | 4 | 8);
//					tbl.addCell(c);
					
					c = new PdfPCell(new Paragraph("", this.baseFont));
					c.setHorizontalAlignment(Element.ALIGN_LEFT);
					c.disableBorderSide(1 | 2 | 4 | 8);
					tbl.addCell(c);

					c = new PdfPCell(new Paragraph("Thank You.", this.baseFont));
					c.setHorizontalAlignment(Element.ALIGN_RIGHT);
					c.disableBorderSide(1 | 2 | 4 | 8);
					tbl.addCell(c);

//					tbl.addCell(new Paragraph("Thank You.", this.baseFont));

					footer.addCell(tbl);
				} catch (DocumentException e) {
				}


			footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

		}
	}
	boolean lastPage = false;
}
