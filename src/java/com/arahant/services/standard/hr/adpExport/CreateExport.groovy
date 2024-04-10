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

package com.arahant.services.standard.hr.adpExport


import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.KissConnection
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.Utils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record
import org.json.JSONObject

import java.text.DecimalFormat

/**
 * Author: Blake McBride
 * Date: 4/28/19
 */
class CreateExport {

    private static int seq = 1
    private static final String FFCRA = "00001-0000003334"  //  project_id

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int start_date = injson.getInt "beginning_period"
        int end_date = injson.getInt "ending_period"

        DecimalFormat dateFmt = new DecimalFormat("00000000")
        String fname = "ADPExport-" + dateFmt.format(DateUtils.now()) + "-" + seq++
        File file = FileSystemUtils.createTempFile(fname, ".csv")
        doExport file, hsu,
                start_date,
                end_date

        outjson.put("filename", FileSystemUtils.getHTTPPath(file))
    }

    private static void doExport(File file, HibernateSessionUtil hsu, start_date, end_date) {
        DelimitedFileWriter dfw = new DelimitedFileWriter(file.getAbsolutePath())

        writeColumnHeaders dfw

        spinRecords dfw, hsu, start_date, end_date

        writeFileTrailer dfw
        dfw.close()
    }

    private static void spinRecords(DelimitedFileWriter dfw, HibernateSessionUtil hsu, start_date, end_date) {
        double previousHours = 0
        Connection con = KissConnection.get()
        Command cmd = con.newCommand()
        Command wcmd = con.newCommand()
        String ADPCode = BProperty.get(StandardProperty.ADP_COMPANY_CODE)
        Cursor c = cmd.query("""
            select t.person_id, e.ext_ref, p.lname, p.fname, p.mname, t.beginning_date, t.total_hours, proj.project_state
            from timesheet t
            join person p
             on t.person_id = p.person_id
            join employee e
             on t.person_id = e.person_id
            join project_shift ps
              on t.project_shift_id = ps.project_shift_id
            join project proj
             on ps.project_id = proj.project_id
            where t.beginning_date >= ? and t.beginning_date <= ? and e.employment_type = 'E' and 
                  proj.project_id <> ? 
            order by p.lname, p.fname, p.mname, t.person_id, proj.project_state, t.beginning_date
            """,
                (Integer) start_date, (Integer) end_date, FFCRA)
        Record prec = null
        while (c.isNext()) {
            Record rec = c.getRecord()

            double new_rate = getRate(wcmd, rec.getString("person_id"), rec.getInt("beginning_date"))
            if (new_rate < 0.0)
                continue // don't include salary people
            if (prec == null) {
                prec = rec;
                prec.set("rate", getRate(wcmd, rec.getString("person_id"), rec.getInt("beginning_date")))
                previousHours = 0
                continue
            }
            double old_rate = prec.getDouble("rate")
            boolean same_rate = Math.abs(new_rate - old_rate) < 0.01
            boolean samePerson = rec.getString("person_id") == prec.getString("person_id")
            if (samePerson && same_rate && rec.getString("project_state") == prec.getString("project_state")) {
                prec.set("total_hours", prec.getDouble("total_hours") + rec.getDouble("total_hours"))
                continue
            }
            printRecord(prec, dfw, wcmd, ADPCode, previousHours)
            if (samePerson)
                previousHours += prec.getDouble("total_hours")
            else
                previousHours = 0
            prec = rec
            prec.set("rate", getRate(wcmd, rec.getString("person_id"), rec.getInt("beginning_date")))
        }
        printRecord(prec, dfw, wcmd, ADPCode, 0)
        cmd.close()
        wcmd.close()
    }

    private static void printRecord(Record prec, DelimitedFileWriter dfw, Command wcmd, String ADPCode, double previous_hours) {
        if (prec == null)
            return;
        double rate = getRate(wcmd, prec.getString("person_id"), prec.getInt("beginning_date"))
        String srate = Utils.Format(rate, "", 0, 2)
        double hours = prec.getDouble("total_hours")
        double maxHours = 40 - previous_hours
        if (maxHours < 0)
            maxHours = 0
        double rhours = hours > maxHours ? maxHours : hours
        double ohours = hours > maxHours ? hours - maxHours : 0.0

        if (rhours > 0.01  ||  ohours > 0.01) {
            dfw.writeField ADPCode
            dfw.writeField "Batch002"
            dfw.writeField prec.getString("ext_ref")
            dfw.writeField "Hourly PD"
            dfw.writeField prec.getString("fname") + " " + prec.getString("lname")
            dfw.writeField srate
            dfw.writeField srate
            dfw.writeField prec.getString("project_state")
            dfw.writeField rhours
            dfw.writeField ohours
            dfw.writeField ""
            dfw.endRecord()
        }
    }

    private static double getRate(Command cmd, String person_id, int dt) {
        Record r = cmd.fetchOne("""
            select w.wage_amount, wt.period_type from hr_wage w 
            join wage_type wt 
              on w.wage_type_id = wt.wage_type_id 
            where w.effective_date <= ? and w.employee_id = ? 
            order by w.effective_date desc
            """, (Integer) dt, person_id)
        if (r == null) {
            r = cmd.fetchOne("""
            select w.wage_amount, wt.period_type from hr_wage w 
            join wage_type wt 
              on w.wage_type_id = wt.wage_type_id
            where w.employee_id = ? 
            order by w.effective_date
            """, person_id)
        }
        if (r == null)
            return 0;
        int periodType = r.getShort("period_type")
        if (periodType == 2)
            return -1
        return r.getDouble("wage_amount")
    }

    private static void writeColumnHeaders(DelimitedFileWriter dfw) {
        dfw.writeField "!Do not change rows and column headers preceded by \"!\".  Only employee data can be modified."
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!CO CODE"
        dfw.writeField "BATCH ID"
        dfw.writeField "FILE #"
        dfw.writeField "BATCH DESCRIPTION"
        dfw.writeField "Name"
        dfw.writeField "Rate"
        dfw.writeField "Temporary Rate"
        dfw.writeField "Temp State Worked"
        dfw.writeField "Regular Hours"
        dfw.writeField "Overtime Hours"
        dfw.writeField "Bonus Earnings"
        dfw.endRecord()

        dfw.writeField "!"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()
    }

    private static void writeFileTrailer(DelimitedFileWriter dfw) {
        dfw.writeField "!"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column1"
        dfw.writeField "CO CODE"
        dfw.writeField ""
        dfw.writeField "CO CODE"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column2"
        dfw.writeField "BATCH ID"
        dfw.writeField ""
        dfw.writeField "BATCH ID"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column3"
        dfw.writeField "FILE #"
        dfw.writeField ""
        dfw.writeField "FILE #"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "LOCKED"
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column4"
        dfw.writeField "BATCH DESCRIPTION"
        dfw.writeField ""
        dfw.writeField "BATCH DESCRIPTION"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column5"
        dfw.writeField "Name"
        dfw.writeField ""
        dfw.writeField "Employee Name"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column6"
        dfw.writeField "Rate"
        dfw.writeField ""
        dfw.writeField "Rate"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column7"
        dfw.writeField "Temporary Rate"
        dfw.writeField ""
        dfw.writeField "Temp Rate"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column8"
        dfw.writeField "Temp State Worked In Tax Code"
        dfw.writeField ""
        dfw.writeField "Temp State Code"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column9"
        dfw.writeField "Regular Hours"
        dfw.writeField ""
        dfw.writeField "Reg Hours"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column10"
        dfw.writeField "Overtime Hours"
        dfw.writeField ""
        dfw.writeField "O/T Hours"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "!Column11"
        dfw.writeField "Bonus Earnings"
        dfw.writeField ""
        dfw.writeField "Earnings 4 Code"
        dfw.writeField "Earnings 4 Amount"
        dfw.writeField "B"
        dfw.writeField ""
        dfw.writeField "80"
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()
    }

}
