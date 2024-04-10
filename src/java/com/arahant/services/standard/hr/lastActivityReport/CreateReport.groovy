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

package com.arahant.services.standard.hr.lastActivityReport

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.Groff
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 7/31/21
 */
class CreateReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateReport().build(hsu, injson.getString("sort"))
        outjson.put("filename", fname)
    }

    private static String build(HibernateSessionUtil hsu, String sort) {
        Groff rpt = new Groff("LastActivityReport-", "Last Activity Report", true)
        report_title(rpt, sort)
        start_table(rpt)
        Connection db = KissConnection.get()
        int today = DateUtils.today()

        String select = """
                with last_timesheet as (
                  select person_id, MAX(end_date) end_date
                  from timesheet
                  where billable = 'Y' and total_hours > .01
                  group by person_id
                )
                
                select p.person_id, p.lname, p.mname, p.fname, ts.end_date
                from employee e
                join person p
                  on e.person_id = p.person_id
                join current_employee_status es
                  on e.person_id = es.employee_id
                join hr_employee_status hes
                  on es.status_id = hes.status_id
                left join last_timesheet ts
                  on p.person_id = ts.person_id
                where hes.active = 'Y'
                        """
        if (sort == "N")
            select += "order by p.lname, p.fname, p.mname"
        else
            select += "order by case when ts.end_date is null then 1 else 0 end, ts.end_date desc, p.lname, p.fname, p.mname"

        List<Record> recs = db.fetchAll(select)
        for (Record rec : recs) {
            String name = rec.getString("lname") + ", " + rec.getString("fname")
            String mname = rec.getString("mname")
            if (mname?.isEmpty())
                name += " " + mname
            Integer dt = rec.getInt("end_date")
            if (dt == null)
                dt = 0

            String personId = rec.getString("person_id")

            Record r2 = db.fetchOne("""
                        select p.description
                        from timesheet ts
                        join project_shift ps
                          on ts.project_shift_id = ps.project_shift_id
                        join project p
                          on ps.project_id = p.project_id
                        where ts.person_id = ?
                          and ts.billable = 'Y' and ts.total_hours > .01
                        order by end_date desc
                        """, personId)

            String desc = r2?.getString("description")
            if (desc == null)
                desc = ""

            r2 = db.fetchOne("""
                            select
                            from project_employee_join pej
                            join project_shift psh
                              on pej.project_shift_id = psh.project_shift_id
                            join project p
                              on psh.project_id = p.project_id
                            join project_status ps
                              on p.project_status_id = ps.project_status_id
                            where pej.person_id = ?
                                  and ps.active = 'Y'
                                  and p.estimated_last_date < ?
                             """, personId, today)


            rpt.column name
            rpt.column DateUtils.format4(dt)
            rpt.column desc
            rpt.column r2 ? "Yes" : ""
        }
        return rpt.process(0.5f)
    }

    private static void report_title(Groff rpt, String sort) {
        rpt.out(".SP")
        if (sort == "N")
            rpt.out("Sort by Name")
        else
            rpt.out("Sort by Last date worked")
        rpt.out(".SP")
    }

    private static void start_table(Groff rpt) {
        rpt.startTable "L C L C"
        rpt.column "Worker"
        rpt.column "Last Date Worked"
        rpt.column "Last Project Worked"
        rpt.column "Currently Assigned"
        rpt.endTitle()
    }

}
