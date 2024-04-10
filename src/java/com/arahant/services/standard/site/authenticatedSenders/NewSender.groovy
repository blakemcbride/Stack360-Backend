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

package com.arahant.services.standard.site.authenticatedSenders

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 9/5/21
 */
class NewSender {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = hsu.getKissConnection()
        Record rec = db.newRecord("authenticated_senders")
        rec.set("auth_send_id", IDGenerator.generate("authenticated_senders", "auth_send_id"))
        rec.set("address_type", injson.getString("type"))
        rec.set("address", injson.getString("address"))
        rec.addRecord()
    }
}
