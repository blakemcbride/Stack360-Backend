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


import com.arahant.utils.StandardProperty
import com.arahant.business.BPerson
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONArray
import org.json.JSONObject

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Author: Blake McBride
 * Date: 1/16/20
 */
class GetEmailList {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        String person_id = injson.getString "person_id"

        BPerson bp = new BPerson(person_id)
        String email_address = bp.getPersonalEmail()
        outjson.put("email_address", email_address)

        Connection con = KissConnection.get()

        List<Record> recs = con.fetchAll "select person_email_id, date_sent, sent_to, subject " +
                "from person_email " +
                "where person_id=? " +
                "order by date_sent",
                person_id
        JSONArray ja = new JSONArray()
        
        DateFormat df = new SimpleDateFormat("EEE MMMM dd, yyyy hh:mm aa zzz")
        String tz = BProperty.get(StandardProperty.TIMEZONE)
        if (tz != null  &&  !tz.isEmpty())
            df.setTimeZone(TimeZone.getTimeZone(tz))
        for (Record rec : recs) {
            JSONObject jo = new JSONObject()

            jo.put "person_email_id", rec.getString("person_email_id")
            Date dt = rec.getDateTime("date_sent")

            jo.put "date_sent", df.format(dt)
            jo.put "sent_to", rec.getString("sent_to")
            jo.put "subject", rec.getString("subject")

            ja.put jo
        }
        outjson.put("emailList", ja)
    }

}
