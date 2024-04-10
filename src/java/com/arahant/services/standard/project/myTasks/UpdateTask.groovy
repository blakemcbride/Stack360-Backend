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
import com.arahant.utils.IDGenerator
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/9/22
 */
class UpdateTask {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String projectTaskDetailId = injson.getString("projectTaskDetailId")
        final int status = injson.getInt("status")
        final int workDate = injson.getInt("workDate")
        final Connection db = hsu.getKissConnection()
        final Record rec = db.fetchOne("select * from project_task_detail where project_task_detail_id = ?", projectTaskDetailId)
        rec.set("title", injson.getString("title"))
        rec.set("description", injson.getString("description"))
        rec.set("comments", injson.getString("comments"))
        rec.set("missing_items", injson.getString("missingItems"))
        rec.set("status", status)
        final int completionDate = rec.getInt("completion_date")
        if (status == 1 || status == 2) {
            if (completionDate == 0) {
                rec.set("completion_date", workDate)
                if (rec.getString("recurring") == "Y") {
                    int nextDate = DateUtils.addDays(workDate, 1)
                    String shiftId = rec.getString("project_shift_id")
                    Record rec2 = db.fetchOne("select * from project_task_detail where project_shift_id = ? and task_date = ? and title = ?", shiftId, nextDate, injson.getString("title"))
                    if (rec2 == null) {
                        int seqno = 1
                        rec2 = db.newRecord("project_task_detail")
                        String newProjectTaskDetailId = IDGenerator.generate(rec2, "project_task_detail_id")

                        Record srec = db.fetchOne("select seqno from project_task_detail where project_shift_id = ? order by seqno desc", shiftId)
                        if (srec != null)
                            seqno = srec.getInt("seqno") + 1
                        rec2.set("seqno", seqno)

                        rec2.set("title", rec.getString("title"))
                        rec2.set("description", rec.getString("description"))
                        rec2.set("status", 0)  // open
                        rec2.set("project_shift_id", shiftId)
                        rec2.set("task_date", nextDate)
                        rec2.set('recurring', "Y")
                        rec2.set("completion_date", 0)
                        rec2.addRecord()

                        List<Record> arecs = db.fetchAll("select * from project_task_assignment where project_task_detail_id = ?", projectTaskDetailId)
                        for (Record arec : arecs) {
                            Record arec2 = db.newRecord("project_task_assignment")
                            IDGenerator.generate(arec2, "project_task_assignment_id")
                            arec2.set("project_task_detail_id", newProjectTaskDetailId)
                            arec2.set("person_id", arec.getString("person_id"))
                            arec2.set("team_lead", arec.getString("team_lead"))
                            arec2.addRecord()
                        }
                    }
                }
            }
        } else {
            rec.set("completion_date", 0)
        }
        rec.update()
    }

}
