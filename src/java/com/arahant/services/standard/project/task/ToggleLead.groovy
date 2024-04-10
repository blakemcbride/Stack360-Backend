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
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/31/23
 */
class ToggleLead {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String taskId = injson.getString("taskId")
        final String personId = injson.getString("personId")
        final Connection db = KissConnection.get()

        Record rec = db.fetchOne("select * from project_task_assignment where person_id = ? and project_task_detail_id = ?", personId, taskId)
        if (rec == null) {
            db.execute("update project_task_assignment set team_lead = 'N' where project_task_detail_id = ?", taskId)
            rec = db.newRecord("project_task_assignment")
            IDGenerator.generate(rec, "project_task_assignment_id")
            rec.set("project_task_detail_id", taskId)
            rec.set("person_id", personId)
            rec.set("team_lead", "Y")
            rec.addRecord()
        } else {
            if (rec.getString("team_lead") == "Y") {
                rec.set("team_lead", "N")
                rec.update()
            } else {
                db.execute("update project_task_assignment set team_lead = 'N' where project_task_detail_id = ?", taskId)
                rec.set("team_lead", "Y")
                rec.update()
            }
        }
    }
}
