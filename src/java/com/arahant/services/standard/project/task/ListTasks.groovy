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

package com.arahant.services.standard.project.task

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/24/23
 */
class ListTasks {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String shiftId = injson.getString("shiftId")
        final int taskDate = injson.getInt("taskDate")
        final Connection db = hsu.getKissConnection()
        final List<Record> recs = db.fetchAll("""select td.*, rs.*
                                                from project_task_detail td
                                                left join recurring_schedule rs
                                                  on td.recurring_schedule_id = rs.recurring_schedule_id
                                                where td.project_shift_id = ?
                                                      and td.task_date <= ?
                                                      and not ((td.status = 1 or td.status = 2) and td.completion_date < ?)
                                                order by td.seqno""", shiftId, taskDate, taskDate)
        final JSONArray ary = Record.toJSONArray(recs)
        for (int i=0 ; i < ary.length() ; i++) {
            JSONObject obj = ary.getJSONObject(i)
            obj.put("number_assigned", getNumberAssigned(db, obj.getString("project_task_detail_id"), shiftId, taskDate))
        }
        outjson.put("tasks", ary)
    }

    private static int getNumberAssigned(final Connection db, String projectTaskDetailId, String shiftId, int taskDate) {
        Record rec = db.fetchOne("select count(*) " +
                "from project_task_assignment " +
                "where project_task_detail_id = ?", projectTaskDetailId)
        return rec.getLong("count")
    }

}
