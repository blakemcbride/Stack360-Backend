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
 * Created on Mar 2, 2007
 * 
 */
package com.arahant.reports;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.Employee;
import com.arahant.beans.HrWage;
import com.arahant.beans.Person;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
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
 * Created on Mar 2, 2007
 *
 */
public class HRWageHistoryReport extends PdfPageEventHelper {

	private Font baseFont;
	private Document document;
	private HibernateSessionUtil hsu;
    
    public HRWageHistoryReport() {
    	this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
    }

	public void build(final HibernateSessionUtil hsu, final File fileName, final String employeeId) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {
        	final Employee employee = this.hsu.get(Employee.class, employeeId);		
    		
            PdfWriter pdfWriter;
            
            this.document = new Document(PageSize.LETTER.rotate(), 50F, 50F, 50F, 50F);
            
            pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(this);
            
            this.document.open();
            
        	// write out the parts of our report
            this.writeHeader(employee);
            this.writeWageHistory(employee);
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
	
	protected void writeHeader(final Employee employee) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
    	
        // report header
        table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(225F);
        table.setLockedWidth(true);
        
        cell = new PdfPCell(new Paragraph("Employee Wage & Position History Report", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
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
	
	protected void writeWageHistory(final Employee employee) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
		boolean alternateRow = true;
		final BaseColor alternateRowColor = new BaseColor(192, 192, 192);
		final List wageHistoryItems = hsu.createCriteria(HrWage.class)
			.orderByDesc(HrWage.EFFECTIVEDATE)
			.joinTo(HrWage.EMPLOYEE).eq(Person.PERSONID, employee.getPersonId())
			.list();
		final Iterator iterator = wageHistoryItems.iterator();
		HrWage wageHistoryItem;
		String wageType;
		
        table = new PdfPTable(5);
        table.setWidthPercentage(100F);
        table.setSpacingBefore(15F);
        table.setWidths(new int[] { 15, 15, 9, 21, 40 });
        table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
        
        cell = new PdfPCell(new Paragraph("Effective Date", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Wage Amount", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Wage Type", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Position", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Notes", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        while (iterator.hasNext()) {
        	wageHistoryItem = (HrWage)iterator.next();
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
            cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(wageHistoryItem.getEffectiveDate()), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(wageHistoryItem.getWageAmount()), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);

			wageType=wageHistoryItem.getWageType().getWageName();
            
            cell = new PdfPCell(new Paragraph(wageType, this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(wageHistoryItem.getHrPosition().getName(), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(wageHistoryItem.getNotes(), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
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
	        } catch(final Exception e) {
	            throw new ExceptionConverter(e);
	        }
    }
}

	