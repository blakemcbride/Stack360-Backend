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
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.TimeUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 7/18/21
 */
class NewReminder {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        Record rec = db.newRecord('reminder')
        rec.set('reminder_id', IDGenerator.generate('reminder', 'reminder_id'))

        String remindType = injson.getString('remindType')
        if (remindType == "E") {
            String person_id = injson.getString('remindPerson')
            if (person_id != null && !person_id.isEmpty())
                rec.set('person_id', person_id)
        } else if (remindType == 'M') {
            rec.set('person_id', hsu.getCurrentPerson().getPersonId())
        }

        String aboutPerson = injson.getString('aboutPerson')
        if (aboutPerson != null && !aboutPerson.isEmpty())
            rec.set('about_person', aboutPerson)

        String aboutProject = injson.getString('aboutProject')
        if (aboutProject != null && !aboutProject.isEmpty())
            rec.set('about_project', aboutProject)

        rec.set('reminder_date', injson.getInt('dueDate'))
        rec.set('summary', injson.getString('summary'))
        rec.set('detail', injson.getString('detail'))
        rec.set('status', 'A')
        rec.set('who_added', hsu.getCurrentPerson().getPersonId())
        rec.set('date_added', DateUtils.today())
        rec.set('time_added', TimeUtils.now())
        rec.addRecord()
    }

}
