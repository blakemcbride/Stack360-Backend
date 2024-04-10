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

import com.arahant.utils.ArahantLogger
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/17/22
 */
class P_3969 {

    private static final ArahantLogger logger = new ArahantLogger(P_3969.class)

    public static run(Connection db) {
        update_timesheet(db)
        update_project_employee_history(db)
        update_worker_confirmation(db)
    }

    private static update_timesheet(Connection db) {
        logger.info("Updating timesheet table")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from timesheet")
        int i = 1;
        while (c.isNext()) {
            Record rec = c.getRecord()
            String project_id = c.getString("project_id")
            Record srec = cmd.fetchOne("select project_shift_id from project_shift where project_id = ?", project_id)
            rec.set("project_shift_id", srec.getString("project_shift_id"))
            rec.update()
            if (i % 100 == 0)
                db.commit()
            if (i % 1000 == 0)
                logger.info(i)
            i++
        }
        db.commit()
        cmd.close()
        logger.info("Complete; " + i + " records updated")
    }

    private static update_project_employee_history(Connection db) {
        logger.info("Updating project_employee_history table")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from project_employee_history")
        int i = 1;
        while (c.isNext()) {
            Record rec = c.getRecord()
            String project_id = c.getString("project_id")
            Record srec = cmd.fetchOne("select project_shift_id from project_shift where project_id = ?", project_id)
            rec.set("project_shift_id", srec.getString("project_shift_id"))
            rec.update()
            if (i % 100 == 0) {
                logger.info(i)
                db.commit()
            }
            i++
        }
        db.commit()
        cmd.close()
        logger.info("Complete; " + i + " records updated")
    }

    private static update_worker_confirmation(Connection db) {
        logger.info("Updating worker_confirmation table")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from worker_confirmation")
        int i = 1;
        while (c.isNext()) {
            Record rec = c.getRecord()
            String project_id = c.getString("project_id")
            Record srec = cmd.fetchOne("select project_shift_id from project_shift where project_id = ?", project_id)
            rec.set("project_shift_id", srec.getString("project_shift_id"))
            rec.update()
            if (i % 100 == 0) {
                logger.info(i)
                db.commit()
            }
            i++
        }
        db.commit()
        cmd.close()
        logger.info("Complete; " + i + " records updated")
    }
}
