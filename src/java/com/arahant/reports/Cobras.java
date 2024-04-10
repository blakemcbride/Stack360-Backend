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
 * Created on Jan 9, 2008
 * 
 */
package com.arahant.reports;

import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jan 9, 2008
 *
 */
public class Cobras extends ReportBase {

    public Cobras() throws ArahantException {
        super("DepCob", "Dependents on COBRA", true);
    }

    public String build() throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).distinct().eq(HrBenefitJoin.USING_COBRA, 'Y').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);

            HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

            int count = 0;

            table = makeTable(new int[]{15, 22, 22, 22, 22, 19});

            writeColHeader(table, "Paying SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Paying Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Covered Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Policy Start Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Policy End Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit Name", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            while (scr.next()) {
                count++;



                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, scr.get().getPayingPerson().getUnencryptedSsn(), alternateRow);
                write(table, scr.get().getPayingPerson().getNameLFM(), alternateRow);
                write(table, scr.get().getCoveredPerson().getNameLFM(), alternateRow);
                write(table, DateUtils.getDateFormatted(scr.get().getPolicyStartDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(scr.get().getPolicyEndDate()), alternateRow);
                write(table, scr.get().getHrBenefitConfig().getHrBenefit().getName(), alternateRow);

            }
            
            scr.close();
            addTable(table);

			table=makeTable(new int[]{100});
			
            write(table, "Total: " + count);

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new Cobras().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

	
