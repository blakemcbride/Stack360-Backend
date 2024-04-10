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
import com.arahant.beans.Timesheet;
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

public class TimesheetFinalizationReport extends PdfPageEventHelper {

	private Font baseFont;
	private Document document;
	private HibernateSessionUtil hsu;

	public TimesheetFinalizationReport() {
		this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
	}

	public void build(final HibernateSessionUtil hsu, final File fileName, final List employeeList, final int cutoffDate) throws FileNotFoundException, DocumentException {
		this.hsu = hsu;

		try {
			PdfWriter pdfWriter;

			this.document = new Document(PageSize.LETTER, 50F, 50F, 50F, 50F);

			pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
			pdfWriter.setPageEvent(this);

			this.document.open();

			// write out the parts of our report
			this.writeHeader(cutoffDate);
			this.writeFinalizations(employeeList);
		} finally {
			try {
				if (this.document != null) {
					this.document.close();
					this.document = null;
				}
			} catch (final Exception ignored) {
			}

			this.hsu = null;
		}
	}

	protected void writeHeader(final int cutoffDate) throws DocumentException {
		PdfPTable table;
		PdfPCell cell;
		String cutoffMessage;

		// report header
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setTotalWidth(225F);
		table.setLockedWidth(true);

		cell = new PdfPCell(new Paragraph("Timesheet Finalization Report", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
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

		if (cutoffDate == 0)
			cutoffMessage = "No Employees excluded based on Finalized Date";
		else
			cutoffMessage = "Exclude Employees who have Finalized as of Date " + DateUtils.getDateFormatted(cutoffDate);

		cell = new PdfPCell(new Paragraph(cutoffMessage, this.baseFont));
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

	protected void writeFinalizations(final List employeeList) throws DocumentException {
		PdfPTable table;
		PdfPCell cell;
		final Iterator employeeItr = employeeList.iterator();
		Employee employee;
		List employeeTimes;
		Iterator employeeTimesItr;
		int lastDate;
		double totalHours;
		Timesheet timesheet;
		boolean alternateRow = true;
		final BaseColor alternateRowColor = new BaseColor(192, 192, 192);

		// line items
		table = new PdfPTable(4);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(new int[]{40, 25, 10, 25});
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

		cell = new PdfPCell(new Paragraph("Name", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Time Last Entered", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Hours", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Finalization Date", this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		table.setHeaderRows(1);

		// spin through all employees passed in
		while (employeeItr.hasNext()) {
			employee = (Employee) employeeItr.next();

			// calculate hours and last time entered for this employee
			final Timesheet lastTSDate = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, employee).orderByDesc(Timesheet.WORKDATE).first();
			lastDate = 0;
			totalHours = 0;

			if (lastTSDate != null) {
				employeeTimes = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, employee).eq(Timesheet.WORKDATE, lastTSDate.getWorkDate()).list();
				employeeTimesItr = employeeTimes.iterator();

				lastDate = lastTSDate.getWorkDate();
				while (employeeTimesItr.hasNext()) {
					timesheet = (Timesheet) employeeTimesItr.next();
					totalHours += timesheet.getTotalHours();
				}

				totalHours = Math.round(totalHours * 100) / 100;
			}
			// toggle the alternate row
			alternateRow = !alternateRow;

			cell = new PdfPCell(new Paragraph(employee.getNameLFM(), this.baseFont));
			if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
			cell.disableBorderSide(1 | 2 | 4 | 8);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(lastDate), this.baseFont));
			if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
			cell.disableBorderSide(1 | 2 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(totalHours + "", this.baseFont));
			if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
			cell.disableBorderSide(1 | 2 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(employee.getTimesheetFinalDate()), this.baseFont));
			if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
			cell.disableBorderSide(1 | 2 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
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
