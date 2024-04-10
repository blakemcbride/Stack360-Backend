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
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 8/18/23
 *
 * Correct phone formats
 */
class P_4922 {

    public static run(Connection db) {
        int n = 0
        Command cmd = db.newCommand()
        db.execute("delete from phone_cr")  // delete all change requests
        db.commit()
        Cursor c = cmd.query("select * from phone")
        while (c.isNext()) {
            Record rec = c.getRecord()
            String phone = rec.getString("phone_number")
            if (phone == null || phone.replaceAll("[^0-9]", "").length() < 10) {
                rec.delete()
            } else {
                rec.set("phone_number", Formatting.formatPhoneNumber(phone))
                rec.update()
            }
            if (++n > 40) {
                db.commit()
                n = 0
            }
        }
        db.commit()
    }

}
