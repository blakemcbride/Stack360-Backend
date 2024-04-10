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

package com.arahant.services.standard.billing.expenseEditByProject

import com.arahant.beans.Person
import com.arahant.business.BProject
import com.arahant.business.BProjectShift
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/16/18
 */

class AddWorker {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        String shift_id = injson.getString("shift_id")
        String employee_id = injson.getString("employee_id")

        // see if employee already assigned to project
        BProject proj = new BProject(project_id)
        List<Person> pers = proj.getAssignedPersons2(shift_id)
        for (Person per : pers) {
            String person_id = per.getPersonId()
            if (person_id == employee_id)
                return;  // already assigned to project
        }
        BProjectShift bps = new BProjectShift(shift_id)
        bps.assignPerson(employee_id, 10, false)
        bps.update()
    }

}
