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

import com.arahant.business.BPerson
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import com.arahant.utils.SendEmailGeneric
import com.arahant.utils.SendEmailProvider
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 1/17/20
 */
class ResendMessage {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String person_email_id = injson.getString "person_email_id"

        Connection con = KissConnection.get()

        Record rec = con.fetchOne "select * from person_email where person_email_id=?", person_email_id
        if (rec == null)
            throw new Exception("Missing person email record")

        String person_id = rec.getString "person_id"
        String subject = rec.getString "subject"
        String message = rec.getString "message"

        BPerson bp = new BPerson(person_id)
        String email_address = bp.getPersonalEmail()

        if (email_address == null  ||  email_address.isEmpty())
            throw new Exception("Missing email address")

        SendEmailGeneric em = null

        try {
            em = SendEmailProvider.newEmail()
            em.setHTMLMessage(message)
            em.sendEmail(email_address, bp.getNameFML(), subject)
        } finally {
            if (em != null)
                em.close()
        }

        rec = con.newRecord("person_email")
        person_email_id = IDGenerator.generate("person_email", "person_email_id")
        rec.set "person_email_id", person_email_id
        rec.set "person_id", person_id
        rec.setDateTime "date_sent", new Date()
        rec.set "sent_to", email_address
        rec.set "subject", subject
        if (message != null)
            rec.set "message", message
        rec.addRecord()

        outjson.put "person_email_id", person_email_id
    }

}
