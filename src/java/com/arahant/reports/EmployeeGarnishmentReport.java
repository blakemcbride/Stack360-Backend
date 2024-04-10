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

import com.arahant.beans.Employee;
import com.arahant.beans.Garnishment;
import com.arahant.business.BEmployee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class EmployeeGarnishmentReport extends ReportBase {

	public EmployeeGarnishmentReport() {
		super("garn", "Employee Garnishment Report");
	}
	
	public String build(String personId) throws DocumentException {

        try {

            PdfPTable table;
			BEmployee bemp=new BEmployee(personId);
			writeHeaderLine("Employee", bemp.getNameLFM());

            addHeaderLine();

            List<Garnishment> garns = ArahantSession.getHSU()
					.createCriteria(Garnishment.class)
					.orderBy(Garnishment.PRIORITY)
					.joinTo(Garnishment.EMPLOYEE)
					.eq(Employee.PERSONID,personId)
					.list();
			

            int count = 0;

            table = makeTable(new int[]{25,75});

            boolean alternateRow = false;

            for (Garnishment g : garns) {
                count++;

				write (table, "Collection State: ", alternateRow);
				write (table, g.getCollectionState(), alternateRow);
				write (table, "Issue State: ", alternateRow);
				write (table, g.getIssueState(), alternateRow);
				write (table, "FIPS Code: ", alternateRow);
				write (table, g.getFipsCode(), alternateRow);
				write (table, "Docket Number: ", alternateRow);
				write (table, g.getDocketNumber(), alternateRow);
				write (table, "Start Date: ", alternateRow);
				write (table, DateUtils.getDateFormatted(g.getStartDate()), alternateRow);
				write (table, "End Date: ", alternateRow);
				write (table, DateUtils.getDateFormatted(g.getEndDate()), alternateRow);
				
				if (g.getDeductionAmount()>.001)
				{
					write (table, "Deduction Amount: ", alternateRow);
					write (table, MoneyUtils.formatMoney(g.getDeductionAmount()), alternateRow);
				}
				else
				{
					write (table, "Deduction Percentage: ", alternateRow);
					writeLeft (table, g.getDeductionPercentage()+"%", alternateRow);
				}
				
				if (g.getDeductionAmount()>.001)
				{
					write (table, "Maximum Amount: ", alternateRow);
					write (table, MoneyUtils.formatMoney(g.getMaxDollars()), alternateRow);
				}
				else
				{
					write (table, "Maximum Percentage: ", alternateRow);
					writeLeft (table, g.getMaxPercent()+"%", alternateRow);
				}

				write (table, "Apply to:", alternateRow);
				write(table, (g.getNetOrGross()=='N'?"Net":"Gross"));

				write(table, "Priority:",alternateRow);
				writeLeft(table, g.getPriority()+"", alternateRow);
			
				write(table, "Remit To:",alternateRow);
				write(table, g.getRemitTo().toString(), alternateRow);	

                // toggle the alternate row
                alternateRow = !alternateRow;


            }

            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    } 

}
