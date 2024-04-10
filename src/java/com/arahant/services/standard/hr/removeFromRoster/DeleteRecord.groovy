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

package com.arahant.services.standard.hr.removeFromRoster

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.*

/**
 * Author: Blake McBride
 * Date: 8/6/23
 */
class DeleteRecord {

    private static final String TERM_REVIEW = "00001-0000000008"

        static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
            final Connection db = KissConnection.get()
            final String rfrId = injson.getString("rfrId")
            final boolean termReview = injson.getBoolean("termReview")
            Record rec = db.fetchOne("select rfr_id, person_id from removed_from_roster where rfr_id = ?", rfrId)
            final String personId = rec.getString("person_id")
            rec.delete()

            if (termReview) {
                rec = db.fetchOne("select * from employee_label_association where employee_id = ? and employee_label_id = ?", personId, TERM_REVIEW)
                if (rec == null) {
                    rec = db.newRecord("employee_label_association")
                    rec.set("employee_id", personId)
                    rec.set("employee_label_id", TERM_REVIEW)
                    rec.set("who_added", hsu.getCurrentPerson().getPersonId())
                    rec.set("notes", "Removed from Roster")
                    rec.addRecord()
                }
            }
    }

}
