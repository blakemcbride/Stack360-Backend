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

package com.arahant.services.standard.project.myTasks

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
 * Date: 11/30/22
 */
class ListTaskWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String projectId = injson.getString("projectId")
        final String shiftId = injson.getString("shiftId")
        final String taskId = injson.getString("taskId")
        final int workDate = injson.getInt("workDate")
        final Connection db = KissConnection.get()

        // leaders
        JSONArray workers1 = db.fetchAllJSON("""
                                                with wage_now as (
                                                select t2.* from hr_wage t2
                                                inner join
                                                    (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                                     from hr_wage t0
                                                     where t0.effective_date <= ?
                                                     group by t0.employee_id) t1
                                                on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date)

                                                select p.person_id, p.lname, p.fname, p.mname, pta.project_task_assignment_id, 
                                                         pta.team_lead, pos.position_name
                                                from project_task_assignment pta
                                                
                                                join person p
                                                  on pta.person_id = p.person_id
                                                  
                                                left join wage_now wn
                                                  on pta.person_id = wn.employee_id
                                                  
                                                left join hr_position pos
                                                  on wn.position_id = pos.position_id
                                                  
                                                where pta.team_lead = 'Y'
                                                      and pta.project_task_detail_id = ?
                                                order by p.lname, p.fname""", workDate, taskId)
        // assigned but not leaders
        JSONArray workers2 = db.fetchAllJSON("""
                                                with wage_now as (
                                                select t2.* from hr_wage t2
                                                inner join
                                                    (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                                     from hr_wage t0
                                                     where t0.effective_date <= ?
                                                     group by t0.employee_id) t1
                                                on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date)

                                                select p.person_id, p.lname, p.fname, p.mname, pta.project_task_assignment_id, 
                                                         pta.team_lead, pos.position_name
                                                from project_task_assignment pta
                                                
                                                join person p
                                                  on pta.person_id = p.person_id
                                                  
                                                left join wage_now wn
                                                  on pta.person_id = wn.employee_id
                                                  
                                                left join hr_position pos
                                                  on wn.position_id = pos.position_id
                                                  
                                                where pta.team_lead = 'N'
                                                      and pta.project_task_detail_id = ?
                                                order by p.lname, p.fname""", workDate, taskId)
        // not assigned
        JSONArray workers3 = db.fetchAllJSON("""
                                                with wage_now as (
                                                select t2.* from hr_wage t2
                                                inner join
                                                    (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                                     from hr_wage t0
                                                     where t0.effective_date <= ?
                                                     group by t0.employee_id) t1
                                                on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date)

                                                select p.person_id, p.lname, p.fname, p.mname, null project_task_assignment_id, 
                                                         null team_lead, pos.position_name
                                                from project_employee_join pej
                                                
                                                join person p
                                                  on pej.person_id = p.person_id
                                                  
                                                left join wage_now wn
                                                  on pej.person_id = wn.employee_id
                                                  
                                                left join hr_position pos
                                                  on wn.position_id = pos.position_id
                                                  
                                                left join project_task_assignment pta
                                                  on p.person_id = pta.person_id and pta.project_task_detail_id = ?
                                                  
                                                where pej.project_shift_id = ?
                                                      and pta.project_task_assignment_id is null
                                        
                                                order by
                                                      pos.seqno, 
                                                      p.lname, p.fname""", DateUtils.today(), taskId, shiftId)
        JSONArray workers = new JSONArray()
        append(workers, workers1)
        append(workers, workers2)
        append(workers, workers3)
        int len = workers.length()
        for (int i=0 ; i < len ; i++) {
            JSONObject worker = workers.getJSONObject(i)
            String personId = worker.getString("person_id")
            Record rec = db.fetchOne("""select count(*) 
                                        from project_task_assignment pta
                                        join project_task_detail ptd
                                          on pta.project_task_detail_id = ptd.project_task_detail_id
                                        where pta.project_task_detail_id <> ? 
                                              and pta.person_id = ?
                                              and ptd.project_shift_id = ?
                                              and (ptd.status <> 1 and ptd.status <> 2)""", taskId, personId, shiftId)
            worker.put("number_other_assignments", rec.getLong("count"))

            rec = db.fetchOne("""select count(*)
                                 from timesheet
                                 where person_id = ?
                                       and project_shift_id = ?
                                       and beginning_date = ?
                                       and billable = 'Y'""", personId, shiftId, workDate)
            worker.put("present", rec.getLong("count") != 0L)

            rec = db.fetchOne("""select sum(total_hours)
                                 from timesheet
                                 where person_id = ?
                                       and billable = 'Y'""", personId)
            worker.put("total_hours", rec.getDouble("sum"))
        }
        outjson.put("workers", workers)
    }

    private static void append(JSONArray a1, JSONArray a2) {
        final int n = a2.size()
        for (int i=0 ; i < n ; i++)
            a1.put(a2.getJSONObject(i))
    }
}
