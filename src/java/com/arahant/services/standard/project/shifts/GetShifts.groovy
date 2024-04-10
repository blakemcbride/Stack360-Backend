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

package com.arahant.services.standard.project.shifts

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.TimeUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/29/22
 */
class GetShifts {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString('projectId')
        Connection db = hsu.getKissConnection()
        List<Record> recs = db.fetchAll("""
             select project_shift_id, required_workers, shift_start, description
             from project_shift 
             where project_id = ?
             order by shift_start""", projectId)
        JSONArray shifts = new JSONArray()
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            obj.put("project_shift_id", rec.getString("project_shift_id"))
            obj.put("required_workers", rec.getShort("required_workers"))
            obj.put("shift_start", TimeUtils.parse(rec.getString("shift_start")))
            obj.put("description", rec.getString("description"))
            shifts.put(obj)
        }
        outjson.put("shifts", shifts)
    }

}
