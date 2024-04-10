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
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.TimeUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/1/20
 */
class RemoveAssignment {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("projectId")
        String shiftId = injson.getString("shiftId")
        String personId = injson.getString("personId")
        Connection db = KissConnection.get()
        Record rec = db.fetchOne("""select project_employee_join_id 
                                    from project_employee_join
                                    where person_id=? 
                                          and project_shift_id = ?""", personId, shiftId)
        if (rec == null)
            throw new ArahantException("Worker is not assigned to that project.")
        rec.delete()

        rec = db.newRecord("project_employee_history")
        rec.set("project_employee_history_id", IDGenerator.generate("project_employee_history", "project_employee_history_id"))
        rec.set("person_id", personId)
        rec.set("project_shift_id", shiftId)
        rec.set("change_person_id", hsu.getCurrentPerson().getPersonId())
        rec.set("change_date", DateUtils.today())
        rec.set("change_time", TimeUtils.now())
        rec.set("change_type", "D")
        rec.addRecord()
    }

}
