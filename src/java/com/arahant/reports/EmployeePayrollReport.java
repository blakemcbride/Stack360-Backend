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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.reports;

import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class EmployeePayrollReport extends ReportBase {

	 public EmployeePayrollReport() throws ArahantException {
        super("EmpPay", "Employee Payroll Report", true);
    }

    public String build(BEmployee bc) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            int count = 0;

            table = makeTable(new int[]{25,75});

			writeLeft(table, "Name", false);
			writeLeft(table, bc.getNameLFM(), false);
			writeLeft(table, "SSN", false);
			writeLeft(table, bc.getSsn(), false);
			
			writeLeft(table, "Marital Status", false);
			writeLeft(table, bc.getMaritalStatus(), false);
			
			writeLeft(table, "Local Tax Code", false);
			writeLeft(table, bc.getLocalTaxCode(), false);
			
			writeLeft(table, "EIC Status", false);
			writeLeft(table, bc.getEarnedIncomeCreditStatus(), false);

			writeLeft(table, "Bank Code", false);
			writeLeft(table, bc.getPayrollBankCode(), false);
			
			writeLeft(table, "Tax State", false);
			writeLeft(table, bc.getTaxState(), false);
			
			writeLeft(table, "Unemployment State", false);
			writeLeft(table, bc.getUnemploymentState(), false);
			
			writeLeft(table, "W4 Status", false);
			writeLeft(table, bc.getW4StatusName(), false);
			
			writeLeft(table, "Pay Periods Per Year", false);
			writeLeft(table, bc.getPayPeriodsPerYear()+"", false);
			
			writeLeft(table, "Hours per period", false);
			writeLeft(table, bc.getExpectedHoursPerPayPeriod()+"", false);
		
			
			writeLeft(table, "Federal Exemptions", false);
			writeLeft(table, bc.getFederalExemptions()+"", false);
			
			writeLeft(table, "State Exemptions", false);
			writeLeft(table, bc.getStateExemptions()+"", false);
			
			writeLeft(table, "Exempt", false);
			writeLeft(table, bc.getExempt()?"Yes":"No", false);


            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

}


