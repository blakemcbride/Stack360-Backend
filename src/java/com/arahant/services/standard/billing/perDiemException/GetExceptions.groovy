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
import org.kissweb.NumberFormat
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/30/20
 */
class GetExceptions {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("project_id")
        Connection db = KissConnection.get()
        List<Record> exps = db.fetchAll("""
                            select e.*, p.lname, p.fname, p.mname, pos.position_name, pos.weekly_per_diem
                            from per_diem_exception e
                            join person p
                              on e.person_id = p.person_id
                            join (
                                -- Create an hr_wage table that only includes their current status as
                                -- of a specific date - a single record for each employee.
                                select t4.*
                                from hr_wage t4
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
                              on e.person_id = w.employee_id
                            join hr_position pos
                              on w.position_id = pos.position_id
                            where e.project_id=?
                            order by p.lname, p.fname, p.mname
                            """, DateUtils.today(), projectId)
        JSONArray exceptions = new JSONArray()
        for (Record exp : exps) {
            JSONObject obj = new JSONObject()
            obj.put("per_diem_exception_id", exp.getString("per_diem_exception_id"))
            String type
            switch (exp.getString("exception_type")) {
                case 'A':
                    type = "Add"
                    break
                case 'S':
                    type = "Subtract"
                    break
                case 'D':
                    type = "Delete"
                    break
                case 'R':
                    type = "Replace"
                    break
                default:
                    type = "Unknown"
                    break
            }
            obj.put("exception_type", exp.getString("exception_type"))
            obj.put("exception_type_formatted", type)
            obj.put("exception_amount", exp.getFloat("exception_amount"))
            obj.put("exception_amount_formatted", NumberFormat.Format(exp.getFloat("exception_amount"), "CD", 0, 2))
            String name = exp.getString("lname") + ", " + exp.getString("fname")
            String mname = exp.getString("mname")
            if (mname != null  &&  !mname.isEmpty())
                name += " " + mname
            obj.put("worker_name", name)
            obj.put("person_id", exp.getString("person_id"))
            obj.put("notes", exp.getString("notes"))
            obj.put("position_name", exp.getString("position_name"))
            obj.put("weekly_per_diem", exp.getFloat("weekly_per_diem"))
            exceptions.put(obj)
        }
        outjson.put("exceptions", exceptions)
    }

}
