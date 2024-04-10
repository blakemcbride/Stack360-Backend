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
 * Created on Mar 19, 2007
 * 
 */
package com.arahant.reports;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import com.arahant.beans.Employee;
import com.arahant.business.BHREmployeeEvent;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;



/**
 * 
 *
 * Created on Mar 19, 2007
 *
 */
public class HREventReport extends PdfPageEventHelper {
	private Font baseFont;
	private Document document;
	private HibernateSessionUtil hsu;
    
    public HREventReport() {
    	this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
    }

	public void build(final HibernateSessionUtil hsu, final File fileName, final String employeeId, final BHREmployeeEvent[] employeeEvents, final int startDate, final int endDate, final boolean isAscending, int max) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {
        	final Employee employee = this.hsu.get(Employee.class, employeeId);
            PdfWriter pdfWriter;
            
            this.document = new Document(PageSize.LETTER, 50F, 50F, 50F, 50F);
            
            pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(this);
            
            this.document.open();
            
        	// write out the parts of our report
            this.writeHeader(employee, startDate, endDate, isAscending, max);
            this.writeEvents(employeeEvents);
        } finally {
            try {
                if (this.document != null) {
                	this.document.close();
                	this.document = null;
                }
            } catch(final Exception ignored) { }
            
            this.hsu = null;
        }
	}
	
	protected void writeHeader(final Employee employee, final int startDate, final int endDate, final boolean isAscending, int max) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
        final String sort = isAscending ? "Ascending" : "Descending";
        String date;
    	
        // report header
        table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(225F);
        table.setLockedWidth(true);
        
        cell = new PdfPCell(new Paragraph("Employee Event Report", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
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
        
        cell = new PdfPCell(new Paragraph("Employee: " + employee.getNameLFM(), this.baseFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(1 | 2 | 4 | 8);
        table.addCell(cell);
        
        if (startDate == 0)
			date = "First Record";
		else
			date = DateUtils.getDateFormatted(startDate);
        cell = new PdfPCell(new Paragraph("Start Date: " + date, this.baseFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(1 | 2 | 4 | 8);
        table.addCell(cell);

        if (endDate == 0)
			date = "Last Record";
		else
			date = DateUtils.getDateFormatted(endDate);
        cell = new PdfPCell(new Paragraph("End Date: " + date, this.baseFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(1 | 2 | 4 | 8);
        table.addCell(cell);

        String sortStr = "Sort by Event Date: " + sort;
        if (max > 0)
            sortStr += " (max records " + max + ")";
        cell = new PdfPCell(new Paragraph(sortStr, this.baseFont));
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
	
	protected void writeEvents(final BHREmployeeEvent[] employeeEvents) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
		BHREmployeeEvent employeeEvent;
		String supervisor;
		String employeeNotified;
		
		for (int idx = 0; idx < employeeEvents.length; idx++) {	
			employeeEvent = employeeEvents[idx];
			
			// write out header
		    table = new PdfPTable(5);
		    table.setWidthPercentage(100F);
		    if (idx > 0)
				table.setSpacingBefore(50F);
			else
				table.setSpacingBefore(10F);		    
		    table.setWidths(new int[] { 14, 20, 11, 13, 42 });
		    table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
		    
		    cell = new PdfPCell(new Paragraph("Event Date", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("Supervisor", this.baseFont));
		    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("Empl. Notified", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("Date Notified", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("Summary", this.baseFont));
		    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    table.setHeaderRows(1);			
		    
			cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(employeeEvent.getEventDate()), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
		    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            supervisor = employeeEvent.getSupervisorNameLFM();
            if (supervisor.equals(", "))
				supervisor = "";
            cell = new PdfPCell(new Paragraph(supervisor, this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
		    
            employeeNotified = employeeEvent.getEmployeeNotified() == 'Y' ? "Yes" : "No";
		    cell = new PdfPCell(new Paragraph(employeeNotified, this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
		    
		    cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(employeeEvent.getDateNotified()), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
		    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
		    
		    cell = new PdfPCell(new Paragraph(employeeEvent.getSummary(), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            this.document.add(table);
		    
            // write out header
		    table = new PdfPTable(1);
		    table.setWidthPercentage(100F);
		    table.setSpacingBefore(15F);
		    table.setWidths(new int[] { 100 });
		    table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
		    
		    cell = new PdfPCell(new Paragraph("Detail", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    table.setHeaderRows(1);
		    
		    cell = new PdfPCell(new Paragraph(employeeEvent.getDetail(), this.baseFont));
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            this.document.add(table);
		}     

        
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
	        } catch(final Exception e) {
	            throw new ExceptionConverter(e);
	        }
    }
}

	
