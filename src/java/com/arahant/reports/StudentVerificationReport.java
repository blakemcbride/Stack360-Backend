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

import com.arahant.beans.StudentVerification;
import com.arahant.business.BPerson;
import com.arahant.business.BStudentVerification;
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
public class StudentVerificationReport extends ReportBase {

    public StudentVerificationReport() throws ArahantException {
        super("stuver", "Student Verification", true);
    }

    public String build(String personId) throws DocumentException {

        try {

            PdfPTable table;

			BPerson bp=new BPerson(personId);

			writeHeaderLine("Student", bp.getNameLFM());
			writeHeaderLine("School Term", bp.getSchoolTermName());

            addHeaderLine();


            HibernateCriteriaUtil<StudentVerification> hcu = ArahantSession.getHSU()
					.createCriteria(StudentVerification.class).eq(StudentVerification.PERSON, bp.getPerson())
					.orderBy(StudentVerification.YEAR).orderBy(StudentVerification.TERM);

            HibernateScrollUtil<StudentVerification> scr = hcu.scroll();

            int count = 0;

            table = makeTable(new int[]{50,50});

            writeColHeader(table, "Year", Element.ALIGN_LEFT);
            writeColHeader(table, "Term", Element.ALIGN_LEFT);
 
            boolean alternateRow = true;

            while (scr.next()) {
                count++;



                // toggle the alternate row
                alternateRow = !alternateRow;
				BStudentVerification ver=new BStudentVerification(scr.get());

                write(table, scr.get().getSchoolYear(), alternateRow);
                write(table, ver.getTermName(), alternateRow);
            

            }
            
            scr.close();
            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }

}

	
