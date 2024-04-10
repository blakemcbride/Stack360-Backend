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

package com.arahant.services.standard.project.projectSubtype

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 1/30/21
 */
class NewProjectSubtype {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        Record rec = db.newRecord("project_subtype")
        rec.set("project_subtype_id", IDGenerator.generate("project_subtype", "project_subtype_id"))
        rec.set("code", injson.getString("code"))
        rec.set("description", injson.getString("description"))
        rec.set("scope", injson.getString("scope"))
        if (!injson.getBoolean("allCompanies"))
            rec.set("company_id", hsu.getCurrentCompany().getOrgGroupId())
        rec.set("last_active_date", injson.getInt("lastActiveDate"))
        rec.addRecord()
    }

}
