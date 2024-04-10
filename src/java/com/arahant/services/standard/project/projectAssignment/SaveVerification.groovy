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

package com.arahant.services.standard.project.projectAssignment

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 8/10/21
 */
class SaveVerification {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String personId = injson.getString('personId')
        String projectId = injson.getString('projectId')
        boolean verified = injson.getBoolean('verified')
        Connection db = KissConnection.get()
        List<Record> recs = db.fetchAll("""select pej.* 
                                    from project_employee_join pej
                                    join project_shift ps
                                      on pej.project_shift_id = ps.project_shift_id
                                    where pej.person_id=? 
                                          and ps.project_id=?""", personId, projectId)
        for (Record rec : recs) {
            if (verified) {
                rec.setDateTime("verified_date", new Date())
                rec.set("verified_person_id", hsu.getCurrentPerson().getPersonId())
            } else {
                rec.setDateTime("verified_date", null)
                rec.set("verified_person_id", null)
            }
            rec.update()
        }
    }
}
