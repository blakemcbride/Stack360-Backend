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

package com.arahant.services.standard.hr.workerLastDayExport


import com.arahant.servlets.REST
import com.arahant.utils.*
import org.kissweb.DelimitedFileWriter
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record
import org.json.JSONObject

import java.text.DecimalFormat

/**
 * Author: Blake McBride
 * Date: 8/28/18
 */
class CreateExport {

    private static int seq = 1

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int start_date = injson.getInt "start_date"
        int end_date = injson.getInt "end_date"

        DecimalFormat dateFmt = new DecimalFormat("00000000")
        String fname = "WorkerLastDayExport-" + dateFmt.format(DateUtils.now()) + "-" + seq++
        File file = FileSystemUtils.createTempFile(fname, ".csv")
        doExport file, hsu,
                start_date,
                end_date

        outjson.put("filename", FileSystemUtils.getHTTPPath(file))
    }

    private static void doExport(File file, HibernateSessionUtil hsu,
                                 start_date,
                                 end_date) {
        DelimitedFileWriter dfw = new DelimitedFileWriter(file.getAbsolutePath())

        writeColumnHeaders(dfw)

        spinRecords(dfw, hsu,
                start_date,
                end_date)

        dfw.close()
    }

    private static void spinRecords(DelimitedFileWriter dfw, HibernateSessionUtil hsu,
                                    start_date,
                                    end_date) {
        Connection con = KissConnection.get()
        Command cmd = con.newCommand()
        Cursor c = cmd.query("with pers as ( " +
                "  select distinct person_id " +
                "  from timesheet " +
                "  where end_date >= ? and end_date <= ? " +
                "  and person_id not in ( " +
                "  select distinct person_id " +
                "  from timesheet " +
                "  where end_date > ? " +
                ")), pers_date as ( " +
                "  select distinct person_id, max(end_date) end_date " +
                "  from timesheet " +
                "  where person_id in (select * from pers) " +
                "  group by person_id " +
                "), pers_date_time as ( " +
                "  select distinct t1.person_id, t1.end_date, max(t1.end_time) end_time " +
                "  from timesheet t1 " +
                "  inner join pers_date " +
                "    on pers_date.person_id = t1.person_id and pers_date.end_date = t1.end_date " +
                "  group by t1.person_id, t1.end_date " +
                ") " +
                "select ts.person_id, ts.end_date, ts.end_time, p.lname, p.fname, p.mname, e.ext_ref from timesheet ts " +
                "join pers_date_time pdt " +
                "on ts.person_id = pdt.person_id and ts.end_date = pdt.end_date  " +
                "  and ts.end_time = pdt.end_time " +
                "join person p " +
                "  on ts.person_id = p.person_id " +
                "join employee e " +
                "  on ts.person_id = e.person_id " +
                "order by p.lname, p.fname, p.mname, ts.person_id ",
                (Integer) start_date, (Integer) end_date, (Integer) end_date)
        while (c.isNext()) {
            Record rec = c.getRecord()
            dfw.writeField rec.getString("lname")
            dfw.writeField rec.getString("fname")
            dfw.writeField rec.getString("mname")
            dfw.writeField rec.getString("ext_ref")
            dfw.writeField DateUtils.getDateFormatted(rec.getInt("end_date"))
            dfw.endRecord()
        }
        cmd.close()
    }

    private static void writeColumnHeaders(DelimitedFileWriter dfw) {
        dfw.writeField "Last Name"
        dfw.writeField "First Name"
        dfw.writeField "Middle Name"
        dfw.writeField "Employee ID"
        dfw.writeField "Date Last Worked"
        dfw.endRecord()
    }

}
