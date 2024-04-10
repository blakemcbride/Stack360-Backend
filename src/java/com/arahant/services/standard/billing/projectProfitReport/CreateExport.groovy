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

package com.arahant.services.standard.billing.projectProfitReport

import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DelimitedFileWriter
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

import java.util.concurrent.TimeUnit

/**
 * Author: Blake McBride
 * Date: 4/27/19
 */
class CreateExport {
    private final static double workers_comp_payroll_taxes = 0.15d
    private final static double non_billable_wage = 7.25d

    private static class OvertimeInfo {
        String person_id
        String project_id
        char billable  //  Y or N
        double overtimeDollars

        OvertimeInfo(String person_id, String project_id, char billable) {
            this.person_id = person_id
            this.project_id = project_id
            this.billable = billable
        }
    }

    private final ArrayList<OvertimeInfo> overtimeInfo = new ArrayList<>()
    private double wage_hourly, wage_non_billable, wage_taxes
    private double perDiem, expense, hotel

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateExport().build(hsu, injson.getInt("beginning_date"), injson.getInt("ending_date"))
        outjson.put("filename", fname)
    }

    private String build(HibernateSessionUtil hsu, int begdate, int enddate) {
        overtimeInfo.clear()
        File csvFile = FileSystemUtils.createTempFile("ProjectIncomeExport-" + begdate + "-", ".csv")
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        writeColumnHeader dfw, begdate, enddate
        Connection con = KissConnection.get()
        Command project_cmd = con.newCommand()
        Command cmd = con.newCommand()
        Command cmd2 = con.newCommand()
        calcOvertimeInfo(cmd, cmd2, begdate, enddate)
        Cursor pc = project_cmd.query("""
            select og.group_name, p.project_id, p.description, p.estimated_first_date, p.estimated_last_date
            from project p
            join org_group og
              on p.requesting_org_group = og.org_group_id
              where p.project_id in 
                  (select distinct p2.project_id
                   from project p2
                   join project_shift ps
                     on p2.project_id = ps.project_id
                   inner join timesheet t
                     on  ps.project_shift_id = t.project_shift_id
                   where t.beginning_date >= ?
                         and t.beginning_date <= ?)
                and (og.org_group_type = 2 or og.org_group_type = 4)
            order by og.group_name, p.description
            """, begdate, enddate)
        while (pc.isNext()) {
            wage_hourly = wage_non_billable = wage_taxes =  perDiem = expense = hotel = 0.0d
            String project_id = pc.getString("project_id")
            double overtimeDollars = getOvertimeDollars(project_id)
            double invoice = invoice_total(cmd, project_id, begdate, enddate)
            wage_total(cmd, cmd2, project_id, begdate, enddate)
            expense_total(cmd, project_id, begdate, enddate)
            dfw.writeField pc.getString("group_name")
            dfw.writeField pc.getString("description")
            dfw.writeField invoice
            dfw.writeField wage_hourly
            dfw.writeField overtimeDollars
            dfw.writeField wage_non_billable
            dfw.writeField wage_taxes
            dfw.writeField perDiem
            dfw.writeField expense
            dfw.writeField hotel
            dfw.writeField((double)(invoice - wage_hourly - overtimeDollars - wage_non_billable - wage_taxes - perDiem - expense - hotel))
            dfw.endRecord()
        }
        cmd.close()
        cmd2.close()
        project_cmd.close()
        dfw.close()
        return FileSystemUtils.getHTTPPath(csvFile);
    }

    private static double invoice_total(Command cmd, String project_id, int begdate, int enddate) {
        Cursor c = cmd.query """
            select adj_hours, adj_rate, amount
            from invoice_line_item
            join invoice 
              on invoice.invoice_id = invoice_line_item.invoice_id
            where invoice_line_item_id in (
              select invoice_line_item_id
              from timesheet ts
              join project_shift ps
                on ts.project_shift_id = ps.project_shift_id
              where ps.project_id = ? and ts.billable = 'Y')
                    and create_date >= ? and create_date < ?
            """, project_id, org.kissweb.DateUtils.toDate(begdate), org.kissweb.DateUtils.toDate(org.kissweb.DateUtils.addDays(enddate, 1))
        double total = 0
        while (c.isNext()) {
            Record rec = c.getRecord()
            total += rec.getFloat("adj_hours") * rec.getDouble("adj_rate") + rec.getDouble("amount")
        }
        return total
    }

    private void wage_total(Command cmd, Command cmd2, String project_id, int begdate, int enddate) {
        Cursor c = cmd.query """
                                select ts.person_id, ts.beginning_date, ts.total_hours, ts.total_expenses, ts.fixed_pay, billable
                                from timesheet ts
                                join project_shift ps
                                  on ts.project_shift_id = ps.project_shift_id
                                where ps.project_id = ?
                                      and ts.beginning_date >= ? and ts.end_date <= ?
                                """, project_id, begdate, enddate
        while (c.isNext()) {
            Record rec = c.getRecord()
            String person_id = rec.getString("person_id")
            Record warec = cmd2.fetchOne("""
                                                select w.wage_amount, wt.period_type
                                                from hr_wage w
                                                join wage_type wt
                                                  on w.wage_type_id = wt.wage_type_id
                                                where w.employee_id=? and w.effective_date <= ?
                                                order by w.effective_date desc""", person_id, rec.getInt("beginning_date"))
            if (warec == null || warec.getInt("period_type") == 2) {
                // salaried or unknown wage person
                if (rec.getChar("billable") == 'N' as char) {
                    wage_non_billable += rec.getFloat("total_expenses") + rec.getDouble("fixed_pay")
                } else {
                    wage_hourly += rec.getFloat("total_expenses") + rec.getDouble("fixed_pay")
                }
            } else if (rec.getChar("billable") == 'N' as char) {
                wage_non_billable += rec.getDouble("total_hours") * non_billable_wage + rec.getFloat("total_expenses") + rec.getDouble("fixed_pay")
                wage_taxes += rec.getDouble("total_hours") * non_billable_wage * workers_comp_payroll_taxes
            } else {
                wage_hourly += rec.getDouble("total_hours") * warec.getDouble("wage_amount") + rec.getFloat("total_expenses") + rec.getDouble("fixed_pay")
                wage_taxes += rec.getDouble("total_hours") * warec.getDouble("wage_amount") * workers_comp_payroll_taxes
            }
        }
    }

    private void calcOvertimeInfo(Command cmd, Command cmd2, int startDate, int endDate) {
        overtimeInfo.clear()
        // Convert integers to Calendar dates
        Calendar startCalendar = convertIntToCalendar(startDate)
        Calendar endCalendar = convertIntToCalendar(endDate)

        // Adjust dates
        adjustStartDate(startCalendar)
        adjustEndDate(endCalendar)

        // Loop through each 7-day period
        while (startCalendar.before(endCalendar)) {
            Calendar weekEndCalendar = (Calendar) startCalendar.clone()
            weekEndCalendar.add(Calendar.DAY_OF_MONTH, 6) // Add 6 days to get the end of the week

            // Make sure we don't go beyond the end date
            if (weekEndCalendar.after(endCalendar))
                weekEndCalendar = (Calendar) endCalendar.clone()

            //System.out.println("Week Start Date: " + convertCalendarToInt(startCalendar) + ", Week End Date: " + convertCalendarToInt(weekEndCalendar))

            startDate = convertCalendarToInt(startCalendar)
            endDate = convertCalendarToInt(weekEndCalendar)

            // build the overtime structure
            List<Record> precs = cmd.fetchAll("""select distinct person_id from timesheet
                                                where beginning_date >= ? and end_date <= ?""", startDate, endDate)
            List<Record> recs = cmd.fetchAll("""select ts.person_id, ps.project_id, ts.total_hours, ts.billable
                                                from timesheet ts
                                                join project_shift ps
                                                  on ts.project_shift_id = ps.project_shift_id
                                                where ts.beginning_date >= ? and ts.end_date <= ?
                                                order by ts.beginning_date, ts.beginning_time""", startDate, endDate)
            for (Record prec : precs) {
                String rec_person_id = prec.getString("person_id")
                double total_regular_hours = 0
                for (Record r : recs) {
                    if (r.getString("person_id").equals(rec_person_id)) {
                        double current_hours = r.getDouble("total_hours")
                        if (current_hours + total_regular_hours > 40) {
                            double overtime = (total_regular_hours + current_hours) - 40
                            total_regular_hours = 40
                            char billable = r.getChar("billable")
                            OvertimeInfo oti = findOvertimeRecord(rec_person_id, r.getString("project_id"), billable)
                            if (billable == (char) 'Y') {
                                Record wrec = cmd2.fetchOne("""
                                                select w.wage_amount, wt.period_type
                                                from hr_wage w
                                                join wage_type wt
                                                  on w.wage_type_id = wt.wage_type_id
                                                where w.employee_id=? and w.effective_date <= ?
                                                order by w.effective_date desc""", rec_person_id, endDate)
                                if (wrec != null)
                                    oti.overtimeDollars += overtime * wrec.getDouble("wage_amount") * 0.5d
                                else
                                    oti.overtimeDollars += overtime * non_billable_wage * 0.5d
                            } else
                                oti.overtimeDollars += overtime * non_billable_wage * 0.5d
                        } else
                            total_regular_hours += current_hours
                    }
                }
            }

            // Prepare for the next iteration
            startCalendar.add(Calendar.DAY_OF_MONTH, 7)
        }
    }

    private void expense_total(Command cmd, String project_id, int begdate, int enddate) {
        Cursor c = cmd.query """
            select per_diem_amount, expense_amount, hotel_amount
            from expense
            where project_id = ?
                  and date_paid >= ? and date_paid <= ?
            """, project_id, begdate, enddate
        while (c.isNext()) {
            Record rec = c.getRecord()
            perDiem += rec.getFloat("per_diem_amount")
            expense += rec.getFloat("expense_amount")
            hotel += rec.getFloat("hotel_amount")
        }
    }

    private static void writeColumnHeader(DelimitedFileWriter dfw, int begdate, int enddate) {
        dfw.writeField("Active projects between:  " + DateUtils.getDateFormatted(begdate) + " and " + DateUtils.getDateFormatted(enddate))
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()
        dfw.writeField "Client"
        dfw.writeField "Project"
        dfw.writeField "Invoice Total"
        dfw.writeField "Hourly"
        dfw.writeField "Overtime"
        dfw.writeField "Non-billable"
        dfw.writeField "Taxes"
        dfw.writeField "Per Diem"
        dfw.writeField "Expense"
        dfw.writeField "Hotel"
        dfw.writeField "Net Income"
        dfw.endRecord()
    }

    private static Calendar convertIntToCalendar(int date) {
        int year = date / 10000
        int month = (date % 10000) / 100 - 1 // Calendar month is 0-based
        int day = date % 100

        Calendar calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar
    }

    private static int convertCalendarToInt(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR)
        int month = calendar.get(Calendar.MONTH) + 1 // Convert back to 1-based
        int day = calendar.get(Calendar.DAY_OF_MONTH)
        return year * 10000 + month * 100 + day
    }

    private static void adjustStartDate(Calendar calendar) {
        // Calendar.SATURDAY == 7
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
            calendar.add(Calendar.DAY_OF_MONTH, -1)
    }

    private static void adjustEndDate(Calendar calendar) {
        // Calendar.FRIDAY == 6
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    private static long calculateWeeksBetween(Calendar startCalendar, Calendar endCalendar) {
        long startMillis = startCalendar.getTimeInMillis()
        long endMillis = endCalendar.getTimeInMillis()
        long diff = endMillis - startMillis
        return TimeUnit.MILLISECONDS.toDays(diff) / 7
    }

    private OvertimeInfo findOvertimeRecord(String person_id, String project_id, char billable) {
        for (OvertimeInfo info : overtimeInfo) {
            if (info.person_id.equals(person_id) && info.project_id.equals(project_id) && billable == info.billable)
                return info
        }
        OvertimeInfo oti = new OvertimeInfo(person_id, project_id, billable)
        overtimeInfo.add(oti)
        return oti
    }

    private double getOvertimeDollars(String project_id) {
        double dollars = 0d
        for (OvertimeInfo info : overtimeInfo)
            if (info.project_id.equals(project_id))
                dollars += info.overtimeDollars
        return dollars
    }

}
