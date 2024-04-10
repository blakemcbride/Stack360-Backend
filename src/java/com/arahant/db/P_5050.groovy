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

import com.arahant.utils.ExternalFile
import com.arahant.utils.KissConnection
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/25/23
 */
class P_5050 {

    public static run(Connection db) {
        int n = 0
        final byte [] emptyData = "external".getBytes()
        KissConnection.set(db)
        println "Begin moving forms out of person_form"
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from person_form")
        long size = c.size()
        while (c.isNext()) {
            Record pfrec = c.getRecord()
            byte [] data = pfrec.getByteArray("form")
            if (data.size() <= 10)
                continue  // already moved
            String personFormId = pfrec.getString("person_form_id")
            String ext = pfrec.getString("file_name_extension")
            pfrec.set("form", emptyData)
            ExternalFile.saveData(ExternalFile.PERSON_FORM, personFormId, ext, data)
            pfrec.update()
            if (++n % 20 == 0)
                db.commit()
            if (n % 100 == 0)
                println n + " of " + size
        }
        db.commit()
        cmd.close()
        println "Total records = " + n
    }

}
