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
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 8/28/21
 */
class SaveStartDate {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String shiftId = injson.getString("shiftId")
        String personId = injson.getString("personId")
        int startDate = injson.getInt("startDate")
        Connection db = KissConnection.get()
        Record rec = db.fetchOne("""select * 
                                    from project_employee_join
                                    where person_id=? 
                                          and project_shift_id = ?""", personId, shiftId)
        if (rec == null)
            throw new ArahantException("Worker is not assigned to that project.")
        rec.set("start_date", startDate)
        rec.update()
    }

}
