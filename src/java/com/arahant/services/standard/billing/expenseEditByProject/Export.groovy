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

package com.arahant.services.standard.billing.expenseEditByProject

import com.arahant.beans.Employee
import com.arahant.beans.Expense
import com.arahant.beans.Project
import com.arahant.business.BEmployee
import com.arahant.business.BPerson
import com.arahant.servlets.REST
import com.arahant.utils.*
import org.json.JSONObject
import org.kissweb.DelimitedFileWriter

import java.text.DecimalFormat

/**
 * Author: Blake McBride
 * Date: 3/22/18
 */
class Export {

    private static int seq = 1

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int bdt = injson.getInt("beginning_date")
        int edt = injson.getInt("ending_date")
        DecimalFormat dateFmt = new DecimalFormat("00000000")
        String fname = "PerDiemExport-" + dateFmt.format(DateUtils.now()) + "-" + seq++
        File file = FileSystemUtils.createTempFile(fname, ".csv")
        doExport(file, hsu, bdt, edt)
        outjson.put("filename", FileSystemUtils.getHTTPPath(file))
    }

    private static void doExport(File file, HibernateSessionUtil hsu, int bdt, int edt) {
        DelimitedFileWriter dfw = new DelimitedFileWriter(file.getAbsolutePath())

        writeColumnHeaders(dfw)

        spinRecords(dfw, hsu, bdt, edt)

        dfw.close()
    }

    private static void spinRecords(DelimitedFileWriter dfw, HibernateSessionUtil hsu, int bdt, int edt) {
        int today = DateUtils.today()
        HibernateCriteriaUtil<Expense> crit = hsu.createCriteria(Expense.class)
                .ge(Expense.WEEK_PAID_FOR, bdt)
                .le(Expense.WEEK_PAID_FOR, edt)
                .orderBy(Expense.WEEK_PAID_FOR)
                .joinTo(Expense.EMPLOYEE)
                .orderBy(Employee.LNAME).orderBy(Employee.FNAME)
        HibernateScrollUtil<Expense> scr = crit.scroll()
        while (scr.next()) {
            Expense exp = scr.get()
            Employee emp = exp.getEmployee()
            BPerson bper = new BPerson(emp.getPersonId());
            Project prj = exp.getProject()

            BEmployee bemp = new BEmployee(emp)
            String positionName = bemp.getPositionNameOnOrBefore(today)

            dfw.writeField emp.getExtRef()
            dfw.writeField emp.getFname()
            dfw.writeField emp.getLname()
            dfw.writeField prj.getDescription()
            dfw.writeField exp.getSchedulingComments()
            dfw.writeField DateUtils.dateFormat("M/d/yyyy", exp.getDatePaid())
            dfw.writeField DateUtils.dateFormat("M/d/yyyy", exp.getWeekPaidFor())
            switch (exp.getPaymentMethod()) {
                case 'D': dfw.writeField "Direct Deposit";    break;
                case 'M': dfw.writeField "Cash";              break;
                case 'C': dfw.writeField "Check";             break;
                case 'W': dfw.writeField "Walmart";           break;
                case 'O': dfw.writeField "Comdata";           break;
                default:  dfw.writeField "";                  break;
            }
            dfw.writeField bper.getNote("CDID", exp.getEmployeeId())
            dfw.writeField exp.getPerDiemAmount()
            dfw.writeField exp.getExpenseAmount()
            dfw.writeField exp.getHotelAmount()
            dfw.writeField exp.getAdvanceAmount()
            dfw.writeField exp.getPerDiemReturn()
            dfw.writeField exp.getComments()
            dfw.writeField positionName
            dfw.endRecord()
        }
    }

    private static void writeColumnHeaders(DelimitedFileWriter dfw) {
        dfw.writeField "Worker ID"
        dfw.writeField "First Name"
        dfw.writeField "Last Name"
        dfw.writeField "Project Summary"
        dfw.writeField "Scheduling Comments"
        dfw.writeField "Date Paid"
        dfw.writeField "Work Day"
        dfw.writeField "Payment Method"
        dfw.writeField "Comdata ID"
        dfw.writeField "Per Diem"
        dfw.writeField "Expenses"
        dfw.writeField "Hotel"
        dfw.writeField "Advance"
        dfw.writeField "Returned Per Diem"
        dfw.writeField "Payroll Comments"
        dfw.writeField "Position"
        dfw.endRecord()
    }
}
