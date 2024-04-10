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

import com.arahant.beans.Employee;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TimesheetExceptionReport extends PdfPageEventHelper {

	private Font baseFont;
	private Document document;

	public TimesheetExceptionReport() {
		this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
	}

	public void build(final HibernateSessionUtil hsu, final File fileName, final List exceptionList, final int fromDate, final int toDate) throws FileNotFoundException, DocumentException, ArahantException {
		try {
			PdfWriter pdfWriter;

			this.document = new Document(PageSize.LETTER, 50F, 50F, 50F, 50F);

			pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
			pdfWriter.setPageEvent(this);

			this.document.open();

			// write out the parts of our report
			this.writeHeader(fromDate, toDate);
			this.writeExceptions(exceptionList);
		} finally {
			try {
				if (this.document != null) {
					this.document.close();
					this.document = null;
				}
			} catch (final Exception ignored) {
				throw new ArahantException(ignored);
			}
		}
	}

	protected void writeHeader(final int fromDate, final int toDate) throws DocumentException {
		PdfPTable table;
		PdfPCell cell;

		// report header
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setTotalWidth(225F);
		table.setLockedWidth(true);

		cell = new PdfPCell(new Paragraph("Timesheet Exception Report", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8F);
		cell.setPaddingTop(5F);
		table.addCell(cell);

		this.document.add(table);


		// run date
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(5F);

		cell = new PdfPCell(new Paragraph("Run Date: " + DateUtils.getDateTimeFormatted(new Date()), this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);


		// cutoff date
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);

		cell = new PdfPCell(new Paragraph("Report from " + DateUtils.getDateFormatted(fromDate) + " to " + DateUtils.getDateFormatted(toDate), this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);


		// header line
		table = new PdfPTable(1);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.setBackgroundColor(new BaseColor(192, 192, 192));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);
	}

	protected void writeExceptions(final List exceptionList) throws DocumentException {
		PdfPTable table;
		PdfPCell cell;
		final Iterator exceptionItr = exceptionList.iterator();
		List employeeAndTimeList;
		Iterator employeeAndTimeListItr;
		Employee employee;
		//	String employeeName;
		Integer missingTimeDate;
		boolean alternateRow = true;
		final BaseColor alternateRowColor = new BaseColor(192, 192, 192);

		// line items
		table = new PdfPTable(2);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(new int[]{75, 25});
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

		cell = new PdfPCell(new Paragraph("Name", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Date", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		table.setHeaderRows(1);

		// spin through all exceptions passed in
		while (exceptionItr.hasNext()) {
			employeeAndTimeList = (List) exceptionItr.next();
			employeeAndTimeListItr = employeeAndTimeList.iterator();
			employee = (Employee) employeeAndTimeListItr.next();
			//    	employeeName = employee.getPerson().getLname() + ", " + employee.getPerson().getFname();

			// spin through all of their missing entries        	
			while (employeeAndTimeListItr.hasNext()) {
				missingTimeDate = (Integer) employeeAndTimeListItr.next();

				// toggle the alternate row
				alternateRow = !alternateRow;

				cell = new PdfPCell(new Paragraph(employee.getNameLFM(), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(missingTimeDate.intValue()), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);
			}
		}

		this.document.add(table);
	}

	@Override
	public void onEndPage(final PdfWriter writer, final Document document) {
		final int pageNum = writer.getPageNumber();

		if (pageNum > 1)
			try {
				final Rectangle page = document.getPageSize();
				final Font defaultFont = new Font(FontFamily.COURIER, 10F, Font.ITALIC);
				final PdfPTable footer = new PdfPTable(1);
				footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
				footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				footer.addCell(new Paragraph("Page " + pageNum, defaultFont));
				footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}
	}
}
