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

package com.arahant.services.standard.hr.viewLog

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 6/19/23
 */
class GetEvents {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final Connection db = KissConnection.get()
        final String personId = injson.getString("personId")
        outjson.put("events", db.fetchAllJSON("""
                                              select log.*, cp.fname cp_fname, ap.fname ap_fname, cp.lname cp_lname, ap.lname ap_lname
                                              from change_log log
                                              left join person cp
                                                on log.change_person_id = cp.person_id
                                              left join person ap 
                                                on log.person_id = ap.person_id
                                              where log.person_id = ?
                                              order by log.change_when desc
                                              """, personId))
    }
}
