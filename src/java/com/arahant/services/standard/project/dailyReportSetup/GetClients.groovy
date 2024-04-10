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


import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 11/15/21
 */
class GetClients {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int maxrecs = BProperty.getInt(StandardProperty.COMBO_MAX)
        Connection db = hsu.getKissConnection()
        JSONArray lst = db.fetchAllJSON(maxrecs, """select og.org_group_id, og.group_name
                                           from client c
                                           join org_group og
                                             on c.org_group_id = og.org_group_id
                                           where c.inactive_date < ?
                                           order by og.group_name
                                        """, DateUtils.today())
        outjson.put("maxrecs", maxrecs)
        outjson.put("clients", lst)
    }

}

