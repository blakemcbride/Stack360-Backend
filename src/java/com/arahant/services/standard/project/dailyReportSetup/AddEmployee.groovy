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

package com.arahant.services.standard.project.dailyReportSetup

import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/21/21
 */
class AddEmployee {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String clientId = injson.getString("clientId")
        String employeeId = injson.getString("employeeId")
        Connection db = hsu.getKissConnection()
        Record rec = db.fetchOne("select * from project_report_person where person_id=? and client_id=?", employeeId, clientId)
        if (rec != null)
            throw new ArahantWarning("Employee is already associated with this client.")
        rec = db.newRecord("project_report_person")
        rec.set("person_id", employeeId)
        rec.set("client_id", clientId)
        rec.addRecord()
    }

}
