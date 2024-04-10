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

package com.arahant.services.standard.project.sendTextMessage

import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.SendSMSTwilio
import org.json.JSONObject
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * User: Blake McBride
 * Date: 10/23/21
 */
class SendTextMessage {

    private static boolean testMode = BProperty.getBoolean("TestEnvironment", false)

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        testMode = BProperty.getBoolean("TestEnvironment", false)

        String project_id = injson.getString("project_id")
        String shift_id = injson.getString("shift_id")
        String detail = injson.getString("detail")

        if (detail == null  ||  detail.isEmpty())
            throw new Exception("Missing email message")

        try {
            SendSMSTwilio sm
            if (!testMode) {
                sm = new SendSMSTwilio()
                if (sm.status != SendSMSTwilio.Status.OK)
                    throw new ArahantException("Twilio is not configured.")
            }
            Connection con = hsu.getKissConnection()
            Command cmd = con.newCommand()
            List<Record> recs
            if (shift_id == null || shift_id.isEmpty())
                recs = cmd.fetchAll("select p.person_id, ph.phone_number " +
                        "from project_employee_join j " +
                        "join person p " +
                        "  on j.person_id = p.person_id " +
                        "join phone ph " +
                        "  on p.person_id = ph.person_join " +
                        "join project_shift ps " +
                        "  on j.project_shift_id = ps.project_shift_id " +
                        "where ps.project_id = ? " +
                        "      and ph.phone_type = 3 " +
                        "      and ph.record_type = 'R' " +
                        "order by p.lname, p.fname", project_id)
            else
                recs = cmd.fetchAll("select p.person_id, ph.phone_number " +
                        "from project_employee_join j " +
                        "join person p " +
                        "  on j.person_id = p.person_id " +
                        "join phone ph " +
                        "  on p.person_id = ph.person_join " +
                        "where j.project_shift_id = ? " +
                        "      and ph.phone_type = 3 " +
                        "      and ph.record_type = 'R' " +
                        "order by p.lname, p.fname", shift_id)
            for (Record rec : recs) {
                String personId = rec.getString("person_id")
                String phone = rec.getString("phone_number")
                if (!testMode && phone != null && phone.trim().length() >= 10)
                    sm.sendSMStoPerson(personId, detail)
            }
        } catch (Exception e) {
            throw new ArahantException(e)
        }
    }

}
