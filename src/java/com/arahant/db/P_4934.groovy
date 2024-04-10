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

import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 9/3/23
 */
class P_4934 {

    public static run(Connection db) {
        int n = 0
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from message")
        while (c.isNext()) {
            Record mrec = c.getRecord()
            char messageType = mrec.getChar("message_type")
            if (messageType == 'T' as char)
                mrec.set("send_text", "Y")
            else if (messageType == 'E' as char)
                mrec.set("send_email", "Y")
            else if (messageType == 'M' as char)
                mrec.set("send_internal", "Y")
            mrec.update()
            if (++n > 40) {
                db.commit()
                n = 0
            }
        }
        db.commit()
    }

}
