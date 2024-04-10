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
 * Created on Feb 27, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.beans.Employee;
import com.arahant.beans.HrEvalCategory;
import com.arahant.business.BHREmployeeEval;
import com.arahant.business.BHREmployeeEvalDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Feb 27, 2007
 *
 */
public class HREvaluationResponseReport extends ReportBase {
	
	private HibernateSessionUtil hsu;
    
    public HREvaluationResponseReport() throws ArahantException {
    	super("EvalRpt","Employee Evaluation Report");
    }

	public String build(final HibernateSessionUtil hsu, final String employeeId, final BHREmployeeEval[] employeeEvals) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {
        	final Employee employee = this.hsu.get(Employee.class, employeeId);
                        
        	// write out the parts of our report
            this.writeHeader(employee);
            this.writeEvaluation(employeeEvals);
        } finally {
            close();
        }
        return getFilename();
	}
	
	protected void writeHeader(final Employee employee) throws DocumentException {
		final PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidthPercentage(100F);
        table.setSpacingBefore(5F);
        
        writeCentered(table, "Employee: " + employee.getNameLFM());
        
        addTable(table);
        
        addHeaderLine();
	}
	
	protected void writeEvaluation(final BHREmployeeEval[] employeeEvals) throws DocumentException {
		PdfPTable table;
        PdfPCell cell;
		BHREmployeeEval employeeEval;
		String supervisor;
		BHREmployeeEvalDetail employeeEvalDetail;
		
		for (final BHREmployeeEval element : employeeEvals) {	
        	employeeEval = element;
        	
        	// write out header
		    table = makeTable(new int[] { 30, 30, 40 });
		   		    
		    write(table, "Evaluation Date");
		    write(table, "Next Evaluation Date");
		    write(table, "Supervisor");
		   
		    table.setHeaderRows(1);
		    
		    write(table,DateUtils.getDateFormatted(employeeEval.getEvalDate()));
		    write(table,DateUtils.getDateFormatted(employeeEval.getNextEvalDate()));
            
            
            supervisor = employeeEval.getSupervisorNameLFM();
            if (supervisor.equals(", "))
				supervisor = "";

            write(table,supervisor);
            
            addTable(table);
            
            
            // write out description
            table = makeTable(new int[] { 100 });
		  
		    cell = new PdfPCell(new Paragraph("Description", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    table.setHeaderRows(1);
		    
		    write (table,employeeEval.getDescription());        

		    addTable(table);
            
            
            // write out comments
            table = makeTable(new int[] { 100 });
		   		    
		    cell = new PdfPCell(new Paragraph("Supervisor Comments", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    table.setHeaderRows(1);

		    write(table,employeeEval.getComments());

		    addTable(table);
		    
		    // write out employee comments
            table = makeTable(new int[] { 100 });
		   		    
		    cell = new PdfPCell(new Paragraph("Employee Comments", this.baseFont));
		    cell.disableBorderSide(1 | 4 | 8);
		    table.addCell(cell);
		    table.setHeaderRows(1);
		    
		    write(table,employeeEval.getEmployeeComments());

		    addTable(table);
            
		    
            
            // write out detail
            try {
            	final BHREmployeeEvalDetail[] employeeEvalDetails = new BHREmployeeEval(employeeEval.getEmployeeEvalId()).listDetails();
        		boolean alternateRow = true;

            	
            	table = makeTable(new int[] {20, 20, 20, 20, 20 });
    		 	    
    		    cell = new PdfPCell(new Paragraph("Category Name", this.baseFont));
    		    cell.disableBorderSide(1 | 4 | 8);
    		    table.addCell(cell);
    		    cell = new PdfPCell(new Paragraph("Sup Score", this.baseFont));
    		    cell.disableBorderSide(1 | 4 | 8);
    		    table.addCell(cell);
    		    cell = new PdfPCell(new Paragraph("Sup Notes", this.baseFont));
    		    cell.disableBorderSide(1 | 4 | 8);
    		    table.addCell(cell);
    		    cell = new PdfPCell(new Paragraph("Emp Score", this.baseFont));
    		    cell.disableBorderSide(1 | 4 | 8);
    		    table.addCell(cell);
    		    cell = new PdfPCell(new Paragraph("Emp Notes", this.baseFont));
    		    cell.disableBorderSide(1 | 4 | 8);
    		    table.addCell(cell);
    		    
    		    table.setHeaderRows(1);
    		    
    		    
    		    
    		    
    		    
    		    
    		    
            	for (final BHREmployeeEvalDetail element0 : employeeEvalDetails) {
            		employeeEvalDetail = element0;
            		
                	// toggle the alternate row
        			alternateRow = !alternateRow;
        		    
        			write(table,employeeEvalDetail.getEvalCategoryName(),alternateRow);
        			write(table,employeeEvalDetail.getScore(),alternateRow);
        			write(table,employeeEvalDetail.getNotes(),alternateRow);
        			write(table,employeeEvalDetail.getEScore(),alternateRow);
        			write(table,employeeEvalDetail.getENotes(),alternateRow);
        	
            	}            	

                addTable(table);
            } catch (final ArahantException e) {
            	throw new DocumentException(e.getMessage());
            }

    		// reset
    		newPage();
    		resetPageCount();
		}
		
		//HrEvaluation report should now list all categories and their description as a report appendix.  
		//Just start a newPage() as the last part of the report and write out the category name, description, and possibly weight.
		newPage();
		table=makeTable(new int[]{30,60,10});
		final java.util.Iterator catItr=hsu.createCriteria(HrEvalCategory.class).orderBy(HrEvalCategory.NAME).list().iterator();
		cell = new PdfPCell(new Paragraph("Category Name", this.baseFont));
	    cell.disableBorderSide(1 | 4 | 8);
	    table.addCell(cell);
	    cell = new PdfPCell(new Paragraph("Description", this.baseFont));
	    cell.disableBorderSide(1 | 4 | 8);
	    table.addCell(cell);
	    cell = new PdfPCell(new Paragraph("Weight", this.baseFont));
	    cell.disableBorderSide(1 | 4 | 8);
	    table.addCell(cell);
		table.setHeaderRows(1);
		
		boolean alt=false;
		while (catItr.hasNext())
		{
			final HrEvalCategory cat=(HrEvalCategory)catItr.next();
			write(table,cat.getName(),alt);
			write(table,cat.getDescription(),alt);
			write(table,cat.getWeight(),alt);
			alt=!alt;
		}
		
		
	}
	
}

	
