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

import com.arahant.utils.IDGenerator
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 6/25/23
 */
class P_4817 {

    public static run(Connection db) {
        List<Record> recs = db.fetchAll("select * from project_task_detail where recurring = 'Y'")
        for (Record rec : recs) {
            Record rrec = db.newRecord("recurring_schedule")
            String id = IDGenerator.generate(rrec, "recurring_schedule_id")
            rrec.set("type", 7)
            rrec.addRecord()
            rec.set("recurring_schedule_id", id)
            rec.update()
        }
        db.commit()
    }

}
