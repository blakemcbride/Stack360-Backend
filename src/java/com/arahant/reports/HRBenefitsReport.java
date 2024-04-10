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
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Feb 27, 2007
 *
 *
 *
 *update report to include the benefit information for each benefit (start date, endDate, insuranceId, salaryAdvanceAmount, dependent list with name, relationshiop, enrolled)
 */
public class HRBenefitsReport  extends ReportBase {

	private HibernateSessionUtil hsu;
    
    public HRBenefitsReport() throws ArahantException {
    	super("HRBRept","Employee Benefits Report");

    	this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
    }

	public String build(final HibernateSessionUtil hsu, final Employee employee) throws FileNotFoundException, DocumentException, ArahantException {
        this.hsu = hsu;
        ArahantSession.setCalcDate(DateUtils.now());
        try {

        	// write out the parts of our report
            this.writeHeader(employee);
            this.writeBenefits(employee);
        } finally {
        	close();
	         
            this.hsu = null;
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
	
	protected void writeBenefits(final Employee employee) throws DocumentException, ArahantException {
		PdfPTable table;

		boolean alternateRow = true;
		
		final HibernateCriteriaUtil<HrBenefitJoin> hcu=this.hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.COVERED_PERSON, employee)
			.joinTo(HrBenefitJoin.HRBENEFIT).orderBy(HrBenefit.NAME);

		 
		 
		 final List<HrBenefitJoin> benefitCategories = hcu.list();
			 
		final Iterator<HrBenefitJoin> iterator = benefitCategories.iterator();
			
		

        
		// start date, endDate, insuranceId, salaryAdvanceAmount, dependent list with name, relationshiop, enrolled
        while (iterator.hasNext()) {
        	final BHRBenefitJoin beneJoin = new BHRBenefitJoin(iterator.next());
        	
        	// toggle the alternate row

			table = makeTable(new int[] {20,25,30,25});
			writeLeft(table, "Benefit:", false);
			writeLeft(table, beneJoin.getBenefitName(), false);
			writeLeft(table, "Category:",false);
			writeLeft(table, beneJoin.getBenefitCategoryName(), false);
			writeLeft(table, "Start Date:",false);
			writeLeft(table, DateUtils.getDateFormatted(beneJoin.getPolicyStartDate()), false);
			writeLeft(table, "End Date:",false);
			writeLeft(table, DateUtils.getDateFormatted(beneJoin.getPolicyEndDate()), false);
			writeLeft(table, "Insurance ID:",false);
			writeLeft(table, beneJoin.getInsuranceId(), false);
			writeLeft(table, "Salary Advance Amount:",false);
			writeLeft(table, MoneyUtils.formatMoney(beneJoin.getAmountPaid()), false);
			addTable(table);
			//write the dep list out
			final PdfPTable sub = makeTable(new int[] {50,30,20});
			writeColHeader(sub, "Name",Element.ALIGN_LEFT);
			writeColHeader(sub, "Relationship",Element.ALIGN_LEFT);
			writeColHeader(sub, "Enrolled",Element.ALIGN_LEFT);
			sub.setHeaderRows(1);
			
			alternateRow=false;
			
			writeLeft(sub, employee.getNameLFM(), alternateRow);
			writeLeft(sub, "Employee", alternateRow);
			//THIS IS A BUG, SHOULD BE CHECKING IF THE EMPLOYEE IS ENROLLED, NOT IF BENEFIT COVERS EMPLOYEE
			//writeLeft(sub, beneJoin.getBenefitCoversEmployee()?"Yes":"No", alternateRow);
			
			for (final BHREmplDependent dep :beneJoin.getAllDependents())
			{	
				alternateRow = !alternateRow;
				writeLeft(sub, dep.getNameLFM(), alternateRow);
				writeLeft(sub, dep.getTextRelationship(), alternateRow);
				writeLeft(sub, dep.getEnrolled(), alternateRow);
			}
			addTable(sub);
        }
        
	}
	
}

	
