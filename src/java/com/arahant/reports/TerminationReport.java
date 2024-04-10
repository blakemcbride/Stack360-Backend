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
 * Created on Jul 10, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.beans.Address;
import java.io.FileNotFoundException;
import java.util.List;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRBenefitJoinH;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class TerminationReport extends ReportBase {

	public TerminationReport() throws ArahantException {
		super("TermRpt","Termination Report");
	}

	public String buildEmployee(String employeeId) throws FileNotFoundException, DocumentException, ArahantException {

        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeEmployeeData(employeeId);
        } finally {
            close();
        }

        return getFilename();
	}
	
	protected void writeEmployeeData(String employeeId) throws DocumentException, ArahantException {
		BEmployee be = new BEmployee(employeeId);
		PdfPTable headerTable;
		PdfPTable dataTable;
		PdfPTable depTable;
		float fontSize = 10;

		boolean alternateRow = true;

        headerTable = makeTable(createColumnWidths(5));
        dataTable = makeTable(createColumnWidths(4));
        depTable = makeTable(createColumnWidths(4));

		setAlternateRowColor(new BaseColor(235, 235, 235));
		writeAlign(headerTable, "Company:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, be.getCompanyName(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Name:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, be.getNameFML(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Benefit Class:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable,  be.getBenefitClass() == null ? "(None)" : be.getBenefitClass().getName(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Address:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		if(be.getAddresses() != null) {
			Address a = be.getAddresses().iterator().next();
			writeAlign(headerTable, a.getStreet() + " " + a.getStreet2() + ", " + a.getCity() + ", " + a.getState() + " " + a.getZip(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
		}
		else
			writeAlign(headerTable, "(None)", Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Phone:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		if(be.getPhones() != null && be.getPhones().size() > 0)
			writeAlign(headerTable, be.getPhones().iterator().next().getPhoneNumber(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
		else
			writeAlign(headerTable, "(None)", Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Last 4 SSN:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, isEmpty(be.getSsn()) ? "(None)" : be.getSsn().substring(7), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Status:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, be.getEmployeeStatusName(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Status Date:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, DateUtils.getDateFormatted(be.getEmployeeStatusDate()), Element.ALIGN_LEFT, alternateRow, 4, fontSize);
        writeAlign(headerTable, "Comments:", Element.ALIGN_RIGHT, alternateRow, 1, fontSize);
		writeAlign(headerTable, be.getLastInactiveStatusHistory().getNotes(), Element.ALIGN_LEFT, alternateRow, 4, fontSize);

		setAlternateRowColor(new BaseColor(220, 220, 220));
        writeColHeader(dataTable, "Category", Element.ALIGN_LEFT);
        writeColHeader(dataTable, "Plan", Element.ALIGN_LEFT);
        writeColHeader(dataTable, "Coverage Type", Element.ALIGN_LEFT);
        writeColHeader(dataTable, "Policy End Date", Element.ALIGN_LEFT);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId)
																						  .eq(HrBenefitJoin.COVERED_PERSON_ID, employeeId)
																						  .ltAndNeq(HrBenefitJoin.POLICY_START_DATE, DateUtils.now(), 0)
																						  .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
																						  .joinTo(HrBenefitConfig.HR_BENEFIT)
																						  .ne(HrBenefit.TIMERELATED, 'Y');
		HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

		while(scr.next()) {
			BHRBenefitJoin bj = new BHRBenefitJoin((HrBenefitJoin)scr.get());
			if(DateUtils.getDate(bj.getBean().getRecordChangeDate()) != DateUtils.now())
				continue;
			writeAlign(dataTable, bj.getBenefitCategoryName(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, bj.getBenefitName(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, bj.getBenefitConfigName(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, DateUtils.getDateFormatted(bj.getPolicyEndDate()), Element.ALIGN_LEFT, alternateRow);

			alternateRow = !alternateRow;
		}
		
		HibernateCriteriaUtil<HrBenefitJoinH> hcu2 = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, employeeId)
																							 .eq(HrBenefitJoinH.COVERED_PERSON_ID, employeeId)
																							 .leOrEq(HrBenefitJoinH.POLICY_END_DATE, DateUtils.now(), 0)
																							 .ltAndNeq(HrBenefitJoinH.POLICY_START_DATE, DateUtils.now(), 0)
																							 .joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
																							 .joinTo(HrBenefitConfig.HR_BENEFIT)
																							 .ne(HrBenefit.TIMERELATED, 'Y');
		HibernateScrollUtil<HrBenefitJoinH> scr2 = hcu2.scroll();

		while(scr2.next()) {
			HrBenefitJoinH hj = scr2.get();
			BHRBenefitJoinH bj = new BHRBenefitJoinH(hj);

			if(DateUtils.getDate(bj.getBean().getRecordChangeDate()) != DateUtils.now())
				continue;

			writeAlign(dataTable, new BHRBenefitCategory(bj.getCategoryId()).getDescription(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, new BHRBenefit(bj.getBenefitId()).getName(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, bj.getConfigName(), Element.ALIGN_LEFT, alternateRow);
			writeAlign(dataTable, DateUtils.getDateFormatted(bj.getPolicyEndDate()), Element.ALIGN_LEFT, alternateRow);

			alternateRow = !alternateRow;
		}

		writeAlign(depTable, "", Element.ALIGN_LEFT, false, 4);
		writeAlign(depTable, "Dependents:", Element.ALIGN_LEFT, false, 4);
		writeColHeader(depTable, "First Name", Element.ALIGN_LEFT);
		writeColHeader(depTable, "Last Name", Element.ALIGN_LEFT);
		writeColHeader(depTable, "Last 4 SSN", Element.ALIGN_LEFT);
		writeColHeader(depTable, "Relationship", Element.ALIGN_LEFT);

		List<HrEmplDependent> deps = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, new BEmployee(employeeId).getEmployee())
																			  .gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0)
																			  .eq(HrEmplDependent.RECORD_TYPE, 'R')
																			  .list();
		if(deps.size() == 0) {
			writeAlign(depTable, "(NONE)");
			writeAlign(depTable, "(NONE)");
			writeAlign(depTable, "(NONE)");
			writeAlign(depTable, "(NONE)");
		}

		for(HrEmplDependent d : deps) {
			BHREmplDependent dep = new BHREmplDependent(d);

			writeAlign(depTable, dep.getFirstName());
			writeAlign(depTable, dep.getLastName());
			writeAlign(depTable, dep.getSsn().substring(dep.getSsn().length() - 4, dep.getSsn().length()));
			writeAlign(depTable, dep.getRelationshipType().equals("S") ? "Spouse" : (dep.getRelationshipType().equals("C") ? "Child" : "Other"));
		}

        addTable(headerTable);
        addTable(dataTable);
        addTable(depTable);
	}

	public String build() throws FileNotFoundException, DocumentException, ArahantException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeData();
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	protected void writeData() throws DocumentException, ArahantException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(new int[] { 25,35,10,10,10,10 });

        writeColHeader(table, "SSN", Element.ALIGN_LEFT);
        writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Term Date", Element.ALIGN_LEFT);
        writeColHeader(table, "Medical", Element.ALIGN_LEFT);
        writeColHeader(table, "Dental", Element.ALIGN_LEFT);
        writeColHeader(table, "Vision", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
        
        final int now=DateUtils.now()/100;
        final int startOfMonth=now*100;
        final int endOfMonth=(now+1)*100;
        
        final List<Employee> l=ArahantSession.getHSU().createCriteria(Employee.class)
        	.joinTo(Employee.HREMPLSTATUSHISTORIES)
        	.gt(HrEmplStatusHistory.EFFECTIVEDATE, startOfMonth)
        	.lt(HrEmplStatusHistory.EFFECTIVEDATE, endOfMonth)
        	.list();
		
    	// spin through all exceptions passed in
        for (final Employee e : l) {
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
			
			final BEmployee be=new BEmployee(e);
            
			write(table, be.getSsn(), alternateRow);
			write(table, be.getNameLFM(), alternateRow);
			write(table, be.getTerminationDate(), alternateRow);
			write(table, be.getHasMedical(), alternateRow);
			write(table, be.getHasDental(), alternateRow);
			write(table, be.getHasVision(), alternateRow);
        }
        
        addTable(table);
	}

	public static void main(String args[]) {
		String employeeId = "00001-0000401676";

		TerminationReport term = new TerminationReport();
		try {
			term.buildEmployee(employeeId);
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}
 
	
