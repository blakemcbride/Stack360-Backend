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
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 6/1/23
 */
class DeleteTask {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String projectTaskDetailId = injson.getString("projectTaskDetailId")
        final Connection db = hsu.getKissConnection()

        db.execute("delete from project_task_assignment where project_task_detail_id = ?", projectTaskDetailId)
        List<Record> recs = db.fetchAll("select * from project_task_picture where project_task_detail_id = ?", projectTaskDetailId)
        for (Record rec : recs) {
            String pictureFullPath = ExternalFile.makeExternalFilePath(ExternalFile.PROJECT_TASK_PICTURE, rec.getString("project_task_picture_id"), rec.getString("file_name_extension"));
            new File(pictureFullPath).delete()
            rec.delete()
        }
        db.execute("delete from project_task_detail where project_task_detail_id = ?", projectTaskDetailId)
        Record rec = db.fetchOne("select recurring_schedule_id from project_task_detail where project_task_detail_id = ?", projectTaskDetailId)
        if (rec != null) {
            String recurringScheduleId = rec.getString("recurring_schedule_id")
            if (recurringScheduleId != null)
                db.execute("delete from recurring_schedule where recurring_schedule_id = ?", recurringScheduleId)
        }
    }

}
