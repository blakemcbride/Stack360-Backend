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
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/8/18
 */
class GetWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        BProject proj = new BProject(project_id)
        JSONArray ary = new JSONArray()
        List<Person> pers = proj.getAssignedPersons2()
        for (Person p : pers) {
            JSONObject jobj = new JSONObject()
            jobj.put("worker_name", p.getNameLFM())
            jobj.put("employee_id", p.getPersonId())
            ary.put(jobj)
        }
        outjson.put("workers", ary)
    }

}
