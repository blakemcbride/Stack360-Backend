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

package com.arahant.services.standard.hr.employeeLabel

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/22/20
 */
class SaveEmployeeLabel {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        Record rec = db.fetchOne("""select * from employee_label_association 
               where employee_label_id=? and employee_id=?""", injson.getString("labelId"), injson.getString("personId"))
        if (rec == null)
            return
        rec.set("notes", injson.getString("notes"))
        rec.update()
    }
}
