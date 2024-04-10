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

package com.arahant.services.standard.components.searchProject

import com.arahant.beans.Project
import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateScrollUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/8/18
 * Modified: 1/3/19
 */
class SearchProjects {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String search_type = inJSON.getString("search_type")
        Boolean showIncomplete = inJSON.getBoolean("show_incomplete")

        int max = BProperty.getInt(StandardProperty.SEARCH_MAX);

        HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class)
                .orderBy(Project.DESCRIPTION)
                .like(Project.DESCRIPTION, "%" + inJSON.getString("project_summary_contains") + "%")

        int n = 0
        HibernateScrollUtil<Project> scr = hcu.scroll()
        JSONArray jsonArray = new JSONArray()

        outJSON.put('isMore', false)

        while (scr.next()) {
            Project project = scr.get()
            // Skip expired projects if showIncomplete is true.
            int estLstDate = project.estimatedLastDate
            if (showIncomplete && estLstDate != 0 && estLstDate < DateUtils.today())
                continue

            String status = project.getProjectStatus().getDescription()

            if (search_type == "active" && status != "Active")
                continue

            if (search_type == "inactive" && status != "Inactive")
                continue

            if (n >= max) {
                outJSON.put('isMore', true)
                break
            }

            JSONObject jsonObject = new JSONObject()
            jsonObject.put("project_id", project.getProjectId())

            boolean incomplete = false
            if (showIncomplete) {
                if (project.estimatedFirstDate == 0 || project.estimatedLastDate == 0)
                    incomplete = true

                if (project.address == null) {
                    incomplete = true
                } else if (project.address.zip == null) {
                    incomplete = true
                }
            }

            // Add an asterisk if incomplete.
            if (incomplete) {
                jsonObject.put("name", project.getProjectName() + "*")
            } else {
                jsonObject.put("name", project.getProjectName())
            }

            jsonObject.put("summary", project.getDescription())
            jsonObject.put("status", status)
            jsonObject.put("reference", project.getReference())
            jsonObject.put("client", project.getRequestingOrgGroup().getName())
            jsonArray.put(jsonObject)

            ++n
        }

        outJSON.put("projects", jsonArray)
    }
}