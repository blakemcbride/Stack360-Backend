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

/*
 * Author: Blake McBride
 * Date: 10/17/20
 */

package com.arahant.services.standard.hr.currentAssignments

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record


class GetAssignments {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String personId = injson.getString("personId")
        Connection db = KissConnection.get()
        List<Record> recs = db.fetchAll("""select p.project_id, p.project_name, p.reference, p.description, p.estimated_first_date, p.estimated_last_date,
                                                  ej.start_date, ps.project_shift_id, ps.shift_start
                                           from project_employee_join ej
                                           join project_shift ps
                                             on ej.project_shift_id = ps.project_shift_id
                                           join project p
                                             on ps.project_id = p.project_id
                                           join project_status s
                                             on p.project_status_id = s.project_status_id
                                           where ej.person_id = ?
                                             and (p.estimated_last_date >= ? or p.estimated_last_date = 0)
                                             and s.active = 'Y'
                                           order by p.estimated_first_date""", personId, DateUtils.today())
        JSONArray allProjects = new JSONArray()
        for (Record rec : recs) {
            JSONObject obj = new JSONObject();
            obj.put("projectId", rec.get("project_id"))
            obj.put("shiftId", rec.get("project_shift_id"))
            obj.put("projectName", rec.getString("project_name"))
            obj.put("extRef", rec.getString("reference"))
            obj.put("summary", rec.getString("description") + " (" + rec.getString("shift_start") + ")")
            obj.put("projFirstDate", rec.getInt("estimated_first_date"))
            obj.put("projLastDate", rec.getInt("estimated_last_date"))
            obj.put("startDate", rec.getInt("start_date"))
            allProjects.put(obj)
        }
        Record rec = db.fetchOne("""select e.employment_type, p.i9_part1
                                    from employee e 
                                    join person p
                                      on e.person_id = p.person_id
                                    where e.person_id = ?""", personId)
        outjson.put("i9_part1", rec.getChar("employment_type") == (char) 'C' ? true : rec.getChar("i9_part1") == (char) 'Y')
        outjson.put("allProjects", allProjects)
    }

}
