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

package com.arahant.services.standard.hr.message

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 8/30/23
 */
class HandleSent {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String currentPersonId = hsu.getCurrentPerson().getPersonId()
        final JSONArray messageIds = injson.getJSONArray("messageIds")
        final Connection db = KissConnection.get()
        final int n = messageIds.length()
        for (int i=0 ; i < n ; i++) {
            final String messageId = messageIds.getString(i)
            Record mrec = db.fetchOne("select * from message where message_id = ?", messageId)
            String fromPersonId = mrec.getString("from_person_id")
            if (currentPersonId == fromPersonId) {
                mrec.set("from_show", mrec.getString("from_show") == "Y" ? "N" : "Y")
                mrec.update()
                continue
            }
            List<Record> recs = db.fetchAll("select * from message_to where message_id = ? and to_person_id = ?", messageId, currentPersonId)
            for (Record rec : recs) {
                String toPersonId = rec.getString("to_person_id")
                if (currentPersonId == toPersonId) {
                    rec.set("to_show", rec.getString("to_show") == "Y" ? "N" : "Y")
                    rec.update()
                    break
                }
            }
        }
    }
}
