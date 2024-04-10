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
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.LifeEvent;
import com.arahant.beans.Person;
import com.arahant.services.standard.tutorial.tutorialKalvin.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class OpenEnrollmentReport extends ReportBase {

    public HibernateSessionUtil hsu = ArahantSession.getHSU();

    public OpenEnrollmentReport() throws ArahantException {
        super("openenrollment", "Open Enrollment", true);
    }

    public String printReport(String bcr_id, boolean done, String description) {
        try {
            PdfPTable table;


            table = makeTable(new int[]{1});
            //written before other data, centered
            String doneItString = "The follow employees have gone through Open Enrollment";
            if (done != true) {
                doneItString = "The follow employees have NOT gone through Open Enrollment";
            }
            writeHeader(table, doneItString + " for " + description);
            //writeHeader(table, "Report headers 2 with some other stuff");
            addTable(table); //whenever you create a table, once the data is set, call addTable
            addHeaderLine();//add a solid line before writing other data below

            //create the table columns, percentage
            table = makeTable(new int[]{15, 22});

            //write column header for first page
            writeColHeader(table, "Last Name", Element.ALIGN_LEFT);
            writeColHeader(table, "First Name", Element.ALIGN_LEFT);

            //addColumnHeader: this will print on page > 1
            addColumnHeader("Last Name", Element.ALIGN_LEFT, 15);
            addColumnHeader("First Name", Element.ALIGN_LEFT, 22);

            //GET THE REPORT
            List<String> enrolledPerson = (List) hsu.createCriteria(HrBenefitJoin.class).distinct().selectFields(HrBenefitJoin.PAYING_PERSON_ID).joinTo(HrBenefitJoin.LIFE_EVENT).joinTo(LifeEvent.CHANGE_REASON).eq(HrBenefitChangeReason.ID, bcr_id).list();

            List<String> enrolledPerson2 = (List) hsu.createCriteria(HrBenefitJoin.class).distinct().selectFields(HrBenefitJoin.PAYING_PERSON_ID).joinTo(HrBenefitJoin.CHANGE_REASON).eq(HrBenefitChangeReason.ID, bcr_id).list();


            //merge the lists and get people back in sorted order
            enrolledPerson2.addAll(enrolledPerson);
            List<Employee> personList;
            if (done) {
                personList = hsu.createCriteria(Employee.class).in(Employee.PERSONID, enrolledPerson2).list();
            } else {
                personList = hsu.createCriteria(Employee.class).notIn(Employee.PERSONID, enrolledPerson2).list();
            }

            boolean alternateRow = true;
            int counter = 0;

            //write several row, enough to go to new page
            for (Person p : personList) {
                alternateRow = !alternateRow;   //toggle alternate row,highlights
                write(table, p.getLname(), alternateRow);
                write(table, p.getFname(), alternateRow);
                counter++;
            }
            addTable(table);
            //make a new table and print something else
            table = makeTable(new int[]{50});

            writeLeft(table, "Total: " + counter, true);


            addTable(table);

            close();

        } catch (DocumentException ex) {
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getFilename();
    }

    public static void main(String[] args) {
        OpenEnrollmentReport rpt = new OpenEnrollmentReport();
        rpt.printReport("00001-0000000017", false, "Open Enrollment-June");

    }
}
