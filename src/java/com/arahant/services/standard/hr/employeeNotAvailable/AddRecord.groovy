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

package com.arahant.services.standard.hr.employeeNotAvailable

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 6/7/21
 */
class AddRecord {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String employeeId = injson.getString "employeeId"
        int startDate = injson.getInt "startDate"
        int endDate = injson.getInt "endDate"
        String reason = injson.getString "reason"
        Connection db = KissConnection.get()
        Record rec = db.newRecord("employee_not_available")
        String key = IDGenerator.generate("employee_not_available", "employee_not_available_id")
        rec.set "employee_not_available_id", key
        rec.set "employee_id", employeeId
        rec.set "start_date", startDate
        rec.set "end_date", endDate
        rec.set "reason", reason
        rec.addRecord()
    }

}
