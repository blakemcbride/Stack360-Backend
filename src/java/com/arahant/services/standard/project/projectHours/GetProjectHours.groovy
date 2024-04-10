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

package com.arahant.services.standard.project.projectHours

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/5/20
 */
class GetProjectHours {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int date = injson.getInt("date")
        boolean onlyZero = injson.getBoolean("onlyZero")
        int totalWorkers = 0
        double totalHours = 0
        int zeroHourProjects = 0

        Connection db = KissConnection.get()
        List<Record> recs
        if (onlyZero)
            recs = db.fetchAll("""
                                SELECT 
                                    p.project_id,
                                    p.description, p.project_name,
                                    COUNT(DISTINCT ts.person_id) AS number_of_workers,
                                    COALESCE(SUM(ts.total_hours), 0) AS total_hours
                                FROM project p
                                JOIN project_status s
                                  ON p.project_status_id = s.project_status_id
                                LEFT JOIN project_shift ps 
                                  ON p.project_id = ps.project_id
                                LEFT JOIN timesheet ts 
                                  ON ps.project_shift_id = ts.project_shift_id AND ts.beginning_date = ?
                                WHERE s.active = 'Y' and (total_hours = 0 or total_hours is null)
                                GROUP BY p.project_id, p.description
                                ORDER by p.project_id
                           """, date)
        else
            recs = db.fetchAll("""
                                SELECT 
                                    p.project_id,
                                    p.description, p.project_name,
                                    COUNT(DISTINCT ts.person_id) AS number_of_workers,
                                    COALESCE(SUM(ts.total_hours), 0) AS total_hours
                                FROM project p
                                JOIN project_status s
                                  ON p.project_status_id = s.project_status_id
                                LEFT JOIN project_shift ps 
                                  ON p.project_id = ps.project_id
                                LEFT JOIN timesheet ts 
                                  ON ps.project_shift_id = ts.project_shift_id AND ts.beginning_date = ?
                                WHERE s.active = 'Y'
                                GROUP BY p.project_id, p.description
                                ORDER by p.project_id
                           """, date)
        JSONArray ary = new JSONArray()
        for (Record rec in recs) {
            Double hours = rec.getDouble("total_hours")
            Long workers = rec.getLong("number_of_workers")
            if (hours == null)
                hours = 0
            if (workers == null)
                workers = 0
            JSONObject obj = new JSONObject()
            obj.put "project_id", rec.getString("project_id")
            obj.put "id", rec.getString("project_name")
            obj.put "summary", rec.getString("description")
            obj.put "hours", hours
            obj.put "workers", workers
            ary.put obj

            totalHours += hours
            totalWorkers += workers
            if (hours == 0)
                zeroHourProjects++
        }
        outjson.put "list", ary
        outjson.put "totalHours", totalHours
        outjson.put "totalWorkers", totalWorkers
        outjson.put "zeroHourProjects", zeroHourProjects
    }

}

