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


import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 7/18/21
 */
class GetLoginEmployees {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX)
        outjson.put "employees", db.fetchAllJSON(lowCap, """
                select e.person_id, p.lname, p.fname, p.mname
                from employee e
                join current_employee_status esh
                  on e.person_id = esh.employee_id
                join hr_employee_status es
                  on esh.status_id = es.status_id
                join person p
                  on e.person_id = p.person_id
                join prophet_login pl
                  on e.person_id = pl.person_id
                where es.active = 'Y'
                      and pl.can_login = 'Y'
                order by lname, fname, mname
                                """)
        outjson.put "lowCap", lowCap
    }
}
