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

package com.arahant.services.standard.hr.comments

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.StringUtils
import org.kissweb.database.*

/**
 * Author: Blake McBride
 * Date: 6/29/21
 */
class SaveComments {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String personId = injson.getString("personId")
        String comment = injson.getString('globalComment')
        final Connection db = KissConnection.get()
        db.execute('update applicant set comments=? where person_id=?', comment, personId)

        comment = "Comment updated - " + comment

        Record rec = db.newRecord("change_log")
        rec.set("change_person_id", hsu.getCurrentPerson().getPersonId())
        rec.set("person_id", personId)
        rec.set("table_changed", "applicant")
        if (comment != null && comment.length() > 80)
            comment = StringUtils.take(comment, 80)
        rec.set("description", comment)
        rec.addRecord()
    }

}
