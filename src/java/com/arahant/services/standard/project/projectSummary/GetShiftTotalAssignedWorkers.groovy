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

package com.arahant.services.standard.project.projectSummary

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 4/1/22
 */
class GetShiftTotalAssignedWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString('projectId')
        String shiftId = injson.getString('shiftId')
        Connection db = KissConnection.get()
        Record rec
        if (shiftId != null  &&  !shiftId.isEmpty())
            rec = db.fetchOne("select count(*) from project_employee_join where project_shift_id = ?", shiftId)
        else
            rec = db.fetchOne("""select count(*) 
                                 from project_employee_join pej
                                 join project_shift ps
                                   on pej.project_shift_id = ps.project_shift_id
                                 where ps.project_id = ?""", projectId)
        outjson.put "assignedWorkers", rec.getLong("count")
    }

}
