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

package com.arahant.services.standard.billing.perDiemException

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/31/20
 */
class GetWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("project_id")
        Connection db = KissConnection.get()
        List<Record> workers = db.fetchAll("""
                               select p.person_id, p.fname, p.mname, p.lname, pos.position_name
                               from person p
                               inner join project_employee_join ej
                                 on p.person_id = ej.person_id
                               join (
                                   -- Create an hr_wage table that only includes their current status as
                                   -- of a specific date - a single record for each employee.
                                   select t4.* from hr_wage t4
                                   inner join
                                       (select distinct t2.employee_id, t2.effective_date, t2.wage_id
                                        from hr_wage t2
                                        inner join
                                            (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                             from hr_wage t0
                                             where t0.effective_date <= ?
                                             group by t0.employee_id) t1
                                               on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date) t3
                                             on t4.wage_id = t3.wage_id
                               ) w
                                 on p.person_id = w.employee_id
                               join hr_position pos
                                 on w.position_id = pos.position_id
                               where ej.project_id = ?
                               and p.person_id not in (
                                   select ex.person_id from per_diem_exception ex
                                   where ex.project_id = ?
                               )
                               order by p.lname, p.fname, p.mname
                               """, DateUtils.today(), projectId, projectId)
        JSONArray wa = new JSONArray()
        for (Record worker : workers) {
            JSONObject w = new JSONObject()
            w.put("person_id", worker.getString("person_id"))
            w.put("fname", worker.getString("fname"))
            w.put("lname", worker.getString("lname"))
            w.put("mname", worker.getString("mname"))
            w.put("position", worker.getString("position_name"))
            wa.put(w)
        }
        outjson.put("workers", wa)
    }

}
