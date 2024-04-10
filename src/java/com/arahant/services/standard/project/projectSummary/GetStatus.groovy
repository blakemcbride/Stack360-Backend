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
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 2/17/21
 */
class GetStatus {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString('projectId')
        Connection db = KissConnection.get()
        outjson.put "projectStatus", db.fetchAllJSON("""
             select project_status_id, code, active
             from project_status 
             where company_id is null or company_id = ? 
                   and (last_active_date = 0 or last_active_date >= ?)
             order by code""", hsu.getCurrentCompany().getOrgGroupId(), DateUtils.today())
        Record rec = db.fetchOne("select project_status_id from project where project_id=?", projectId)
        outjson.put("project_status_id", rec?.getString("project_status_id"))
    }

}
