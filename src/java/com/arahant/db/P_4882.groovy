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

import org.kissweb.database.*

/**
 * Author: Blake McBride
 * Date: 8/9/23
 *
 * Remove duplicate phone records
 */
class P_4882 {

    public static void run(Connection db) {
        Command cmd = db.newCommand()
        db.execute("delete from phone_cr")
        deleteDuplicatePersonRecords(db, cmd)
        deleteDuplicateOrgRecords(db, cmd)
    }

    private static void deleteDuplicatePersonRecords(Connection db, Command cmd) {
        Cursor c = cmd.query("select * from phone where person_join is not null order by person_join, phone_type")
        int n = 0
        int t = 0
        String lastPersonId = ""
        int lastPhoneType = -1
        while (c.isNext()) {
            Record r1 = c.getRecord()
            String personId = r1.getString("person_join")
            int phoneType = r1.getInt("phone_type")
            if (personId.equals(lastPersonId) && phoneType == lastPhoneType)
                continue
            else {
                lastPersonId = personId
                lastPhoneType = phoneType
            }
            List<Record> recs2 = db.fetchAll("""select * 
                                                      from phone 
                                                      where person_join = ?
                                                            and phone_type = ?
                                                            and phone_id <> ?""", personId, phoneType, r1.getString("phone_id"))
            for (Record r2 : recs2) {
                r2.delete()
                n++
                t++
            }
            if (n > 20) {
                db.commit()
                n = 0
            }
        }
        db.commit()
        println "Deleted $t duplicate person phone records"
    }

    private static void deleteDuplicateOrgRecords(Connection db, Command cmd) {
        Cursor c = cmd.query("select * from phone where org_group_join is not null order by org_group_join, phone_type")
        int n = 0
        int t = 0
        String lastOrgGroupId = ""
        int lastPhoneType = -1
        while (c.isNext()) {
            Record r1 = c.getRecord()
            String orgGroupId = r1.getString("org_group_join")
            int phoneType = r1.getInt("phone_type")
            if (orgGroupId.equals(lastOrgGroupId) && phoneType == lastPhoneType)
                continue
            else {
                lastOrgGroupId = orgGroupId
                lastPhoneType = phoneType
            }
            List<Record> recs2 = db.fetchAll("""select * 
                                                      from phone 
                                                      where org_group_join = ?
                                                            and phone_type = ?
                                                            and phone_id <> ?""", orgGroupId, phoneType, r1.getString("phone_id"))
            for (Record r2 : recs2) {
                r2.delete()
                n++
                t++
            }
            if (n > 20) {
                db.commit()
                n = 0
            }
        }
        db.commit()
        println "Deleted $t duplicate orgGroup phone records"
    }
}
