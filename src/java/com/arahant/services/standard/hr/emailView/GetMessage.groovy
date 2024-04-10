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

package com.arahant.services.standard.hr.emailView

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 1/17/20
 */
class GetMessage {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        String person_email_id = injson.getString "person_email_id"

        Connection con = KissConnection.get()

        Record rec = con.fetchOne("select message from person_email where person_email_id=?", person_email_id)
        outjson.put "message", rec == null ? "" : rec.getString("message")

    }
}