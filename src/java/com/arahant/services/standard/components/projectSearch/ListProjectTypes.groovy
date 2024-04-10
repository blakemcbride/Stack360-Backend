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

/*
 * All rights reserved.
 *
 * Author: Blake McBride
 * Date: 5/9/20
 */

package com.arahant.services.standard.components.projectSearch

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

class ListProjectTypes {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        List<Record> recs = db.fetchAll("""
            select project_type_id, code, description, scope, last_active_date
            from project_type
            where (company_id=? or company_id is null) and (last_active_date > ? or last_active_date = 0) 
            order by code
        """, injson.getString("_contextCompanyId"), DateUtils.today())
        JSONArray ary = new JSONArray()
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            obj.put('projectTypeId', rec.getString('project_type_id'))
            obj.put('code', rec.getString('code'))
            obj.put('description', rec.getString('description'))
            obj.put('scope', rec.getString('scope'))
            obj.put('lastActiveDate', rec.getInt('last_active_date'))
            ary.put(obj)
        }
        outjson.put('types', ary)
    }

}
