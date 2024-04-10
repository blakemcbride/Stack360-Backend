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

package com.arahant.services.standard.hr.comments

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 6/29/21
 */
class GetComments {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String personId = injson.getString("personId")
        Connection db = KissConnection.get()
        db.fetchOneJSON(outjson, "select comments general_comments from applicant where person_id=?", personId)
        outjson.put("detail_comments", db.fetchAllJSON("""
                           select ac.applicant_contact_id, ac.contact_date, ac.contact_time, ac.contact_mode, ac.description, p.fname, p.mname, p.lname
                           from applicant_contact ac
                           join person p
                             on ac.who_added = p.person_id
                           where ac.person_id = ?
                           order by ac.contact_date desc, ac.contact_time desc
                        """, personId))
    }
}
