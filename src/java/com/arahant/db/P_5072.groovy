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
 * Date: 11/30/23
 */
class P_5072 {

    public static run(Connection db) {
        int n = 0
        KissConnection.set(db)
        println "Begin moving images out of expense_receipt"
        println "Reading records.  Please wait."
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select expense_receipt_id, file_type, picture1, picture2 from expense_receipt")
        long size = c.size()
        while (c.isNext()) {
            Record rec  = c.getRecord()
            String expenseReceiptId = rec.getString("expense_receipt_id")
            String ext = rec.getString("file_type")

            byte[] pic = rec.getByteArray("picture1")
            if (pic != null && pic.size() > 10)
                ExternalFile.saveData(ExternalFile.EXPENSE_RECEIPT_PICTURE1, expenseReceiptId, ext, pic)

            pic = rec.getByteArray("picture2")
            if (pic != null && pic.size() > 10)
                ExternalFile.saveData(ExternalFile.EXPENSE_RECEIPT_PICTURE2, expenseReceiptId, ext, pic)

            if (++n % 100 == 0)
                println n + " of " + size
        }
        cmd.close()
        println "Total records = " + n
    }

}
