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

package com.arahant.db

import com.arahant.utils.Formatting
import com.arahant.utils.IDGenerator
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 8/20/23
 *
 * Consolidate text messages from the text_message table to the message table.
 * Remove the text_message table.
 */
class P_4924 {

    public static run(Connection db) {
        int n = 0
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from text_message")
        while (c.isNext()) {
            Record tmrec = c.getRecord()
            Record mrec = db.newRecord("message")
            String messageId = IDGenerator.generate(mrec, "message_id")
            String message = tmrec.getString("message")
            String subject
            if (message.length() <= 80) {
                subject = message
                message = null
            } else
                subject = StringUtils.take(message, 80)
            mrec.set("message", message)
            mrec.setDateTime("created_date", tmrec.getDateTime("when_sent"))
            mrec.set("subject", subject)
            mrec.set("message_type", "T")
            mrec.set("send_text", "Y")
            mrec.addRecord()

            String toPersonId = tmrec.getString("person_id")
            Record mtrec = db.newRecord("message_to")
            IDGenerator.generate(mtrec, "message_to_id")
            mtrec.set("message_id", messageId)
            mtrec.set("to_person_id", toPersonId)
            mtrec.set("send_type", "T")
            mtrec.set("sent", "Y")
            mtrec.set("to_address", tmrec.getString("phone_number"))
            Record prec = db.fetchOne("select fname, lname from person where person_id = ?", toPersonId)
            if (prec != null)
                mtrec.set("to_name", prec.getString("fname") + " " + prec.getString("lname"))
            mrec.setDateTime("date_received", tmrec.getDateTime("when_sent"))
            mtrec.addRecord()

            if (++n > 40) {
                db.commit()
                n = 0
            }
        }
        db.commit()
    }

}
