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

package com.arahant.services.standard.project.assignmentHistory

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.TimeUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/3/20
 */
class ListAssignmentHistory {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        String worker = injson.getString("worker")
        String editor = injson.getString("editor")
        int max = 100, n = 0
        Connection db = KissConnection.get()
        boolean needWhere = true
        boolean needAnd = false
        ArrayList<String> args = new ArrayList<>()

        JSONArray rows = new JSONArray()
        Command cmd = db.newCommand()
        // c=client, s=supervisor, p=person/worker
        String sql = """select peh.change_date, peh.change_time, peh.change_type,
                                            pj.project_name, pj.description,
                                            c.group_name client,
                                            p.fname w_fname, p.mname w_mname, p.lname w_lname,
                                            s.fname s_fname, s.mname s_mname, s.lname s_lname
                         from project_employee_history peh
                         join person p
                           on peh.person_id = p.person_id
                         join project_shift ps
                           on peh.project_shift_id = ps.project_shift_id
                         join project pj
                           on ps.project_id = pj.project_id
                         join person s
                           on peh.change_person_id = s.person_id
                         join org_group c
                           on pj.requesting_org_group = c.org_group_id
                                     """

        if (project_id != null && !project_id.isEmpty()) {
            if (needWhere) {
                sql += " where"
                needWhere = false
            }
            sql += " pj.project_id = ?"
            args.add(project_id)
            needAnd = true
        }
        if (worker != null && !worker.isEmpty()) {
            if (needWhere) {
                sql += " where"
                needWhere = false
            } else if (needAnd)
                sql += " and"
            sql += " p.person_id = ?"
            args.add(worker)
            needAnd = true
        }
        if (editor != null && !editor.isEmpty()) {
            if (needWhere) {
                sql += " where"
                needWhere = false
            } else if (needAnd)
                sql += " and"
            sql += " s.person_id = ?"
            args.add(editor)
            needAnd = true
        }
        sql += " order by peh.change_date desc, peh.change_time desc"
        Cursor c = cmd.query(max+1, sql, args)
        while (c.isNext() && ++n <= max) {
            Record rec = c.getRecord()
            JSONObject obj = new JSONObject()
            obj.put "ProjectId", rec.getString("project_name").trim()
            obj.put "ProjectDescription", rec.getString("description")
            String mname = rec.get("w_mname")
            mname = mname == null ? "" : mname
            String name = rec.getString("w_lname") + ", " + rec.getString("w_fname") + " " + mname
            obj.put "worker", name
            String type = rec.getString("change_type")
            obj.put "type", type == "A" ? "Assign" : "De-assign"
            obj.put "date", DateUtils.format4(rec.getInt("change_date"))
            obj.put "time", TimeUtils.formatAMPM(rec.getInt("change_time"))
            mname = rec.get("s_mname")
            mname = mname == null ? "" : mname
            name = rec.getString("s_lname") + ", " + rec.getString("s_fname") + " " + mname
            obj.put "supervisor", name
            rows.put obj
        }
        outjson.put("list", rows)
        outjson.put("overmax", n > max)
    }

}
