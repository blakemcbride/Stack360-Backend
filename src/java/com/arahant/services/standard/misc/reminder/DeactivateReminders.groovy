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

package com.arahant.services.standard.misc.reminder

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.TimeUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 7/18/21
 */
class DeactivateReminders {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        JSONArray reminderIds = injson.getJSONArray('reminderIds')
        int n = reminderIds.length()
        for (int i=0 ; i < n ; i++) {
            String reminderId = reminderIds.getString(i)
            Record rec = db.fetchOne("select * from reminder where reminder_id = ?", reminderId)
            rec.set('status', 'I')
            rec.set('who_inactivated', hsu.getCurrentPerson().getPersonId())
            rec.set('date_inactive', DateUtils.today())
            rec.set('time_inactive', TimeUtils.now())
            rec.update()
        }

    }

}
