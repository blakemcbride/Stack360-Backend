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
 *
 * Convert message_attachment.attachment to an external file.
 *
 * Author: Blake McBride
 * Date: 11/3/23
 */
class P_5027 {

    public static run(Connection db) {
        int n = 0
        final byte [] emptyData = "X".getBytes()
        KissConnection.set(db)
        println "Begin table message_attachment"
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from message_attachment")
        long size = c.size()
        while (c.isNext()) {
            Record mrec = c.getRecord()
            String messageAttachmentId = mrec.getString("message_attachment_id")
            byte [] data = mrec.getByteArray("attachment")
            String fname = mrec.getString("source_file_name")
            mrec.set("attachment", emptyData)
            ExternalFile.saveData(ExternalFile.MESSAGE_ATTACHMENT_ATTACHMENT, messageAttachmentId, fname, data)
            mrec.update()
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
