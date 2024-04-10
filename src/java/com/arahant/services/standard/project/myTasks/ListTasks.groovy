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
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 6/5/23
 */
class ListTasks {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String personId = hsu.getCurrentPerson().getPersonId()
        final Connection db = KissConnection.get()
        final int date = injson.getInt("date")
        final List<Record> recs = db.fetchAll("""
                   select task.*, ass.project_task_assignment_id, ass.person_id, ass.team_lead
                   from project_task_assignment ass
                   join project_task_detail task
                     on ass.project_task_detail_id = task.project_task_detail_id
                   where ass.person_id = ?
                         and (task.task_date = ? or 
                              task.completion_date = ? or
                              task.task_date < ? and task.status <> 1 and task.status <> 2 or 
                              task.completion_date < ? and task.task_date < ? and task.status <> 1 and task.status <> 2)
                   order by task.task_date, task.seqno
             """, personId, date, date, date, date, date)
        final JSONArray ary = Record.toJSONArray(recs)
        for (int i=0 ; i < ary.length() ; i++) {
            JSONObject obj = ary.getJSONObject(i)
            int n = getNumberAssigned(db, obj.getString("project_task_detail_id"), obj.getString("project_shift_id"), date)
            obj.put("number_assigned", getNumberAssigned(db, obj.getString("project_task_detail_id"), obj.getString("project_shift_id"), date))
        }
        outjson.put("tasks", ary)
    }

    private static int getNumberAssigned(final Connection db, String projectTaskDetailId, String shiftId, int taskDate) {
        List<Record> recs = db.fetchAll("select person_id " +
                "from project_task_assignment " +
                "where project_task_detail_id = ?", projectTaskDetailId)
        return recs.size()
    }
}
