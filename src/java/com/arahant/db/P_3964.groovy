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
import com.arahant.utils.IDGeneratorKiss
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/10/22
 */
class P_3964 {

    private static final ArahantLogger logger = new ArahantLogger(P_3964.class)

    public static run(Connection db) {
        update_project_shift(db)
        update_project_employee_join(db)
    }

    private static void update_project_shift(Connection db) {
        logger.info("Updating project_shift table")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select project_id, required_workers, shift_start, description from project")
        int i = 1
        while (c.isNext()) {
            Record prec = c.getRecord()

            String shift = prec.getString("shift_start")
            String desc = prec.getString("description")
            if (shift != null  &&  !shift.isEmpty()  && desc != null && !desc.isEmpty() && desc.endsWith(shift)) {
                desc = StringUtils.drop(desc, -shift.length()).trim()
                prec.set("description", desc)
                prec.update()
            }

            Record srec = db.newRecord("project_shift")
            IDGeneratorKiss.generate(srec, "project_shift_id")
            srec.set("project_id", prec.getString("project_id"))
            srec.set("required_workers", prec.getShort("required_workers"))
            srec.set("shift_start", shift)
            srec.addRecord()
            if (i % 100 == 0) {
                db.commit()
                logger.info(i)
            }
            i++
        }
        db.commit()
        cmd.close()
        logger.info("Complete")
    }

    private static void update_project_employee_join(Connection db) {
        logger.info("Updating project_employee_join table")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from project_employee_join")
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
        logger.info("Complete")
    }

}
