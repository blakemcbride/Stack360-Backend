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

package com.arahant.groovy.exports


import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.utils.ArahantSession
import com.arahant.utils.DateUtils
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.hibernate.ScrollableResults

/**
 * Author: Blake McBride
 * Date: 1/23/18
 */
class PaychexPerDiemExport {
    private DelimitedFileWriter dfw
    private HibernateSessionUtil hsu
    private int nCols

    public String export(int begDate, int endDate) throws Exception {

        hsu = ArahantSession.getHSU(false)

        File csvFile = FileSystemUtils.createTempFile("PaychexPerDiemExport-" + begDate + "-", ".csv")
        dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("yyyyMMdd")

        writeColumnHeader()

        spinExpenses(begDate, endDate)

        dfw.close()
        return FileSystemUtils.getHTTPPath(csvFile)
    }

    static private final int EMPLOYEE_ID = 0
    static private final int FNAME=1
    static private final int MNAME=2
    static private final int LNAME=3
    static private final int DATE_PAID=4
    static private final int PER_DIEM=5
    static private final int EXPENSE=6
    static private final int ADVANCE=7
    static private final int METHOD=8
    static private final int COMMENTS=9
    static private final int RETURN=10
    static private final int CLIENT_ID=11
    static private final int PROJECT_ID=12
    static private final int DESCRIPTION=13


    private void spinExpenses(int begDate, int endDate) {

        ScrollableResults scr = hsu.getSession().createSQLQuery("select " +
                "    emp.ext_ref, " +
                "    per.fname, " +
                "    per.mname, " +
                "    per.lname, " +
                "    exp.date_paid, " +
                "    exp.per_diem_amount, " +
                "    exp.expense_amount, " +
                "    exp.advance_amount, " +
                "    exp.payment_method ," +
                "    exp.comments, " +
                "    exp.per_diem_return, " +
                "    og.external_id," +
                "    proj.reference, " +
                "    proj.description " +
                "from expense exp " +
                "join person per " +
                "  on exp.employee_id = per.person_id " +
                "join employee emp " +
                "  on emp.person_id = exp.employee_id " +
                "join project proj " +
                "  on exp.project_id = proj.project_id " +
                "join org_group og " +
                "  on proj.requesting_org_group = og.org_group_id " +
                "where exp.date_paid >= :beg_date and exp.date_paid <= :end_date " +
                "order by per.lname, per.fname, per.mname, proj.description")
        .setParameter("beg_date", begDate)
        .setParameter("end_date", endDate)
        .scroll()

        while (scr.next()) {
            Object [] rec = scr.get()
            writeRecord(rec, PER_DIEM)
            writeRecord(rec, EXPENSE)
            writeRecord(rec, ADVANCE)
            writeRecord(rec, RETURN)
        }
        scr.close()

    }

    private String makeOrg(String proj) {
        String res = "1|";
        int i = 0;
        if (proj == null)
            proj = "";
        for ( ; i < proj.length() ; i++)  // skip letters
            if (Character.isDigit(proj.charAt(i)))
                break;
        for ( ; i < proj.length() ; i++)  { // collect numbers
            char c = proj.charAt(i);
            if (Character.isDigit(c))
                res += c;
            else
                break;
        }
        res += "|";
        if (i < proj.length())  { // get trailing character
            switch (proj.charAt(i)) {
                case 'S': res += '1';
                    break;
                case 'R': res += '2';
                    break;
                case 'C': res += '3';
                    break;
                case 'W': res += '2';
                    break;
            }
        }
        return res.length() > 4 ? res : "";
    }

    private void writeRecord(Object [] rec, int item) {

        float itemVal = rec[item]

        if (itemVal < 0.009)
            return

        dfw.writeField BProperty.get(StandardProperty.PaychexClientID)			// Client ID
        dfw.writeField rec[EMPLOYEE_ID]									// Employee ID

        dfw.writeField rec[LNAME]
        dfw.writeField rec[FNAME]

        dfw.writeField DateUtils.getDateFormatted(rec[DATE_PAID])		// fields for Tony's reference
        dfw.writeField rec[DESCRIPTION]                                   // Shift code


        dfw.writeField makeOrg(rec[PROJECT_ID])		//  Org
        dfw.writeField ''		// Job Number

        if (item == RETURN)
            dfw.writeField "Returned Per Diem"
        else if (item == PER_DIEM)
            dfw.writeField "Per Diem"
        else if (item == EXPENSE)
            dfw.writeField "Reimbursement"
        else if (item == ADVANCE)
            dfw.writeField "Cash Advance"
        else
            dfw.writeField ""


        dfw.writeField ''       // rate

        dfw.writeField ''		//  rate number

        dfw.writeField ''       // hours

        dfw.writeField ''	// Units
        dfw.writeField ''	// Line Date
        dfw.writeField itemVal      //  Amount
        dfw.writeField '1'	// Check Seq Number
        dfw.writeField ''	// Override State
        dfw.writeField ''	// Override Local
        dfw.writeField ''	// Override Local Juristiction
        dfw.writeField ''	// Labor Assignment
        dfw.endRecord()
    }

    private void writeColumnHeader() {
        dfw.writeField "Client ID"
        dfw.writeField "Worker ID"

        dfw.writeField "Last Name"
        dfw.writeField "FirstName"

        dfw.writeField "Date Worked"   // Added for Tony's reference
        dfw.writeField "Shift Code"

        dfw.writeField "Org"
        dfw.writeField "Job Number"
        dfw.writeField "Pay Component"
        dfw.writeField "Rate"
        dfw.writeField "Rate Number"
        dfw.writeField "Hours"
        dfw.writeField "Units"
        dfw.writeField "Line Date"
        dfw.writeField "Amount"
        dfw.writeField "Check Seq Number"
        dfw.writeField "Override State"
        dfw.writeField "Override Local"
        dfw.writeField "Override Local  Jurisdiction"
        dfw.writeField "Labor Assignment"

        nCols = dfw.getFieldCount()
        dfw.endRecord()
    }

}
