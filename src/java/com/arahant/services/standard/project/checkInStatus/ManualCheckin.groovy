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

package com.arahant.services.standard.project.checkInStatus

import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 7/16/22
 */
class ManualCheckin {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String personId = injson.getString("personId")
        final String projectId = injson.getString("projectId")
        final String notes = injson.getString("notes")
        final Connection db = hsu.getKissConnection()

        // need the shiftId
        // first try to find what shift they're assigned to
        Record srec = db.fetchOne("""select pej.project_shift_id 
                                     from project_employee_join pej
                                     join project_shift ps
                                       on pej.project_shift_id = ps.project_shift_id
                                     where pej.person_id = ?
                                           and ps.project_id = ?""", personId, projectId)
        String shiftId = srec == null ? null : srec.getString("project_shift_id")

        if (shiftId == null) {
            // not assigned to the project
            // just use any shift from the project
            srec = db.fetchOne("select project_shift_id from project_shift where project_id = ?", projectId)
            if (srec != null)
                shiftId = srec.getString("project_shift_id")
            else
                throw new ArahantException("Project has no shifts")  // should never happen
        }

        final Record rec = db.newRecord("worker_confirmation")
        IDGenerator.generate(rec, 'worker_confirmation_id')
        rec.set "person_id", personId
        rec.setDateTime "confirmation_time", new Date()
        rec.set "project_shift_id", shiftId
        rec.set "who_added", hsu.getCurrentPerson().getPersonId()
        rec.set "notes", notes
        rec.addRecord()
    }
}
