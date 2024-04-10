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
 * Created on Mar 16, 2007
 * 
 */
package com.arahant.reports;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitPackage;
import com.arahant.beans.HrBenefitPackageJoin;
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
 * Created on Mar 16, 2007
 *
 */
public class HRBenefitPackageReport extends PdfPageEventHelper {

	private Font baseFont;
	private Document document;
	private HibernateSessionUtil hsu;
    
    public HRBenefitPackageReport() {
    	this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
    }

	public void build(final HibernateSessionUtil hsu, final File fileName, final HrBenefitPackage benefitPackage) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {
            PdfWriter pdfWriter;
            
            this.document = new Document(PageSize.LETTER, 50F, 50F, 50F, 50F);
            
            pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(this);
            
            this.document.open();
            
        	// write out the parts of our report
            this.writeHeader(benefitPackage);
            this.writeBenefitPackage(benefitPackage);
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
	
	protected void writeHeader(final HrBenefitPackage benefitPackage) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
    	
        // report header
        table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(225F);
        table.setLockedWidth(true);
        
        cell = new PdfPCell(new Paragraph("Benefit Package Report", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
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
        
        cell = new PdfPCell(new Paragraph("Benefit Package: " + benefitPackage.getName(), this.baseFont));
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
	
	protected void writeBenefitPackage(final HrBenefitPackage benefitPackage) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
		boolean alternateRow = true;
		final BaseColor alternateRowColor = new BaseColor(192, 192, 192);
		final List benefitCategories = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME)
			.joinTo(HrBenefit.HRBENEFITPACKAGEJOINS)
			.eq(HrBenefitPackageJoin.HRBENEFITPACKAGE, benefitPackage).list();
		final Iterator iterator = benefitCategories.iterator();
		HrBenefit benefitCategory;
		
        table = new PdfPTable(3);
        table.setWidthPercentage(100F);
        table.setSpacingBefore(15F);
        table.setWidths(new int[] { 40, 33, 27 });
        table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
        
        cell = new PdfPCell(new Paragraph("Benefit Name", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);        
        cell = new PdfPCell(new Paragraph("Rule Name", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);        
        cell = new PdfPCell(new Paragraph("Time Related", this.baseFont));
        cell.disableBorderSide(1 | 4 | 8);
        table.addCell(cell);
        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        while (iterator.hasNext()) {
        	benefitCategory = (HrBenefit)iterator.next();
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
            cell = new PdfPCell(new Paragraph(benefitCategory.getName(), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(benefitCategory.getRuleName(), this.baseFont));
            if (alternateRow)
				cell.setBackgroundColor(alternateRowColor);
            cell.disableBorderSide(1 | 2 | 4 | 8);
            table.addCell(cell);            
                       
            cell = new PdfPCell(new Paragraph(benefitCategory.getTimeRelated() + "", this.baseFont));
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

	
