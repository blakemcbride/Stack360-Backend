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
 * Created on Jul 12, 2007
 * 
 */
package com.arahant.reports;

import java.io.FileNotFoundException;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * Created on Dec 11, 2007
 *
 */
public class HRUnapprovedBenefitsReport extends ReportBase {

    private final HibernateSessionUtil hsu = ArahantSession.getHSU();

    public HRUnapprovedBenefitsReport() throws ArahantException {
        super("HRUnapprBenRpt", "Unapproved Benefit Changes");
    }

    public String build() throws FileNotFoundException, DocumentException, ArahantException {
        try {
            addHeaderLine();

            writeBenefits();
        } finally {
            close();
        }

        return getFilename();
    }
//	.joinTo(HrBenefitJoin.PAYING_PERSON)
//  .i

    protected void writeBenefits() throws DocumentException, ArahantException {
        PdfPTable table;

		List<Character> benefitWizardStatuses = new ArrayList<Character>();
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_FINALIZED);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_NO_CHANGE);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_PROCESSED);

		HibernateCriteriaUtil hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N');

		hcu.joinTo(HrBenefitJoin.COVERED_PERSON).orderBy(Employee.LNAME).orderBy(Employee.FNAME);
		hcu.joinTo(HrBenefitJoin.PAYING_PERSON).in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses);

        HibernateScrollUtil<HrBenefitJoin> scrollUtill = hcu.scroll();

		boolean alternateRow = true;
        HrBenefitJoin benefitJoin;
        Employee employee;
        String benefitDesc;
        String lastSSN = "";
        String lastLName = "";
        String lastFName = "";

        table = makeTable(new int[]{18, 82});

        writeColHeader(table, "Employee SSN", Element.ALIGN_LEFT);
        writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
        table.setHeaderRows(1);

        while (scrollUtill.next()) {
            benefitJoin = scrollUtill.get();
            employee = hsu.get(Employee.class, benefitJoin.getPayingPersonId());
            if (employee == null) {
                continue;
            }

            if (lastSSN.equals(employee.getUnencryptedSsn()) && lastLName.equals(employee.getLname()) && lastFName.equals(employee.getFname())) {
                // toggle the alternate row
                alternateRow = !alternateRow;
            } else {
                // if we have already written someone before add a spacer
                if (lastSSN.length() > 0) {
                    write(table, " ");
                    write(table, " ");
                }

                lastSSN = employee.getUnencryptedSsn();
                lastLName = employee.getLname();
                lastFName = employee.getFname();

                write(table, employee.getUnencryptedSsn());
                write(table, employee.getNameLFM());
                write(table, " ");
                write(table, " ");

                // reset alternate row
                alternateRow = true;
            }

            if (benefitJoin.getHrBenefit() != null) {
                benefitDesc = "Declined " + benefitJoin.getHrBenefit().getName();
            } else if (benefitJoin.getHrBenefitCategory() != null) {
                benefitDesc = "Declined " + benefitJoin.getHrBenefitCategory().getDescription();
            } else {
                benefitDesc = benefitJoin.getHrBenefitConfig().getName();
            }

            write(table, "", false);
            write(table, benefitDesc, alternateRow);
        }
        scrollUtill.close();
        addTable(table);
    }

    /*public static void main(String[] args) {
    try	{
    new HRUnapprovedBenefitsReport().build();
    } catch (Exception e) {
    logger.info(e.getMessage());
    e.printStackTrace();
    }
    }*/
}

	
