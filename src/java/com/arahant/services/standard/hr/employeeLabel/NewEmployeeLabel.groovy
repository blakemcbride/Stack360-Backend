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

package com.arahant.services.standard.hr.employeeLabel

import com.arahant.business.BHREmployeeEvent
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/22/20
 */
class NewEmployeeLabel {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        String personId = injson.getString('personId')
        String labelId = injson.getString("labelId")
        Record rec = db.fetchOne("select * from employee_label_association where employee_id=? and employee_label_id=?",
                        personId, labelId)
        if (rec != null) {
            rec.set("who_added", hsu.getCurrentPerson().getPersonId())
            rec.setDateTime("when_added", new Date())
            rec.set("completed", "N")
            rec.set("notes", injson.getString("notes"))
            rec.update()
        } else {
            rec = db.newRecord("employee_label_association")
            rec.set('employee_id', personId)
            rec.set("employee_label_id", labelId)
            rec.set("who_added", hsu.getCurrentPerson().getPersonId())
            rec.setDateTime("when_added", new Date())
            rec.set("completed", "N")
            rec.set("notes", injson.getString("notes"))
            rec.addRecord()
        }

        // create the employee event
        Record lbl = db.fetchOne("select name from employee_label where employee_label_id=?", rec.getString("employee_label_id"))
        BHREmployeeEvent bev = new BHREmployeeEvent()
        bev.create()
        bev.setEmployeeId(rec.getString("employee_id"))
        bev.setSupervisorId(hsu.getCurrentPerson().getPersonId())
        bev.setEventDate(DateUtils.today())
        bev.setEmployeeNotified((char) 'N')
        bev.setSummary(lbl.getString("name") + " Label Added")
        bev.setEventType((char)'L')
        bev.setDetail(rec.getString("notes"))
        bev.insert()
    }
}
