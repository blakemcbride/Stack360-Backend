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

package com.arahant.services.standard.billing.expenseEdit


import com.arahant.beans.Project
import com.arahant.beans.ProjectEmployeeJoin
import com.arahant.business.BPerson
import com.arahant.servlets.REST
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/4/18
 */
class GetProjects {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String person_id = injson.getString("person_id")
        BPerson pers = new BPerson(person_id)
        HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class)
                .orderBy(Project.DESCRIPTION)
                .joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, pers.getPerson())
        JSONArray ary = new JSONArray()
        List<Project> projs = hcu.list()
        for (Project proj : projs) {
            JSONObject jobj = new JSONObject()
            jobj.put("project_id", proj.getProjectId())
            jobj.put("description", proj.getDescription())
            ary.put(jobj)
        }
        outjson.put("projects", ary)
    }

}
