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

package com.arahant.services.standard.components.searchClientContact


import com.arahant.business.BCompanyBase
import com.arahant.business.BOrgGroup
import com.arahant.business.BPerson
import com.arahant.business.BProject
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 9/3/18
 */
class SearchClientContact {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString "project_id"

        BProject proj = new BProject(project_id)
        BCompanyBase company = proj.getRequestingCompany()
        BOrgGroup borg = new BOrgGroup(company.getOrgGroupId())

        BPerson [] contacts = borg.listPeople(100)

        JSONArray cary = new JSONArray()
        for (BPerson bp in contacts) {
            JSONObject jobj = new JSONObject()

            jobj.put "person_id", bp.getPersonId()
            jobj.put "name", bp.getNameLFM()
            jobj.put "email", bp.getPersonalEmail()

            cary.put jobj
        }
        outjson.put "contacts", cary
    }

}
