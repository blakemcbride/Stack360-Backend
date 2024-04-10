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
 * Date: 1/31/21
 */
class GetSubtype {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString('projectId')
        Connection db = KissConnection.get()
        outjson.put "projectSubtypes", db.fetchAllJSON("""
             select project_subtype_id, code
             from project_subtype 
             where company_id is null or company_id = ? 
             order by code""", hsu.getCurrentCompany().getOrgGroupId())
        Record rec = db.fetchOne("select project_subtype_id from project where project_id=?", projectId)
        outjson.put("project_subtype_id", rec?.getString("project_subtype_id"))
    }

}
