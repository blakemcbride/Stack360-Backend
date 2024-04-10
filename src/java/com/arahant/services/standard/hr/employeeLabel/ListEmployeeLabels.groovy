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

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 11/22/20
 */
class ListEmployeeLabels {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String personId = injson.getString("personId")
        Connection db = KissConnection.get()
        outjson.put "labels", db.fetchAllJSON("""
                    select ela.employee_label_id, ela.completed, ela.notes, el.name,
                           ela.when_added, ela.when_completed,
                           a.fname a_fname, a.mname a_mname, a.lname a_lname,
                           d.fname d_fname, d.mname d_mname, d.lname d_lname
                    from employee_label_association ela
                    join employee_label el
                      on ela.employee_label_id = el.employee_label_id
                    left outer join person a
                      on ela.who_added = a.person_id
                    left outer join person d
                      on ela.who_completed = d.person_id
                    where ela.employee_id = ?
                    order by el.name
                """, personId)
    }

}
