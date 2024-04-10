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

package com.arahant.services.standard.hr.workHistory

import com.arahant.business.BPerson
import org.kissweb.StringUtils
import org.kissweb.DateTime
import org.kissweb.DateUtils
import org.kissweb.Groff
import org.kissweb.NumberFormat
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

import java.sql.SQLException

/**
 * Author: Blake McBride
 * Date: 11/7/21
 */
class WorkHistoryReport {

    private static void pageTitle(Groff rpt, Connection db, String employeeId) throws SQLException {
        Record person = db.fetchOne("select lname, mname, fname from person where person_id=?", employeeId);
        rpt.noAutoPageHeader();
        rpt.out(".de TP");
        rpt.out("'SP .5i");
        rpt.out("'tl '''Run date: " + DateTime.currentDateTimeFormatted());
        rpt.out("'tl ''" + bigFont("Worker Work History") + "''");
        rpt.out("'ce 1");
        rpt.out(bigFont(BPerson.getNameLFM(person)))
        rpt.out(".SP");
        rpt.out("..");
    }

    private static void columnTitles(Groff rpt) {
        rpt.startTable "Lw(.5i) Lw(.5i) Lw(.2i) Lw(1i) Rw(.5i) Rw(.5i)"
        rpt.column "Date Worked"
        rpt.column "Project ID"
        rpt.column "Ext Ref"
        rpt.column "Summary"
        rpt.column "Billable Hours"
        rpt.column "Nonbillable Hours"
        rpt.endTitle()
    }

    private static String bigFont(String s) {
        return "\\s(14" + s + "\\s0"
    }

    public static String workerHistoryReport(Connection db, String employeeId, Integer firstDate, Integer lastDate) throws IOException, InterruptedException, SQLException {
        Groff rpt = new Groff("WorkerWorkHistory", null, true)
        rpt.dontDeleteGroffFile()
        pageTitle(rpt, db, employeeId)
        rpt.out(".SP")
        if (firstDate > 0 || lastDate > 0  &&  lastDate < 22000000) {
            if (firstDate > 0)
                rpt.out(bigFont("First date: " + DateUtils.formatLong(firstDate)))
            if (lastDate > 0  &&  lastDate < 22000000)
                rpt.out(bigFont("Last date: " + DateUtils.formatLong(lastDate)))
        } else
            rpt.out(bigFont("All dates"))
        rpt.out(".SP")

        columnTitles(rpt)

        spinDetail(rpt, db, employeeId, firstDate, lastDate)

        return rpt.process(0.5f)
    }

    private static void spinDetail(Groff rpt, Connection db, String employeeId, int firstDate, int lastDate) {
        Command cmd = db.newCommand();
        Cursor cursor = cmd.query("Select p.project_id, p.project_name, p.reference, p.description, " +
                "ts.total_hours, ts.end_date, ts.billable, ps.shift_start " +
                "from timesheet ts " +
                "left join project_shift ps " +
                "  on ts.project_shift_id = ps.project_shift_id " +
                "left join project p " +
                " on ps.project_id = p.project_id " +
                "where person_id = ? and entry_state <> 'R' " +
                "      and ts.end_date >= ? " +
                "      and ts.end_date <= ? " +
                "order by ts.end_date desc, p.project_id", employeeId, firstDate, lastDate);

        String last_project_id = null;
        String last_extRef = null;
        String last_desc = null;
        double last_billable_hours = 0;
        double last_nonbillable_hours = 0;
        int last_date = 0;
        boolean one_more = false;
        while (cursor.isNext()) {
            Record rec = cursor.getRecord();
            String project_id = StringUtils.centerStrip(rec.getString("project_name"));
            String extRef = rec.getString("reference");
            String desc = rec.getString("description")
            String shiftStart = rec.getString("shift_start")
            if (shiftStart != null && !shiftStart.isEmpty())
                desc += " (" + shiftStart + ")"
            double hours = rec.getDouble("total_hours");
            int date = rec.getInt("end_date");
            String billable = rec.getString("billable");
            double billableHours, nonbillableHours;
            if (billable.equals("Y")) {
                billableHours = hours;
                nonbillableHours = 0;
            } else {
                nonbillableHours = hours;
                billableHours = 0;
            }
            if (last_project_id != null  &&  Objects.equals(project_id, last_project_id)  &&  date == last_date) {
                last_billable_hours += billableHours;
                last_nonbillable_hours += nonbillableHours;
            } else {
                if (last_project_id != null && (last_billable_hours > 0.009 || last_nonbillable_hours > 0.009)) {
                    rpt.column DateUtils.format4(last_date)
                    rpt.column last_project_id
                    rpt.column last_extRef
                    rpt.column last_desc
                    rpt.column NumberFormat.Format(last_billable_hours, "", 0, 2)
                    rpt.column NumberFormat.Format(last_nonbillable_hours, "", 0, 2)
                }
                last_project_id = project_id;
                last_extRef = extRef;
                last_desc = desc;
                last_billable_hours = billableHours;
                last_nonbillable_hours = nonbillableHours;
                last_date = date;
                one_more = true;
            }
        }
        if (one_more && last_billable_hours > 0.009) {
            rpt.column DateUtils.format4(last_date)
            rpt.column last_project_id
            rpt.column last_extRef
            rpt.column last_desc
            rpt.column NumberFormat.Format(last_billable_hours, "", 0, 2)
            rpt.column NumberFormat.Format(last_nonbillable_hours, "", 0, 2)
        }
    }

}
