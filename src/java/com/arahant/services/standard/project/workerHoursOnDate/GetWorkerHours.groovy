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

package com.arahant.services.standard.project.workerHoursOnDate

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/11/20
 */
class GetWorkerHours {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("projectId")
        int date = injson.getInt("date")
        boolean onlyZero = injson.getBoolean("onlyZero")
        double totalHours = 0
        int workersWithHours = 0
        int workersWithoutHours = 0
        Connection db = KissConnection.get()
        List<Record> recs
        if (onlyZero)
            recs = db.fetchAll("""
                -- Get all records with timesheets with zero hours
                
                (select p.lname, p.fname, p.mname, p.person_id, sum(ts.total_hours) total_hours
                from timesheet ts
                join person p
                  on ts.person_id = p.person_id
                where ts.project_id = ? and ts.beginning_date = ? and ts.total_hours = 0
                group by p.lname, p.fname, p.mname, p.person_id
                order by p.lname, p.fname, p.mname, p.person_id)
                
                UNION
                -- Get all joint to project but with no timesheet records
                
                (select p.lname, p.fname, p.mname, pj.person_id, 0 total_hours
                from project_employee_join pj
                join person p
                  on pj.person_id = p.person_id
                where pj.project_id = ?
                and pj.person_id not in (select ts.person_id from timesheet ts where ts.project_id = ? and  ts.beginning_date = ?))
                
                order by lname, fname, mname
                               """, projectId, date, projectId, projectId, date)
        else
            recs = db.fetchAll("""
                -- Get all records with timesheets
                
                (select p.lname, p.fname, p.mname, p.person_id, sum(ts.total_hours) total_hours
                from timesheet ts
                join person p
                  on ts.person_id = p.person_id
                where ts.project_id = ? and ts.beginning_date = ?
                group by p.lname, p.fname, p.mname, p.person_id
                order by p.lname, p.fname, p.mname, p.person_id)
                
                UNION
                -- Get all joint to project but with no timesheet records
                
                (select p.lname, p.fname, p.mname, pj.person_id, 0 total_hours
                from project_employee_join pj
                join person p
                  on pj.person_id = p.person_id
                where pj.project_id = ?
                and pj.person_id not in (select ts.person_id from timesheet ts where ts.project_id = ? and  ts.beginning_date = ?))
                
                order by lname, fname, mname
                               """, projectId, date, projectId, projectId, date)
        JSONArray ary = new JSONArray()
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            String lname = rec.getString "lname"
            String fname = rec.getString "fname"
            String mname = rec.getString "mname"
            obj.put "personId", rec.getString("person_id")
            String name = lname + ", " + fname
            if (mname != null && !mname.isEmpty())
                name += " " + mname
            obj.put "worker", name
            double hours = rec.getDouble("total_hours")
            obj.put "hours", hours
            totalHours += hours
            if (hours < 0.01)
                workersWithoutHours++
            else
                workersWithHours++
            ary.put obj
        }
        outjson.put "list", ary
        outjson.put "totalHours", totalHours
        outjson.put "workersWithoutHours", workersWithoutHours
        outjson.put "workersWithHours", workersWithHours
    }

}
