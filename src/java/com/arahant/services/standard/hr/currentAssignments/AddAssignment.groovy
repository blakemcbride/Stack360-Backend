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

package com.arahant.services.standard.hr.currentAssignments

import com.arahant.exceptions.ArahantException
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.TimeUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/1/20
 */
class AddAssignment {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("projectId")
        String shiftId = injson.getString("shiftId")
        String personId = injson.getString("personId")
        Integer startDate = injson.getInt("startDate")
        if (startDate == null)
            startDate = 0
        Connection db = hsu.getKissConnection()
        Record rec = db.fetchOne("""select project_employee_join_id 
                                    from project_employee_join
                                    where person_id=? 
                                          and project_shift_id = ?""", personId, shiftId)
        if (rec != null) {
            rec = db.fetchOne("select estimated_last_date from project where project_id=?", projectId)
            int lastDate = rec.getInt("estimated_last_date")
            if (lastDate < DateUtils.today())
                throw new ArahantWarning("Worker is already assigned to that project, but the project last date has expired.")
            else
                throw new ArahantWarning("Worker is already assigned to that project.")
        }
        rec = db.newRecord("project_employee_join")
        rec.set("project_employee_join_id", IDGenerator.generate("project_employee_join", "project_employee_join_id"))
        rec.set("person_id", personId)
        rec.set("project_shift_id", shiftId)
        rec.set("date_assigned", DateUtils.today())
        rec.set("time_assigned", TimeUtils.now())
        rec.set("start_date", startDate)
        rec.addRecord()

        rec = db.newRecord("project_employee_history")
        IDGenerator.generate(rec, "project_employee_history_id")
        rec.set("person_id", personId)
        rec.set("project_shift_id", shiftId)
        rec.set("change_person_id", hsu.getCurrentPerson().getPersonId())
        rec.set("change_date", DateUtils.today())
        rec.set("change_time", TimeUtils.now())
        rec.set("change_type", "A")
        rec.addRecord()
    }

}
