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

package com.arahant.services.standard.project.workerAssignmentReport

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.StringUtils
import org.kissweb.DateUtils
import org.kissweb.Groff
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.json.JSONObject
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 2/9/20
 */
class CreateReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateReport().build(hsu)
        outjson.put("filename", fname)
    }

    private String build(HibernateSessionUtil hsu) {
        Groff rpt = new Groff("WorkerAssignment-", "Worker Assignment Report", true)
        int workDate = DateUtils.today()
        workDate = DateUtils.addDays(workDate, -1)
        page_title rpt,workDate
        Connection con = KissConnection.get()
        Command cmd = con.newCommand()
        Command cmd2 = con.newCommand()
        Cursor c = cmd.query(
                "select p.project_id, p.project_name, p.estimated_first_date, p.estimated_last_date, og.group_name, p.description, " +
                        "  per.lname, per.fname, per.mname, " +
                        "  psh.required_workers, psh.project_shift_id, per.person_id " +
                        "from project p " +
                        "join project_shift psh " +
                        "  on p.project_id = psh.project_id " +
                        "join project_status ps " +
                        "  on p.project_status_id = ps.project_status_id " +
                        "join org_group og " +
                        "  on p.requesting_org_group = og.org_group_id " +
                        "left outer join project_employee_join ej " +
                        "  on psh.project_shift_id = ej.project_shift_id " +
                        "left outer join person per " +
                        "  on ej.person_id = per.person_id " +
                        "where ps.active = 'Y' " +
                        "order by og.group_name, p.description ")
        String lastProject = ""
        while (c.isNext()) {
            String projectId = c.getString "project_id"
            String projectShiftId = c.getString "project_shift_id"
            if (lastProject != projectId) {

                Record assigned = cmd2.fetchOne("select count(*) from project_employee_join where project_shift_id=?", projectShiftId)
                Record timeSheets = cmd2.fetchOne("select count(*) from timesheet where project_shift_id=? and beginning_date=?", projectShiftId, workDate)

                rpt.out StringUtils.take(c.getString("group_name"), 25) + "~" + c.getString("project_name") + "~" +
                        StringUtils.take(c.getString("description"), 40) + "~" +
                        DateUtils.format2(c.getInt("estimated_first_date")) + "~" + DateUtils.format2(c.getInt("estimated_last_date")) +
                        "~" + c.getShort("required_workers") + "~" + assigned.getLong("count") + "~" + timeSheets.getLong("count")
                lastProject = projectId
                String lname = c.getString("lname")
                if (lname != null && !lname.isEmpty())
                    printPerson cmd2, c, workDate, rpt
            } else
                printPerson cmd2, c, workDate, rpt
        }

        rpt.out(".TE");
        return rpt.process(0.5f)
    }

    private static void printPerson(Command cmd2, Cursor c, int workDate, Groff rpt) {
        String projectId = c.getString "project_id"
        String shiftId = c.getString "project_shift_id"
        String fname = c.getString("fname")
        String lname = c.getString("lname")
        String mname = c.getString("mname")
        String personId = c.getString("person_id")
        if (fname == null)
            fname = ""
        if (lname == null)
            lname = ""
        if (mname == null)
            mname = ""
        String name = lname
        if (name.length() > 0 && (fname.length() > 0 || mname.length() > 0))
            name += ", "
        name += fname + ' ' + mname
        Record timeSheets = cmd2.fetchOne("select count(*) from timesheet where project_shift_id=? and beginning_date=? and person_id=?", shiftId, workDate, personId)
        boolean showed = timeSheets.getLong("count") != 0
        if (showed)
            rpt.out "~~\\ \\ \\ \\ \\ " + name
        else
            rpt.out "~~\\ \\ \\ \\ \\ " + name + " (no show)"
    }

    private static void page_title(Groff rpt, int dt) {
        String cols
        rpt.out(".SP 2")
        rpt.out(".TS H")
        rpt.out("tab(~);")
        rpt.out("L L L R R C C C.");
        cols = "~~~~~~~Showed"
        rpt.out(cols)
        cols = "Client~Project ID~Description / Worker Name~First Date~Last Date~Required~Assigned~" + DateUtils.format2(dt)
        rpt.out(cols)
        rpt.out("\\_~\\_~\\_~\\_~\\_~\\_~\\_~\\_")
        rpt.out(".TH")
    }

}
