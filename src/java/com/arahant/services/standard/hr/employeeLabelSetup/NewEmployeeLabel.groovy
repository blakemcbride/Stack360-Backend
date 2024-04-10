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

package com.arahant.services.standard.hr.employeeLabelSetup

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/21/20
 */
class NewEmployeeLabel {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        Record rec = db.newRecord("employee_label")
        rec.set("employee_label_id", IDGenerator.generate("employee_label", "employee_label_id"))
        rec.set("name", injson.getString("name"))
        rec.set("description", injson.getString("description"))
        rec.set("auto_add_new_employee", injson.getBoolean("autoAddNewEmployee") ? "Y" : "N")
        rec.addRecord()
    }
}
